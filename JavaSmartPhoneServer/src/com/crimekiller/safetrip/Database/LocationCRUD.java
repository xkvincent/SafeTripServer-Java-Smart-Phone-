package com.crimekiller.safetrip.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LocationCRUD {
	
	private String db;
    private Connection connection;
    private PreparedStatement statement;
    private String query;

    public LocationCRUD(String database) {
        this.db = database;
    }
    
    public void addUserLocationToDB(String username, String latitude, String longitude){
        if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();
                
                query = "SELECT locationId FROM Location "
                        + "WHERE username = ? ";

                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setString(1, username);
                
                ResultSet rs = statement.executeQuery();
                
                if (rs.next()) {
                	
                    int locationId = Integer.parseInt(rs.getString("locationID"));
                    System.out.println("Lat: "+ latitude +"Longtitude " + longitude 
                    					+" locationId: " + locationId);
                    query = "UPDATE Location SET latitude =?, longitude =? WHERE LocationId =?";
                    statement = (PreparedStatement) connection.prepareStatement(query);
                    statement.setString(1, latitude);
                    statement.setString(2, longitude);
                    statement.setInt(3,locationId);
                    statement.executeUpdate();
                    //statement.close();
                    System.out.println("The location of " + username +" is successfully updated.");
                    statement.close();
                } else {
                	query = "INSERT INTO Location (username, latitude, longitude) VALUES(?,?,?)";
                    statement = connection.prepareStatement(query);
                    statement.setString(1, username);
                    statement.setString(2, latitude);
                    statement.setString(3, longitude);
                    statement.executeUpdate();

                    System.out.println("New user location is successfully added to database.");
                    statement.close();
                	
                }

                //add the user location to  table
                
            } catch (SQLException e){
                System.out.println ("SQL Exception when adding user location to database.");
                e.printStackTrace();
            }
        }
    }
    
    
    //get the geolocation for a specific user
    public ArrayList<String> getUserLocation(String username) {

        ArrayList<String> geoLocation = new ArrayList<String>();

        if(DBconnection.openConnectionToDB(db)){
            try{
                connection = (Connection) DBconnection.getConnection();

                query = "SELECT latitude, longitude FROM Location WHERE username = ?  ";
                statement = (PreparedStatement) connection.prepareStatement(query);
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){

                    String currentLatitude = rs.getString("latitude");
                    geoLocation.add(currentLatitude);
                    
                    String currentLongitude = rs.getString("longitude");
                    geoLocation.add(currentLongitude);

                }
                System.out.println("Geo location are obtained successfully.");
                statement.close();
            } catch (SQLException e){
                System.out.println ("SQL Exception when getting user geolocation from database.");
                e.printStackTrace();
            }
        }

        return geoLocation;
    }

}
