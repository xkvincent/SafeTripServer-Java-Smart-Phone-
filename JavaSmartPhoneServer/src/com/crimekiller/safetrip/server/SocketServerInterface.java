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
public interface SocketServerInterface {
	
	public boolean openConnection();
	
    public void handleSession();
    
    public void closeConnection();

}
