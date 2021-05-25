package at.brigot.l33t.io;

import at.brigot.l33t.beans.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONParser {

    private ObjectMapper objectMapper;
    private JsonMapper json;
    private static JSONParser instance;

    private JSONParser(){
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        json = new JsonMapper();

    }

    public static JSONParser getInstance(){
        if(instance == null){
            instance = new JSONParser();
        }
        return instance;
    }

    /**
     * Converts currently active usernames of users to JSON string which gets sent to client
     * @param users --> The list of usernames that should be converted to JSON
     * @return the list of users in form of a string in JSON format
     * @throws IOException
     */
    public String parseChatUsersToJSONString(List<String> users) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main","java", "at", "brigot", "l33t", "res", "chatusers.json");
        JsonNode node = json.readTree(path.toFile());
        String jsonStr = node.toPrettyString();
        String usersStr = "[";
        for (int i = 0; i < users.size(); i++) {
            usersStr += "\"" + users.get(i) + "\"";
            if(!(i + 1 == users.size())){
                usersStr += ", ";
            }
        }
        usersStr += "]";
        jsonStr = jsonStr.replace("[ ]", usersStr);
        return jsonStr;
    }

    /**
     * Method for parsing JSON String with current chat users to String
     *          NOT FOR IMPLEMENTATION ON SERVER SIDE
     *          WILL GET TRANSFERRED TO CLIENT PROJECT
     * @param userJSON Raw JSON string of usernames in chat
     * @return A list of strings containing the usernames currently using the chat
     * @throws JsonProcessingException
     */
    public List<String> readChatUsersFromString(String userJSON) throws JsonProcessingException {
        JsonNode node = json.readTree(userJSON);
        System.out.println(node.toPrettyString());
        node = node.get("users");
        List<String> currentUsernamesInChat = new ArrayList<>();
            //Iterator --> basically the same as ResultSet when we were working with databases
            // Works perfectly, as node.elements() returns an Iterator of type JsonNode
        Iterator<JsonNode> nodeIterator = node.elements();
        while(nodeIterator.hasNext()){
            currentUsernamesInChat.add(nodeIterator.next().toString());
        }
        return currentUsernamesInChat;
    }

    public static void main(String[] args) {
        JSONParser json = JSONParser.getInstance();
        List<String> test = new ArrayList<>();
        test.add("test1");
        test.add("test2");
        try {
            String userJSON = json.parseChatUsersToJSONString(test);
            System.out.println(json.readChatUsersFromString(userJSON).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
