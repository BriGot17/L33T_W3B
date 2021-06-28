package at.brigot.l33t.server;

import at.brigot.l33t.beans.Node;
import at.brigot.l33t.threads.UserThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonServer {

    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, UserThread> users = new HashMap<>();

    public void startServer() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(6969, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("server started on port 6969");

        while(true){
            try{
                Socket client = server.accept();
                UserThread user = new UserThread(client, this);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public Set<String> getIPs(){
        return nodes.keySet();
    }

    public void removeUser(String sid){
        users.remove(sid);
    }

    public void addNewUser(String sid, UserThread user){
        users.put(sid, user);
    }

    public void sendNodeToUser(String requestingSID, String requestedIP){
        UserThread requester = users.get(requestingSID);
        Node requestedNode = nodes.get(requestedIP);
        requester.sendNode(requestedNode, requestingSID);

    }

    public void addToNodes(Node newNode){
        nodes.put(newNode.getIp(), newNode);
    }

    public static void main(String[] args) throws IOException {
        JsonServer js = new JsonServer();
        js.startServer();
    }
}
