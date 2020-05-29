package com.sid.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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


	public double getValorMedicao() {
		return this.valorMedicao;
	}

	public double getLimite() {
		return this.limite;
	}
	public String getTipoSensor() {
		return this.tipoSensor;
	}
	public String getExtra() {
		return this.extra;
	}
	public String getDescricao() {
		return this.descricao;
	}
	public String getDataHoraMedicao() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String data = this.dataHoraMedicao.format(formatter);
		return data;
	}
	public int getControlo() {
		if (this.controlo)
			return 1;
		return 0;
	}
}
