package at.brigot.l33t.threads;

import at.brigot.l33t.io.JSONParser;
import at.brigot.l33t.server.ChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ClientThread extends Thread {
    String name = "";
    ChatServer server;
    BufferedReader input;
    PrintWriter output;
    JSONParser json;

    public ClientThread(Socket client, ChatServer server) throws Exception {
        // get input and output streams
        this.server = server;
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output = new PrintWriter(client.getOutputStream(), true);
        // read name
        name = input.readLine();

        server.addUser(name);
        json = JSONParser.getInstance();
        start();
    }

    public void sendMessage(String jsonMsg) {

        output.println(jsonMsg);
    }

    public void sendUserUpdate(List<String> usernames) {
        try {
            String userJSON = json.parseChatUsersToJSONString(usernames);
            server.broadcast(userJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return name;
    }

    public void run() {
        String line;

        while (true) {
            try {

                line = input.readLine();

                server.broadcast(line); // method  of outer class - send messages to all

            } // end of while
            // try
            catch(SocketException ex){
                if(ex.getMessage().equals("Connection reset")){
                    server.removeClient(this, name);

                }else{
                    ex.printStackTrace();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    } // end of run()
} // end of inner class