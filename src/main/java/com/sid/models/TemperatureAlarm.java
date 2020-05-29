package com.sid.models;



public class TemperatureAlarm extends Alarm {

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

}
