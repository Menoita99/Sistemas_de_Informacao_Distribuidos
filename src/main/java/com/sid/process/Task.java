package com.sid.process;

import com.sid.database.MySqlConnector;
import com.sid.models.Measure;

import lombok.Data;

@Data
public class Task implements Runnable {

	private Measure measure;
	private Processor process = Processor.getInstance();

	public Task(Measure measure) {
		this.measure = measure;
	}

	@Override
	public void run() {
		process.getMeasures().add(measure);
		MySqlConnector.getInstance().saveMeasure(measure);
		//TODO implement stuff here
	}
	
	//TODO implement stuff here
}
