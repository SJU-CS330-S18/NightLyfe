package backend;
import java.sql.*;
//import oracle.jdbc.OracleTypes;
/**
 * @author k1becker
 *
 */


public class database {
	
	public static Connection connect() {
		try {
			// Load driver and link to driver manager
			Class.forName("oracle.jdbc.OracleDriver");
			// Create connection to our database
			Connection myConnection = DriverManager.getConnection(
					"jdbc:oracle:thin:@//cscioraclesrv.ad.csbsju.edu:1521/csci.cscioraclesrv.ad.csbsju.edu", "TEAM4", "bflz");
			// Return connection object
			return myConnection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param username
	 * @param password
	 * @return 0 if unsucessful, 1 if normal user, 2 if business owner, 3 if admin
	 */
	public static int login(String username, String password) {
		PreparedStatement stmt = null;
		Connection con = connect();
		String query = "SELECT type from users where username = ? and password = ?";
		System.out.println(query);
		try {
			stmt = con.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, password);
	        ResultSet rs = stmt.executeQuery();
	        rs.next();
			return rs.getInt("type");
		} catch (Exception e) {

		}
		return -1;
	}
	

}
