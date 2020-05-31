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
		alarm = verificarTemperatura();
		super.run();
	}

	private Alarm verificarTemperatura() {
		String descricao = "";
		Boolean alarming = false;
		Boolean controlo = false;
		double limTemp = process.getMysqlSystem().getLimiteTemperatura();
		double[] tempVals = measures.stream().mapToDouble(measure->measure.getValorTmpMedicao()).toArray();
		double variance = varianceCheck(tempVals);
		
		if(process.isTempOverLim()) {
			if(this.measure.getValorTmpMedicao() < limTemp && (averageValue(tempVals) - limTemp) < -4) {
				descricao += "Temperatura desceu abaixo do limite";
				controlo = true;
				process.setTempOverLim(false);
				alarming = true;
			}
		}else {
			if(this.measure.getValorTmpMedicao() > limTemp && (averageValue(tempVals) - limTemp) > -2) {
				descricao += "Temperatura ultrapasou o limite";
				controlo = true;
				process.setTempOverLim(true);
				alarming = true;
			}
		}
		
		System.out.println(process.getTempStatus()+"  :  "+variance);
		if(Math.abs(variance) > 5) {
			System.out.println("im traped :" + variance);
		}else {
			try {
				switch (process.getTempStatus()) 
				{
					case 0: 
					{
						if(variance >0.2) 
						{
							if(alarming) descricao += " e ";
							descricao += "Temperatura a subir";
							process.setTempStatus(1);
							alarming = true;
						}
						else if(variance < -0.2) 
						{
							if(alarming) descricao += " e ";
							descricao += "Temperatura a descer";
							process.setTempStatus(-1);
							alarming = true;
						}
					}
					case 1: 
					{
						if(variance >-0.2 && variance <0.2) 
						{
							if(alarming) descricao += " e ";
							descricao += "Temperatura estabilizou";
							process.setTempStatus(0);
							alarming = true;
						}
						else if(variance < -0.2) 
						{
							if(alarming) descricao += " e ";
							descricao += "Temperatura a descer";
							process.setTempStatus(-1);
							alarming = true;
						}
					}
					case -1: 
					{
						if(variance >0.2) 
						{
							if(alarming) descricao += " e ";
							descricao += "Temperatura a subir";
							process.setTempStatus(1);
							alarming = true;
						}
						else if(variance< 0.2 && variance>-0.2) 
						{
							if(alarming) descricao += " e ";
							descricao += "Temperatura estabilizou";
							process.setTempStatus(0);
							alarming = true;
						}
					}
					default:
						
						throw new IllegalArgumentException("Unexpected value: " + process.getTempStatus());
						
				}
			} catch (IllegalArgumentException e) {
				// TODO: handle exception
				System.out.println(e);
				process.setTempStatus(0);
			}
		}
		
		
		if (alarming) {
			return new TemperatureAlarm(measure, descricao, "", controlo);
		}else {
			return null;
		}
		
	}
}
