package com.crimekiller.safetrip.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.crimekiller.safetrip.model.User;

public class UserCRUD {
	private String db;
    private Connection connection;
    private PreparedStatement statement;
    private String query;

    public UserCRUD(String database) {
        this.db = database;
    }

    public void addUserToDB(User a){
        if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();

                //add the automobile to the Post table
                query = "INSERT INTO User (username, password, email) VALUES(?,?,?)";
                statement = connection.prepareStatement(query);
                statement.setString(1, a.getName());
                statement.setString(2, a.getPassword());
                statement.setString(3, a.getEmail());
                statement.executeUpdate();

                System.out.println("New user is successfully added to database.");
                statement.close();

            } catch (SQLException e){
                System.out.println ("SQL Exception when adding user to database.");
                e.printStackTrace();
            }
        }
    }

    public void EditUserInDB(User a, String newPassword){
        if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();

                query = "UPDATE User SET password = ? WHERE username = ?";
                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setString(1, newPassword);
                statement.setString(2, a.getName());
                statement.executeUpdate();

                System.out.println("The password is successfully updated.");
                statement.close();
            } catch (SQLException e){
                System.out.println ("SQL Exception when editing user password in database.");
                e.printStackTrace();
            }
        }
    }

    public ArrayList<User> getAllUser() {

        ArrayList<User> allUsers = new ArrayList<User>();

        if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();

                query = "SELECT username, password, email FROM User";
                statement = (PreparedStatement) connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){

                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");

                    User aUser = new User(username, password, email);
                    allUsers.add(aUser);

                }
                System.out.println("All users are obtained successfully.");
                statement.close();
            } catch (SQLException e){
                System.out.println ("SQL Exception when getting all users from database.");
                e.printStackTrace();
            }
        }

        return allUsers;
    }
    
}
