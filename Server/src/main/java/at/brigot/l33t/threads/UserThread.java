package at.brigot.l33t.threads;

import at.brigot.l33t.beans.Node;
import at.brigot.l33t.io.JSONParser;
import at.brigot.l33t.server.JsonServer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class UserThread extends Thread{
    private String username;
    private String sid;
    private JsonServer server;
    private BufferedReader input;
    private PrintWriter output;
    private JSONParser json;
    private Socket socket;

    public UserThread(Socket client, JsonServer server) throws IOException {
        this.server = server;
        this.socket = client;
        json = JSONParser.getInstance();
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output = new PrintWriter(client.getOutputStream(), true);

        String userAcknowledgement = input.readLine();
        String[] userData = json.parseUserAckFromJSON(userAcknowledgement);
        sid = userData[0];
        username = userData[1];
        sendHostAnnounce();
        server.addNewUser(sid, this);

        start();
    }

    public void sendNode(Node node, String requestingSID){
        try {
            output.println(json.parseNodeToJSON(node, requestingSID));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendHostAnnounce(){
        try {
            output.println(json.parseHostAnnounceToString(server.getIPs()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String line = "";
        while (!socket.isClosed()) {
            try {
                line = input.readLine();
                if(line.contains("json_id")){
                    String json_id = json.getJsonID(line);

                    switch(json_id){
                        case "node_req":
                            Map<String, String> targetForUser = json.parseNodeRequestToMap(line);
                            String sid = (String) targetForUser.keySet().toArray()[0];
                            server.sendNodeToUser(sid, targetForUser.get(sid));
                            break;
                        case "node_push":
                            Node node = json.parseNodeFromJSON(line);
                            server.addToNodes(node);
                            break;
                        default:
                            System.out.println("How did you get here?");
                            break;
                    }
                }
                System.out.println(line);

            }
            catch(SocketException ex){
                if(ex.getMessage().equals("Connection reset")){

                }else{
                    ex.printStackTrace();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        server.removeUser(sid);

    }
}
