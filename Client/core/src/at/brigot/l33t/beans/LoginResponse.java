package at.brigot.l33t.beans;

import com.fasterxml.jackson.databind.JsonNode;

public class LoginResponse {

    private String sid;
    private String response;

    public LoginResponse(JsonNode node){
        this.sid = node.get("sid").asText();
        this.response = node.get("response").asText();
    }

    public String getSid() {
        return sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "sid='" + sid + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
