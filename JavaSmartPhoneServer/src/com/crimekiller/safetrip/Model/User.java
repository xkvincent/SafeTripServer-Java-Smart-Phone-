package com.crimekiller.safetrip.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author  Wenlu Zhang 
 * @AndrewID: wenluz
 * April 9, 2016
 *
 * 
 */
 public class User implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2278521314817998938L;
	private String name;
    private String ID;
    private static ArrayList<User> users;
    private static ArrayList<User> friends;
    
    public User(String name){
        this.name = name;
    }
    
    public static ArrayList<User> getUser(){
        users = new ArrayList<User>();
        User a = new User("UserA");
        User b = new User("UserB");
        User c = new User("UserC");

        users.add(a);
        users.add(b);
        users.add(c);

        return users;
    }

    public static ArrayList<User> getFriends(){
    	friends = new ArrayList<User>();
        User a = new User("FreindA");
        User b = new User("FreindB");

        friends.add(a);
        friends.add(b);

        return friends;
    }
    
    public String getName() {
        return name;
    }

    public static User getUserByName(String name){
        for( User user: users ){
            if( user.getName().equals(name))
                return user;
        }
        return null;
    }
}
