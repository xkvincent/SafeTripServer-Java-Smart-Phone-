package com.crimekiller.safetrip.Database;

import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {
	 private Statement statement;

	    public void createDatabase(String databaseName){
	        if(DBconnection.openConnection()){
	            try {
	                statement = (Statement) DBconnection.getConnection().createStatement();
	                //statement.executeUpdate("DROP DATABASE " + databaseName);
	                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + databaseName);
	                System.out.println("Database is successfully created!");
	                statement.close();
	            } catch (SQLException e){
	                System.out.println ("SQL Exception when creating new database.");
	                e.printStackTrace();
	            }
	        }
	    }
}
