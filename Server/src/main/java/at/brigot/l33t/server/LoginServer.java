package at.brigot.l33t.server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class LoginServer {

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

    public static void main(String[] args) {
        LoginServer ls = new LoginServer();
        ls.startServer();
    }
}


