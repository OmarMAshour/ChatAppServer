/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatappserver;

import java.util.ArrayList;
import static chatappserver.ChatAppServer.*;

/**
 *
 * @author virtual
 */
public class MultiChat {
    public int roomNumber;
    public ArrayList<User> roomUsers = new ArrayList<User>();
    
    public MultiChat(ArrayList<User> roomUsers){
        int max =0;
        for(MultiChat multi: multiChats){
            if(multi.roomNumber>max){
                max=multi.roomNumber;
            }
        }
        this.roomNumber = max+1;
        this.roomUsers=roomUsers;
        multiChats.add(this);
    }
}