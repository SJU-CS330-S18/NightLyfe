package edu.csbsju.ad.nightlyfeproject;
import java.sql.*;

/**
 * Created by tdrichmond on 2/27/18.
 */

public class User {
    private CallableStatement callStmt;
    private PreparedStatement preparedStmt;
    private Statement stmt;

    public User() {
    }

    /**
     * This method and creates and returns a Connection object to the database.
     * All other methods that need database access should call this method.
     * @return a Connection object to Oracle
     */
    public Connection openDBConnection() {
        try {
            // Load driver and link to driver manager
            Class.forName("oracle.jdbc.OracleDriver");
            // Create a connection to the specified database
            Connection myConnection = DriverManager.getConnection("jdbc:oracle:thin:@//cscioraclesrv.ad.csbsju.edu:1521/" +
                    "csci.cscioraclesrv.ad.csbsju.edu","TEAM4", "bflz");
            return myConnection;
        } catch (Exception E) {
            E.printStackTrace();
        }
        return null;
    }
}
