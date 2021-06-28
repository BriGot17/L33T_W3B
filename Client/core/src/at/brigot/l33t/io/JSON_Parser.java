package at.brigot.l33t.io;

import at.brigot.l33t.beans.Login;
import at.brigot.l33t.beans.LoginResponse;
import at.brigot.l33t.beans.Node;
import at.brigot.l33t.beans.NodeReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JSON_Parser {

    private ObjectMapper objectMapper;
    private JsonMapper json;
    private static JSON_Parser instance;
    //Paths for the JSON Templates
    private Path chatMessagePath = Paths.get(System.getProperty("user.dir"),"core", "src", "at", "brigot", "l33t", "res","json_templates", "chatmessage.json");
    private Path loginPath = Paths.get(System.getProperty("user.dir"),"core", "src", "at", "brigot", "l33t", "res","json_templates", "login.json");
    private Path nodeReqPath = Paths.get(System.getProperty("user.dir"),"core", "src", "at", "brigot", "l33t", "res","json_templates", "node_req.json");
    private Path nodePath = Paths.get(System.getProperty("user.dir"),"core", "src", "at", "brigot", "l33t", "res","json_templates", "node.json");
    private Path userAck = Paths.get(System.getProperty("user.dir"),"core", "src", "at", "brigot", "l33t", "res","json_templates", "user_ack.json");

    public JSON_Parser() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.json = new JsonMapper();
    }

    public static JSON_Parser getInstance(){
        if(instance == null){
            instance = new JSON_Parser();
        }
        return instance;
    }

    /**
     * Method to parse a Message to JSON to send to the Server
     * @param user -> the Name of the User who wrote the message
     * @param message -> the message of the user
     * @return A Json String which will be send to the Server
     * @throws IOException
     */
    public String parseMessageToJSON(String user, String message) throws IOException {
        JsonNode node = json.readTree(chatMessagePath.toFile());
        String jsonStr = node.toString();
        jsonStr = jsonStr.replace("pl1", user);
        jsonStr = jsonStr.replace("[]", "\"" + message + "\"");
        return jsonStr;
    }

    /**
     * Method for parsing a Json String to a message
     * @param json_message -> the message in JSON Format
     * @return a String with the message and the user to display
     * @throws IOException
     */
    public String parseMessageToString(String json_message) throws IOException {
        JsonNode node = json.readTree(json_message);
        String jsonID = node.get("json_id").toString().replace("\"", "");

        switch(jsonID){
            case "chatmessage":
                System.out.println("I am here");
                String user = node.get("user").toString();
                if(!user.replace("\"", "").equals("json")){
                    return "mes_" + user + ">" + node.get("content").toString().replace("\"", "");
                }
                else{
                    return readChatUsersFromString(node);
                }
            default:
                return "Error parsing json";
        }
    }

    /**
     * Method for parsing JSON String with current chat users to String
     * @param node --> JsonNode passed from method parseToString()
     * @return A list of strings containing the usernames currently using the chat
     * @throws JsonProcessingException
     */
    public String readChatUsersFromString(JsonNode node) throws JsonProcessingException {

        node = node.get("content");
        List<String> currentUsernamesInChat = new ArrayList<>();
        //Iterator --> basically the same as ResultSet when we were working with databases
        // Works perfectly, as node.elements() returns an Iterator of type JsonNode
        Iterator<JsonNode> nodeIterator = node.elements();
        while(nodeIterator.hasNext()){
            currentUsernamesInChat.add(nodeIterator.next().toString());
        }

        String usersconcat = "";


        for (int i = 0; i < currentUsernamesInChat.size(); i++){
            usersconcat += currentUsernamesInChat.get(i);
            if(i != currentUsernamesInChat.size()-1)
                usersconcat += ";";
        }
        usersconcat = usersconcat.replace("\"", "");
        System.out.println(currentUsernamesInChat);

        return usersconcat;
    }

    /**
     * Method for parsing the Login Credentials to JSON to send to the Server
     * @param user -> the Username of the User which is used for login
     * @param pwhash -> the hash of the inputted password for login
     * @return jsonStr -> the JSON String which is sent to the Server for Login
     * @throws IOException
     */
    @Deprecated
    public String parseLoginToJSON(String user, String pwhash) throws IOException{
        JsonNode node = json.readTree(loginPath.toFile());
        String jsonStr = node.toString();
        jsonStr = jsonStr.replace("pl1","login"); //JsonID
        jsonStr = jsonStr.replace("pl2",user); //Username
        jsonStr = jsonStr.replace("pl3",""); //E-Mail
        jsonStr = jsonStr.replace("pl4",pwhash); //PwHash
        return jsonStr;
    }


    /**
     * Method for parsing the Register information to JSON to send to the Server
     * @param user -> the Username of the User which is Displayed ingame and is later used for Login
     * @param email -> the email of the User which is used for creating the account
     * @param pwhash -> the pwhash of the password the user wants to have
     * @return jsonStr -> the JSON String which is sent to the Server for Register
     * @throws IOException
     */
    @Deprecated
    public String parseRegisterToJSON(String user,String email,String pwhash) throws IOException{
        JsonNode node = json.readTree(loginPath.toFile());
        String jsonStr = node.toString();
        jsonStr = jsonStr.replace("pl1","register"); //JsonID
        jsonStr = jsonStr.replace("pl2",user); //Username
        jsonStr = jsonStr.replace("pl3",email); //E-Mail
        jsonStr = jsonStr.replace("pl4",pwhash); //PwHash
        return jsonStr;
    }

    /**
     * Method for parsing the Login Response from the Server to String
     * @param response -> the Response of the Server
     * @return
     * @throws IOException
     */
    @Deprecated
    public String parseLoginResponseToString(String response) throws IOException{
        JsonNode node = json.readTree(response);
        String sid = node.get("sid").asText();
        String res = node.get("response").asText();
        String Str = res + ":" + sid;
        return Str;
    }

    /**
     * Method for parsing a Node from a Json Node
     * @param node -> the Node which will be parsed
     * @return
     */
    public Node parseNodeFromJSON(JsonNode node){
        return new Node(node);
    }

    public Node parseNodeFromJSON(String rawString) throws JsonProcessingException {
        JsonNode node = json.readTree(rawString);
        return new Node(node);
    }
    /**
     * Method for parsing a Node to JSON to send to the Server
     * @param node -> the Node which will be parsed
     * @param sid -> the Session ID of the Client
     * @return a JSON String
     * @throws IOException
     */
    public String parseNodeToJSON(Node node, String sid) throws IOException{
        JsonNode jn = json.readTree(nodePath.toFile());
        String jsonStr = jn.toString();
        String temp = "";
        jsonStr = jsonStr.replace("pl1", sid);
        jsonStr = jsonStr.replace("pl2", node.getHostname());
        jsonStr = jsonStr.replace("pl3", node.getIp());
        temp = "";
        Iterator it = node.getFilesystem().getAttacks().iterator();
        temp += "\""+it.next()+"\"";
        while(it.hasNext()){

            temp += ",\""+it.next()+"\"";
        }
        System.out.println(temp);
        jsonStr = jsonStr.replace("\"pl4\"", temp);
        temp = "";
        it = node.getFilesystem().getDefenses().iterator();
        temp += "\""+it.next()+"\"";
        while(it.hasNext()){

            temp += ",\""+it.next()+"\"";
        }
        System.out.println(temp);
        jsonStr = jsonStr.replace("\"pl5\"", temp);
        jsonStr = jsonStr.replace("{\"pl6\":\"\"}", node.getFilesystem().getLib().toString().replace("=",":"));
        return jsonStr;
    }

    /**
     * Method for parsing the IP to an JSON to send to the Server
     * @param ip -> the IP of the requested Node
     * @param sid -> the Session ID of the Client
     * @return a JSON String
     * @throws IOException
     */
    public String parseNodeRequestToJSON(String ip, String sid) throws IOException{
        JsonNode node = json.readTree(nodeReqPath.toFile());
        String jsonStr = node.toString();
        jsonStr = jsonStr.replace("pl1",ip); //IP
        jsonStr =jsonStr.replace("pl2",sid); //SID
        return jsonStr;
    }

    /**
     * Parses a JsonNode from the server to a Map of possible hosts.
     * @param node the JsonNode we receive from the server
     * @return a Map of possible Hosts
     */
    public List<String> parseHostAnnounce(JsonNode node){
        Map<String,String> possibleHosts = new HashMap<>();
        List<String> possibleIps = new ArrayList<>();
        String hosts = node.get("ips").toString().replace("[","").replace("]","");
        for (String s : hosts.split(",")) {
            s = s.replace("\"","");
            possibleIps.add(s);
        }
        return possibleIps;
    }
    public List<String> parseHostAnnounce(String rawString) throws JsonProcessingException {
        JsonNode node = json.readTree(rawString);
        List<String> possibleIps = new ArrayList<>();
        String hosts = node.get("ips").toString().replace("[","").replace("]","");
        for (String s : hosts.split(",")) {
            s = s.replace("\"","");
            possibleIps.add(s);
        }
        return possibleIps;
    }


    public String parseUserAck (String sid,String username) throws IOException{
        JsonNode node = json.readTree(userAck.toFile());
        String jsonStr = node.toString();
        jsonStr = jsonStr.replace("pl1",sid);
        jsonStr = jsonStr.replace("pl2",username);
        return jsonStr;
    }

    public String getJsonID(String rawString) throws JsonProcessingException {
        JsonNode node = json.readTree(rawString);
        return node.get("json_id").toString();
    }

    public Boolean compareSID(String rawString, String otherSID) throws JsonProcessingException {
        JsonNode node = json.readTree(rawString);
        return node.get("sid").toString() == otherSID;
    }

    /**
     * This is a Test Method
     * @return A pre defined Node to test things
     */
    public Node getTestNode(){
        Node test = null;
        JsonNode node = null;
        JsonMapper mapper = new JsonMapper();
        try {
            node = mapper.readTree(Paths.get(System.getProperty("user.dir"), "core", "src", "at", "brigot", "l33t", "res", "json_templates", "nodetest.json").toFile());
        }catch (IOException e){
            e.printStackTrace();
        }
        test = new Node(node);
        return test;
    }


    /**
     * The Main is only being used for testing the JSON_Parser
     * @param args
     */
    public static void main(String[] args) {
        JsonMapper mapper = new JsonMapper();
        JsonNode node = null;
        JsonNode login = null;
        JsonNode loginRes = null;
        JsonNode nodeReq = null;
        JsonNode hostAnn = null;
        try {
            node = mapper.readTree(Paths.get(System.getProperty("user.dir"),"core", "src", "at", "brigot", "l33t", "res","json_templates", "nodetest.json").toFile());
            login = mapper.readTree(Paths.get(System.getProperty("user.dir"),"core", "src", "at", "brigot", "l33t", "res","json_templates", "login.json").toFile());
            loginRes = mapper.readTree(Paths.get(System.getProperty("user.dir"),"core", "src", "at", "brigot", "l33t", "res","json_templates", "loginresponse.json").toFile());
            nodeReq = mapper.readTree(Paths.get(System.getProperty("user.dir"),"core", "src", "at", "brigot", "l33t", "res","json_templates", "node_req.json").toFile());
            hostAnn = mapper.readTree(Paths.get(System.getProperty("user.dir"),"core", "src", "at", "brigot", "l33t", "res","json_templates", "host_announce.json").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        NodeReq nr = new NodeReq(nodeReq);
        //System.out.println(nr);
        LoginResponse lr = new LoginResponse(loginRes);
        //System.out.println(lr);
        Login l = new Login(login);
        //System.out.println(l);
        Node n = new Node(node);
        String temp = "";
        Iterator it = n.getFilesystem().getAttacks().iterator();
        temp += "\""+it.next()+"\"";
        while(it.hasNext()){
            temp += ",\""+it.next()+"\"";
        }
        try {
            System.out.println(getInstance().parseNodeToJSON(n,n.getSid()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(getInstance().parseHostAnnounce(hostAnn));
    }

}