package com.sid.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
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
import java.util.Properties;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.sid.models.Alarm;
import com.sid.models.Measure;
import com.sid.models.Round;

public class MySqlConnector {

	private static MySqlConnector INSTANCE;
	private ArrayList<Round> round_list = new ArrayList<>();

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
				//TODO Process this measures if not processed (Maybe add a field processed in mongoDB collection document)
				checkForUnsavedMeasures();
				try { Thread.sleep(30000); } catch (InterruptedException e) { e.printStackTrace(); }
			}).start();

		} catch (IOException | ClassNotFoundException | SQLException e) {
			System.err.println("User : -> " + user + "\nDBUrl : -> " + dbUrl + "\nPassword : -> " + password);
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
			output.add(rs.getDouble("margem_temperatura"));
			output.add(rs.getDouble("margem_humidade"));
			output.add(rs.getDouble("margem_luminosidade"));
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
				query += (modified ? "," : "") + "(" + m.getValorHumMedicao() + ", 'HUM' , '" + dataHoraMedicao + "', "
						+ (m.isControloHum() ? 1 : 0) + ", '" + m.getExtraHum() + "')";
				modified = true;
			}
			if (!duplicatesCheck[1]) {// TEMPERATURE
				query += (modified ? "," : "") + "(" + m.getValorTmpMedicao() + ", 'TMP' , '" + dataHoraMedicao + "', "
						+ (m.isControloTmp() ? 1 : 0) + ", '" + m.getExtraTmp() + "')";
				modified = true;
			}
			if (!duplicatesCheck[2]) {// MOVEMENT
				query += (modified ? "," : "") + "(" + m.getValorMovMedicao() + ", 'MOV' , '" + dataHoraMedicao + "', "
						+ (m.isControloMov() ? 1 : 0) + ", '" + m.getExtraMov() + "')";
				modified = true;
			}
			if (!duplicatesCheck[3]) {// LUMINOSITY
				query += (modified ? "," : "") + "(" + m.getValorLumMedicao() + ", 'LUM' , '" + dataHoraMedicao + "', "
						+ (m.isControloLum() ? 1 : 0) + ", '" + m.getExtraLum() + "')";
				modified = true;
			}

			if (modified)
				stm.executeUpdate(query);

			MongoConnector.getInstance().deleteEntryWithObjectId(m.getObjectId());
			System.out.println("Saved -> " + m);
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






	private void checkForUnsavedMeasures() {
		List<Measure> unsavedMeasures = MongoConnector.getInstance().findAllMeasures();
		for (Measure measure : unsavedMeasures)
			saveMeasure(measure);
	}








	public boolean[] checkForDuplicates(Measure m) {
		boolean[] duplicates = new boolean[4];
		Statement stm = null;
		try {
			stm = connection.createStatement();
			LocalDateTime dataHoraMedicao = m.getDataHoraMedicao().plusHours(1);
			ResultSet hum = stm
					.executeQuery("Select * from medicaosensores where ValorMedicao = " + m.getValorHumMedicao()
					+ " and TipoSensor = 'HUM' and DataHoraMedicao = '" + dataHoraMedicao + "';");
			duplicates[0] = hum.next();

			ResultSet temp = stm
					.executeQuery("Select * from medicaosensores where ValorMedicao = " + m.getValorTmpMedicao()
					+ " and TipoSensor = 'TMP' and DataHoraMedicao = '" + dataHoraMedicao + "';");
			duplicates[1] = temp.next();

			ResultSet mov = stm
					.executeQuery("Select * from medicaosensores where ValorMedicao = " + m.getValorMovMedicao()
					+ " and TipoSensor = 'MOV' and DataHoraMedicao = '" + dataHoraMedicao + "';");
			duplicates[2] = mov.next();

			ResultSet lum = stm
					.executeQuery("Select * from medicaosensores where ValorMedicao = " + m.getValorLumMedicao()
					+ " and TipoSensor = 'LUM' and DataHoraMedicao =  '" + dataHoraMedicao + "';");
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

	public Round findRondaByDate(LocalDateTime date) {
		round_list.clear();
		Statement stm = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String data = date.format(formatter);
		try {
			// vai buscar a tabela da ronda planeada e extra
			stm = connection.createStatement();
			String command = "(SELECT * FROM sid_2.ronda_extra  where ronda_inicio=\"" + data + "\")" + "Union "
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
		// read line at a time
		while (rp.next()) {
			String user_mail = rp.getString("email");
			String ronda_inicio = rp.getString("ronda_inicio");
			String ronda_fim = rp.getString("ronda_inicio");

			add_round(user_mail, ronda_inicio, ronda_fim);

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
			// vai buscar a tabela da ronda planeada e extra
			stm = connection.createStatement();
			String command = "(SELECT * FROM sid_2.ronda_extra  where ronda_inicio >=\"" + data + "\")" + "Union "
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
			// vai buscar a tabela da ronda planeada e extra
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
		while (tp.next()) {
			String date_string = tp.getString("DataHoraMedicao");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			LocalDateTime date = LocalDateTime.parse(date_string,formatter);


			String tipo = tp.getString("TipoSensor");
			Double value_med = tp.getDouble("ValorMedicao");
			Double limit = tp.getDouble("Limite");
			String description = tp.getString("Descricao");
			boolean control = tp.getBoolean("Controlo");
			String extra = tp.getString("Extra");
			a = add_alarm(date, tipo, value_med, limit, description, control, extra);
		}
		return a;
	}







	private Alarm add_alarm(LocalDateTime data, String tipo, Double value_med, Double limit, String description, boolean control, String extra) {
		return new Alarm(value_med,limit,tipo,extra,description,data,control);
	}

	public Alarm findLastDangerAlarm(){
		Statement stm = null;
		Alarm a = null;
		try {
			// vai buscar a tabela da ronda planeada e extra
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

	public void insertAlarm(Alarm a) {
		Statement stm = null;
		try {
			stm = connection.createStatement();
			String command = "INSERT INTO `sid_2`.`alerta` ( `DataHoraMedicao`, `TipoSensor`, `ValorMedicao`, `Limite`, `Descricao`, `Controlo`, `Extra`) "
					+ "VALUES ( '"+a.getDataHoraMedicao()+"', '"+a.getTipoSensor()+"', '"+a.getValorMedicao()+"', '"+a.getLimite()+"', '"+a.getDescricao()+"','"+a.getControlo()+"' , '"+a.getExtra()+"');";
			int tp = stm.executeUpdate(command);
			System.out.println(tp);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	public Round findNextOrCurrentRound(LocalDateTime date) {
		round_list.clear();
		Statement stm = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String data = date.format(formatter);
		try {
			// vai buscar a tabela da ronda planeada e extra
			stm = connection.createStatement();
			String command = "(SELECT * FROM sid_2.ronda_extra where ronda_inicio=\"" + data + "\" || "
					+ "ronda_inicio >\"" + data + "\" || "
					+ "( ( ronda_inicio=\"" + data + "\" || ronda_inicio >\"" + data + "\") "
							+ "&& (ronda_fim >\"" + data + "\" ||  ronda_fim=\"" + data + "\") ) "
				             + "Union "+ "(SELECT * FROM sid_2.ronda_planeada  where ronda_inicio=\"" + data + "\" || "
				             + "ronda_inicio >\"" + data + "\" || "
				             + "( ( ronda_inicio=\"" + data + "\" || ronda_inicio >\"" + data + "\") "
									+ "&& (ronda_fim >\"" + data + "\" ||  ronda_fim=\"" + data + "\") ) )"
									+ ")" ;

			ResultSet ronda_planeada_extra = stm.executeQuery(command);
			read_round(ronda_planeada_extra);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			if(!round_list.isEmpty())
				return round_list.get(0);
			else
				return null;
	}




	private void read_round(ResultSet rp) throws SQLException {
		// read line at a time
		if (rp.next()) {
			String user_mail = rp.getString("email");
			String ronda_inicio = rp.getString("ronda_inicio");
			String ronda_fim = rp.getString("ronda_fim");
			add_round(user_mail, ronda_inicio, ronda_fim);

		}
	}


	public static void main(String[] args) {
		int year=2019;
		int month=12;
		int dayOfMonth=31;
		int hour=02;

		int minute=00;
		LocalDateTime date = LocalDateTime.of(year,month,dayOfMonth,hour,minute);
		Alarm aa = new Alarm(40.2,"hum",LocalDateTime.now(), 40.2, "2", "1", true);
		getInstance().insertAlarm(aa);
		System.out.println("s");
		//		System.out.println(getInstance().findRondaByDate(date));
		//		System.out.println(getInstance().findAllRondasBiggerThen(date));
		//
		//		System.out.println(getInstance().findLastDangerAlarm());
		//		System.out.println(getInstance().findLastSevereAlarm());
		//System.out.println(getInstance().findNextOrCurrentRound( LocalDateTime.parse( "2020-05-25 20:31:00",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") ) ));




	}
}
