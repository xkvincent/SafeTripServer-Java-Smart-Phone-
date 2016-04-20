package com.crimekiller.safetrip.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RelationshipCRUD {
	private String db;
    private Connection connection;
    private PreparedStatement statement;
    private String query;

    public RelationshipCRUD(String database) {
        this.db = database;
    }

    //when one user send friend request, then a new pending relationship will be created in the relationship table
    public void addPendingRelationshipToDB(String username1, String username2, String actionUsername){
    	int user_one_id=0, user_two_id=0,action_user_id=0;
    	
        if(DBconnection.openConnectionToDB(db)){
               
             try{           	 
            	 user_one_id = findUserID(username1);
            	 user_two_id = findUserID(username2);
            	 
                //set up the action user id 
                if(username1.equals(actionUsername)) {
                	action_user_id = user_one_id;
                } else {
                	action_user_id = user_two_id;
                }
                
                //insert the record into database
                query = "INSERT INTO Relationship (user_one_ID,user_two_ID,status, action_user_ID) VALUES (?,?,?,?)";
                statement = (PreparedStatement) connection.prepareStatement(query);
                
                if(user_one_id<user_two_id) {
                    statement.setInt(1, user_one_id);
                    statement.setInt(2, user_two_id);
                    statement.setInt(3,0);
                    statement.setInt(4,action_user_id);
                } else {
                	statement.setInt(1, user_two_id);
                    statement.setInt(2, user_one_id);
                    statement.setInt(3,0);
                    statement.setInt(4,action_user_id);
                }
                
                statement.executeUpdate();
                System.out.println("New relationship is successfully added to database.");
                statement.close();

            } catch (SQLException e){
                System.out.println ("SQL Exception when adding relationship to database.");
                e.printStackTrace();
            }
        }
    }
    
   
    
    //If the user accept the request, the status code will be changed from 0 (pending) to 1 (accept)    
    public void addFreiendRelationshipToDB(String username1, String username2, String actionUsername){
    	int user_one_id=0, user_two_id=0,action_user_id=0;
    	
        if(DBconnection.openConnectionToDB(db)){
               
             try{           	 
            	 user_one_id = findUserID(username1);
            	 user_two_id = findUserID(username2);
            	 
                //set up the action user id 
                if(username1.equals(actionUsername)) {
                	action_user_id = user_one_id;
                } else {
                	action_user_id = user_two_id;
                }
                
                //insert the record into database
                query = "UPDATE Relationship SET status = 1 AND action_user_ID = ? Where user_one_ID =? AND user_two_ID = ? AND status = 0";
                statement = (PreparedStatement) connection.prepareStatement(query);
                
                if(user_one_id<user_two_id) {
                    statement.setInt(1, action_user_id);
                    statement.setInt(2, user_one_id);
                    statement.setInt(3,user_two_id);
                
                } else {
                	statement.setInt(1, action_user_id);
                    statement.setInt(2, user_two_id);
                    statement.setInt(3,user_one_id);
                }
                statement.executeUpdate();
                System.out.println("New friend relationship is successfully added to database.");
                statement.close();

            } catch (SQLException e){
                System.out.println ("SQL Exception when adding friend relationship to database.");
                e.printStackTrace();
            }
        }
    }
    
    
    //get the all the friends of a user    
    public ArrayList<String> getFriendList(String username){
    	
        ArrayList<String> friendList = new ArrayList<String>();
    	int user_id=0, friendID = 0;
    	String friendName;
    	
        if(DBconnection.openConnectionToDB(db)){
               
             try{           	 
            	 user_id = findUserID(username);
                
                //get the userID of friends from database
                query = "SELECT user_one_ID FROM Relationship " + "WHERE user_two_ID = ? AND status = 1";
                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setInt(1, user_id);
                
                ResultSet rs = statement.executeQuery();
                
                if (rs.next()) {
                    friendID = Integer.parseInt(rs.getString("user_one_ID"));
                    friendName = findUsername(friendID);
                    friendList.add(friendName);
                }
                
                query = "SELECT user_two_ID FROM Relationship " + "WHERE user_one_ID = ? AND status = 1";
                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setInt(1, user_id);
                
                rs = statement.executeQuery();
                
                if (rs.next()) {
                    friendID = Integer.parseInt(rs.getString("user_two_ID"));
                    friendName = findUsername(friendID);
                    friendList.add(friendName);
                }
                
                System.out.println("Friend list are obtained successfully.");
                statement.close();

            } catch (SQLException e){
                System.out.println ("SQL Exception when getting friend list from database.");
                e.printStackTrace();
            }
        }
        
        return friendList;
    }
    
    
    //when a user deletes a relationship with another user OR 
    //when a user declines a friend request, the relationship record will be deleted from the relationship table
    public void deleteRelationshipFromDB(String username1,String username2){
    	
        int user_one_id=0, user_two_id=0;
    	
        if(DBconnection.openConnectionToDB(db)){
               
           try{           	 
            	 user_one_id = findUserID(username1);
            	 user_two_id = findUserID(username2);
            	 
                
                //delete the relationship from table
            	query = "DELETE FROM Relationship WHERE user_one_ID = ? AND user_two_ID=?";
                statement = (PreparedStatement) connection.prepareStatement(query);
                
                if(user_one_id<user_two_id) {
                    statement.setInt(1, user_one_id);
                    statement.setInt(2, user_two_id);
                } else {
                	statement.setInt(1, user_two_id);
                    statement.setInt(2, user_one_id);
                }              
                
                statement.executeUpdate();
                System.out.println("The relationship is successfully deleted from database.");
                statement.close();

            } catch (SQLException e){
                System.out.println ("SQL Exception when deleting relationship to database.");
                e.printStackTrace();
            }
        }
    	
       
    }

    
    // this method is used to find the userID through the username
    private int findUserID(String username) {
           int userID =0;
    	
        if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();
                
                //get the userID for user one
                query = "SELECT userID FROM User " + "WHERE username = ?";

                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setString(1,username);
                
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    userID = Integer.parseInt(rs.getString("userID"));
                }
               
                statement.close();
                

            } catch (SQLException e){
                System.out.println ("SQL Exception when finding the username.");
                e.printStackTrace();
            }
        }
        
        return userID;
    }
    
 // this method is used to find the username through the userID
    private String findUsername(int userID) {
           String username = "";
    	
        if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();
                
                //get the userID for user one
                query = "SELECT username FROM User " + "WHERE userID = ?";

                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setInt(1,userID);
                
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    username = rs.getString("username");
                }
               
                statement.close();               

            } catch (SQLException e){
                System.out.println ("SQL Exception when finding the userID.");
                e.printStackTrace();
            }
        }
        
        return username;
    }
    
}
