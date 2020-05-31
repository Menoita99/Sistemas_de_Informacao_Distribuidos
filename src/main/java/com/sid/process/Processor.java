package com.sid.process;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONObject;

import com.sid.database.MongoConnector;
import com.sid.database.MySqlConnector;
import com.sid.models.Measure;
import com.sid.models.MysqlSystem;
import com.sid.models.Round;
import com.sid.util.EmailSender;
import com.sid.util.ThreadPool;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

@Data
public class Processor {
	
	
	private static final String EMAIL_SUBJECT = "URGENTE MALFUNCIONAMENTO SENSOR ";
	private static final String EMAIL_FIELD = "Urgente! Estão a ser enviadas mensagens inválidas através do sensor de ";


	private static final int NUMBER_OF_MEASURES_SAVED = 5;
	private static final long MINUTES_TO_RECHECK_ROUNDS = 10;
	private static final int NUMBER_WRONG__TO_EMAIL = 20;
	private static final int NUMBER_RIGHT__TO_RESET = 5;
	private static final int NUMBER_RESET_COOLDOWN = 21600;

	private static Processor INSTANCE; //this is used by performance monitor
	private ObservableList<Measure> measures = FXCollections.observableArrayList();
	private ObservableList<Measure> tempMeasures = FXCollections.observableArrayList();
	private ObservableList<Measure> humMeasures = FXCollections.observableArrayList();
	private ObservableList<Measure> movMeasures = FXCollections.observableArrayList();
	private ObservableList<Measure> lumMeasures = FXCollections.observableArrayList();
	
	//sendEmail
	private int wrongMeasuresTemp =0;
	private int wrongMeasuresHum =0;
	private int wrongMeasuresMov =0;
	private int wrongMeasuresLum =0;
	
	private int rightMeasuresTemp =0;
	private int rightMeasuresHum =0;
	private int rightMeasuresMov =0;
	private int rightMeasuresLum =0;
	
	
	private boolean temp_sent_email ;
	private boolean hum_sent_email ;
	private boolean mov_sent_email ;
	private boolean lum_sent_email ;
	
	private int temp_send_email_cooldown;
	private int hum_send_email_cooldown;
	private int mov_send_email_cooldown;
	private int lum_send_email_cooldown;
	


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
	private int counter_to_worry;
	private int cooldown;
	

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

		//time_to_worry = MovementTask.getTimeToWorryMov();

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
		
