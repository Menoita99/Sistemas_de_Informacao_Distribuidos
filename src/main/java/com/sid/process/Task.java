package com.sid.process;

import java.util.ArrayList;

import com.sid.database.MySqlConnector;
import com.sid.models.Measure;

import lombok.Data;

@Data
public class Task implements Runnable {

	private ArrayList<Measure> measures;
	private Measure measure;
	private Processor process = Processor.getInstance();

	public Task(ArrayList<Measure> measuresCopy) {
		this.measures = measuresCopy;
		this.measure = measures.get(measures.size()-1);
	}

	@Override
	public void run() {
		MySqlConnector.getInstance().saveMeasure(measure);
		//TODO implement stuff here
	}
	
	//TODO implement stuff here
	
}
