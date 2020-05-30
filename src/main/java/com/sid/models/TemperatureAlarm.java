package com.sid.models;

import java.time.LocalDateTime;

public class TemperatureAlarm extends Alarm {

	public TemperatureAlarm(double valorMedicao, String tipoSensor, LocalDateTime dataHoraMedicao, double limite,
			String descricao, String extra, boolean controlo) {
		super(valorMedicao, tipoSensor, dataHoraMedicao, limite, descricao, extra, controlo);
		// TODO Auto-generated constructor stub
	}

	public TemperatureAlarm(Measure measure, double limite, String descricao, String extra, boolean controlo) {
		super(measure, limite, descricao, extra, controlo);
		// TODO Auto-generated constructor stub
	}

	public TemperatureAlarm(Measure measure, String descricao, String extra, boolean controlo) {
		super(measure, descricao, extra, controlo);
		// TODO Auto-generated constructor stub
	}

	public TemperatureAlarm(Measure measure, String descricao) {
		super(measure, descricao);
		// TODO Auto-generated constructor stub
	}

	public TemperatureAlarm(double valorMedicao, double limite, String tipoSensor, String extra, String descricao,
			LocalDateTime dataHoraMedicao, boolean controlo) {
		super(valorMedicao, limite, tipoSensor, extra, descricao, dataHoraMedicao, controlo);
		// TODO Auto-generated constructor stub
	}

}
