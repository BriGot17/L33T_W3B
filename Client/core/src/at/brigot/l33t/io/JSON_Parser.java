package at.brigot.l33t.io;

import at.brigot.l33t.beans.Node;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSON_Parser {

    private ObjectMapper objectMapper;
    private JsonMapper json;
    private static JSON_Parser instance;
    private Path chatMessagePath = Paths.get(System.getProperty("user.dir"),"core", "src", "at", "brigot", "l33t", "res","json_templates", "chatmessage.json");
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

    public String parseMessageToJSON(String user, String message) throws IOException {
        JsonNode node = json.readTree(chatMessagePath.toFile());
        String jsonStr = node.toString();
        jsonStr = jsonStr.replace("pl1", user);
        jsonStr = jsonStr.replace("[]", "\"" + message + "\"");
        return jsonStr;
    }

    public String parseToString(String is ) throws IOException {
        JsonNode node = json.readTree(is);
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

    public static void main(String[] args) {
        JsonMapper mapper = new JsonMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(new File("C:/Users/Admin/Desktop/Schule/L33T_W3B/Client/core/src/at/brigot/l33t/res/json_templates/node.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node n = new Node(node);
        System.out.println(n);
    }

}