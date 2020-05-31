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
		double margin = process.getMysqlSystem().getMargemTemperatura();
		double[] tempVals = measures.stream().mapToDouble(measure -> measure.getValorTmpMedicao()).toArray();
		double variance = varianceCheck(tempVals);
		double averageTemp = averageValue(tempVals);
		double variationLimit = 5;

		if (Math.abs(variance) > variationLimit) {
			//System.out.println("i'm quarentined :" + variance + " || " + variationLimit);
			
		} else {
			
			//System.out.println("i was let tru :" + variance + " || " + variationLimit);
			
			if (process.isTempOverLim()) {
				if (this.measure.getValorTmpMedicao() < limTemp && (averageTemp - limTemp) < -4) {
					descricao += "Temperatura desceu abaixo do limite";
					controlo = true;
					process.setTempOverLim(false);
					alarming = true;
				}
			} else {
				if (this.measure.getValorTmpMedicao() >= limTemp && (averageTemp - limTemp) > -2) {
					descricao += "Temperatura ultrapasou o limite";
					controlo = true;
					process.setTempOverLim(true);
					alarming = true;
				}
			}

			// System.out.println(process.getTempStatus()+" : "+variance);

			switch (process.getTempStatus()) {
			case 0: {
				//System.out.println("im in 0");
				if (variance > 0.2) {
					if (alarming)
						descricao += " e ";
					descricao += "Temperatura a subir";
					process.setTempStatus(1);
					alarming = true;
				} else if (variance < -0.2) {
					if (alarming)
						descricao += " e ";
					descricao += "Temperatura a descer";
					process.setTempStatus(-1);
					alarming = true;
				}
				break;
			}
			case 1: {
				//System.out.println("im in 1");
				if (variance > -0.2 && variance < 0.2) {
					if (alarming)
						descricao += " e ";
					descricao += "Temperatura estabilizou";
					process.setTempStatus(0);
					alarming = true;
				} else if (variance < -0.2) {
					if (alarming)
						descricao += " e ";
					descricao += "Temperatura a descer";
					process.setTempStatus(-1);
					alarming = true;
				}
				break;
			}
			case -1: {
				//System.out.println("im in -1");
				if (variance > 0.2) {
					if (alarming)
						descricao += " e ";
					descricao += "Temperatura a subir";
					process.setTempStatus(1);
					alarming = true;
				} else if (variance < 0.2 && variance > -0.2) {
					if (alarming)
						descricao += " e ";
					descricao += "Temperatura estabilizou";
					process.setTempStatus(0);
					alarming = true;
				}
				break;
			}
			default: {
				System.out.println("im Out " + process.getTempStatus());
				process.setTempStatus(0);
				break;
			}
			}
		}

		if (process.getTempCooldown() <= 0 && !process.isTempOverLim()) {
//			System.out.println(limTemp - averageTemp + "||" + margin);
			if ((limTemp - averageTemp) < margin) {
//				System.out.println("im getting there");
				if (alarming)
					descricao += " e ";
				descricao += "Temperatura proxima do limite";
				process.setTempCooldown(process.getTEMP_COOLDOWN_VALUE());
				alarming = true;
			}
		} else if (process.getTempCooldown() > 0) {
			if((limTemp - averageTemp) > margin){
				process.setTempCooldown(process.getTempCooldown() - (process.getTEMP_COOLDOWN_VALUE()/10));
			}
			process.setTempCooldown(process.getTempCooldown() - 1);
		}

		if (alarming) {
			return new TemperatureAlarm(measure, descricao, controlo);
		} else {
			return null;
		}

	}
}
