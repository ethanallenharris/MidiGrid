package Controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Path("/user/")
public class Users {
    @GET
    @Path("select")
    @Produces(MediaType.APPLICATION_JSON)
    public static String selectUser(@FormDataParam("Username") String UserName) throws Exception {
        if (UserName == null) {
            throw new Exception("Users 'UserName' is missing in the HTTP requests URL");
        }
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, UserName, Password FROM Users WHERE UserName=?");

            ps.setString(1, UserName);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                item.put("UserID",results.getInt(1));
                item.put("Username", results.getString(2));
                item.put("Password", results.getString(3));
            }
            return item.toString();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list users, please see server console for more info.\"}";
        }


    }

    @POST
    @Path("create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String createUser(@FormDataParam("UserName") String UserName, @FormDataParam("Password")String Password, @FormDataParam("Admin") Boolean Admin) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (UserName, Password, Admin) VALUES (?, ?, ?)");
            ps.setString(1, UserName);
            ps.setString(2, Password);
            ps.setBoolean(3, Admin);
            ps.executeUpdate();
            System.out.println("Record added to 'Users' table");
            return "{\"status\": \"OK\"}";
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error: Something has gone wrong, Error message: " + exception.getMessage());
            return "{\"error\": \"Unable to create new user, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("allUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public static String listUser() {
        try {
            JSONArray users = new JSONArray();
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, UserName, Password FROM Users");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject user = new JSONObject();
                user.put("UserID",results.getInt(1));
                user.put("Username", results.getString(2));
                user.put("Password", results.getString(3));
                users.add(user);
            }
            return users.toString();
        } catch (SQLException exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list users, please see server console for more info.\"}";

        }
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static void deleteUser() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID =?");
            ps.setInt(1, 5);

            ps.execute();
            System.out.println("Deletion successful");
        } catch (SQLException exception) {
            System.out.println("Database error " + exception.getMessage());

        }
    }

    @POST
    @Path("list")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String listSongs(@FormDataParam("UserID") Integer UserID) {
        try {
            JSONArray Songs = new JSONArray();
            PreparedStatement ps = Main.db.prepareStatement("SELECT SongName FROM Songs WHERE UserID=?");
            ps.setInt(1,UserID);
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("SongName", results.getString(1));
                Songs.add(item);
            }
            return Songs.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }


    @GET
    @Path("list/load/{SongID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getThing(@PathParam("SongID") Integer SongID) throws Exception {
        if (SongID == null) {
            throw new Exception("Songs's 'SongID' is missing in the HTTP request's URL.");
        }
        System.out.println("list/load/" + SongID);
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT SongName, SongContents FROM Songs WHERE SongID = ?");
            ps.setInt(1, SongID);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                item.put("SongID", SongID);
                item.put("SongName", results.getString(1));
                item.put("SongContents", results.getInt(2));
            }
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }


    @POST
    @Path("list/rename/{SongID}")
    @Produces(MediaType.APPLICATION_JSON)
    public static void renameSong(@PathParam("SongID") Integer SongID, @FormDataParam("SongName") String SongName) {
        System.out.println("list/rename/" + SongID);
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Songs SET SongName = ? WHERE SongID = ?");
            ps.setString(1, SongName);
            ps.setInt(2, SongID);
            ps.execute();
            System.out.println("Song successfully renamed to: " + SongName);
        }
        catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    @GET
    @Path("list/delete/{SongID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteSong(@PathParam("SongID") Integer SongID) {
        System.out.println("list/delete/" + SongID);
        try {
            if (SongID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("list/delete/" + SongID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Songs WHERE SongID = ?");

            ps.setInt(1, SongID);

            ps.execute();

            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }




    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUser(@FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password, @CookieParam("Token") String Token) {

        try {

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
            ps1.setString(1, UserName);
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next()) {

                String correctPassword = loginResults.getString(1);

                if (Password.equals(correctPassword)) {

                    Token = UUID.randomUUID().toString();

                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE Username = ?");
                    ps2.setString(1, Token);
                    ps2.setString(2, UserName);
                    ps2.executeUpdate();

                    return "{\"token\": \""+ Token + "\"}";

                } else {

                    return "{\"error\": \"Incorrect password\"}";

                }

            } else {

                return "{\"error\": \"Unknown user\"}";

            }

        }catch (Exception exception){
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"error\": \"Server side error, please see server console for more info\"}";
        }


    }





}
