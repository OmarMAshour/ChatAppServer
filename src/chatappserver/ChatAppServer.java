/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatappserver;

import java.util.ArrayList;

/**
 *
 * @author root
 */
public class ChatAppServer {

    /**
     * @param args the command line arguments
     */
    public static ArrayList<MultiChat> multiChats = new ArrayList<MultiChat>();
    public static ArrayList<User> availableUsers = new ArrayList<User>();
    public static ArrayList<User> bannedUsers = new ArrayList<User>();
    public static Listener listener = new Listener();
    public static Transmitter transmitter = new Transmitter();
    public static boolean isAvailableUser(String ip){
        for(User user: availableUsers){
            if(user.ip.equals(ip)){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBannedUser(String ip){
        for(User user: bannedUsers){
            if(user.ip.equals(ip)){
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) {
        // TODO code application logic here
        listener.start();
        
    }
    
}
