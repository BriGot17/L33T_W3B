package at.brigot.l33t.bl;

import at.brigot.l33t.beans.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class RegisterHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        User newUser = null;
        if("POST".equals(httpExchange.getRequestMethod())) {
            newUser = handleRegisterRequest(httpExchange);
        }
        handleResponse(httpExchange, newUser);

    }


    private User handleRegisterRequest(HttpExchange httpExchange){
        String rawString = httpExchange.getRequestURI().toString();
        rawString = rawString.split("\\?")[1];
        String[] paramPairs = rawString.split("&");
        return new User(paramPairs[0].split("=")[1],paramPairs[1].split("=")[1], paramPairs[2].split("=")[1]);

    }


    private void handleResponse(HttpExchange httpExchange, User newUser)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        boolean dbResponse = false;
        //Database not yet implemented
        //This code is only temporary
        if(!newUser.getUsername().equals("admin")){
            dbResponse = true;
        }

        if(dbResponse) {
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
