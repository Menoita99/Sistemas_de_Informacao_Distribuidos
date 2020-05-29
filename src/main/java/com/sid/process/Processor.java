package com.sid.process;

import java.util.ArrayList;

import org.json.JSONObject;

import com.sid.database.MongoConnector;
import com.sid.database.MySqlConnector;
import com.sid.models.Measure;
import com.sid.models.MysqlSystem;
import com.sid.util.ThreadPool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

@Data
public class Processor {

	private static Processor INSTANCE; //this is used by performance monitor
	
	//Atenção que ao iterar remover ou adicionar pode haver um erro de concurrencia.
	//Irei implementar um mecanismo de locks para evitar este problema
	private ObservableList<Measure> measures = FXCollections.observableArrayList();

	//connectors
	private MySqlConnector mysqlConnector;
	private MongoConnector mongoConnector;
	
	private MysqlSystem mysqlSystem;
	
	private ThreadPool workers;

	
	
	
	public Processor() {
		mysqlConnector = MySqlConnector.getInstance();
		mongoConnector = MongoConnector.getInstance();
		mysqlSystem = MysqlSystem.getInstance();
		workers = new ThreadPool(10);
	}


	
	
	
	/**
	 * Main loop
	 */
	public void Process() {
		while(true) {
			JSONObject jobj = mongoConnector.read();
			System.out.println("Read-> "+jobj);
			try {
				addMeasure(new Measure(jobj));
				workers.submit(new Task(new ArrayList<Measure>(measures)));
			} catch (Exception e) {
				System.err.println("Could not read -> "+jobj);
				e.printStackTrace();
			}
		}
	}


	
	
	
	public static Processor getInstance() {
		if(INSTANCE == null)
			INSTANCE = new Processor();
		return INSTANCE;
	}

	
	
	

	private void addMeasure(Measure newMeasure) {
		measures.add(newMeasure);
		if (measures.size() >= 3) {
			measures.remove(0);
		}
	}

	public void close() {
		System.exit(0);
	}
	
	protected double getTempLimit() {
		return mysqlSystem.getLimiteTemperatura();
	}
}
