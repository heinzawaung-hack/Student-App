package sample.Database;
import java.sql.*;
public class Database {

    String url = "jdbc:mysql://localhost:3307/crypto";
    String userName = "root";
    String password = "";

    private static Database database;

    private Database() throws SQLException {
        connect();
    }

    public static Database getInstance() throws SQLException {
        if (database == null){
            database = new Database();
        }
        return database;
    }

    private Connection connection = null;
    public void connect() throws SQLException {
         connection = DriverManager.getConnection(url, userName, password);
        System.out.println("Connected to Database");
    }

    public Connection getConnection(){
        return connection;
    }
}
