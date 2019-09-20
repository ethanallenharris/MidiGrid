import org.sqlite.SQLiteConfig;

import java.sql.*;

public class Main {

    public static Connection db = null;

    public static void main(String[] args) {
        openDatabase("DatabaseSQLite.db");
// code to get data from, write to the database etc goes here!
        insertWeight();
        selectUser();
        closeDatabase();
    }

    private static void openDatabase(String dbFile) {
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Database connection successfully established.");
        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());
        }

    }

    private static void closeDatabase() {
        try {
            db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }

    private static void selectUser() {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT UserID, UserName, Password FROM UsersAndPasswords");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int UserID = results.getInt(1);
                String UserName = results.getString(2);
                String Password = results.getString(3);
                System.out.println(UserID + " " + UserName + " " + Password);
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }


    }


    public static void insertWeight() {
        try {
            PreparedStatement ps = db.prepareStatement("INSERT INTO UsersAndPasswords (UserID, UserName, Password) VALUES (?, ?, ?)");
            ps.setInt(1, 8);
            ps.setString(2, "hAPPYjOE");
            ps.setString(3, "Beans");
            ps.executeUpdate();
            System.out.println("Record added to UsersAndPasswords table");

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error: Something as gone wrong. Please contact the administrator with the error code WC-WA.");
        }
    }


    public static void listThings() {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT UserID, UserName, Password FROM UsersAndPasswords");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int UserID = results.getInt(1);
                String UserName = results.getString(2);
                String Password = results.getString(3);
                System.out.print("UserID: " + UserID + ", ");
                System.out.print("UserName: " + UserName + ", ");
                System.out.print("Password: " + Password + ", ");
            }
        } catch (SQLException exception) {
            System.out.println("Database errorL " + exception.getMessage());
        }
    }


    public static void updateThing(int UserID, String UserName, String Password) {

        try {

            PreparedStatement ps = db.prepareStatement(UPDATE);
        }  catch (SQLException exception) {
            System.out.println("Database errorL " + exception.getMessage());
        }


    }


}

