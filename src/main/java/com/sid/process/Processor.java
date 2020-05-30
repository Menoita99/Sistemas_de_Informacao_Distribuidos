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
	private ObservableList<Measure> tempMeasures = FXCollections.observableArrayList();
	private ObservableList<Measure> humMeasures = FXCollections.observableArrayList();
	private ObservableList<Measure> movMeasures = FXCollections.observableArrayList();
	private ObservableList<Measure> lumMeasures = FXCollections.observableArrayList();

	//connectors
	private MySqlConnector mysqlConnector;
	private MongoConnector mongoConnector;
	
	private MysqlSystem mysqlSystem;
	
	private ThreadPool tempWorkers;
	private ThreadPool humWorkers;
	private ThreadPool movWorkers;
	private ThreadPool lumWorkers;
	
	//variables to help check movement

	private Round nextOrCurrentRound;
	private LocalDateTime lastTimeChecked;
	private LocalDateTime lastMovement;
	private LocalDateTime badMovement;
	private int time_to_worry;
	private int time_to_send_email;

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

		time_to_worry = MovementTask.getTimeToWorryMov();
		time_to_worry = MovementTask.getTimeToSendEmail();
		workers = new ThreadPool(10);

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
				addAndTreatMeasure(new Measure(jobj));
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

	
	
	

	private void addAndTreatMeasure(Measure newMeasure) {
		measures.add(newMeasure);
		if (measures.size() >= NUMBER_OF_MEASURES_SAVED) {
			measures.remove(0);
		}
		if(!newMeasure.isControloTmp()) {
			tempMeasures.add(newMeasure);
			if (tempMeasures.size() >= NUMBER_OF_MEASURES_SAVED) {
				tempMeasures.remove(0);
			}
			tempWorkers.submit(new TemperatureTask(new ArrayList<Measure>(tempMeasures)));
		}
		if(!newMeasure.isControloHum()) {
			humMeasures.add(newMeasure);
			if (humMeasures.size() >= NUMBER_OF_MEASURES_SAVED) {
				humMeasures.remove(0);
			}
			//humWorkers.submit(new HumidityTask(new ArrayList<Measure>(humMeasures)));
		}
		if(!newMeasure.isControloMov()) {
			movMeasures.add(newMeasure);
			if (movMeasures.size() >= NUMBER_OF_MEASURES_SAVED) {
				movMeasures.remove(0);
			}
			//movWorkers.submit(new MovTask(new ArrayList<Measure>(movMeasures)));
		}
		if(!newMeasure.isControloLum()) {
			lumMeasures.add(newMeasure);
			if (lumMeasures.size() >= NUMBER_OF_MEASURES_SAVED) {
				lumMeasures.remove(0);
			}
			//lumWorkers.submit(new LumTask(new ArrayList<Measure>(lumMeasures)));
		}
	}

	public void close() {
		System.exit(0);
	}
	

	
	public Round setNextOrCurrentRound(LocalDateTime time) {
		
		
		if(nextOrCurrentRound== null || (isTimeTocheckRounds() && !nextOrCurrentRound.getRound_begin().isAfter(time) )) {
			
			System.out.println("getting next round");
			 nextOrCurrentRound = mysqlConnector.findNextOrCurrentRound(time);
			 lastTimeChecked = LocalDateTime.now();
			 lastMovement= time;
			 time_to_worry= MovementTask.getTimeToWorryMov();
			 System.out.println("next round: " + nextOrCurrentRound);
			 return nextOrCurrentRound;
			 
		}else if(nextOrCurrentRound.getRound_begin().isAfter(time) ) //in case an older message pops up
			return  mysqlConnector.findNextOrCurrentRound(time);
		
		return nextOrCurrentRound;
			
		
	}
	public boolean isTimeTocheckRounds() {
		
			LocalDateTime limit= lastTimeChecked.plusMinutes(MINUTES_TO_RECHECK_ROUNDS);
			LocalDateTime now = LocalDateTime.now();
			return now.isAfter( limit ) || now.isEqual(limit) ;
	
	}
	
	public void setLastTimeChecked() {
		lastTimeChecked = LocalDateTime.now();
	}

	
	

	protected double getTempLimit() {
		return mysqlSystem.getLimiteTemperatura();

	}
}
