package com.sid.models;

import java.time.LocalDateTime;

public class MovementAlarm extends Alarm{

	public MovementAlarm(Measure measure,String descricao) {
		super(measure.getValorMovMedicao(),  "Mov", measure.getDataHoraMedicao(), measure.getValorMovMedicao(), descricao,"" , true);
		
	}

}
