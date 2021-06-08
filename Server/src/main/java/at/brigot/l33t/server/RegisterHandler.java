package at.brigot.l33t.server;

import at.brigot.l33t.beans.User;
import at.brigot.l33t.io.JSONParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RegisterHandler implements HttpHandler {

    private JSONParser json;
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
    }

    private User handleRegisterRequest(HttpExchange httpExchange){
        String rawString = httpExchange.getRequestURI().toString();
        rawString = rawString.split("\\?")[1];
        String[] paramPairs = rawString.split("&");
        return new User(paramPairs[0].split("=")[1],paramPairs[1].split("=")[1], paramPairs[2].split("=")[1]);

    }


    private void handleResponse(HttpExchange httpExchange, User newUser)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        boolean response = false;
        //Database not yet implemented
        //This code is only temporary
        if(!newUser.getUsername().equals("admin")){
            response = true;
        }

        String res = json.parseAuthResponseToJSON(response, true);
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
