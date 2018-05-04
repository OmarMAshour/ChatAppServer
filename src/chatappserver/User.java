/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatappserver;

/**
 *
 * @author root
 */
public class User {
    String ip;
    String username;
    boolean online;

    

    public User(String ip, String username, boolean online) {
        this.ip = ip;
        this.username = username;
        this.online = online;
    }
    
    
}
