package chitchat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class Database {

	static Connection connection;
	DriverManager dm;
	static Statement stm;
	private static Database instance;
	
	private Database() {}

	 public static Database getInstance(){
	        if(instance == null){
	            instance = new Database();
	        }
	        return instance;
	    }
	 
	public java.sql.Connection connect(String DB_URL, String username, String password) {
		try {
			connection = DriverManager.getConnection(DB_URL, username, password);
			System.out.println();
			System.out.println();
			return connection;
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Problems with server / database");
			return null;
		}catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("Null");
			return null;
		}
	}

	public static int executeStatement(String sql) {
		try {
			stm = connection.createStatement();
			return stm.executeUpdate(sql);
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("You did something wrong!!!");
			return -22;
		}

	}

	public static ResultSet resultSet(String query) {
		ResultSet rs = null;
		try {
			Statement stm = connection.createStatement();
			rs = stm.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("error in method resultSet");
			e.printStackTrace();
		}
		return rs;
	}

	public static void editPassword(String username)   {

		try {
			String query = "update users set password = ? where user_name = ?";

			PreparedStatement p = connection.prepareStatement(query);
			p.setNull(1, Types.VARCHAR);
			p.setString(2, username);
			p.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void connectionClose() {

		if (stm != null) {
			try {
				stm.close();


			} catch (SQLException |NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {


				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) { /* ignored */}
				}
			}}
	}
}

