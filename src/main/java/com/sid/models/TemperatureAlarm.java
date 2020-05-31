package com.sid.models;

import java.time.LocalDateTime;

import com.sid.process.Processor;

public class TemperatureAlarm extends Alarm {

	public TemperatureAlarm(double valorMedicao, String tipoSensor, LocalDateTime dataHoraMedicao, double limite,
			String descricao, String extra, boolean controlo) {
		super( valorMedicao, tipoSensor, dataHoraMedicao,
				 limite,  descricao,  extra,  controlo);
		// TODO Auto-generated constructor stub
	}

	public TemperatureAlarm(Measure measure, double limite, String descricao, String extra, boolean controlo) {
		super( measure.getValorTmpMedicao(), "tmp", measure.getDataHoraMedicao(),
				 limite,  descricao,  extra,  controlo);
	}

	public TemperatureAlarm(Measure measure, String descricao, String extra, boolean controlo) {
		super( measure.getValorTmpMedicao(), "tmp", measure.getDataHoraMedicao(),
				 Processor.getInstance().getMysqlSystem().getLimiteTemperatura(),  descricao,  extra,  controlo);
		// TODO Auto-generated constructor stub
	}
	public TemperatureAlarm(Measure measure, String descricao, boolean controlo) {
		super( measure.getValorTmpMedicao(), "tmp", measure.getDataHoraMedicao(),
				 Processor.getInstance().getMysqlSystem().getLimiteTemperatura(),  descricao,  measure.getExtraTmp(),  controlo);
		// TODO Auto-generated constructor stub
	}

	public TemperatureAlarm(Measure measure, String descricao) {
		super( measure.getValorTmpMedicao(), "tmp", measure.getDataHoraMedicao(),
				 Processor.getInstance().getMysqlSystem().getLimiteTemperatura(),  descricao,  measure.getExtraTmp(),  false);
		// TODO Auto-generated constructor stub
	}

	public TemperatureAlarm(double valorMedicao, double limite, String tipoSensor, String extra, String descricao,
			LocalDateTime dataHoraMedicao, boolean controlo) {
		super( valorMedicao, tipoSensor, dataHoraMedicao,
				 limite,  descricao,  extra,  controlo);
		// TODO Auto-generated constructor stub
	}

}
