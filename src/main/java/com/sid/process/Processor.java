package com.sid.process;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

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
	 * 
	 */
	public void Process() {
		System.out.println("Running");
		Random r = new Random();
		while(true) {
			/*
			 *Isto é apenas para testar o Performance Monitor Depois é para apagar 
			 */
			try {
				Thread.sleep(2000);
				//{"tmp":"52","hum":"52.00","dat":"19/04/2020","tim":"09:54:58","cell":"228","mov":"1",”sens":"eth"}
				Map<String, Object> m = Map.of("tmp",r.nextInt(20)+10,
												"hum",r.nextInt(50)+10,
												"dat","15/05/2020",
												"tim",LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")),
												"cell",r.nextInt(200)+10,
												"mov",r.nextBoolean() ? 1 : 0,
												"sens","tst");
				measures.add(new Measure(new JSONObject(m)));
				if(measures.size() >= 3)
					measures.clear();
				System.out.println(measures.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			//TODO implement
		}
	}


	
	
	
	public static Processor getInstance() {
		if(INSTANCE == null)
			INSTANCE = new Processor();
		return INSTANCE;
	}
}
