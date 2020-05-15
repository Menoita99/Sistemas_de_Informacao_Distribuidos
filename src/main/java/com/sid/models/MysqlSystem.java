package com.sid.models;


import java.util.List;

import com.sid.database.MySqlConnector;

import lombok.Data;

@Data
public class MysqlSystem {

	private double limiteTemperatura = 100;
	private double limiteHumidade = 100;
	private double limiteLuminosidade = 1000;

	private static MysqlSystem INSTANCE;


	
	
	
	public MysqlSystem() {
		List<Object> systemValues = MySqlConnector.getInstance().getSystemValues();
		if(systemValues.isEmpty())
			System.err.println("[Warning] Could not load system values, please check if there are values in Sistema table");
		else {
			limiteTemperatura = (double) systemValues.get(0);
			limiteHumidade = (double) systemValues.get(1);
			limiteLuminosidade = (double) systemValues.get(2);
		}
	}


	
	
	
	public static MysqlSystem getInstance() {
		if(INSTANCE == null)
			INSTANCE = new MysqlSystem();
		return INSTANCE;
	}


	
	
	
	public double getSystemLimit(String sensorType) {
		//TODO
		return 0.0;
	}
}
