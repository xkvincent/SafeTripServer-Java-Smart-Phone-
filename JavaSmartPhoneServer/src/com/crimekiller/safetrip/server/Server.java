package com.crimekiller.safetrip.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.crimekiller.safetrip.Database.CreateDatabase;
import com.crimekiller.safetrip.Database.CreateTable;
import com.crimekiller.safetrip.Database.RelationshipCRUD;
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
//		
		CreateDatabase createDB = new CreateDatabase();
		createDB.createDatabase(dataBaseName);
		CreateTable createTable = new CreateTable(dataBaseName);

		User a = new User("a","123","123");
	    User b = new User("b","124","124");
	    User c = new User("c","125","125");
	    User d = new User("d","126","126");
	    User e = new User("e","127","127");
		
	    UserCRUD userCrud = new UserCRUD(dataBaseName);
		userCrud.addUserToDB(a);
		userCrud.addUserToDB(b);
		userCrud.addUserToDB(c);
		userCrud.addUserToDB(d);
		userCrud.addUserToDB(e);
		
		RelationshipCRUD relationCRUD = new RelationshipCRUD(dataBaseName);
		//relationCRUD.addPendingRelationshipToDB(username1, username2, actionUsername);
		// a & b are friends
		// c -> a send friend request
		// a -> d send friend request
		relationCRUD.addPendingRelationshipToDB("a", "b", "a");
		relationCRUD.addFreiendRelationshipToDB("a", "b","b");
		relationCRUD.addPendingRelationshipToDB("c","a","c");
		relationCRUD.addPendingRelationshipToDB("a","d","a");
		
		Server server = new Server();
		server.runServer();
	}

}
