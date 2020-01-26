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
    @Path("select") // path "/user/select
    @Produces(MediaType.APPLICATION_JSON)
    public static String selectUser(@FormDataParam("Username") String UserName) throws Exception { //Fields required for SQL statements
        if (UserName == null) { //Checks if the Username is missing
            throw new Exception("Users 'UserName' is missing"); //if it is this statement is returned
        }
        JSONObject item = new JSONObject(); //creating a JSON object for the select method
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, UserName, Password FROM Users WHERE UserName=?"); //SQL statement selects all the information about a user from their username

            ps.setString(1, UserName);
            ResultSet results = ps.executeQuery();
            if (results.next()) { //selects one record
                item.put("UserID",results.getInt(1));
                item.put("Username", results.getString(2));
                item.put("Password", results.getString(3)); //these add the data into a string
            }
            return item.toString();

        } catch (Exception exception) { //catches any errors
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list users, please see server console for more info.\"}"; //JSON error message
        }


    }

    @POST
    @Path("create") //API path : /user/create
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String createUser(@FormDataParam("UserName") String UserName, @FormDataParam("Password")String Password) { //Fields required for SQL statements
        if (UserName == null) { //checks if UserName is missing
            return "{\"error\": \"UserName is missing.\"}"; //returns appropriate message
        }
        if (Password == null) { //checks if Password is missing
            return "{\"error\": \"Password is missing.\"}"; //returns appropriate message
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users (UserName, Password) VALUES (?, ?)"); //SQL statements inserts the data and creates a new record in the database
            ps.setString(1, UserName);
            ps.setString(2, Password);
            ps.executeUpdate(); //executes the SQL statement
            System.out.println("Record added to 'Users' table");
            return "{\"status\": \"OK\"}"; //Lets the program know no errors occurred
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error: Something has gone wrong, Error message: " + exception.getMessage()); //Prints that there was an error
            return "{\"error\": \"Unable to create new user, please see server console for more info.\"}"; //Returns that there was an error
        }
    }

    @GET
    @Path("allUsers") //API path: /user/allUsers
    @Produces(MediaType.APPLICATION_JSON)
    public static String listUser() { //No fields required
        try {
            JSONArray users = new JSONArray(); //creates JSON array to store all of the User records
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, UserName, Password FROM Users"); //SQL that selects all of the User fields and records

            ResultSet results = ps.executeQuery(); //executes SQL
            while (results.next()) { //This loop makes it go through all of the User records until it reaches the end
                JSONObject user = new JSONObject(); //This object stores the values returned from the SQL statement
                user.put("UserID",results.getInt(1));
                user.put("Username", results.getString(2));
                user.put("Password", results.getString(3));
                users.add(user); //This then gets added to the JSON array
            }
            return users.toString(); //Returns the array of Users
        } catch (SQLException exception) {
            System.out.println("Database error " + exception.getMessage()); //Prints that there was an error
            return "{\"error\": \"Unable to list users, please see server console for more info.\"}"; //Returns that there was an error

        }
    }

    @POST
    @Path("delete") //API path: /user/delete
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static void deleteUser(@FormDataParam("UserID") Integer UserID) { //Fields required for SQL statements
        if (UserID == null) { //Checks if UserID is null
            System.out.println("Database error: UserID is missing"); //Prints that there was an error
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID =?"); //SQL deletes the record that matches the userID
            ps.setInt(1, UserID);

            ps.execute(); //Executes the SQL
            System.out.println("Deletion successful"); //Prints that the deletion was successful
        } catch (SQLException exception) {
            System.out.println("Database error " + exception.getMessage());  //prints the Error message
        }
    }

    @POST
    @Path("list") //API path: /user/list
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String listSongs(@FormDataParam("UserID") Integer UserID) { //Fields required for SQL statements
        if (UserID == null) { //Checks if UserID is null
            return "{\"error\": \"Missing UserID.\"}"; //returns JSON message that the UserID is missing
        }
        try {
            JSONArray Songs = new JSONArray(); //Creates the JSON array to store the users songs
            PreparedStatement ps = Main.db.prepareStatement("SELECT SongName FROM Songs WHERE UserID=?"); //SQL statement selects all of the SongName's of the UserID's songs
            ps.setInt(1,UserID);
            ResultSet results = ps.executeQuery(); //Executes the SQL statement
            while (results.next()) { //This loop makes it go through all of the User records until it reaches the end
                JSONObject item = new JSONObject();
                item.put("SongName", results.getString(1)); //Object stores the SongName
                Songs.add(item); //This adds the object to an array
            }
            return Songs.toString(); //Returns the JSON array
        } catch (Exception exception) { //catches error
            System.out.println("Database error: " + exception.getMessage()); //Prints error message
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}"; //Returns JSON message stating the issue
        }
    }


    @GET
    @Path("list/load/{SongID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getThing(@PathParam("SongID") Integer SongID) throws Exception { //Fields required for SQL statements
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
    public static void renameSong(@PathParam("SongID") Integer SongID, @FormDataParam("SongName") String SongName) { //Fields required for SQL statements
        if (SongID == null) {
            System.out.println("SongID is not found in path");
        }
        if (SongName == null) {
            System.out.println("SongName is missing");
        }
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
    public String deleteSong(@PathParam("SongID") Integer SongID) { //Fields required for SQL statements
        System.out.println("list/delete/" + SongID);
        try {
            if (SongID == null) {
                throw new Exception("SongID is missing.");
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
    public String loginUser(@FormDataParam("UserName") String UserName, @FormDataParam("Password") String Password, @CookieParam("Token") String Token) { //Fields required for SQL statements

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
