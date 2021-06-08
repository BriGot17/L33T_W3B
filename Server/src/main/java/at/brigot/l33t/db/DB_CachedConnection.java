/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.brigot.l33t.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Gottl
 */
public class DB_CachedConnection {
 
    private Queue<Statement> statementQueue = new LinkedList<>();
    private Connection connection;

    public DB_CachedConnection(Connection connection) {
        this.connection = connection;
    }
    
    public Statement getStatement() throws SQLException{
        if (connection == null){
            throw new RuntimeException("Not connected to database");
        }
        if(!statementQueue.isEmpty()){
            return statementQueue.poll();
        }
        return connection.createStatement();
    }
    
    public void releaseStatement(Statement statement){
        if(connection == null){
            throw new RuntimeException("Not connected to database");
        }
        statementQueue.offer(statement);
    }    
    
    
}
