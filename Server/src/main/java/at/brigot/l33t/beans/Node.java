package at.brigot.l33t.beans;

import com.fasterxml.jackson.databind.JsonNode;

public class Node {

    private String sid;
    private String hostname;
    private String ip;
    private Filesystem filesystem;

    public Node(String sid, String hostname, String ip, Filesystem filesystem){
        this.sid = sid;
        this.hostname = hostname;
        this.ip = ip;
        this.filesystem = filesystem;
    }

    public Node(JsonNode node){
        this.sid = node.get("sid").asText();
        this.hostname = node.get("hostname").asText();
        this.ip = node.get("ip").asText();
        this.filesystem = new Filesystem(node.get("filesystem"));
    }

    public String getSid() {
        return sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }
    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public Filesystem getFilesystem() {
        return filesystem;
    }
    public void setFilesystem(Filesystem filesystem) {
        this.filesystem = filesystem;
    }

    @Override
    public String toString() {
        return "Node{" +
                "sid='" + sid + '\'' +
                ", hostname='" + hostname + '\'' +
                ", ip='" + ip + '\'' +
                ", filesystem=" + filesystem +
                '}';
    }
}