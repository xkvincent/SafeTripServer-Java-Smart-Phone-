package com.crimekiller.safetrip.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
	private static final boolean DEBUG = true;

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static String URL= "jdbc:mysql://127.0.0.1:3306/";
	//private static String URL = "jdbc:mysql://localhost/";//Modify
    private static String USERNAME = "root";
    private static String PASSWORD= "";//Modify
    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    //connection to mySQL
    public static boolean openConnection() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connecting to Database.");
        } catch (SQLException e) {
            if (DEBUG) System.out.println ("Cannot connect to this database.");
            return false;
        } catch (ClassNotFoundException e) {
        	System.err.println(e);
        	if (DEBUG) System.out.println ("ClassNotFoundException");
            return false;
		}
        return true;
    }

    //connection to the specific database
    public static boolean openConnectionToDB(String database) {
        try {
            String dbURL = URL + database;
            //Class.forName(DRIVER);
            connection = DriverManager.getConnection(dbURL, USERNAME, PASSWORD);
            //System.out.println("Connect to database: " + database);
        } catch (SQLException e) {
            if (DEBUG) System.out.println ("SQL Exception when connecting to database: " + database);
            return false;
        }
        return true;
    }
}
