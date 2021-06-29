package at.brigot.l33t.beans;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Filesystem {

    private Map<String, Object> root;

    public Filesystem(JsonNode node){
        init();
        node = node.get("root");
        System.out.println(node);
        if(node.get("att").isArray()){
            for (JsonNode n : node.get("att")) {
                addAttack(n.asText());
            }
        }
        if(node.get("def").isArray()){
            for (JsonNode n : node.get("def")) {
                addDefense(n.asText());
            }
        }
        String lib = node.get("lib").toString().replace("{","").replace("}","");
        for (String s : lib.split(",")) {
            addToLib(s.split(":")[0],s.split(":")[1]);
        }
    }

    private void init(){
        this.root = new HashMap<>();
        this.root.put("att",new LinkedList<String>());
        this.root.put("def",new LinkedList<String>());
        this.root.put("lib",new HashMap<String,String>());
    }

    public void addAttack(String att){
        ((LinkedList)root.get("att")).add(att);
    }
    public LinkedList getAttacks(){
        return (LinkedList) root.get("att");
    }
    public void addDefense(String def){
        ((LinkedList)root.get("def")).add(def);
    }
    public LinkedList getDefenses(){
        return (LinkedList) root.get("def");
    }
    public void addToLib(String key, String value){
        ((HashMap)root.get("lib")).put(key,value);
    }
    public HashMap getLib(){
        return (HashMap) root.get("lib");
    }

    @Override
    public String toString() {
        return "Filesystem{" +
                "root=" + root +
                '}';
    }
}
