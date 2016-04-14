package com.crimekiller.safetrip.model;

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
	private String password;
    private String name;
    private String email;;
    private static ArrayList<User> users;
    private static ArrayList<User> friends;
    
    public User(String name,String password, String email){
        this.name = name;
        this.password = password;
        this.email = email;

    }
    
    public static ArrayList<User> getUser(){
        users = new ArrayList<User>();
        User a = new User("UserA","123","123");
        User b = new User("UserB","124","124");
        User c = new User("UserC","125","125");

        users.add(a);
        users.add(b);
        users.add(c);

        return users;
    }

    public static ArrayList<User> getFriends(){
    	friends = new ArrayList<User>();
    	User a = new User("FriendA","211","211");
        User b = new User("FriendB","212","212");

        friends.add(a);
        friends.add(b);

        return friends;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPassword()
    {
        return password;
    }

    public String getEmail()
    {
        return email;
    }

    public static User getUserByName(String name){
        for( User user: users ){
            if( user.getName().equals(name))
                return user;
        }
        return null;
    }
}
