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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.sid.models.Measure;

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
			
			new Thread(() -> {
				checkForUnsavedMeasures();
				try {Thread.sleep(30000);} catch (InterruptedException e) {e.printStackTrace();}
			}).start();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("User : -> " + user);
			System.err.println("DBUrl : -> " + dbUrl);
			System.err.println("Password : -> " + password);
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("Couldn't connect database " + dbUrl + "\nCredentials:\n\tUser: " + user
					+ "\n\tPassword: " + password);
			e.printStackTrace();
		}
	}






	/**
	 * Executes schema.sql present in resources folder
	 */
	public void executeSchemaScript() {
		ScriptRunner sr = new ScriptRunner(connection);
		try (Reader reader = new BufferedReader(new FileReader("src/main/resources/schema.sql"))) {
			sr.runScript(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}





	public static MySqlConnector getInstance() {
		if (INSTANCE == null)
			INSTANCE = new MySqlConnector();
		return INSTANCE;
	}





	public List<Object> getSystemValues() {
		List<Object> output = new ArrayList<>();
		Statement stm = null;
		try {
			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("Select * from sistema;");
			rs.next();
			output.add(rs.getDouble("LimiteTemperatura"));
			output.add(rs.getDouble("LimiteHumidade"));
			output.add(rs.getDouble("LimiteLuminosidade"));
		} catch (SQLException e) {
			System.err.println("Something happened while fecthing Sistema values\nVerify if Sistema has values");
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return output;
	}





	public boolean saveMeasure(Measure m) {
		Statement stm = null;
		try {
			boolean[] duplicatesCheck = checkForDuplicates(m);
			stm = connection.createStatement();
			String query = "INSERT INTO medicaosensores (ValorMedicao, TipoSensor, DataHoraMedicao, Controlo, Extra)  VALUES ";
			boolean modified = false;
			LocalDateTime dataHoraMedicao = m.getDataHoraMedicao().plusHours(1);
			
			if (!duplicatesCheck[0]) {// HUMIDITY
				query += (modified ? "," : "" )+"(" + m.getValorHumMedicao() + ", 'HUM' , '" + dataHoraMedicao + "', " + (m.isControloHum() ? 1 : 0) + ", '" + m.getExtraHum() + "')";
				modified = true;
			}
			if (!duplicatesCheck[1]) {// TEMPERATURE
				query += (modified ? "," : "" )+"(" + m.getValorTmpMedicao() + ", 'TMP' , '" + dataHoraMedicao + "', " + (m.isControloTmp() ? 1 : 0) + ", '" + m.getExtraTmp() + "')";
				modified = true;
			}
			if (!duplicatesCheck[2]) {// MOVEMENT
				query += (modified ? "," : "" )+"(" + m.getValorMovMedicao() + ", 'MOV' , '" + dataHoraMedicao + "', " + (m.isControloMov() ? 1 : 0) + ", '" + m.getExtraMov() + "')";
				modified = true;
			}
			if (!duplicatesCheck[3]) {// LUMINOSITY
				query += (modified ? "," : "" )+"(" + m.getValorHumMedicao() + ", 'LUM' , '" + dataHoraMedicao + "', " + (m.isControloLum() ? 1 : 0) + ", '" + m.getExtraLum() + "')";
				modified = true;
			}
			
			if(modified) stm.executeUpdate(query);
			MongoConnector.getInstance().deleteEntryWithObjectId(m.getObjectId());
			System.out.println("Saved -> "+m);
		} catch (SQLException e) {
			System.out.println("[SEVERE] An error ocurred while saving Measure please make sure the JDBC connection is open and running");
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}





	private void checkForUnsavedMeasures() {
		List<Measure> unsavedMeasures =  MongoConnector.getInstance().findAllMeasures();
		for (Measure measure : unsavedMeasures)
			saveMeasure(measure);
	}






	public boolean[] checkForDuplicates(Measure m) {
		boolean[] duplicates = new boolean[4];
		Statement stm = null;
		try {
			stm = connection.createStatement();
			ResultSet hum = stm.executeQuery("Select * from medicaosensores where ValorMedicao = " 
					+ m.getValorHumMedicao() + " and TipoSensor = 'HUM' and DataHoraMedicao = '" + m.getDataHoraMedicao() + "';");
			duplicates[0] = hum.next();

			ResultSet temp = stm.executeQuery("Select * from medicaosensores where ValorMedicao = "
					+ m.getValorTmpMedicao() + " and TipoSensor = 'TMP' and DataHoraMedicao = '" + m.getDataHoraMedicao() + "';");
			duplicates[1] = temp.next();

			ResultSet mov = stm.executeQuery("Select * from medicaosensores where ValorMedicao = "
					+ m.getValorMovMedicao() + " and TipoSensor = 'MOV' and DataHoraMedicao = '"+ m.getDataHoraMedicao() + "';");
			duplicates[2] = mov.next();

			ResultSet lum = stm.executeQuery("Select * from medicaosensores where ValorMedicao = "
					+ m.getValorLumMedicao() + " and TipoSensor = 'LUM' and DataHoraMedicao =  '"+ m.getDataHoraMedicao() + "';");
			duplicates[3] = lum.next();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return duplicates;
	}





	public static void main(String[] args) {
		//		getInstance().executeSchemaScript();
//		for (int i = 13; i < 16; i++) {
//			for (int j = 10; j < 60; j++) {
//				Measure m = new Measure(new JSONObject(Map.of("_id",new JSONObject(Map.of("$oid","5ec4fe8002c13a79407b3bab")),
//						"hum",38.30,
//						"mov",0,
//						"tmp",Double.parseDouble((Math.random()*90+10+"").substring(0,6)),
//						"dat","25/5/2020",
//						"sens","wifi",
//						"tim","02:"+i+":"+j,
//						"cell",3042)));
//				System.out.println(m);
//				getInstance().saveMeasure(m);
//			}
//		}
	}
}
