package com.sid.models;

import java.time.LocalDateTime;

import org.json.JSONObject;

import lombok.Data;

@Data
public class Measure {

	private String objectId;
	
	private double valorTmpMedicao = -1;
	private double valorHumMedicao = -1;
	private double valorLumMedicao = -1;
	private double valorMovMedicao = -1;
	private String tipoSensor;
	private LocalDateTime dataHoraMedicao;

	private boolean controloTmp = false;
	private String extraTmp = " ";
	
	private boolean controloHum = false;
	private String extraHum = " ";
	
	private boolean controloLum = false;
	private String extraLum = " ";
	
	private boolean controloMov = false;
	private String extraMov = " ";

	private boolean valid;

	
	
	
	
	
	public Measure(JSONObject measure) {
		objectId = measure.getJSONObject("_id").getString("$oid");
		setTemperature(measure);
		setHumidity(measure);
		setLuminosity(measure);
		setMovement(measure);
		tipoSensor = measure.getString("sens");
		String[] splitedDat = measure.getString("dat").split("/");
		String[] splitedTim = measure.getString("tim").split(":");
		dataHoraMedicao = LocalDateTime.of(Integer.parseInt(splitedDat[2]), Integer.parseInt(splitedDat[1]), Integer.parseInt(splitedDat[0]), 
											Integer.parseInt(splitedTim[0]), Integer.parseInt(splitedTim[1]), Integer.parseInt(splitedTim[2]));
		valid = validate();
	}



	private void setMovement(JSONObject measure) {
		try {
			valorMovMedicao = measure.getDouble("mov");
			//TODO VERIFICATIONS
		} catch (Exception e) {
			controloMov = false;
			extraMov = "Could not find value mov";
		}
	}






	private void setLuminosity(JSONObject measure) {
		try {
			valorLumMedicao = measure.getDouble("cell");
			//TODO VERIFICATIONS
		} catch (Exception e) {
			controloLum = false;
			extraLum = "Could not find value cell";
		}
	}






	private void setHumidity(JSONObject measure) {
		try {
			valorHumMedicao = measure.getDouble("hum");
			//TODO VERIFICATIONS
		} catch (Exception e) {
			controloHum = false;
			extraHum = "Could not find value hum";
		}
	}






	private void setTemperature(JSONObject measure) {
		try {
			valorTmpMedicao = measure.getDouble("tmp");
			//TODO VERIFICATIONS
		} catch (Exception e) {
			controloTmp = false;
			extraTmp = "Could not find value tmp";
		}
	}

	
	
	
	
	
	public boolean validate() {
		return tipoSensor.matches("[a-zA-Z]{4}") && valorLumMedicao>=0 && valorHumMedicao >=0;
	}

}
