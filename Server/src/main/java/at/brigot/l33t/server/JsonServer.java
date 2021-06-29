package at.brigot.l33t.server;

import at.brigot.l33t.beans.Filesystem;
import at.brigot.l33t.beans.Node;
import at.brigot.l33t.io.JSONParser;
import at.brigot.l33t.threads.UserThread;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonServer {

    private Map<String, Filesystem> filesystems = new HashMap<>();
    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, UserThread> users = new HashMap<>();
    private JsonMapper json;

    public void startServer() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(6969, 10);
            loadFilesystems();
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

    public void loadFilesystems() throws IOException{
        Path path = Paths.get(System.getProperty("user.dir"),"src","main","java","at","brigot","l33t","res","FileDatabase.txt");

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path.toFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String st;
        while ((st = br.readLine()) != null) {
            filesystems.put(st.split("-")[0], JSONParser.getInstance().parseFilesystemFromJSON(st.split("-")[1]));
        }
    }

    public Filesystem getFilesystem(String ip){
        System.out.println(filesystems.get(ip));
        return filesystems.get(ip);
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
        requestedIP = requestedIP.replace("\"","");
        Node requestedNode = new Node(requestingSID,"hostname",requestedIP,getFilesystem(requestedIP));
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
