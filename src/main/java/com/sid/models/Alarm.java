package com.sid.models;

import java.time.LocalDateTime;

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
}
