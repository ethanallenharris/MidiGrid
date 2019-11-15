package Main;

import Controllers.Songs;
import Controllers.Users;
import org.sqlite.SQLiteConfig;
import java.sql.*;
import java.util.Scanner;

public class Main {

    private static void openDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + "DatabaseSQLite.db", config.toProperties());
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
    public static Connection db = null;

    public static void main(String[] args) {
        openDatabase();
        Scanner input = new Scanner(System.in);
        String decision = "";
        Boolean successful = false;
// code to get data from, write to the database etc goes here!
        while (!decision.equals("exit")) {
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
            System.out.println("9 - Delete User");
            System.out.println("10 - List songs");
            System.out.println("exit - exits the menu");
            while (!successful) {
                System.out.println("Please enter your choice");
                decision = input.nextLine();
                switch (decision) {
                    case "1":
                        Users.createUser();
                        successful = true;
                        break;
                    case "2":
                        Users.selectUser();
                        successful = true;
                        break;
                    case "3":
                        closeDatabase();
                        successful = true;
                        break;
                    case "4":
                        Users.listUser();
                        successful = true;
                        break;
                    case "5":
                        Songs.saveSong();
                        successful = true;
                        break;
                    case "6":
                        Songs.deleteSong(2);
                        successful = true;
                        break;
                    case "7":
                        Songs.createSong();
                        successful = true;
                        break;
                    case "8":
                        Songs.renameSong();
                        successful = true;
                        break;
                    case "9":
                        Users.deleteUser();
                        successful = true;
                        break;
                    case "10":
                        Songs.listSongs();
                        successful = true;
                        break;
                    case "exit":
                        successful = true;
                        break;
                    default:
                        System.out.println("Data entered is wrong, please try again");
                        break;
                }
            }
        }
        closeDatabase();
        System.out.println("TEST");
    }







}

