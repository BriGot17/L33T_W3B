package at.brigot.l33t.beans;

public class Login {

    private String username;
    private String email;
    private String pwhash;

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
}