		temp_sent_email = false;
		hum_sent_email = false;
		mov_sent_email = false;
		lum_sent_email = false;
		
	}


	
	
	
	/**
	 * Main loop
	 */
	public void Process() {
		while(true) {
			JSONObject jobj = mongoConnector.read();
			//System.out.println("Read-> "+jobj);
			try {
				Measure measure = new Measure(jobj);
				addAndTreatMeasure(measure);
				MySqlConnector.getInstance().saveMeasure(measure);

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
		addTempMeasure(newMeasure);
		addHumMeasure(newMeasure);
		addMovMeasure(newMeasure);
		addLumMeasure(newMeasure);
		
		
		
		
	}

	
	
	
	
	
	private void addTempMeasure(Measure newMeasure) {
		if(temp_sent_email) {
			temp_send_email_cooldown--;
			if(temp_send_email_cooldown<=0)
				temp_sent_email=false;
		}
		
		if(newMeasure.isControloTmp()) {
			tempMeasures.add(newMeasure);
			if (tempMeasures.size() >= NUMBER_OF_MEASURES_SAVED) {
				tempMeasures.remove(0);
			}
			tempWorkers.submit(new TemperatureTask(new ArrayList<Measure>(tempMeasures)));
			rightMeasuresTemp++;
			
			if(rightMeasuresTemp >= NUMBER_RIGHT__TO_RESET) {
				wrongMeasuresTemp = 0;
				rightMeasuresTemp = 0;
			}
				
			}else{
				wrongMeasuresTemp++;
				rightMeasuresTemp = 0;
				if (wrongMeasuresTemp >= NUMBER_WRONG__TO_EMAIL && !temp_sent_email ) {
					EmailSender.sendEmail(EMAIL_SUBJECT, EMAIL_FIELD + " Movimento");
					wrongMeasuresTemp = 0;
					temp_sent_email=true;
					temp_send_email_cooldown = NUMBER_RESET_COOLDOWN;
					
				}
					
			
			
		}
		
	}
	private void addHumMeasure(Measure newMeasure) {
		
		if(hum_sent_email) {
			hum_send_email_cooldown--;
			if(hum_send_email_cooldown<=0)
				hum_sent_email=false;
				
		}
		
		
		if(newMeasure.isControloHum()) {
			humMeasures.add(newMeasure);
			if (humMeasures.size() >= NUMBER_OF_MEASURES_SAVED) {
				humMeasures.remove(0);
			}
			//humWorkers.submit(new HumidityTask(new ArrayList<Measure>(humMeasures)));
			
			rightMeasuresHum++;
			
			if(rightMeasuresHum >= NUMBER_RIGHT__TO_RESET) {
				wrongMeasuresHum = 0;
				rightMeasuresHum = 0;
				
				
			}
				
	
			}else{
				wrongMeasuresHum++;
				rightMeasuresHum = 0;
				if (wrongMeasuresHum >= NUMBER_WRONG__TO_EMAIL && !hum_sent_email ) {
					EmailSender.sendEmail(EMAIL_SUBJECT, EMAIL_FIELD + " Movimento");
					wrongMeasuresHum = 0;
					hum_sent_email=true;
					hum_send_email_cooldown = NUMBER_RESET_COOLDOWN;
				
			}
				
			
			
		}
		
	}
	private void addMovMeasure(Measure newMeasure) {
		
		if(mov_sent_email) {
			mov_send_email_cooldown--;
			if(mov_send_email_cooldown<=0)
				mov_sent_email=false;
				
		}
		
		
		if(newMeasure.isControloMov()) {
			movMeasures.add(newMeasure);
			if (movMeasures.size() >= NUMBER_OF_MEASURES_SAVED) {
				movMeasures.remove(0);
			}
			movWorkers.submit(new MovementTask(new ArrayList<Measure>(movMeasures)));
			rightMeasuresMov++;
			
			if(rightMeasuresMov >= NUMBER_RIGHT__TO_RESET) {
				wrongMeasuresMov = 0;
				rightMeasuresMov = 0;
				
				
			}
		}
		else {
			wrongMeasuresMov++;
			rightMeasuresMov = 0;
			if (wrongMeasuresMov >= NUMBER_WRONG__TO_EMAIL && !mov_sent_email ) {
				EmailSender.sendEmail(EMAIL_SUBJECT, EMAIL_FIELD + " Movimento");
				wrongMeasuresMov = 0;
				mov_sent_email=true;
				mov_send_email_cooldown = NUMBER_RESET_COOLDOWN;
				
			}
				
			
		}
		
	}
	private void addLumMeasure(Measure newMeasure) {
		
		if(lum_sent_email) {
			lum_send_email_cooldown--;
			if(lum_send_email_cooldown<=0)
				lum_sent_email=false;
				
		}
		
		if(newMeasure.isControloLum()) {
			lumMeasures.add(newMeasure);
			if (lumMeasures.size() >= NUMBER_OF_MEASURES_SAVED) {
				lumMeasures.remove(0);
			}
			//lumWorkers.submit(new LumTask(new ArrayList<Measure>(lumMeasures)));
			rightMeasuresLum++;

	
			if(rightMeasuresLum >= NUMBER_RIGHT__TO_RESET) {
				wrongMeasuresLum = 0;
				rightMeasuresLum = 0;
				
			}
			
		}
			else {
				wrongMeasuresLum++;
				rightMeasuresLum = 0;
				if (wrongMeasuresLum >= NUMBER_WRONG__TO_EMAIL && !lum_sent_email ) {
					EmailSender.sendEmail(EMAIL_SUBJECT, EMAIL_FIELD + " Luminosidade");
					wrongMeasuresLum = 0;
					lum_sent_email=true;
					lum_send_email_cooldown = NUMBER_RESET_COOLDOWN;
					
				}
					
				
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
			// time_to_worry= MovementTask.getTimeToWorryMov();
			 reset_counter_to_worry();
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
	
	public void resetCooldown() {
		cooldown = 0;
		
	}
	public void activateCooldown() {
		cooldown = MovementTask.getCooldown();
	}
	public void decreaseCooldown() {
		if(cooldown-1>= 0)
			cooldown--;
	}
	public void  increment_counter_to_worry() {
		counter_to_worry ++;
		
	}
	public void  reset_counter_to_worry() {
		counter_to_worry = 0;
		
	}
	
	
}
