package com.sid.process;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.sid.models.Measure;
import com.sid.models.Round;

public class MovementTask extends Task {
		private static final int TIME_TO_WORRY_MOV = 10;

		private ArrayList<Measure> measures;

		private Round nextOrCurrentRound;
		private LocalDateTime lastTimeChecked;
		private LocalDateTime lastMovement;
		private Processor process;

	public MovementTask(ArrayList<Measure> measuresCopy) {
		super(measuresCopy);
		process = getProcess();
		nextOrCurrentRound = process.getNextOrCurrentRound();
		lastTimeChecked = process.getLastTimeChecked();
		lastMovement = process.getLastMovement();
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
			System.out.println("geting round");
			
		}
		//se estiver a occorer ronda
		else if(isCurrentRound(time) && nextOrCurrentRound!= null) {
				//se houver movimento
			if(measures.get(0).getValorMovMedicao() == 1.0)
				process.setLastTimeChecked();
			//se nao houver movimento por mais de TIME_TO_WORRY  minutos
			else if( time.isAfter(lastTimeChecked.plusMinutes(TIME_TO_WORRY_MOV)) ) {
				System.out.println("ALERTA TIME TO WORRY");
			}
		
				    	 
			
		//se não estiver a ocorrer ronda	
		}else if (nextOrCurrentRound!= null) {
			
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
