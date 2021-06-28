package at.brigot.l33t.bl;

import at.brigot.l33t.beans.Node;
import at.brigot.l33t.io.JSON_Parser;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class JsonCommunicator{

    private JSON_Parser json;
    private String sid;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    /**
     * Constructor of JsonCommunicator
     * @param sid The sid of the logged in user
     * @throws IOException
     */
    public JsonCommunicator(String sid) throws IOException {
        this.sid = sid;
        json = JSON_Parser.getInstance();
        socket = new Socket("localhost", 6969);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream());
    }

    /**
     * Parses node to json string and calls send() method
     * @param node Node object to be sent
     */
    public void sendNodePush(Node node){
        try {
            String message = json.parseNodeToJSON(node, sid);
            send(message);
        } catch (IOException e) {
            System.out.println("Error parsing Node to JSON");
            e.printStackTrace();
        }
    }

    /**
     * parses ip into node request and calls send() method
     * @param ip String to be parsed into node_req
     */
    public  void sendNodeReq(String ip){
        try {
            String message = json.parseNodeRequestToJSON(ip, sid);
            send(message);
        } catch (IOException e) {
            System.out.println("Error parsing Node Request");
            e.printStackTrace();
        }
    }

    /**
     * Parses received string to Node object
     * @param message received String
     * @return Node object
     */
    private Node receiveNode(String message){
        try {
            return json.parseNodeFromJSON(message);
        } catch (JsonProcessingException e) {
            System.out.println("Error parsing node from JSON - returning null");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parses received string into list of ip addresses of type string
     * @param message received String
     * @return List of type string
     */
    private List<String> receiveHostAnnounce(String message){
        try {
            return json.parseHostAnnounce(message);
        } catch (JsonProcessingException e) {
            System.out.println("Error parsing host announcement - returning null");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generally sends a message out of the outputstream of the socket
     * @param message String to be sent
     */
    private void send(String message){
        output.println(message);
        output.flush();
    }

    /**
     * Generally receives input from server and passes it to other methods
     * @return returned value of called methods, null if no method is called or an error appears
     */
    public Object receive() {
        String line = "";
        Object result = null;
            try {
            line = input.readLine();
            if(line.contains("json_id")){
                if(!json.compareSID(line, sid)){
                    return null;
                }
                switch(json.getJsonID(line)){
                    case "node_push":
                        result = receiveNode(line);
                        break;
                    case "host_announcement":
                        result = receiveHostAnnounce(line);
                        break;
                    default:
                        result = null;
                        System.out.println("What are you doing here?");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
