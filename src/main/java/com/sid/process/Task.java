package com.sid.process;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.sid.database.MySqlConnector;
import com.sid.models.Alarm;
import com.sid.models.Measure;
import com.sid.models.Round;

import lombok.Data;

@Data
public class Task implements Runnable {

	private ArrayList<Measure> measures;
	private Measure measure;
	private Processor process = Processor.getInstance();
	
	

	public Task(ArrayList<Measure> measuresCopy) {
		this.measures = measuresCopy;
		this.measure = measures.get(measures.size()-1);
		
		// Debug
		
		
		
	}

	@Override
	public void run() {
		System.out.println(measures);
		Alarm temp = verificarTemperatura();
		MySqlConnector.getInstance().saveMeasure(measure);
		
		
		//TODO implement stuff here
	}
	
	private Alarm verificarTemperatura() {
		double[] tempVals = measures.stream().mapToDouble(measure->measure.getValorTmpMedicao()).toArray();
		double variance = varianceCheck(tempVals);
		System.out.println(variance);
		return null;
	}
	//TODO implement stuff here
	private double varianceCheck(double[] vals) {
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
