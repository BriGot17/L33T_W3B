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
        while(true) {
            Socket client = server.accept();
            ClientThread c = new ClientThread(client, this);
            clients.add(c);
            c.sendUserUpdate(users);
        }
    }

    public void removeClient(ClientThread client, String user){
        clients.remove(client);
        users.remove(user);
        client.sendUserUpdate(users);
    }
    public void addUser(String user){
        users.add(user);
    }

    public void broadcast(String jsonMessage)  {
        for (ClientThread c : clients)
            c.sendMessage(jsonMessage);
    }

    public static void main(String[] args) {
        ChatServer cs = new ChatServer();
        try {
            cs.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}