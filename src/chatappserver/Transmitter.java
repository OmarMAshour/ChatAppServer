/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatappserver;

import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author root
 */
public class Transmitter extends Thread {

    String hostname;
    JSONObject jsonMessage;
    int port=8001;

    public Transmitter() {
    }

    public Transmitter(JSONObject jsonMessage, String hostname, int port) {
        this.jsonMessage = jsonMessage;
        this.hostname = hostname;
        this.port = port;
    }

    public void setData(JSONObject jsonMessage, String hostname) {
        this.jsonMessage = jsonMessage;
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void run() {
        try {
//                System.out.println("hey");
            StringWriter message = new StringWriter();
            jsonMessage.writeJSONString(message);

            String jsonText = message.toString();
            Socket s = new Socket(hostname, port);
            s.getOutputStream().write(jsonText.getBytes());
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(Transmitter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
