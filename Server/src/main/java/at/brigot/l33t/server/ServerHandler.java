package at.brigot.l33t.server;

public class ServerHandler {

    private JsonServer json;
    private ChatServer chat;
    private LoginServer login;

    public ServerHandler(){
        login = new LoginServer();
        chat = new ChatServer();
        json = new JsonServer();

        try {
            login.start();
            chat.startServer();
            json.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ServerHandler sh = new ServerHandler();
    }
}
