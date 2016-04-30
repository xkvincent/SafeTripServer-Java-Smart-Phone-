/**
 * 
 */
package com.crimekiller.safetrip.server;

/**
 * @author  Wenlu Zhang 
 * @AndrewID: wenluz
 * April 9, 2016
 *
 * 
 */
public interface SocketServerConstants {
	int PORT = 4000;
	static String dataBaseName = "SafeTrip";
	static String GET_FRIEND_LIST_COMMAND = "Get Friend List";
	static String GET_USER_LIST_COMMAND = "Get User List";
	static String SEND_FRIEND_REQUEST_COMMAND = "Send Friend Request" ;
	static String GET_PENDING_REQUEST_COMMAND = "Get Pending Request";
	static String ACCEPT_PENDING_REQUEST_COMMAND = "Accept Pending Request";
    static String DECLINE_PENDING_REQUEST_COMMAND = "Decline Pending Request";
    static String ADD_NEW_POST_COMMAND = "New Post";
    static String DELETE_POST_COMMAND= "Delete Post";
    static String ALL_POST_COMMAND= "Get All Post";
    static String LOG_IN_COMMAND = "Login";
    static String SIGN_UP_COMMAND = "SignUp";
    static String EDIT_PASSWORD_COMMAND = "EditPassword";
    static String ADMIN_GET_ALLPOST_COMMAND = "Admin Get All Post List";
    static String TRACK_FRIEND_LIST_COMMAND = "Track Friend List";
    static String SHARE_LOCATION = "Share my location";
}
