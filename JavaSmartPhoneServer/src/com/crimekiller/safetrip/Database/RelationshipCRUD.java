package com.crimekiller.safetrip.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.crimekiller.safetrip.model.User;


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
                query = "INSERT INTO Relationship (user_one_id,user_two_id,status, action_user_id) VALUES (?,?,?,?)";
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

                query = "UPDATE Relationship SET status =1, action_user_id =? Where user_one_id =? AND user_two_id =? AND status =0";

            //    query = "UPDATE Relationship SET status = 1 AND action_user_ID = ? Where user_one_ID =? AND user_two_ID = ? AND status = 0";

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
                query = "SELECT user_one_id FROM Relationship " + "WHERE user_two_id = ? AND status = 1";
                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setInt(1, user_id);
                
                ResultSet rs = statement.executeQuery();
                
                if (rs.next()) {
                    friendID = Integer.parseInt(rs.getString("user_one_id"));
                    friendName = findUsername(friendID);
                    friendList.add(friendName);
                }
                
                query = "SELECT user_two_id FROM Relationship " + "WHERE user_one_id = ? AND status = 1";
                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setInt(1, user_id);
                
                rs = statement.executeQuery();
                
                if (rs.next()) {
                    friendID = Integer.parseInt(rs.getString("user_two_id"));
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
            	query = "DELETE FROM Relationship WHERE user_one_id = ? AND user_two_id=?";
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
    
    //get Friend ID through the username 
	public ArrayList<Integer> getFriendIDList(String username) {
		int finderID = findUserID(username);
		int userID;
		
		ArrayList<Integer> friendIDList = new ArrayList<Integer>();	
		 if(DBconnection.openConnectionToDB(db)){
	            try{
	                connection = (Connection) DBconnection.getConnection();
	                
	                //get friendlist for username
	                query = "SELECT user_one_id FROM Relationship " + "WHERE status = 1 AND user_two_id = ?"; 
	                statement = (PreparedStatement) connection.prepareStatement(query);
	                statement.setInt(1,finderID);
	                ResultSet rs = statement.executeQuery();
	                
	                while(rs.next()){
	                	 userID = Integer.parseInt(rs.getString("user_one_id"));
	                	 friendIDList.add(userID);
	                }
	                
	                query = "SELECT user_two_id FROM Relationship " + "WHERE status = 1 AND user_one_id = ?"; 
	                statement = (PreparedStatement) connection.prepareStatement(query);
	                statement.setInt(1,finderID);
	                ResultSet rs2 = statement.executeQuery();
	                while(rs2.next()){
	                	 userID = Integer.parseInt(rs2.getString("user_two_id"));
	                	 friendIDList.add(userID);
	                }        
	                statement.close();
	                
	            } catch (SQLException e){
	                System.out.println ("SQL Exception when getting friend list ID.");
	                e.printStackTrace();
	            }
	        }        
	        return friendIDList;
	}
    
	//get Friend List through the username using getFriendIDList
	//Wenlu
	public ArrayList<User> getFriendNameList(String username) {
		ArrayList<Integer> friendIDList = getFriendIDList( username );
		ArrayList<User> friendList = new ArrayList<User>();	
		if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();
                
                for( int userID: friendIDList) {
	                query = "SELECT username, email, password FROM User " + "WHERE userID = ?";
	
	                statement = (PreparedStatement) connection.prepareStatement(query);
	                statement.setInt(1,userID);
	                
	                ResultSet rs = statement.executeQuery();
	                if (rs.next()) {
	                	String userName = rs.getString("username");
	                	String email= rs.getString("email");
	                	String passWord = rs.getString("password");
	                	User user = new User(userName, passWord, email);
	                    friendList.add(user);
	                }
                }   
                statement.close();
            } catch (SQLException e){
                System.out.println ("SQL Exception when getting Friend List.");
                e.printStackTrace();
            }
        }   
		return friendList;
	}

	//get Pending Request List ID through username 
	public ArrayList<Integer> getPendingRequestIDList(String username){
		int finderID = findUserID(username);
		ArrayList<Integer> pendingRequestIDList = new ArrayList<Integer>();	
		
		if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();               
                //user_two is the finder
                //action_user must be user_one in previous sending request action
                query = "SELECT user_one_id FROM Relationship " +
                		"WHERE status = 0 AND action_user_Id != ? AND user_two_id = ?"; 
                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setInt(1,finderID);
                statement.setInt(2,finderID);
                ResultSet rs = statement.executeQuery();
                
                while(rs.next()){
                	 int userID = Integer.parseInt(rs.getString("user_one_id"));
                	 pendingRequestIDList.add(userID);
                }
                //user_one is the finder
                //action_user must be user_two in previous sending request action
                query = "SELECT user_two_id FROM Relationship " + 
                		"WHERE status = 0 AND action_user_Id != ? AND user_one_id = ?"; 
                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setInt(1,finderID);
                statement.setInt(2,finderID);
                
                ResultSet rs2 = statement.executeQuery();
                while(rs2.next()){
                	 int userID = Integer.parseInt(rs2.getString("user_two_id"));
                	 pendingRequestIDList.add(userID);
                }        
                statement.close();
                
            } catch (SQLException e){
                System.out.println ("SQL Exception when getting Pending Request ID List.");
                e.printStackTrace();
            }
        }
		return pendingRequestIDList;
	}
	
	//get Pending Request List through username
	public ArrayList<String> getPendingRequestList(String userName) {
		ArrayList<Integer> pendingRequestIDList = getPendingRequestIDList( userName );
		ArrayList<String> pendingRequestList = new ArrayList<String>();	
		if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();
                
                //get the userID for user one
                for( int userID: pendingRequestIDList) {
	                query = "SELECT username FROM User " + "WHERE userID = ?";
	
	                statement = (PreparedStatement) connection.prepareStatement(query);
	                statement.setInt(1,userID);
	                
	                ResultSet rs = statement.executeQuery();
	                if (rs.next()) {
	                	String username = rs.getString("username");
	                	pendingRequestList.add(username);
	                }
                }   
                statement.close();
            } catch (SQLException e){
                System.out.println ("SQL Exception when getting Pending Request List.");
                e.printStackTrace();
            }
        }   
		return pendingRequestList;
	}

	//get Already Sent Request from username List ID through username 
	public ArrayList<Integer> getAlreadyRequestIDList(String username){
			int finderID = findUserID(username);
			ArrayList<Integer> alreadyRequestIDList = new ArrayList<Integer>();	
			
			if(DBconnection.openConnectionToDB(db)){
	            try{
	                connection = (Connection) DBconnection.getConnection();               
	                //user_two is the finder
	                //action_user == user_two 
	                query = "SELECT user_one_id FROM Relationship " +
	                		"WHERE status = 0 AND action_user_Id = ? AND user_two_id = ?"; 
	                statement = (PreparedStatement) connection.prepareStatement(query);
	                statement.setInt(1,finderID);
	                statement.setInt(2,finderID);
	                ResultSet rs = statement.executeQuery();
	                
	                while(rs.next()){
	                	 int userID = Integer.parseInt(rs.getString("user_one_id"));
	                	 alreadyRequestIDList.add(userID);
	                }
	                //user_one is the finder
	                //action_user == user_one
	                query = "SELECT user_two_id FROM Relationship " + 
	                		"WHERE status = 0 AND action_user_Id = ? AND user_one_id = ?"; 
	                statement = (PreparedStatement) connection.prepareStatement(query);
	                statement.setInt(1,finderID);
	                statement.setInt(2,finderID);
	                
	                ResultSet rs2 = statement.executeQuery();
	                while(rs2.next()){
	                	 int userID = Integer.parseInt(rs2.getString("user_two_id"));
	                	 alreadyRequestIDList.add(userID);
	                }        
	                statement.close();
	                
	            } catch (SQLException e){
	                System.out.println ("SQL Exception when getting Pending Request ID List.");
	                e.printStackTrace();
	            }
	        }
			return  alreadyRequestIDList;
		}
	//get Already Request List through username
	
	public ArrayList<String> getAlreadyRequestList(String userName) {
		ArrayList<Integer> alreadyRequestIDList = getAlreadyRequestIDList( userName );
		ArrayList<String> alreadyRequestList = new ArrayList<String>();	
		if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();
                
                //get the userID for user one
                for( int userID: alreadyRequestIDList) {
	                query = "SELECT username FROM User " + "WHERE userID = ?";
	
	                statement = (PreparedStatement) connection.prepareStatement(query);
	                statement.setInt(1,userID);
	                
	                ResultSet rs = statement.executeQuery();
	                if (rs.next()) {
	                	String username = rs.getString("username");
	                	alreadyRequestList.add(username);
	                }
                }   
                statement.close();
            } catch (SQLException e){
                System.out.println ("SQL Exception when getting Pending Request List.");
                e.printStackTrace();
            }
        }   
		return alreadyRequestList;
	}
}
