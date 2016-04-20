package com.crimekiller.safetrip.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.crimekiller.safetrip.model.Post;

public class PostCRUD {
	private String db;
    private Connection connection;
    private PreparedStatement statement;
    private String query;

    public PostCRUD(String database) {
        this.db = database;
    }

    public void addPostToDB(Post a){
        if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();

                //add the automobile to the Post table
                query = "INSERT INTO Post (date, licenseplate, destination, model, color, departure,"
                        +" owner) VALUES (?,?,?,?,?,?,?)";
                statement = connection.prepareStatement(query);
                statement.setString(1, a.getDate());
                statement.setString(2, a.getLicensePlate());
                statement.setString(3, a.getDestination());
                statement.setString(4, a.getModel());
                statement.setString(5, a.getColor());
                statement.setString(6, a.getDeparture());
                statement.setString(7, a.getOwner());
                statement.executeUpdate();

                System.out.println("New post is successfully added to database.");
                statement.close();

            } catch (SQLException e){
                System.out.println ("SQL Exception when adding post to database.");
                e.printStackTrace();
            }
        }
    }

    public void deletePostFromDB(Post a){
        if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();

                query = "SELECT postID FROM Post WHERE date = ? and licenseplate = ? and destination = ? and owner = ?";
                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setString(1, a.getDate());
                statement.setString(2, a.getLicensePlate());
                statement.setString(3, a.getDestination());
                statement.setString(4, a.getOwner());
                ResultSet rs = statement.executeQuery();
                while(rs.next()){

                    int postID = Integer.parseInt(rs.getString("postID"));
                    // Delete from Auto table
                    query = "DELETE FROM Post WHERE postID = ?";
                    statement = (PreparedStatement) connection.prepareStatement(query);
                    statement.setInt(1, postID);
                    statement.executeUpdate();
                }

                System.out.println("The post is successfully deleted.");
                statement.close();
            } catch (SQLException e){
                System.out.println ("SQL Exception when deleting the post from database.");
                e.printStackTrace();
            }
        }
    }

    //get all posts from all users in the database
    public ArrayList<Post> getAllPost() {

        ArrayList<Post> allPosts = new ArrayList<Post>();

        if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();

                query = "SELECT * FROM Post";
                statement = (PreparedStatement) connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){

                    String date = rs.getString("date");
                    String plate = rs.getString("licenseplate");
                    String destination = rs.getString("destination");
                    String model = rs.getString("model");
                    String color = rs.getString("color");
                    String departure = rs.getString("departure");
                    String owner = rs.getString("owner");

                    Post aPost = new Post(date, plate, destination, model,color, departure, owner);
                    allPosts.add(aPost);

                }
                System.out.println("All posts are obtained successfully.");
                statement.close();
            } catch (SQLException e){
                System.out.println ("SQL Exception when getting all posts from database.");
                e.printStackTrace();
            }
        }

        return allPosts;
    }
    
    //get all posts from a specific user
    public ArrayList<Post> getUserPost(String username) {

        ArrayList<Post> allUserPosts = new ArrayList<Post>();

        if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();

                query = "SELECT * FROM Post WHERE owner = ?";
                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setString(1, username);
                
                ResultSet rs = statement.executeQuery();
                while(rs.next()){

           		    String date = rs.getString("date");
                    String plate = rs.getString("licenseplate");
                    String destination = rs.getString("destination");
                    String model = rs.getString("model");
                    String color = rs.getString("color");
                    String departure = rs.getString("departure");
                    String owner = rs.getString("owner");
               
                    Post aPost = new Post(date, plate, destination, model,color, departure, owner);
                    allUserPosts.add(aPost);               	

                }
                
                System.out.println("All posts from the user: " + username + " are obtained successfully.");
                statement.close();
            } catch (SQLException e){
                System.out.println ("SQL Exception when getting all posts from database.");
                e.printStackTrace();
            }
        }

        return allUserPosts;
    }

}
