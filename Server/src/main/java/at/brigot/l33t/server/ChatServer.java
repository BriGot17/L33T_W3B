package at.brigot.l33t.server;

// Chat Server runs at port no. 9999
import at.brigot.l33t.threads.ClientThread;
import java.util.*;
import java.net.*;

public class  ChatServer {
    List<String> users = new Vector<>();
    List<ClientThread> clients = new Vector<>();
    public void startServer() throws Exception  {
        ServerSocket server = new ServerSocket(9999,10);
        System.out.println("Chatserver started on port 9999");
        while( true) {
            Socket client = server.accept();
            ClientThread c = new ClientThread(client, this);
            clients.add(c);
            initiateUserUpdate();
        }
    }

    public void removeClient(ClientThread client, String user){
        clients.remove(client);
        users.remove(user);
    }
    public void addUser(String user){
        users.add(user);
    }
    public static void main(String ... args) throws Exception {
        new ChatServer().startServer();
    }

    public void initiateUserUpdate(){
        for (ClientThread c: clients) {
            c.sendUserUpdate(users);
        }
    }
    public void broadcast(String user, String message)  {
        for (ClientThread c : clients)
            c.sendMessage(user,message);
    }
    
}