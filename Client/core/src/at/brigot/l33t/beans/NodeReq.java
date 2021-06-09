package at.brigot.l33t.beans;

import com.fasterxml.jackson.databind.JsonNode;

public class NodeReq {

    private String sid;
    private String target_ip;

    public NodeReq(JsonNode node){
        this.sid = node.get("sid").asText();
        this.target_ip = node.get("target_ip").asText();
    }

    public String getSid() {
        return sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }
    public String getTarget_ip() {
        return target_ip;
    }
    public void setTarget_ip(String target_ip) {
        this.target_ip = target_ip;
    }

    @Override
    public String toString() {
        return "NodeReq{" +
                "sid='" + sid + '\'' +
                ", target_ip='" + target_ip + '\'' +
                '}';
    }
}
