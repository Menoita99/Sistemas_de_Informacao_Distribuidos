package com.sid.process;

import java.util.ArrayList;

import com.sid.database.MySqlConnector;
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
		
		if(tempAlarm != null) {
			System.out.println(tempAlarm);
			MySqlConnector.getInstance().insertAlarm(tempAlarm);
		}
		super.run();
	}
	private Alarm verificarTemperatura() {
		String descricao = "";
		Boolean alarming = false;
		Boolean controlo = false;
		double[] tempVals = measures.stream().mapToDouble(measure->measure.getValorTmpMedicao()).toArray();
		double variance = varianceCheck(tempVals);
		
		if(process.isTempOverLim()) {
			if(this.measure.getValorTmpMedicao() < process.getTempLimit() && (averageValue(tempVals) - process.getTempLimit()) < -4) {
				descricao += "Temperatura desceu abaixo do limite";
				controlo = true;
				process.setTempOverLim(false);
				alarming = true;
			}
		}else {
			if(this.measure.getValorTmpMedicao() > process.getTempLimit() && (averageValue(tempVals) - process.getTempLimit()) > -2) {
				descricao += "Temperatura ultrapasou o limite";
				controlo = true;
				process.setTempOverLim(true);
				alarming = true;
			}
		}
		
		System.out.println(process.getTempStatus()+"  :  "+variance);
		if(process.getTempStatus() == 0) {
			if(variance >0.2) {
				descricao += "Temperatura a subir";
				process.setTempStatus(1);
				alarming = true;
			}else if(variance < -0.2) {
				descricao += "Temperatura a descer";
				process.setTempStatus(-1);
				alarming = true;
			}
		}else if(process.getTempStatus() == 1) {
			if(variance >-0.2 && variance <0.2) {
				descricao += "Temperatura estabilizou";
				process.setTempStatus(0);
				alarming = true;
			}else if(variance < -0.2) {
				descricao += "Temperatura a descer";
				process.setTempStatus(-1);
				alarming = true;
			}
		}else if(process.getTempStatus() == -1) {
			if(variance >0.2) {
				if(alarming) descricao += " e ";
				descricao += "Temperatura a subir";
				process.setTempStatus(1);
				alarming = true;
			}else if(variance< 0.2 && variance>-0.2) {
				descricao += "Temperatura estabilizou";
				process.setTempStatus(0);
				alarming = true;
			}
		}
		
		if (alarming) {
			return new TemperatureAlarm(measure, descricao, "", controlo);
		}else {
			return null;
		}
		
	}
}
