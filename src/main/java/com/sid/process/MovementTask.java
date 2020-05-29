package com.sid.process;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.sid.models.Measure;
import com.sid.models.Round;

public class MovementTask extends Task {
		private static final int TIME_TO_WORRY_MOV = 1;

		private ArrayList<Measure> measures;

		private Round nextOrCurrentRound;
		private LocalDateTime lastTimeChecked;
		private LocalDateTime lastMovement;
		private Processor process;

	public MovementTask(ArrayList<Measure> measuresCopy) {
		super(measuresCopy);
		process = getProcess();
		System.out.println("before initializing");
		nextOrCurrentRound = process.getNextOrCurrentRound();
		lastTimeChecked = process.getLastTimeChecked();
		lastMovement = process.getLastMovement();
		System.out.println("after initializing");

		measures = getMeasures();
		}
	@Override
	public void run() {
		//super.run();
		System.out.println("Movement " + measures);
		verifyMomentValues();
		

		
	}
private void verifyMomentValues() {
		
		LocalDateTime time = measures.get(0).getDataHoraMedicao();
		
		if( !isCurrentRound(time)) {
			nextOrCurrentRound = process.setNextOrCurrentRound(time);
		
		}
		//se estiver a occorer ronda
		if(nextOrCurrentRound!= null && isCurrentRound(time) ) {
			System.out.println("there is a round right now");
			
			//se houver movimento
			if(measures.get(0).getValorMovMedicao() == 1.0)
				process.setLastMovement(time);
			//se nao houver movimento por mais de TIME_TO_WORRY  minutos
			else if( time.isAfter(lastMovement.plusMinutes(TIME_TO_WORRY_MOV)) ) {
				System.out.println("ALERTA TIME TO WORRY");
			}
			System.out.println("last movement " + lastMovement + "Time to worry " + lastMovement.plusMinutes(TIME_TO_WORRY_MOV) + "time now "+ time);
				    	 
			
		//se não estiver a ocorrer ronda	
		}else if (nextOrCurrentRound!= null) {
			System.out.println("no round right now");
			
			if(measures.get(0).getValorMovMedicao() == 1.0) {
				boolean send_alert = true;
				nextOrCurrentRound = process.setNextOrCurrentRound(time);
				System.out.println(" Someone moved, getting round");
				
				if(!isCurrentRound(time)) {
					for(Measure m : measures) 
						if(m.getValorMovMedicao()== 1.0)
							send_alert=false;
					
					if(send_alert)
						System.out.println("ALERTA SOMEONE'S MOVING");
				}
			
			}
		}
	}

	
	
	public boolean isCurrentRound(LocalDateTime datetime) {
		if(nextOrCurrentRound != null && nextOrCurrentRound.isCurrentRound(datetime) )
			return true;
		else
			return false;
		
		
	}
	
	
	
	
}
