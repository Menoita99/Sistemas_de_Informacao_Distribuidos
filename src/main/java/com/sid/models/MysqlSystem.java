	package com.sid.models;


import java.io.Serializable;
import java.util.List;

import com.sid.database.MySqlConnector;

import lombok.Data;

@Data
public class MysqlSystem  implements Serializable{

    private static final long serialVersionUID = 1L;

    private double limiteTemperatura;
    private double limiteHumidade;
    private double limiteLuminosidade;
    private double margemTemperatura;
    private double margemHumidade;
    private double margemLuminosidade;

    private static MysqlSystem INSTANCE;


	
	
	
	public MysqlSystem() {
		setSystemValues();
	}

	
	public void setSystemValues() {
		List<Object> systemValues = MySqlConnector.getInstance().getSystemValues();
		if(systemValues.isEmpty())
			System.err.println("[Warning] Could not load system values, please check if there are values in Sistema table");
		else {
			limiteTemperatura = (double) systemValues.get(0);
			limiteHumidade = (double) systemValues.get(1);
			limiteLuminosidade = (double) systemValues.get(2);
			margemTemperatura = (double) systemValues.get(3);
			margemHumidade = (double) systemValues.get(4);
			margemLuminosidade = (double) systemValues.get(5);
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
