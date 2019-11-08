import org.sqlite.SQLiteConfig;
import java.sql.*;
import java.util.Scanner;

public class Main {

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

    public static Connection db = null;

    public static void main(String[] args) {
        openDatabase("DatabaseSQLite.db");
        Scanner input = new Scanner(System.in);
        String decision = "";
        Boolean successful = false;
// code to get data from, write to the database etc goes here!
        while (decision != "exit") {
            successful = false;
            System.out.println("Please choose an option");
            System.out.println("1 - Create a new user");
            System.out.println("2 - Select User");
            System.out.println("3 - Close database");
            System.out.println("4 - List users");
            System.out.println("5 - Save song");
            System.out.println("6 - Delete song");
            System.out.println("7 - Create song");
            System.out.println("8 - Rename song");
            System.out.println("exit - exits the menu");
            while (successful != true) {
                System.out.println("Please enter your choice");
                decision = input.nextLine();
                System.out.println(decision);
                if (decision.equals("1")){
                    createUser();
                    successful = true;
                } else if (decision.equals("2")) {
                    selectUser();
                    successful = true;
                } else if (decision.equals("3")) {
                    closeDatabase();
                    successful = true;
                } else if (decision.equals("4")) {
                    listUsers();
                    successful = true;
                } else if (decision.equals("5")) {
                    saveSong();
                    successful = true;
                } else if (decision.equals("6")) {
                    deleteSong();
                    successful = true;
                } else if (decision.equals("7")) {
                    createSong();
                    successful = true;
                } else if (decision.equals("8")) {
                    renameSong();
                    successful = true;
                } else if (decision.equals("exit")) {
                    successful = true;
                } else {
                    System.out.println("Data entered is wrong, please try again");
                }
            }
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
            PreparedStatement ps = db.prepareStatement("SELECT UserID, UserName, Password FROM Users");

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


    public static void createUser() {
        try {
            PreparedStatement ps = db.prepareStatement("INSERT INTO Users (UserID, UserName, Password) VALUES (?, ?, ?)");
            ps.setString(2, "Anonymous");
            ps.setString(3, "123456789");
            ps.executeUpdate();
            System.out.println("Record added to 'Users' table");

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error: Something has gone wrong. Please contact the administrator with the error code WC-WA.");
        }
    }


    public static void listUsers() {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT UserID, UserName, Password FROM Users");

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
            System.out.println("Database error " + exception.getMessage());
        }
    }


    public static void saveSong() {
        try {
            PreparedStatement ps = db.prepareStatement("UPDATE Songs SET SongID, UserID, SongName, SongContents");
            ps.setInt(2,2);
            ps.setString(3, "Sausage");
            ps.setString(4, "984535894845");

            ps.execute();
        } catch (Exception exception){
            System.out.println("Database error:" + exception.getMessage());
        }
    }

    public static void deleteSong() {
        try {
            PreparedStatement ps = db.prepareStatement("DELETE FROM Songs WHERE SongID = ?");
            ps.setInt(1, 2);

            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error:" + exception.getMessage());
        }
    }

    public static void createSong() {
        try {
            PreparedStatement ps = db.prepareStatement("INSERT INTO songs (SongID, UserID, SongName, SongContents)");
            ps.setInt(2, 2);
            ps.setString(3, "Wood Tables");
            ps.setString(4, "35840483058");

            ps.executeUpdate();
        } catch (Exception exception) {
            System.out.println("Database error:" + exception.getMessage());
        }
    }

    public static void renameSong() {
        try {
            PreparedStatement ps = db.prepareStatement("UPDATE Songs SET SongName WHERE SongID = 3");
            ps.setString(3, "Spinning");
        } catch (Exception exception) {
            System.out.println("Database error:" + exception.getMessage());
        }
    }

}

