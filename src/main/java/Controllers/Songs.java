package Controllers;

import Main.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Songs {




    public static void saveSong(int SongID, int UserID, String SongName, String SongContents) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Songs SET UserID = ?, SongName = ?, SongContents = ? WHERE SongID = ?");
            ps.setInt(1, UserID);
            ps.setString(2, SongName);
            ps.setString(3, SongContents);
            ps.setInt(4, SongID);

            ps.executeUpdate();
            System.out.println("Update successful");
        } catch (Exception exception){
            System.out.println("Database error:"+ exception.getMessage());
        }
    }

    public static void deleteSong(int SongID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Songs WHERE SongID = ?");
            ps.setInt(1, SongID);

            ps.execute();
            System.out.println("Deletion successful");
        } catch (Exception exception) {
            System.out.println("Database error:" + exception.getMessage());
        }
    }

    public static void createSong() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Songs (UserID, SongName, SongContents) VALUES ( ?, ?, ?)");
            ps.setInt(1, 2);
            ps.setString(2, "Wood Tables");
            ps.setString(3, "35840483058");

            ps.executeUpdate();
        } catch (Exception exception) {
            System.out.println("Database error:" + exception.getMessage());
        }
    }

    public static void renameSong() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Songs SET SongName = ? WHERE (SongID = 3)");
            ps.setString(1, "Spinning");

            ps.executeUpdate();
            System.out.println("Song successfully renamed");
        } catch (Exception exception) {
            System.out.println("Database error:" + exception.getMessage());
     }
    }

    public static void listSongs() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT SongID, UserID, SongName, SongContents FROM Songs");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int SongID = results.getInt(1);
                int UserID = results.getInt(2);
                String SongName = results.getString(3);
                String SongContents = results.getString(4);
                System.out.println(SongID + " " + UserID + " " + SongName + " " + SongContents);
             }

        } catch (Exception exception) {
            System.out.println("Database error:" + exception.getMessage());
        }
    }

}
