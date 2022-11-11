package reddit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	public static Connection initializeDatabase()
	        throws SQLException, ClassNotFoundException
	    {
	        // Initialize all the information regarding
	        // Database Connection
//	        String dbDriver = "com.mysql.jdbc.Driver";
			Class.forName("org.postgresql.Driver");
	        String dbURL = "jdbc:postgresql://localhost:5432/";
	        // Database name to access
	        String dbName = "reddit";
	        String dbUsername = "postgres";
	        String dbPassword = "Password";
	  
//	        Class.forName(dbDriver);
	        Connection con = DriverManager.getConnection(dbURL + dbName,
	                                                     dbUsername, 
	                                                     dbPassword);
	        return con;
	    }
}
