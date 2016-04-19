/**
 * 
 */
package com.crimekiller.safetrip.server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import com.crimekiller.safetrip.Database.PostCRUD;
import com.crimekiller.safetrip.Database.UserCRUD;
import com.crimekiller.safetrip.model.Post;
import com.crimekiller.safetrip.model.User;

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
	private static String dataBaseName = "SafeTrip";
	
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
				
				UserCRUD userCrud = new UserCRUD(dataBaseName);
				ArrayList<User> userList = userCrud.getAllUser();
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
			}else if (command.equals("New Post")){
				
				PostCRUD postCrud = new PostCRUD(dataBaseName);
				
				try {
					
					Post newPost = (Post)objInputStream.readObject();
					postCrud.addPostToDB(newPost);
					ArrayList<Post> postList = postCrud.getAllPost();
					objOutputStream.writeObject( postList );
					objOutputStream.flush();
					System.out.println("Add new post Successfully");
										
				} catch (ClassNotFoundException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
				
			}else if(command.equals("Get All Post")) {
                PostCRUD postCrud = new PostCRUD(dataBaseName);
				
				try {
					String username = (String)objInputStream.readObject();
					
					
					ArrayList<Post> postList = postCrud.getAllPost();
					objOutputStream.writeObject( postList );
					objOutputStream.flush();
					System.out.println("Get All Posts Successfully");
										
				}  catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  catch (IOException e) {
					e.printStackTrace();
				}
				break;				
				
			}else if (command.equals("Delete Post")) {
				
				 PostCRUD postCrud = new PostCRUD(dataBaseName);
					
					try {
						Post post = (Post)objInputStream.readObject();
						
						postCrud.deletePostFromDB(post);
						
						ArrayList<Post> postList = postCrud.getAllPost();
						objOutputStream.writeObject( postList );
						objOutputStream.flush();
						System.out.println("Get All Posts Successfully");
											
					}  catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  catch (IOException e) {
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
