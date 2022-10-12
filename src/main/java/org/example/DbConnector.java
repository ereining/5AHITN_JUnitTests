package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {

    private static Connection connection = null;

    private DbConnector() {
    }

    public static synchronized Connection getConnection(int port, String db, String usr, String passwd){
        if (connection == null) {
            connection = createConnection( port,  db, usr, passwd);
        }
        return connection;
    }

    private static Connection createConnection(int port, String db, String usr, String passwd) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:"+port+"/"+db+"?useSSL=false", usr, passwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return connection;
    }

}
