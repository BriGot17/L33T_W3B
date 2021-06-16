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

public class UserThread extends Thread{
    private String username;
    private JsonServer server;
    private BufferedReader input;
    private PrintWriter output;
    private JSONParser json;

    public UserThread(Socket client, JsonServer server) throws IOException {
        this.server = server;
        json = JSONParser.getInstance();
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output = new PrintWriter(client.getOutputStream(), true);

        String userAcknowledgement = input.readLine();
        String[] userData = json.parseUserAckFromJSON(userAcknowledgement);
        username = userData[1];
        sendHostAnnounce();
        server.addNewUser(userData[0], this);

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
        while (true) {
            try {
                line = input.readLine();

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
    }
}
