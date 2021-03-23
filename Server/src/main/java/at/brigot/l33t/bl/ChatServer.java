package at.brigot.l33t.bl;

// Chat Server runs at port no. 9999
import java.io.*;
import java.util.*;
import java.net.*;
import static java.lang.System.out;
public class  ChatServer {
    Vector<String> users = new Vector<String>();
    Vector<ClientThread> clients = new Vector<ClientThread>();
    public void process() throws Exception  {
        ServerSocket server = new ServerSocket(9999,10);
        out.println("Server Started...");
        while( true) {
            Socket client = server.accept();
            ClientThread c = new ClientThread(client, this);
            clients.add(c);
        }  // end of while
    }

    public void removeClient(ClientThread client, String user){
        clients.remove(client);
        users.remove(user);
    }
    public void addUser(String user){
        users.add(user);
    }
    public static void main(String ... args) throws Exception {
        new ChatServer().process();
    } // end of main
    public void broadcast(String user, String message)  {
        // send message to all connected users
        for ( ClientThread c : clients )
            c.sendMessage(user,message);
    }
    
} // end of Server