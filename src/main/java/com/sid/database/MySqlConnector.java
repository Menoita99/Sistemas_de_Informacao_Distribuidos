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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.json.JSONObject;

import com.sid.models.Alarm;
import com.sid.models.Measure;
import com.sid.models.Round;

public class MySqlConnector {

	private static MySqlConnector INSTANCE;
	private ArrayList<Round> round_list;

	private static Connection connection;

	public MySqlConnector() {
		round_list = new ArrayList<>();
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

			if (!duplicatesCheck[0])// HUMIDITY
				stm.executeUpdate(
						"INSERT INTO medicaosensores (ValorMedicao, TipoSensor, DataHoraMedicao, Controlo, Extra) "
								+ "VALUES (" + m.getValorHumMedicao() + ", 'HUM' , '" + m.getDataHoraMedicao() + "', "
								+ (m.isControlo() ? 1 : 0) + ", '" + m.getExtra() + "');");

			if (!duplicatesCheck[1])// TEMPERATURE
				stm.executeUpdate(
						"INSERT INTO medicaosensores (ValorMedicao, TipoSensor, DataHoraMedicao, Controlo, Extra) "
								+ "VALUES (" + m.getValorTmpMedicao() + ", 'TMP' , '" + m.getDataHoraMedicao() + "', "
								+ (m.isControlo() ? 1 : 0) + ", '" + m.getExtra() + "');");

			if (!duplicatesCheck[2])// MOVEMENT
				stm.executeUpdate(
						"INSERT INTO medicaosensores (ValorMedicao, TipoSensor, DataHoraMedicao, Controlo, Extra) "
								+ "VALUES (" + m.getValorMovMedicao() + ", 'MOV' , '" + m.getDataHoraMedicao() + "', "
								+ (m.isControlo() ? 1 : 0) + ", '" + m.getExtra() + "');");

			if (!duplicatesCheck[3])// LUMINOSITY
				stm.executeUpdate(
						"INSERT INTO medicaosensores (ValorMedicao, TipoSensor, DataHoraMedicao, Controlo, Extra) "
								+ "VALUES (" + m.getValorLumMedicao() + ", 'LUM' , '" + m.getDataHoraMedicao() + "', "
								+ (m.isControlo() ? 1 : 0) + ", '" + m.getExtra() + "');");

			MongoConnector.getInstance().deleteEntryWithObjectId(m.getObjectId());
		} catch (SQLException e) {
			System.out.println(
					"[SEVERE] An error ocurred while saving Measure please make sure the JDBC connection is open and running");
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

	public boolean[] checkForDuplicates(Measure m) {
		boolean[] duplicates = new boolean[4];
		Statement stm = null;
		try {
			stm = connection.createStatement();
			ResultSet hum = stm.executeQuery("Select * from medicaosensores where ValorMedicao = "
					+ m.getValorHumMedicao() + " and TipoSensor = 'HUM' " + "and DataHoraMedicao = " + "'"
					+ m.getDataHoraMedicao() + "';");
			if (hum.next())
				duplicates[0] = true;
			else
				duplicates[0] = false;
			ResultSet temp = stm.executeQuery("Select * from medicaosensores where ValorMedicao = "
					+ m.getValorTmpMedicao() + " and TipoSensor = 'TMP' " + "and DataHoraMedicao = " + "'"
					+ m.getDataHoraMedicao() + "';");
			if (temp.next())
				duplicates[1] = true;
			else
				duplicates[1] = false;
			ResultSet mov = stm.executeQuery("Select * from medicaosensores where ValorMedicao = "
					+ m.getValorMovMedicao() + " and TipoSensor = 'MOV' " + "and DataHoraMedicao = " + "'"
					+ m.getDataHoraMedicao() + "';");
			if (mov.next())
				duplicates[2] = true;
			else
				duplicates[2] = false;
			ResultSet lum = stm.executeQuery("Select * from medicaosensores where ValorMedicao = "
					+ m.getValorLumMedicao() + " and TipoSensor = 'LUM' " + "and DataHoraMedicao = " + "'"
					+ m.getDataHoraMedicao() + "';");
			if (lum.next())
				duplicates[3] = true;
			else
				duplicates[3] = false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		for (boolean b : duplicates) {
			System.out.println(b);
		}

		return duplicates;
	}



	
	
	
	
	
	
	
	
	
	public Round findRondaByDate(LocalDateTime date) {
		round_list.clear();
		Statement stm = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String data = date.format(formatter);		
		try {
			//vai buscar a tabela da ronda planeada e extra
			stm = connection.createStatement();
			String command = "(SELECT * FROM sid_2.ronda_extra  where ronda_inicio=\"" + data + "\")"
					+ "Union "
					+ "(SELECT * FROM sid_2.ronda_planeada  where ronda_inicio=\"" + data + "\")";

			ResultSet ronda_planeada_extra = stm.executeQuery(command);
			reading_rounds_table(ronda_planeada_extra);


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return round_list.get(0);
	}
	private void reading_rounds_table(ResultSet rp) throws SQLException {
		//read line at a time
		while (rp.next())
		{
			String user_mail = rp.getString("email");
			String ronda_inicio = rp.getString("ronda_inicio");
			String ronda_fim = rp.getString("ronda_inicio");

			add_round(user_mail,ronda_inicio,ronda_fim);

		}		
	}

	private void add_round(String user_mail, String ronda_inicio, String ronda_fim) {
		round_list.add( new Round(user_mail,ronda_inicio,ronda_fim));
		
	}

	public ArrayList<Round> findAllRondasBiggerThen(LocalDateTime date){
		round_list.clear();
		Statement stm = null;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String data = date.format(formatter);

		try {
			//vai buscar a tabela da ronda planeada e extra
			stm = connection.createStatement();
			String command = "(SELECT * FROM sid_2.ronda_extra  where ronda_inicio >=\"" + data + "\")"
					+ "Union "
					+ "(SELECT * FROM sid_2.ronda_planeada  where ronda_inicio >=\"" + data + "\")";

			ResultSet ronda_planeada_extra = stm.executeQuery(command);
			reading_rounds_table(ronda_planeada_extra);


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
		return round_list;

	}
	
	public Alarm findLastSevereAlarm(){
		Statement stm = null;
		Alarm a = null;
		try {
			//vai buscar a tabela da ronda planeada e extra
			stm = connection.createStatement();
			String command = "SELECT * FROM sid_2.alerta where Controlo = 0 and DataHoraMedicao=(Select max(DataHoraMedicao) from sid_2.alerta);";

			ResultSet tp = stm.executeQuery(command);
			a = reading_alert_table(tp);
		

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return a;
	}
	
	private Alarm reading_alert_table(ResultSet tp) throws SQLException {
		Alarm a = null;
		while (tp.next())
		{
			String date_string = tp.getString("DataHoraMedicao");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime date = LocalDateTime.parse(date_string,formatter);
			
			String tipo = tp.getString("TipoSensor");
			Double value_med = tp.getDouble("ValorMedicao");
			Double limit = tp.getDouble("Limite");
			String description = tp.getString("Descricao");
			boolean control = tp.getBoolean("Controlo");
			String extra = tp.getString("Extra");
						
			a= add_alarm(date,tipo,value_med,limit,description,control,extra);
		}	
		return a;
	}

	private Alarm add_alarm(LocalDateTime data, String tipo, Double value_med, Double limit, String description,
			boolean control, String extra) {
		return new Alarm(value_med,tipo,data,limit,description,extra,control);
	}

	public Alarm findLastDangerAlarm(){
		Statement stm = null;
		Alarm a = null;
		try {
			//vai buscar a tabela da ronda planeada e extra
			stm = connection.createStatement();
			String command = "SELECT * FROM sid_2.alerta where Controlo = 1 and DataHoraMedicao=(Select max(DataHoraMedicao) from sid_2.alerta);";

			ResultSet tp = stm.executeQuery(command);
			a = reading_alert_table(tp);
		

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return a;
	}

	public static void main(String[] args) {
		int year=2003;
		int month=12;
		int dayOfMonth=31;
		int hour=02;
		
		int minute=00;
		LocalDateTime date = LocalDateTime.of(year,month,dayOfMonth,hour,minute);

		System.out.println(getInstance().findRondaByDate(date));
		System.out.println(getInstance().findAllRondasBiggerThen(date));

		System.out.println(getInstance().findLastDangerAlarm());
		System.out.println(getInstance().findLastSevereAlarm());

	}
}
