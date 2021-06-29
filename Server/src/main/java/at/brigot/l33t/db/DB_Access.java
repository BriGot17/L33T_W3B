package at.brigot.l33t.db;

import at.brigot.l33t.beans.Node;
import at.brigot.l33t.beans.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Gottl
 */
public class DB_Access {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DB_Access dbInstance = null;
    private DB_Database db;
    private String validateUserString = "SELECT * FROM public.\"User\" WHERE public.\"User\".\"Username\" = ? AND public.\"User\".\"pwhash\" = ?";
    private String insertUserString = "INSERT INTO public.\"User\" VALUES(?, ?, ?)";
    private String insertNodeString = "INSERT INTO public.\"Host\" VALUES(?, ?, ?)";
    private String getFileSystem = "SELECT filesystem FROM public.\"Host\" WHERE IP = ?";
    private PreparedStatement validateUser;
    private PreparedStatement insertUser;
    private PreparedStatement insertNode;
    private PreparedStatement getFilesystem;

    public static DB_Access getInstance() {
        if (dbInstance == null) {
            dbInstance = new DB_Access();
        }
        return dbInstance;
    }
    private DB_Access() {
        try {
            db = new DB_Database();
            db.connect();

        } catch (ClassNotFoundException ex) {
            System.out.println("An error occured while connecting to the database");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("An error occured while performing SQL operations");
            ex.printStackTrace();
        }
    }

    public void insertNode (String ip, String hostname, String filesystem) throws SQLException {
        if(insertNode == null){
            insertNode = db.getConnection().prepareStatement(insertNodeString);
        }
        insertNode.setString(1, ip);
        insertNode.setString(2, hostname);
        insertNode.setString(3, filesystem);
        insertNode.execute();
    }

    public Boolean validateUserLogin(User attemptingUser) throws SQLException {
        if(validateUser == null){
            validateUser = db.getConnection().prepareStatement(validateUserString);
        }
        validateUser.setString(1, attemptingUser.getUsername());
        validateUser.setString(2, attemptingUser.getPasswordHash());

        ResultSet result = validateUser.executeQuery();
        return result.next();
    }

    public void insertNewUser(User newUser) throws SQLException {
        if(insertUser == null){
            insertUser = db.getConnection().prepareStatement(insertUserString);
        }
        insertUser.setString(1, newUser.getUsername());
        insertUser.setString(2, newUser.getEmail());
        insertUser.setString(3, newUser.getPasswordHash());
        insertUser.execute();
    }

    public static void main(String[] args) {

        try {
            DB_Access dba = DB_Access.getInstance();
            dba.insertNewUser(new User("testUser2","test@test.dev","deadcafe"));
            //System.out.println(dba.validateUserLogin("testUser2", "deadcafe"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
