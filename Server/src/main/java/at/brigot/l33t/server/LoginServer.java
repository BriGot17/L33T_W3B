package at.brigot.l33t.server;

import at.brigot.l33t.beans.User;
import at.brigot.l33t.db.DB_Access;
import at.brigot.l33t.io.JSONParser;
import at.brigot.l33t.threads.LoginThread;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class LoginServer {

    private List<LoginThread> threads = new ArrayList<>();

    public void start(){
        try {
            ServerSocket socket = new ServerSocket(8000, 10);
            System.out.println("Loginserver started on port 8000");
            while(true){
                Socket client = socket.accept();
                threads.add(new LoginThread(this, client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeThread(LoginThread thread){
        threads.remove(thread);
    }

    public static void main(String[] args) {
        LoginServer ls = new LoginServer();
        ls.start();
    }
}


