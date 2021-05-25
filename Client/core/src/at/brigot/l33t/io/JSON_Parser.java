package at.brigot.l33t.io;

import at.brigot.l33t.beans.Node;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSON_Parser {


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