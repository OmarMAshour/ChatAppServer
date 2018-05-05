/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatappserver;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

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
    
    public static String getCurrentEnvironmentNetworkIp() {
        String currentHostIpAddress=null;
        if (currentHostIpAddress == null) {
            Enumeration<NetworkInterface> netInterfaces = null;
            try {
                netInterfaces = NetworkInterface.getNetworkInterfaces();

                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface ni = netInterfaces.nextElement();
                    Enumeration<InetAddress> address = ni.getInetAddresses();
                    while (address.hasMoreElements()) {
                        InetAddress addr = address.nextElement();
                        //                      log.debug("Inetaddress:" + addr.getHostAddress() + " loop? " + addr.isLoopbackAddress() + " local? "
                        //                            + addr.isSiteLocalAddress());
                        if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()
                                && !(addr.getHostAddress().indexOf(":") > -1)) {
                            currentHostIpAddress = addr.getHostAddress();
                        }
                    }
                }
                if (currentHostIpAddress == null) {
                    currentHostIpAddress = "127.0.0.1";
                }

            } catch (SocketException e) {
//                log.error("Somehow we have a socket error acquiring the host IP... Using loopback instead...");
                currentHostIpAddress = "127.0.0.1";
            }
        }
        return currentHostIpAddress;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        listener.start();
        System.out.println("The Server IP Address ==> "+getCurrentEnvironmentNetworkIp());
    }
    
}
