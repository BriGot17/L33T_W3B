package at.brigot.l33t.io;

import at.brigot.l33t.beans.Node;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;

public class JSON_Parser {

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