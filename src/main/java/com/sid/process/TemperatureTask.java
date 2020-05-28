package com.sid.process;

import java.util.ArrayList;

import com.sid.models.Measure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemperatureTask extends Task {

	public TemperatureTask(ArrayList<Measure> measuresCopy) {
		super(measuresCopy);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}

}
