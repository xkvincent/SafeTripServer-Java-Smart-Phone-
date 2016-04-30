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

import com.crimekiller.safetrip.Database.LocationCRUD;
import com.crimekiller.safetrip.Database.PostCRUD;
import com.crimekiller.safetrip.Database.RelationshipCRUD;
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
			
			if( command.equals(GET_FRIEND_LIST_COMMAND)){		
				
				UserCRUD userCrud = new UserCRUD(dataBaseName);
				RelationshipCRUD relationshipCrud = new RelationshipCRUD(dataBaseName);		
				
				ArrayList<User> userList = userCrud.getAllUser();	
				
				try {
						String username = (String)objInputStream.readObject();
						System.out.println("Get userName " + username);
						ArrayList<User> friendList = 
									relationshipCrud.getFriendNameList(username);
						ArrayList<String> pendingList = 
									relationshipCrud.getPendingRequestList(username);
						ArrayList<String> requestList =
								 	relationshipCrud.getAlreadyRequestList(username);
						
						objOutputStream.writeObject( friendList );
						objOutputStream.flush();
						objOutputStream.writeObject( userList );
						objOutputStream.flush();
						objOutputStream.writeObject(pendingList);
						objOutputStream.flush();
						objOutputStream.writeObject(requestList);
						objOutputStream.flush();
						
						System.out.println( "GET FRIEND LIST COMMAND succeed ");
						
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				break;
			}else if( command.equals(GET_USER_LIST_COMMAND)){
				
				UserCRUD userCrud = new UserCRUD(dataBaseName);
				ArrayList<User> userList = userCrud.getAllUser();		
				
				try {
						objOutputStream.writeObject( userList );
						objOutputStream.flush();
						System.out.println( "GET USER LIST COMMAND succeed ");
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				break;
			}else if( command.equals(SEND_FRIEND_REQUEST_COMMAND)){
				
				RelationshipCRUD relationshipCrud = new RelationshipCRUD(dataBaseName);	
				try {
					String userName = (String)objInputStream.readObject();
					String friendRequestName = (String)objInputStream.readObject();
					relationshipCrud.addPendingRelationshipToDB(userName, 
												friendRequestName, userName);
					System.out.println( "SEND FRIEND REQUEST COMMAND succeed ");
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}else if( command.equals(GET_PENDING_REQUEST_COMMAND)){
				
				RelationshipCRUD relationshipCrud = new RelationshipCRUD(dataBaseName);	
				try {
					String userName = (String)objInputStream.readObject();			
					ArrayList<String>pendingRequest=
					relationshipCrud.getPendingRequestList(userName);
					objOutputStream.writeObject(pendingRequest);
					objOutputStream.flush();
					System.out.println( "GET PENDING REQUEST COMMAND succeed ");
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}else if( command.equals(ACCEPT_PENDING_REQUEST_COMMAND)){
				
				RelationshipCRUD relationshipCrud = new RelationshipCRUD(dataBaseName);	
				try {
					String userName = (String)objInputStream.readObject();
					String requestUserName = (String) objInputStream.readObject();
					relationshipCrud.addFreiendRelationshipToDB( userName, 
													requestUserName, userName );
					System.out.println( "ACCEPT PENDING REQUEST COMMAND succeed ");
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			}else if( command.equals(DECLINE_PENDING_REQUEST_COMMAND)){
				
				RelationshipCRUD relationshipCrud = new RelationshipCRUD(dataBaseName);	
				try {
					String userName = (String)objInputStream.readObject();
					String requestUserName = (String) objInputStream.readObject();
					relationshipCrud.deleteRelationshipFromDB( userName, 
															   requestUserName );
					System.out.println( "DECLINE PENDING REQUEST COMMAND succeed ");
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}else if(command.equals(ADMIN_GET_ALLPOST_COMMAND)){
				
				PostCRUD postCrud = new PostCRUD(dataBaseName);	
				ArrayList<Post> allPostList = postCrud.getAllPost();

				try {
					objOutputStream.writeObject( allPostList );
					objOutputStream.flush();
					System.out.println( "GET USER LIST COMMAND succeed ");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}else if (command.equals(TRACK_FRIEND_LIST_COMMAND)){
				RelationshipCRUD relationshipCrud = new RelationshipCRUD(dataBaseName);	
				LocationCRUD locationCrud = new LocationCRUD(dataBaseName);
				ArrayList<String> locationList = new ArrayList<String>();
				System.out.println("Before Track Friend List");
				
				try {
					String userName = (String)objInputStream.readObject();
					String requestName = (String) objInputStream.readObject();
					ArrayList<String> friendNameList = 
								relationshipCrud.getFriendList(userName);
					for( String name : friendNameList ){
						System.out.println( name );
					}
					if( friendNameList.contains(requestName)){
						locationList = locationCrud.getUserLocation(requestName);
						System.out.println( "TRACK FRIEND LIST COMMAND succeed ");
					}
					objOutputStream.writeObject( locationList );
					objOutputStream.flush();
					
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}else if(command.equals(SHARE_LOCATION)){
				
				LocationCRUD locationCrud = new LocationCRUD(dataBaseName);
				
				try {
					String username = (String) objInputStream.readObject();
					ArrayList<String>locationList = (ArrayList<String>) 
													objInputStream.readObject();
					String longitude = locationList.get(0);
					String latitude = locationList.get(1);
					locationCrud.addUserLocationToDB(username, latitude, longitude);
					    
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			else if (command.equals(ADD_NEW_POST_COMMAND)){
				
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
				
			}else if(command.equals(ALL_POST_COMMAND)) {
                PostCRUD postCrud = new PostCRUD(dataBaseName);
                RelationshipCRUD relationshipCrud = new RelationshipCRUD(dataBaseName);
                
                try {
				   String username = (String)objInputStream.readObject();				
				   ArrayList<Post> postList = new ArrayList<Post>();
				   
				   ArrayList<String> friendList = relationshipCrud.getFriendList(username);
				   postList = postCrud.getFriendPost(friendList);
					   			
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
				
			}else if (command.equals(DELETE_POST_COMMAND)) {
				
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
				
				
			}else if(command.equals(EDIT_PASSWORD_COMMAND)){	
				
				UserCRUD userCrud = new UserCRUD(dataBaseName);
				
				try {
					User user = (User)objInputStream.readObject();
					String newPassword = user.getPassword();
					userCrud.EditUserInDB(user,newPassword);

					System.out.println("Edit Password Successfully");
										
				}  catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  catch (IOException e) {
					e.printStackTrace();
				}
				break;				
			}else if(command.equals(SIGN_UP_COMMAND)){
				UserCRUD userCrud = new UserCRUD(dataBaseName);
				Boolean result;
				try {
					String username = (String)objInputStream.readObject();
					System.out.println("get user name from client:"+username);
					User user = userCrud.getUserInDB(username);
					if(user == null){
						result = true;
						objOutputStream.writeObject( result );
						objOutputStream.flush();
						
						User newUser = (User)objInputStream.readObject();
						userCrud.addUserToDB(newUser);
						System.out.println("SignUp Successfully");
					}
					else{
						result = false;
						objOutputStream.writeObject( result );
						objOutputStream.flush();
					}
																			
				}  catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  catch (IOException e) {
					e.printStackTrace();
				}
				break;						
				
			}else if(command.equals(LOG_IN_COMMAND)){
					UserCRUD userCrud = new UserCRUD(dataBaseName);		
				try {
					String username = (String)objInputStream.readObject();
					User user = userCrud.getUserInDB(username);
					
					objOutputStream.writeObject( user );
					objOutputStream.flush();
					System.out.println("Login Successfully");
										
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
