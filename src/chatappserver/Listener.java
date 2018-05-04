/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatappserver;

import org.json.simple.parser.JSONParser;

import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static chatappserver.ChatAppServer.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

/**
 *
 * @author root
 */
public class Listener extends Thread {

    ServerSocket server;
    int port = 8001;

    public Listener(int port) {
        this.port = port;

        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Listener() {
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Socket clientSocket;
        try {
            while ((clientSocket = server.accept()) != null) {
                InputStream is = clientSocket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = br.readLine();
                if (line != null) {
                    System.out.println(line);
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(line);
                    String type = (String) jsonObject.get("type");
                    if (type.equals("connect")) {
                        if (!isAvailableUser((String) jsonObject.get("ip")) && !isBannedUser((String) jsonObject.get("ip"))) {
                            availableUsers.add(new User((String) jsonObject.get("ip"), (String) jsonObject.get("username"), true));
                            JSONObject jsonToUser = new JSONObject();
                            JSONArray users = new JSONArray();
                            users.addAll(availableUsers);
                            jsonToUser.put("type", "sendusers");
                            jsonToUser.put("availableusers", users);
                            transmitter.setData(jsonToUser, (String) jsonObject.get("ip"));
                            transmitter.start();
                        }

                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
