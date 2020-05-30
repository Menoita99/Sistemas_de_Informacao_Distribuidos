package com.sid.process;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

	private static Processor INSTANCE; //this is used by performance monitor
	private final static int MINUTES_TO_RECHECK_ROUNDS = 10;
	
	private ObservableList<Measure> measures = FXCollections.observableArrayList();

	//connectors
	private MySqlConnector mysqlConnector;
	private MongoConnector mongoConnector;
	
	private MysqlSystem mysqlSystem;
	
	private ThreadPool workers;
	
	//variables to help check movement
	private Round nextOrCurrentRound;
	private LocalDateTime lastTimeChecked;
	private LocalDateTime lastMovement;
	private LocalDateTime badMovement;
	private int time_to_worry;
	private int time_to_send_email;
	
	
	
	public Processor() {
		mysqlConnector = MySqlConnector.getInstance();
		mongoConnector = MongoConnector.getInstance();
		mysqlSystem = MysqlSystem.getInstance();
		time_to_worry = MovementTask.getTimeToWorryMov();
		time_to_worry = MovementTask.getTimeToSendEmail();
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
				workers.submit(new MovementTask(new ArrayList<Measure>(measures)));
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
