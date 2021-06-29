package at.brigot.l33t.threads;

import at.brigot.l33t.beans.User;
import at.brigot.l33t.db.DB_Access;
import at.brigot.l33t.io.JSONParser;
import at.brigot.l33t.server.LoginServer;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoginThread extends Thread {
    private LoginServer server;
    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;
    private JSONParser json;
    private DB_Access dba;

    public LoginThread(LoginServer server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        output = new PrintWriter(this.socket.getOutputStream(), true);
        String line = input.readLine();
        json = JSONParser.getInstance();
        dba = DB_Access.getInstance();

        String jsonid = json.getJsonID(line).replace("\"", "");
        try {
            if (jsonid.equals("register")) {
                output.println(json.parseRegisterToJSON(registerNewUser(line)));
                output.flush();
            } else if (jsonid.equals("login")) {
                output.println(json.parseAuthResponseToJSON(verifyLogin(line)));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.server.removeThread(this);
    }

    public boolean registerNewUser(String message) throws IOException, SQLException {
        Map<String, String> values;

        values = json.parseAuthFromJSON(message, true);
        dba.insertNewUser(new User(values.get("user"), values.get("email"), values.get("pwhash")));
        return true;
    }

    public boolean verifyLogin(String message) throws JsonProcessingException, SQLException {
        Map<String, String> values;

        values = json.parseAuthFromJSON(message, false);
        return dba.validateUserLogin(new User(values.get("user"), values.get("pwhash")));
    }

    @Override
    public void run() {
        while(!socket.isClosed()){
            String line = "";

            try {
                line = input.readLine();

                if(line.contains("json_id")){
                    Map<String, String> values = new HashMap<>();
                    String jsonid = json.getJsonID(line);
                    if(jsonid.equals("register")){
                        output.println(json.parseRegisterToJSON(registerNewUser(line)));
                        output.flush();
                    }else if(jsonid.equals("login")){
                        output.println(json.parseAuthResponseToJSON(verifyLogin(line)));
                    }
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        server.removeThread(this);
    }
}
