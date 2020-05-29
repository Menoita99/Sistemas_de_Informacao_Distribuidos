package com.sid.process;

import java.util.ArrayList;

import com.sid.models.Alarm;
import com.sid.models.Measure;
import com.sid.models.TemperatureAlarm;



public class TemperatureTask extends Task {

	public TemperatureTask(ArrayList<Measure> measuresCopy) {
		super(measuresCopy);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run() {
		Alarm tempAlarm = verificarTemperatura();
		super.run();
	}
	private Alarm verificarTemperatura() {
		String descricao = "";
		Boolean alarming = false;
		Boolean controlo = false;
		double[] tempVals = measures.stream().mapToDouble(measure->measure.getValorTmpMedicao()).toArray();
		double variance = varianceCheck(tempVals);
		if(this.measure.getValorTmpMedicao() > process.getTempLimit()) {
			descricao += "Temperatura ultrapasou o limite";
			controlo = true;
		}else{
			if(this.measure.getValorTmpMedicao()+variance > process.getTempLimit()) {
			
			}else if(this.measure.getValorTmpMedicao()+(3*variance) > process.getTempLimit()) {
			
			}else if(this.measure.getValorTmpMedicao()+(5*variance) > process.getTempLimit()) {
			
			}
		}
		
		System.out.println(variance);
		if (alarming) {
			return new TemperatureAlarm(measure, descricao, "", controlo);
		}else {
			return null;
		}
		
	}
}
