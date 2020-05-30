package com.sid.process;

import java.util.ArrayList;

import org.json.JSONObject;

import com.sid.database.MongoConnector;
import com.sid.database.MySqlConnector;
import com.sid.models.Measure;
import com.sid.models.MysqlSystem;
import com.sid.models.Round;
import com.sid.util.ThreadPool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

@Data
public class Processor {

	private static final int NUMBER_OF_MEASURES_SAVED = 5;

	private static Processor INSTANCE; //this is used by performance monitor
	
	private ObservableList<Measure> measures = FXCollections.observableArrayList();

	//connectors
	private MySqlConnector mysqlConnector;
	private MongoConnector mongoConnector;
	
	private MysqlSystem mysqlSystem;
	
	private ThreadPool tempWorkers;
	private ThreadPool humWorkers;
	private ThreadPool movWorkers;
	private ThreadPool lumWorkers;
	
	//variables to help check movement
	private Round nextRounivate;
	boolean TempOverLim;
	private boolean HumOverLim;
	private int TempCooldown;
	private int HumCooldown;
	private int TempStatus;
	private int HumStatus;
	
	
	
	
	public Processor() {
		mysqlConnector = MySqlConnector.getInstance();
		mongoConnector = MongoConnector.getInstance();
		mysqlSystem = MysqlSystem.getInstance();
		tempWorkers = new ThreadPool(1);
		humWorkers = new ThreadPool(1);
		movWorkers = new ThreadPool(1);
		lumWorkers = new ThreadPool(1);
		HumOverLim = false;
		TempOverLim = false;
		TempCooldown = 0;
		HumCooldown = 0;
		TempStatus = 0;
		HumStatus = 0;
	}


	
	
	
	/**
	 * Main loop
	 */
	public void Process() {
		while(true) {
			JSONObject jobj = mongoConnector.read();
			//System.out.println("Read-> "+jobj);
			try {
				addMeasure(new Measure(jobj));
				tempWorkers.submit(new TemperatureTask(new ArrayList<Measure>(measures)));
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
		if (measures.size() >= NUMBER_OF_MEASURES_SAVED) {
			measures.remove(0);
		}
	}

	public void close() {
		System.exit(0);
	}
	
}
