package Controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import server.Main;

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;





@Path("/song/")
public class Songs {
    @POST
    @Path("UserID={UserID}/SongID={SongID}/save") //API path /song/UserID= /SongID= /save
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String saveSong(@PathParam("UserID") Integer UserID, @FormDataParam("SongName") String SongName, @FormDataParam("SongContents") String SongContents, @PathParam("SongID") Integer SongID) { //Fields required for SQL statements
        try {
            if (SongID == 0 || UserID == 0 || SongName == null || SongContents == null) { //Checks if any of the fields are missing/null
                throw new Exception("One or more form data parameters are missing in the HTTP request"); //if it is returns this error message
            }

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Songs SET UserID = ?, SongName = ?, SongContents = ? WHERE SongID = ?"); //SQL statement
            ps.setInt(1, UserID);
            ps.setString(2, SongName);
            ps.setString(3, SongContents);
            ps.setInt(4, SongID);
            ps.executeUpdate();
            return "{\"Status\": \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to Update song\"}";
        }
    }

    @POST
    @Path("UserID={UserID}/newSong")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String newSong(@PathParam("UserID") Integer UserID, @FormDataParam("SongName") String SongName, @FormDataParam("SongContents") String SongContents) { //Fields required for SQL statements
        try {
            if (UserID == 0 || SongName == null || SongContents == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Songs (UserID, SongName, SongContents) VALUES ( ?, ?, ?)");
            ps.setInt(1, UserID);
            ps.setString(2, SongName);
            ps.setString(3, SongContents);

            ps.execute();
            return "{\"Status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error:" + exception.getMessage());
            return "{\"Error\": \"Unable to Save song\"}";
        }
    }






    public static void saveSong(Integer SongID, Integer UserID, String SongName, String SongContents) {
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

    public static void deleteSong(Integer SongID) {
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
