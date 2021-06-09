package at.brigot.l33t.beans;

import com.fasterxml.jackson.databind.JsonNode;

public class Login {

    private String username;
    private String email;
    private String pwhash;

    public Login(JsonNode node){
        this.username = node.get("username").asText();
        this.email = node.get("email").asText();
        this.pwhash = node.get("pwhash").asText();
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPwhash() {
        return pwhash;
    }
    public void setPwhash(String pwhash) {
        this.pwhash = pwhash;
    }

    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", pwhash='" + pwhash + '\'' +
                '}';
    }
}
