package com.sid.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.jdbc.ScriptRunner;


public class MySqlConnector {

	private static MySqlConnector INSTANCE;
	
	private static Connection connection;
	
	
	
	public MySqlConnector() {
		String dbUrl = "";
		String user = "";
		String password = "";
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("src/main/resources/config.ini"));
			dbUrl = p.getProperty("mysql_db_url");
			user = p.getProperty("mysql_user");
			password = p.getProperty("mysql_password");
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(dbUrl, user, password);
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("User : -> "+user);
			System.err.println("DBUrl : -> "+dbUrl);
			System.err.println("Password : -> "+password);
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("Couldn't connect database "+dbUrl+"\nCredentials:\n\tUser: "+user+"\n\tPassword: "+password);
			e.printStackTrace();
		}
	}


	
	
	
	/**
	 * Executes schema.sql present in resources folder
	 */
	public void executeSchemaScript() {
		ScriptRunner sr = new ScriptRunner(connection);
		try(Reader reader =new BufferedReader(new FileReader("src/main/resources/schema.sql"))){
			sr.runScript(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	
	
	
	public static MySqlConnector getInstance() {
		if(INSTANCE == null)
			INSTANCE = new MySqlConnector();
		return INSTANCE;
	}


	
	
	
	public List<Object> getSystemValues() {
		List<Object> output = new ArrayList<>();
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("Select * from sistema;");
			rs.next();
			output.add(rs.getDouble("LimiteTemperatura"));
			output.add(rs.getDouble("LimiteHumidade"));
			output.add(rs.getDouble("LimiteLuminosidade"));
			stm.close();
		} catch (SQLException e) {
			System.err.println("Something happened while fecthing Sistema values\nVerify if Sistema has values");
			e.printStackTrace();
		}
		return output;
	}


	
	
	
	public static void main(String[] args) {
		getInstance().executeSchemaScript();
	}
}
