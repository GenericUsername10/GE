package src;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class database {

    // Reads file full of info about GE courses
    // This file was created by the python script
    private static void readFile(Scanner in, Connection conn) {
        String category;
        in.nextLine();

        while (in.hasNextLine()) {
            category = in.nextLine();
            in.nextLine();
            createTable(in, conn, category);
        }
    }

    // Connect to database
    private static Connection connect(String name) {
        Connection conn = null;
        String url = "jdbc:sqlite:" + name;

        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {

                DatabaseMetaData meta = conn.getMetaData();
                System.out
                        .println("The driver name is " + meta.getDriverName());
                System.out.println(
                        "The connection to the database was successful.");
            } else {
                System.out.println("Connection was null.");
                System.exit(0);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        return conn;
    }

    // Creates each table and inserts data
    private static void createTable(Scanner in, Connection conn,
            String category) {
        String code, title, desc, prereq, temp = null;
        boolean noPrereqs = false;
        PreparedStatement ps;
        String sql = "CREATE TABLE IF NOT EXISTS " + category
                + " (CourseCode VARCHAR(20) PRIMARY KEY, Title TINYTEXT, Description MEDIUMTEXT, Prereqs MEDIUMTEXT)";

        try {
            ps = conn.prepareStatement(sql);

            ps.execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        code = in.nextLine();
        while (!code.isBlank()) {
            title = in.nextLine();
            desc = in.nextLine();
            prereq = in.nextLine();

            if (!prereq.contains("Prereq") && !prereq.contains("Not open to")
                    && !prereq.contains("Concur: ")) {
                temp = prereq;
                prereq = null;
                noPrereqs = true;
            }

            sql = "INSERT INTO " + category + " VALUES (?, ?, ?, ?)";

            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1, code);
                ps.setString(2, title);
                ps.setString(3, desc);
                ps.setString(4, prereq);
                ps.execute();

            } catch (Exception e) {
                /*
                 * System.out.println(e.getMessage()); if
                 * (!e.getMessage().contains("SQLITE_CONSTRAINT_PRIMARYKE")) {
                 * System.exit(0); }
                 */
            }
            if (noPrereqs) {
                if (temp.length() > 20) {
                    code = in.nextLine();
                } else {
                    code = temp;
                }
                noPrereqs = false;
                continue;
            }
            code = in.nextLine();
        }

    }

    // Main
    public static void main(String[] args) {
        File file = new File("ge.txt");
        Scanner user = new Scanner(System.in);

        System.out.print("Enter database file name: ");
        Connection conn = connect(user.nextLine());
        user.close();

        try {
            Scanner in = new Scanner(file);

            readFile(in, conn);

            in.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
