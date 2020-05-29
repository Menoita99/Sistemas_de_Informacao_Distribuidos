package com.sid.process;

import java.util.ArrayList;

import com.sid.database.MySqlConnector;
import com.sid.models.Alarm;
import com.sid.models.Measure;

import lombok.Data;

@Data
public class Task implements Runnable {

	protected ArrayList<Measure> measures;
	protected Measure measure;
	protected Processor process = Processor.getInstance();

	public Task(ArrayList<Measure> measuresCopy) {
		this.measures = measuresCopy;
		this.measure = measures.get(measures.size()-1);
		// Debug
		
		
		
	}

	@Override
	public void run() {
		MySqlConnector.getInstance().saveMeasure(measure);
		
		//TODO implement stuff here
	}
	
	
	//TODO implement stuff here
	protected double varianceCheck(double[] vals) {
		double variance = getInclination(vals[0], vals[1]);
		double average;
		for(int i = 1; i != vals.length-1;i++) {
			average = variance+getInclination(vals[i], vals[i+1]);
			if (average != 0) average /= 2;
			variance = average;
		}
		return variance;
	}
	
	private double getInclination(double val1,double val2) {		
		double result = val1-val2;
		if(result != 0) result /= -1; 
		return result;
	}
}
