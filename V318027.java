import java.sql.*;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

// Entity: Student
class Student {
    private int id;
    private String name;
    private int age;
    private String course;

    public Student(int id, String name, int age, String course) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Age: " + age + ", Course: " + course;
    }
}

// Main class: Student2
public class V318027 {
    private Vector<Student> students = new Vector<>();
    private Connection connection;

    public V318027() {
        // Initialize database connection
        String url = "jdbc:mysql://localhost:3307/studentdb";
        String user = "root";
        String password = "";
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a student
    public void addStudent(Student student) {
        String sql = "INSERT INTO students (id, name, age, course) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, student.getId());
            pstmt.setString(2, student.getName());
            pstmt.setInt(3, student.getAge());
            pstmt.setString(4, student.getCourse());
            pstmt.executeUpdate();
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View all students
    public void viewStudents() {
        String sql = "SELECT * FROM students";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String course = rs.getString("course");
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ", Course: " + course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Search for a student by ID
    public Student searchStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String course = rs.getString("course");
                return new Student(id, name, age, course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Delete a student by ID
    public void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update a student's details
    public void updateStudent(int id, String newName, int newAge, String newCourse) {
        String sql = "UPDATE students SET name = ?, age = ?, course = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newAge);
            pstmt.setString(3, newCourse);
            pstmt.setInt(4, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Validate ID
    private static boolean isValidId(int id) {
        return id > 0;
    }

    // Validate Name
    private static boolean isValidName(String name) {
        return name != null && name.length() > 0 && Pattern.matches("[a-zA-Z ]+", name);
    }

    // Validate Age
    private static boolean isValidAge(int age) {
        return age > 0 && age <= 120; // Assuming valid age range from 1 to 120
    }

    // Validate Course
    private static boolean isValidCourse(String course) {
        return course != null && course.length() > 0;
    }

    public static void main(String[] args) {
        V318027 sms = new V318027();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Delete Student");
            System.out.println("5. Update Student");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    if (!isValidId(id)) {
                        System.out.println("Invalid ID. Please enter a positive integer.");
                        break;
                    }
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    if (!isValidName(name)) {
                        System.out.println("Invalid Name. Please enter a valid name with letters and spaces only.");
                        break;
                    }
                    System.out.print("Enter Age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    if (!isValidAge(age)) {
                        System.out.println("Invalid Age. Please enter a valid age between 1 and 120.");
                        break;
                    }
                    System.out.print("Enter Course: ");
                    String course = scanner.nextLine();
                    if (!isValidCourse(course)) {
                        System.out.println("Invalid Course. Please enter a non-empty course name.");
                        break;
                    }
                    sms.addStudent(new Student(id, name, age, course));
                    break;
                case 2:
                    sms.viewStudents();
                    break;
                case 3:
                    System.out.print("Enter ID: ");
                    id = scanner.nextInt();
                    Student student = sms.searchStudentById(id);
                    if (student != null) {
                        System.out.println(student);
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter ID: ");
                    id = scanner.nextInt();
                    if (!isValidId(id)) {
                        System.out.println("Invalid ID. Please enter a positive integer.");
                        break;
                    }
                    sms.deleteStudent(id);
                    break;
                case 5:
                    System.out.print("Enter ID: ");
                    id = scanner.nextInt();
                    scanner.nextLine();
                    if (!isValidId(id)) {
                        System.out.println("Invalid ID. Please enter a positive integer.");
                        break;
                    }
                    System.out.print("Enter New Name: ");
                    name = scanner.nextLine();
                    if (!isValidName(name)) {
                        System.out.println("Invalid Name. Please enter a valid name with letters and spaces only.");
                        break;
                    }
                    System.out.print("Enter New Age: ");
                    age = scanner.nextInt();
                    scanner.nextLine();
                    if (!isValidAge(age)) {
                        System.out.println("Invalid Age. Please enter a valid age between 1 and 120.");
                        break;
                    }
                    System.out.print("Enter New Course: ");
                    course = scanner.nextLine();
                    if (!isValidCourse(course)) {
                        System.out.println("Invalid Course. Please enter a non-empty course name.");
                        break;
                    }
                    sms.updateStudent(id, name, age, course);
                    break;
                case 6:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please choose a valid option.");
            }
        }
    }
}
