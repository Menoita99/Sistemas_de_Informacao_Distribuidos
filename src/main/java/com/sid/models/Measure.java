package com.sid.models;

import java.time.LocalDateTime;

import org.json.JSONObject;

import lombok.Data;

@Data
public class Measure {

	private String objectId;
	
	private double valorTmpMedicao;
	private double valorHumMedicao;
	private double valorLumMedicao;
	private double valorMovMedicao;
	private String tipoSensor;
	private LocalDateTime dataHoraMedicao;
	private boolean controlo = false;
	private String extra = " ";

	private boolean valid;

	
	
	
	
	
	public Measure(JSONObject measure) {
		objectId = measure.getJSONObject("_id").getString("$oid");
		valorTmpMedicao =  measure.getDouble("tmp");
		valorHumMedicao =  measure.getDouble("hum");
		valorLumMedicao =  measure.getDouble("cell");
		valorMovMedicao =  measure.getDouble("mov");
		tipoSensor = measure.getString("sens");
		String[] splitedDat = measure.getString("dat").split("/");
		String[] splitedTim = measure.getString("tim").split(":");
		dataHoraMedicao = LocalDateTime.of(Integer.parseInt(splitedDat[2]), Integer.parseInt(splitedDat[1]), Integer.parseInt(splitedDat[0]), 
											Integer.parseInt(splitedTim[0]), Integer.parseInt(splitedTim[1]), Integer.parseInt(splitedTim[2]));
		valid = validate();
	}

	
	
	
	
	
	public boolean validate() {
		return tipoSensor.matches("[a-zA-Z]{4}") && valorLumMedicao>=0 && valorHumMedicao >=0;
	}

}
