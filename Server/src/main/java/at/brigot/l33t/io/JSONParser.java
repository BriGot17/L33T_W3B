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
import java.util.UUID;

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



    public String parseAuthResponseToJSON(Boolean success) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main","java", "at", "brigot", "l33t", "res", "loginresponse.json");
        JsonNode node = json.readTree(path.toFile());
        String jsonStr = node.toString();

        if(success){
            jsonStr = jsonStr.replace("pl1", UUID.randomUUID().toString().substring(1,10));
            jsonStr = jsonStr.replace("pl2", "success");
        }
        else{
            jsonStr = jsonStr.replace("pl1", "");
            jsonStr = jsonStr.replace("pl2", "denied");
        }

        return jsonStr;
    }

    public String parseRegisterToJSON(Boolean isPossible) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main","java", "at", "brigot", "l33t", "res", "loginresponse.json");
        JsonNode node = json.readTree(path.toFile());
        String jsonStr = node.toString();

        if(isPossible){
            jsonStr = jsonStr.replace("pl1", UUID.randomUUID().toString().substring(1,10));
            jsonStr = jsonStr.replace("pl2", "user created");
        }
        else{
            jsonStr = jsonStr.replace("pl1", "");
            jsonStr = jsonStr.replace("pl2", "user exists");
        }

        return jsonStr;
    }
}
