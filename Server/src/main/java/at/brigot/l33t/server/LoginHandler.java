package at.brigot.l33t.server;

import at.brigot.l33t.beans.User;
import at.brigot.l33t.io.JSONParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class LoginHandler implements HttpHandler {

    private JSONParser json;

    public LoginHandler(){
        json = JSONParser.getInstance();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        User attemptingUser = null;
        if("POST".equals(httpExchange.getRequestMethod())) {
            attemptingUser = handleLoginRequest(httpExchange);
        }
        handleResponse(httpExchange, attemptingUser);

    }


    private User handleLoginRequest(HttpExchange httpExchange){
        String rawString = httpExchange.getRequestURI().toString();
        rawString = rawString.split("\\?")[1];
        String[] paramPairs = rawString.split("&");
        return new User(paramPairs[0].split("=")[1],paramPairs[1].split("=")[1]);

    }

    private void handleResponse(HttpExchange httpExchange, User attemptingUser)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        boolean response = false;
        //Database authentication not yet implemented
        //Temporarily, a primitive authentication is used
        if(attemptingUser.getUsername().equals("admin")){
            response = true;
        }

        String res = json.parseAuthResponseToJSON(response, true);
        httpExchange.sendResponseHeaders(200, res.length());
        outputStream.write(res.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        //if(response) {
        //    httpExchange.sendResponseHeaders(200, "access granted".length());
        //    outputStream.write("access granted".getBytes());
        //}else{
        //    httpExchange.sendResponseHeaders(200, "access denied".length());
        //    outputStream.write("access denied".getBytes());
        //}
    }
}
