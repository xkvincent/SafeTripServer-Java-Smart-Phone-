package com.crimekiller.safetrip.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.crimekiller.safetrip.Database.CreateDatabase;
import com.crimekiller.safetrip.Database.CreateTable;
import com.crimekiller.safetrip.Database.UserCRUD;
import com.crimekiller.safetrip.model.User;

/**
 * @author  Wenlu Zhang 
 * @AndrewID: wenluz
 * April 9, 2016
 *
 */

public class Server implements SocketServerConstants{
	
	  private ServerSocket serverSocket = null;
	  private DefaultSocketServer defaultServerSocket = null;
	  
	  //Initialize ServerSocket
	  public Server(){
		  try {
				serverSocket = new ServerSocket(PORT);
				System.out.println("Server is running on Port: " + PORT); 
		   }catch (Exception e) {
		      	e.printStackTrace();  
		        System.exit(1);
		   }
	  }
	  
	  public void runServer(){
		try { 
			 while(true) {
			  	Socket socket = serverSocket.accept();
			  
			  	System.out.println("Accept a client from: "+ socket.getInetAddress().getHostAddress());
			  	//Use Thread to handle each socket
			  	defaultServerSocket = new DefaultSocketServer( socket );
			  	defaultServerSocket.start();
			 }
			  	  	
		  }catch( Exception e) {
			  e.printStackTrace();  
	          System.exit(1);
		  }     
	}  
	  
	//Main Driver starts server  
	  public static void main( String[] arg ){
		String dataBaseName = "SafeTrip";
		
		CreateDatabase createDB = new CreateDatabase();
		createDB.createDatabase(dataBaseName);
		CreateTable createTable = new CreateTable(dataBaseName);
		
		User a = new User("UserA","123","123");
	    User b = new User("UserB","124","124");
	    User c = new User("UserC","125","125");
		
	    UserCRUD userCrud = new UserCRUD(dataBaseName);
		userCrud.addUserToDB(a);
		userCrud.addUserToDB(b);
		userCrud.addUserToDB(c);
			
		Server server = new Server();
		server.runServer();
	}

}
