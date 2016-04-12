/**
 * 
 */
package com.crimekiller.safetrip.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import com.crimekiller.safetrip.Model.User;

/**
 * @author  Wenlu Zhang 
 * @AndrewID: wenluz
 * April 9, 2016
 *
 */

public class DefaultSocketServer extends Thread
								implements SocketServerInterface,
										   SocketServerConstants {
	
	private Socket socket;
	private ObjectInputStream objInputStream = null;
	private ObjectOutputStream objOutputStream = null;
	
	public DefaultSocketServer( Socket socket ){
		this.socket = socket;
	}
	
	public void run(){
		if(openConnection()){
			handleSession();	
		    closeConnection();
		}
		else{
			System.out.print("Connection Can not be Set Up");
		}
	}
	
	public boolean openConnection() {
		try {
			objOutputStream = new ObjectOutputStream( socket.getOutputStream() ); 
			objInputStream = new ObjectInputStream( socket.getInputStream() ); 
			
		} catch (Exception e) {
			System.out.print("cannot open socket");
			return false;
		}
		return true;
	}
	
	public void handleSession(){
		String command = null;
		String response = null;
		
		while(true){
			try {
				//Get command from client					
				command = (String)objInputStream.readObject();	
			}catch ( IOException | ClassNotFoundException e ) {
				e.printStackTrace();
			}
			
			if( command.equals("Manage Friend")){
			
				ArrayList<User> userList = User.getFriends();	
				try {
						objOutputStream.writeObject( userList );
						objOutputStream.flush();
						System.out.println("Manage Friend Successfully");
					} catch (IOException e) {
						e.printStackTrace();
					}
				break;
			}else if( command.equals("Administrate User")){
				
				ArrayList<User> userList = User.getUser();	
				try {
						objOutputStream.writeObject( userList );
						objOutputStream.flush();
						System.out.println("Administrate User Successfully");
					} catch (IOException e) {
						e.printStackTrace();
					}
				break;
			}else{
				System.out.println("No Request Received");
			}
	}
}

	@Override
	public void closeConnection() {
		try {
			objInputStream.close();
			objOutputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
