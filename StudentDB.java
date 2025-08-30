import java.sql.*;
import java.util.*;

public class StudentDB {
    static final String URL = "jdbc:mysql://localhost:3306/studentdb";
    static final String USER = "root";   // your MySQL username
    static final String PASS = "MySql@1546";   // your MySQL password

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Load JDBC Driver (optional for newer versions, but safe)
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(URL, USER, PASS);
            int choice;

            do {
                System.out.println("\n--- Student Database ---");
                System.out.println("1. Add Student");
                System.out.println("2. View Students");
                System.out.println("3. Search Student by ID");
                System.out.println("4. Remove Student");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1: // Add
                        System.out.print("Enter ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Age: ");
                        int age = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Course: ");
                        String course = sc.nextLine();

                        String insert = "INSERT INTO students VALUES (?,?,?,?)";
                        try (PreparedStatement pst = con.prepareStatement(insert)) {
                            pst.setInt(1, id);
                            pst.setString(2, name);
                            pst.setInt(3, age);
                            pst.setString(4, course);
                            pst.executeUpdate();
                            System.out.println("✅ Student added!");
                        }
                        break;

                    case 2: // View
                        String query = "SELECT * FROM students";
                        try (Statement st = con.createStatement();
                             ResultSet rs = st.executeQuery(query)) {
                            System.out.println("\nID | Name | Age | Course");
                            while (rs.next()) {
                                System.out.println(rs.getInt(1) + " | " +
                                        rs.getString(2) + " | " +
                                        rs.getInt(3) + " | " +
                                        rs.getString(4));
                            }
                        }
                        break;

                    case 3: // Search
                        System.out.print("Enter ID: ");
                        int sid = sc.nextInt();
                        String search = "SELECT * FROM students WHERE id=?";
                        try (PreparedStatement pst = con.prepareStatement(search)) {
                            pst.setInt(1, sid);
                            ResultSet rs = pst.executeQuery();
                            if (rs.next())
                                System.out.println("Found: " +
                                        rs.getInt(1) + " | " +
                                        rs.getString(2) + " | " +
                                        rs.getInt(3) + " | " +
                                        rs.getString(4));
                            else
                                System.out.println("❌ Student not found!");
                        }
                        break;

                    case 4: // Remove
                        System.out.print("Enter ID to delete: ");
                        int did = sc.nextInt();
                        String delete = "DELETE FROM students WHERE id=?";
                        try (PreparedStatement pst = con.prepareStatement(delete)) {
                            pst.setInt(1, did);
                            int rows = pst.executeUpdate();
                            if (rows > 0)
                                System.out.println("✅ Student deleted successfully!");
                            else
                                System.out.println("❌ Student not found!");
                        }
                        break;
                }
            } while (choice != 5);

            con.close();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        sc.close();
    }
}
