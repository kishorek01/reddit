package reddit;

import com.google.gson.JsonObject;

import java.sql.*;

public class Database {
	public static Connection connection=null;
	static PreparedStatement preparedStatement;
	static Statement statement;
	static {
		try {
			Class.forName("org.postgresql.Driver");
			String dbURL = "jdbc:postgresql://localhost:5432/";
			String dbName = "reddit";
			String dbUsername = "postgres";
			String dbPassword = "Password";
			connection = DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword);
		} catch (Exception e) {
			JsonObject res = new JsonObject();
			JsonObject finalResponse = new JsonObject();
			finalResponse.addProperty("code", 501);
			res.addProperty("message", "Database Connection Error");
			finalResponse.add("data", res);
			System.out.println("Database Connected Error");
		}
	}
	public static Connection initializeDatabase(){
		return connection;
	}

	public static synchronized JsonObject registerUser(String username,String name,String email,String password) throws SQLException {
		JsonObject data = new JsonObject();
		JsonObject finalResponse = new JsonObject();
		String createUserQuery="insert into users(username,email,password,name) values('"+username+"','"+email+"','"+password+"','"+name+"') RETURNING *;";
		preparedStatement = connection.prepareStatement(createUserQuery);
		ResultSet resultSet=preparedStatement.executeQuery();
		finalResponse.addProperty("code", 200);
		data.addProperty("message", "User Created Successfully");
		JsonObject userdata=new JsonObject();
		if(resultSet.next()) {
			userdata.addProperty("username",resultSet.getString("username"));
			userdata.addProperty("name", resultSet.getString("name"));
			userdata.addProperty("password", resultSet.getString("password"));
			userdata.addProperty("email", resultSet.getString("email"));
			userdata.addProperty("created_at", resultSet.getString("created_at"));
			userdata.addProperty("updated_at", resultSet.getString("updated_at"));
			if(!Storage.isUserInStorage(username)) {
				User getU = new User(resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("created_at"), resultSet.getString("updated_at"));
				Storage.setUser(getU);
			}
		}
		data.add("data",userdata);
		finalResponse.add("data", data);
		return finalResponse;
	}

	public static synchronized JsonObject loginUser(String username) throws SQLException{

		String sql="Select * from users where username='"+username+"';";
		statement=connection.createStatement();
		ResultSet resultSet=statement.executeQuery(sql);
		JsonObject userdata=new JsonObject();
		if(resultSet.next()) {
			userdata.addProperty("username",resultSet.getString("username"));
			userdata.addProperty("name", resultSet.getString("name"));
			userdata.addProperty("password", resultSet.getString("password"));
			userdata.addProperty("email", resultSet.getString("email"));
			userdata.addProperty("created_at", resultSet.getString("created_at"));
			userdata.addProperty("updated_at", resultSet.getString("updated_at"));
			if(!Storage.isUserInStorage(username)) {
				User getU = new User(resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("password"), resultSet.getString("created_at"), resultSet.getString("updated_at"));
				Storage.setUser(getU);
			}
		}
		return userdata;
	}
}
