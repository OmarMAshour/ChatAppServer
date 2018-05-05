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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
                            for (User user : availableUsers) {
                                if (user.online) {
                                    JSONObject jsonToUser = new JSONObject();
//                                    JSONArray users = new JSONArray();
//                                    users.addAll(availableUsers);
                                    Type ListType = new TypeToken<ArrayList<User>>() {
                                    }.getType();
                                    Gson gson = new Gson();
                                    String jsonArrayList = gson.toJson(availableUsers, ListType);
                                    System.out.println();
                                    jsonToUser.put("type", "sendusers");
                                    jsonToUser.put("availableusers", jsonArrayList);
                                    transmitter.setData(jsonToUser, user.ip);
                                    transmitter.run();
                                }
                            }
                        }

                    } else if (type.equals("ban")) {
                        Gson gson = new Gson();
                        TypeToken<ArrayList<String>> token = new TypeToken<ArrayList<String>>() {
                        };
                        String json = (String) jsonObject.get("iplist");
                        ArrayList<String> IPsToBan = gson.fromJson(json, token.getType());

                        for (String iptoban : IPsToBan) {
                            for (int i = 0; i < availableUsers.size(); i++) {
                                if (availableUsers.get(i).ip.equals(iptoban)) {
                                    bannedUsers.add(new User(availableUsers.get(i).ip, availableUsers.get(i).username, true));
                                    JSONObject banObject = new JSONObject();
                                    banObject.put("type", "banned");
                                    transmitter.setData(banObject, availableUsers.get(i).ip);
                                    transmitter.run();
                                    availableUsers.remove(availableUsers.get(i));

                                }
                            }
                        }
                    } else if (type.equals("disconnect")) {
                        String ip = (String) jsonObject.get("ip");
                        for (User user : availableUsers) {
                            if (user.ip.equals(ip)) {
                                user.online = false;

                            }
                        }
                        for (User user : availableUsers) {
                            if (user.online) {
                                JSONObject jsonToUser = new JSONObject();
//                                    JSONArray users = new JSONArray();
//                                    users.addAll(availableUsers);
                                Type ListType = new TypeToken<ArrayList<User>>() {
                                }.getType();
                                Gson gson = new Gson();
                                String jsonArrayList = gson.toJson(availableUsers, ListType);
                                System.out.println();
                                jsonToUser.put("type", "sendusers");
                                jsonToUser.put("availableusers", jsonArrayList);
                                transmitter.setData(jsonToUser, user.ip);
                                transmitter.run();
                            }
                        }
                    } else if (type.equals("multimsg")) {
                        int roomnumber = (int) jsonObject.get("roomnumber");
                        for (MultiChat chat : multiChats) {
                            if (chat.roomNumber == roomnumber) {
                                for (User user : chat.roomUsers) {
                                    JSONObject toUserObject = new JSONObject();
                                    toUserObject.put("type", "multimsg");
                                    toUserObject.put("roomnumber", (int) chat.roomNumber);
                                    toUserObject.put("msg", (String) jsonObject.get("msg"));

                                    transmitter.setData(toUserObject, user.ip);
                                    transmitter.run();
                                }
                            }
                        }
                    } else if (type.equals("openmulti")) {
                        Gson gson = new Gson();
                        TypeToken<ArrayList<String>> token = new TypeToken<ArrayList<String>>() {
                        };
                        String json = (String) jsonObject.get("ips");
                        ArrayList<String> ips = new ArrayList<String>();
                        ArrayList<User> roomUsers = new ArrayList<User>();
                        ips = gson.fromJson(json, token.getType());

                        for (String ip : ips) {
                            for (User user : availableUsers) {
                                if (user.ip.equals(ip)) {
                                    roomUsers.add(user);
                                }
                            }
                        }
                        MultiChat chat = new MultiChat(roomUsers);
                        JSONObject sendobject = new JSONObject();
                        sendobject.put("type", "openmulti");
                        Type ListType = new TypeToken<ArrayList<User>>() {
                        }.getType();
                        Gson gsonn = new Gson();
                        String jsonArrayList = gsonn.toJson(roomUsers, ListType);
                        System.out.println();
                        sendobject.put("users", jsonArrayList);
                        sendobject.put("roomnumber", (int)chat.roomNumber);
                        for (User user : roomUsers) {
                            transmitter.setData(sendobject, user.ip);
                            transmitter.run();
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
