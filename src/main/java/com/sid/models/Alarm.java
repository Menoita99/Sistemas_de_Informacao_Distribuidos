package com.sid.models;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Alarm {

	private double valorMedicao;
	private double limite;
	private String tipoSensor;
	private String extra = "";
	private String descricao;
	private LocalDateTime dataHoraMedicao;
	private boolean controlo = false;
	
	public Alarm(double valorMedicao,String tipoSensor,LocalDateTime dataHoraMedicao,
			double limite, String descricao, String extra, boolean controlo) {
		this.limite = limite;
		this.extra = extra;
		this.controlo = controlo;
		this.descricao = descricao;
		this.valorMedicao = valorMedicao;
		this.tipoSensor = tipoSensor;
		this.dataHoraMedicao = dataHoraMedicao;
	}
	
	
	public Alarm(Measure measure, double limite, String descricao, String extra, boolean controlo) {
		this.limite = limite;
		this.extra = extra;
		this.controlo = controlo;
		this.descricao = descricao;
//		valorMedicao = measure.getValorMedicao();
		tipoSensor = measure.getTipoSensor();
		dataHoraMedicao = measure.getDataHoraMedicao();
	}
	
	public Alarm(Measure measure, String descricao, String extra, boolean controlo) {
		//this.limite = MysqlSystem.getLimit(Sensor type);
		this.extra = extra;
		this.controlo = controlo;
		this.descricao = descricao;
//		valorMedicao = measure.getValorMedicao();
		tipoSensor = measure.getTipoSensor();
		dataHoraMedicao = measure.getDataHoraMedicao();
	}
	
	public Alarm(Measure measure, String descricao ) {
		//this.limite = MysqlSystem.getLimit(Sensor type);
		this.descricao = descricao;
//		valorMedicao = measure.getValorMedicao();
		tipoSensor = measure.getTipoSensor();
		dataHoraMedicao = measure.getDataHoraMedicao();
	}
}
