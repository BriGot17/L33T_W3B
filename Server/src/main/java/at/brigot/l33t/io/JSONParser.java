package at.brigot.l33t.io;

import at.brigot.l33t.beans.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONParser {

    private ObjectMapper objectMapper;
    private JsonMapper json;
    private Path chatMessagePath = Paths.get(System.getProperty("user.dir"), "src", "main","java", "at", "brigot", "l33t", "res", "chatmessage.json");
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

    public String parseToString(String jsonRaw) throws IOException {
        JsonNode node = json.readTree(chatMessagePath.toFile());
        String jsonID = node.get("json_id").toString();

        switch (jsonID){

        }
        return "";
    }
    public String parseToString(InputStream is) throws IOException {
        JsonNode node = json.readTree(is);
        return "";
    }

    /**
     * Converts currently active usernames of users to JSON string which gets sent to client
     * @param users --> The list of usernames that should be converted to JSON
     * @return the list of users in form of a string in JSON format
     * @throws IOException
     */
    public String parseChatUsersToJSONString(List<String> users) throws IOException {
        JsonNode node = json.readTree(chatMessagePath.toFile());
        String jsonStr = node.toString();
        String usersStr = "[";
        for (int i = 0; i < users.size(); i++) {
            usersStr += "\"" + users.get(i) + "\"";
            if(!(i + 1 == users.size())){
                usersStr += ", ";
            }
        }
        usersStr += "]";
        jsonStr = jsonStr.replace("pl1", "json");
        jsonStr = jsonStr.replace("[]", usersStr);
        return jsonStr;
    }



    public static void main(String[] args) {
        JSONParser json = JSONParser.getInstance();
        List<String> test = new ArrayList<>();
        test.add("test1");
        test.add("test2");
        try {
            String userJSON = json.parseChatUsersToJSONString(test);
           // System.out.println(json.readChatUsersFromString(userJSON).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
