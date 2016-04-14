package com.crimekiller.safetrip.Database;

import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
	private Statement statement;
    private String database;

    String PostTable = "CREATE TABLE IF NOT EXISTS Post" + "(postID INT NOT NULL AUTO_INCREMENT," +
            "date varchar(255) NOT NULL,"+
            "licenseplate varchar(255) NOT NULL,"+
            "destination varchar(355) NOT NULL,"+
            "model varchar(255),"+"color varchar(255),"+ "owner varchar(255) NOT NULL,"+
            "departure varchar(355), PRIMARY KEY (postID));";

    String UserTable = "CREATE TABLE IF NOT EXISTS User" + "(userID INT NOT NULL AUTO_INCREMENT," +
            "username varchar(255) NOT NULL,"+  "email varchar(255) NOT NULL,"+
            "password varchar(355) NOT NULL, PRIMARY KEY (userID)); ";  //add email
    
    String RelationshipTable = "CREATE TABLE IF NOT EXISTS Relationship" +"(user_one_ID INT, user_two_ID INT," +
                               "status INT, action_user_ID INT);";
    

    public CreateTable(String database){
        this.database = database;
        createTable(PostTable);
        createTable(UserTable);
        createTable(RelationshipTable);
    }

    public void createTable(String query){

        if(DBconnection.openConnectionToDB(database)){
            try {
                statement = (Statement) DBconnection.getConnection().createStatement();
                statement.executeUpdate(query);
                System.out.println("Table is successfully created.");

            } catch (SQLException e){
                System.out.println ("SQL Exception when creating new table.");
                e.printStackTrace();
            }
        }
    }
}
