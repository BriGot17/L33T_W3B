package at.brigot.l33t.server;

import at.brigot.l33t.beans.User;
import at.brigot.l33t.db.DB_Access;
import at.brigot.l33t.io.JSONParser;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class LoginServer {

    private PrintWriter pw;
    private BufferedReader br;
    private JSONParser json;
    private DB_Access dba;
    @Deprecated
    public void startServer(){
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            server.createContext("/login", new  LoginHandler());
            server.createContext("/register", new RegisterHandler());
            server.setExecutor(threadPoolExecutor);
            server.start();
            System.out.println("Server started on port 8001");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        try {
            json = JSONParser.getInstance();
            dba = DB_Access.getInstance();
            ServerSocket socket = new ServerSocket(8001, 10);
            System.out.println("Loginserver started on port 8001");
            while(true){
                Socket client = socket.accept();
                pw = new PrintWriter(client.getOutputStream());
                br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String line = br.readLine();
                if(line.contains("json_id")){
                    Map<String, String> values = new HashMap<>();
                    String jsonid = json.getJsonID(line);
                    if(jsonid.equals("register")){
                        values = json.parseAuthFromJSON(line, true);
                        dba.insertNewUser(new User(values.get("user"), values.get("email"), values.get("pwhash")));
                        pw.println(json.parseRegisterToJSON(true));
                        pw.flush();
                    }else if(jsonid.equals("login")){
                        pw.println(json.parseAuthResponseToJSON(dba.validateUserLogin(new User(values.get("user"), values.get("pwhash")))));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch(SQLException ex){

        }
    }

    public static void main(String[] args) {
        LoginServer ls = new LoginServer();
        ls.start();
    }
}


