package at.brigot.l33t.threads;

import at.brigot.l33t.io.JSONParser;
import at.brigot.l33t.server.ChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class  ClientThread extends Thread {
    String name = "";
    ChatServer server;
    BufferedReader input;
    PrintWriter output;
    JSONParser json;
    public ClientThread(Socket client, ChatServer server) throws Exception {
        // get input and output streams
        this.server = server;
        input = new BufferedReader( new InputStreamReader( client.getInputStream())) ;
        output = new PrintWriter ( client.getOutputStream(),true);
        // read name
        name  = input.readLine();
        json = JSONParser.getInstance();
        server.addUser(name);
        start();
    }
    public void sendMessage(String uname,String  msg)  {
        output.println( uname + ">" + msg);
    }
    public void sendUserUpdate(List<String> usernames){
        try {
            String userJSON = json.parseChatUsersToJSONString(usernames);
            server.broadcast("json", userJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return name;
    }
    public void run()  {
        String line;
        try    {
            while(true)   {
                line = input.readLine();
                if ( line.equals("end") ) {
                    server.removeClient(this, name);
                    break;
                }
                server.broadcast(name,line); // method  of outer class - send messages to all
            } // end of while
        } // try
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    } // end of run()
} // end of inner class