package com.sid.process;

import java.util.ArrayList;

import com.sid.database.MySqlConnector;
import com.sid.models.Alarm;
import com.sid.models.Measure;

import lombok.Data;

@Data
public class Task implements Runnable {
	protected Alarm alarm;
	protected ArrayList<Measure> measures;
	protected Measure measure;
	protected Processor process = Processor.getInstance();
	protected String descricao = "";
	protected Boolean alarming = false;
	protected Boolean controlo = false;

	public Task(ArrayList<Measure> measuresCopy) {
		this.measures = measuresCopy;
		this.measure = measures.get(measures.size()-1);
		// Debug
		
		
		
	}

	@Override
	public void run() {
		if(alarm != null) {
			System.out.println(alarm);
			MySqlConnector.getInstance().insertAlarm(alarm);
		}
		//System.out.println(measures);
		
		//TODO implement stuff here
	}

	//TODO implement stuff here

	protected double varianceCheck(double[] vals) {
		if(vals.length > 1) {

		double sum = getInclination(vals[0], vals[1]);
		for(int i = 1; i != vals.length-1;i++) {
			sum += getInclination(vals[i], vals[i+1]);
		}
		return sum/vals.length;
		}else {
			return 0;
		}
	}
	
	private double getInclination(double val1,double val2) {		
		double result = val1-val2;
		result /= -1; 
		return result;
	}
	
	protected double averageValue(double[] vals) {
		double average = 0;
		for (double value : vals) {
			average += value;
		}
		return vals.length != 0? average/vals.length : 0;
	}
}
