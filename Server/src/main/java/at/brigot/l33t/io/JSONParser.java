package at.brigot.l33t.io;

import at.brigot.l33t.beans.Node;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JSONParser {

    private ObjectMapper objectMapper;
    private JsonMapper json;
    private Path chatMessagePath = Paths.get(System.getProperty("user.dir"), "src", "main","java", "at", "brigot", "l33t", "res", "chatmessage.json");
    private static JSONParser instance;
    private Path nodePath = Paths.get(System.getProperty("user.dir"), "src", "main","java","at", "brigot", "l33t", "res", "node.json");

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

    public Node parseNodeFromJSON(String rawString) throws JsonProcessingException {
        JsonNode node = json.readTree(rawString);
        return new Node(node);
    }

    public Map<String, String> parseAuthFromJSON(String rawJson, boolean isRegister) throws JsonProcessingException {
        JsonNode node = json.readTree(rawJson);
        Map<String, String> result = new HashMap<>();
        result.put("user", node.get("user").toString());
        if(isRegister){
            result.put("email", node.get("email").toString());
        }
        result.put("pwhash", node.get("pwhash").toString());
        return result;
    }

    public String[] parseUserAckFromJSON(String rawJson) throws JsonProcessingException {
        JsonNode node = json.readTree(rawJson);
        String[] response = new String[2];
        response[0] = node.get("sid").toString();
        response[1] = node.get("user").toString();
        return response;
    }

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

    public String parseHostAnnounceToString(Set<String> ips) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main","java", "at", "brigot", "l33t", "res", "host_announce.json");
        JsonNode node = json.readTree(path.toFile());
        String jsonStr = node.toString();

        String ipString = "";
        String[] ipArray = (String[]) ips.toArray();
        for(int i = 0; i < ipArray.length; i++){

            ipString += "\"" + ipArray[i] + "\"";
            if(i++ != ips.size()){
                ipString += ",";
            }
        }

        return jsonStr.replace("pl1", ipString);
    }

    public String getJsonID(String message) throws JsonProcessingException {
        JsonNode node = json.readTree(message);
        return node.get("json_id").toString();
    }

    public Map<String,String> parseNodeRequestToMap(String rawString) throws JsonProcessingException {
        JsonNode node = json.readTree(rawString);
        Map<String,String> request = new HashMap<>();
        request.put("sid",node.get("sid").toString());
        request.put("ip",node.get("target_ip").toString());
        return request;
    }



}
