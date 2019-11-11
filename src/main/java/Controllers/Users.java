package Controllers;

import Main.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users {

    public static void selectUser() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, UserName, Password FROM Users");
            ResultSet results = ps.executeQuery();

            int UserID = results.getInt(1);
            String UserName = results.getString(2);
            String Password = results.getString(3);
            System.out.println(UserID + " " + UserName + " " + Password);


        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }


    }


    public static void createUser() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (UserID, UserName, Password) VALUES (?, ?, ?)");
            ps.setString(2, "Anonymous");
            ps.setString(3, "123456789");
            ps.executeUpdate();
            System.out.println("Record added to 'Users' table");

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error: Something has gone wrong. Please contact the administrator with the error code WC-WA.");
        }
    }


    public static void listUser() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, UserName, Password FROM Users");

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

    public static void deleteUser() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            ps.setInt(1, 8);

            ps.execute();
            System.out.println("Deletion successful");
        } catch (SQLException exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }

}
