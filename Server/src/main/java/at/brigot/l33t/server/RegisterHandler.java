package at.brigot.l33t.server;

import at.brigot.l33t.beans.User;
import at.brigot.l33t.db.DB_Access;
import at.brigot.l33t.io.JSONParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class RegisterHandler implements HttpHandler {

    private JSONParser json;
    private DB_Access dba;
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        User newUser = null;
        if("POST".equals(httpExchange.getRequestMethod())) {
            newUser = handleRegisterRequest(httpExchange);
        }
        handleResponse(httpExchange, newUser);

    }

    public RegisterHandler(){
        json = JSONParser.getInstance();
        dba = DB_Access.getInstance();
    }

    private User handleRegisterRequest(HttpExchange httpExchange){
        String rawString = httpExchange.getRequestURI().toString();
        rawString = rawString.split("\\?")[1];
        String[] paramPairs = rawString.split("&");
        return new User(paramPairs[0].split("=")[1],paramPairs[1].split("=")[1], paramPairs[2].split("=")[1]);

    }


    private void handleResponse(HttpExchange httpExchange, User newUser)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        boolean response = true;
        //Database not yet implemented
        //This code is only temporary
        try{
            dba.insertNewUser(newUser);
        }
        catch(PSQLException ex){
            response = false;
        } catch (SQLException throwables) {
            response = false;
            throwables.printStackTrace();
        }


        String res = json.parseRegisterToJSON(response);
        httpExchange.sendResponseHeaders(200, res.length());
        outputStream.write(res.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        if(response) {
            httpExchange.sendResponseHeaders(200, "User created".length());
            outputStream.write("user created".getBytes());
        }else{
            httpExchange.sendResponseHeaders(200, "user exists already".length());
            outputStream.write("user exists already".getBytes());
        }
        outputStream.flush();
        outputStream.close();

    }

}
