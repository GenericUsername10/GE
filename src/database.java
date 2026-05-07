package src;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class database {

    private static void readFile(Scanner in, Connection conn) {
        String category;
        in.nextLine();

        while (in.hasNextLine()) {
            category = in.nextLine();
            in.nextLine();
            createTable(in, conn, category);
        }
    }

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

    private static void createTable(Scanner in, Connection conn,
            String category) {
        String code, title, desc, prereq;
        boolean noPrereqs = false;
        PreparedStatement ps;
        String sql = "CREATE TABLE IF NOT EXISTS " + category
                + " (CourseCode varchar(15) PRIMARY KEY, Title varchar(25), Description MEDIUMTEXT, Prereqs MEDIUMTEXT)";

        try {
            ps = conn.prepareStatement(sql);

            ps.execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(sql);
            System.exit(0);
        }

        code = in.nextLine();
        while (!code.isBlank()) {
            title = in.nextLine();
            desc = in.nextLine();
            prereq = in.nextLine();

            if (!prereq.contains("Prereq")) {
                code = prereq;
                prereq = null;
                noPrereqs = true;
            }

            sql = "INSERT INTO " + category + " VALUES ('" + code + "', '"
                    + title + "', '" + desc + "', '" + prereq + "')";

            try {
                ps = conn.prepareStatement(sql);
                ps.execute();

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(sql);
                //System.exit(0);
            }
            if (noPrereqs) {
                noPrereqs = false;
                continue;
            }
            code = in.nextLine();
        }

    }

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
