package com.crimekiller.safetrip.server;

import java.net.ServerSocket;
import java.net.Socket;

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
		Server server = new Server();
		server.runServer();
	}

}
