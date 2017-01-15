package bepeakedserver.backend.database;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * MySQLFactory
 * @author Lasse
 * @version 11-01-2017
 */
public class MySQLFactory
{
    private final String url, dbName, userName, password;
    
    public MySQLFactory(String url, String dbName, String userName, String password) {
        this.url = url;
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
    }
    
    protected Connection createConnection() throws SQLException, ConnectException {
        try {
            return DriverManager.getConnection(url+dbName,userName,password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e + "\nCould not connect to database");
            throw new ConnectException("Could not connect to database");
        }
    }
}