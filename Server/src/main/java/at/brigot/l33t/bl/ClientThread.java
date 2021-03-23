package at.brigot.l33t.bl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class  ClientThread extends Thread {
    String name = "";
    ChatServer server;
    BufferedReader input;
    PrintWriter output;
    public ClientThread(Socket client, ChatServer server) throws Exception {
        // get input and output streams
        this.server = server;
        input = new BufferedReader( new InputStreamReader( client.getInputStream())) ;
        output = new PrintWriter ( client.getOutputStream(),true);
        // read name
        name  = input.readLine();
        server.addUser(name);
        start();
    }
    public void sendMessage(String uname,String  msg)  {
        output.println( uname + ">" + msg);
    }

    public String getUserName() {
        return name;
    }
    public void run()  {
        String line;
        try    {
            while(true)   {
                line = input.readLine();
                if ( line.equals("end") ) {
                    server.removeClient(this, name);
                    break;
                }
                server.broadcast(name,line); // method  of outer class - send messages to all
            } // end of while
        } // try
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    } // end of run()
} // end of inner class