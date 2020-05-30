package com.sid.process;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.sid.models.Measure;
import com.sid.models.Round;

public class MovementTask extends Task {
		private static final int TIME_TO_WORRY_MOV = 10;
		private static final int TIME_TO_SEND_EMAIL = 5;
		

		private ArrayList<Measure> measures;

		private Round nextOrCurrentRound;
		private LocalDateTime lastMovement;
		private Processor process;

	public MovementTask(ArrayList<Measure> measuresCopy) {
		super(measuresCopy);
		process = getProcess();
		System.out.println("before initializing");
		nextOrCurrentRound = process.getNextOrCurrentRound();
		lastMovement = process.getLastMovement();
		System.out.println("after initializing");

		measures = getMeasures();
		}
	@Override
	public void run() {
		//super.run();
		System.out.println("Movement " + measures);
		if(measures.get(0).isControloMov()==true) {
			verifyMomentValues();
			process.setBadMovement(null);
			process.setTime_to_send_email(TIME_TO_SEND_EMAIL);
		}
		else if(process.getBadMovement()== null)
					process.setBadMovement(measures.get(0).getDataHoraMedicao());
		else if(  measures.get(0).getDataHoraMedicao().isEqual(process.getBadMovement().plusMinutes(TIME_TO_SEND_EMAIL) ) ||
					measures.get(0).getDataHoraMedicao().isAfter(process.getBadMovement().plusMinutes(TIME_TO_SEND_EMAIL) )   ) {
						process.setBadMovement(null);
						//send email
						process.setTime_to_send_email(TIME_TO_SEND_EMAIL*10);
			
						
		}
						
						
		

		
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
			if(measures.get(0).getValorMovMedicao() == 1.0) {
				boolean moved = true;
				for(Measure m : measures) 
					if(m.getValorMovMedicao()== 0.0) {
						moved=false;
						break;
					}
				
				if(moved) {
					process.setTime_to_worry(TIME_TO_WORRY_MOV);
					process.setLastMovement(time); //tres mensagens
					lastMovement=process.getLastMovement();
					System.out.println("Moved" +lastMovement);
				}
				
			}
			
			//se nao houver movimento por mais de TIME_TO_WORRY  minutos
			else if(measures.get(0).getValorMovMedicao() == 0.0 && time.isAfter(lastMovement.plusMinutes(TIME_TO_WORRY_MOV)) ) {
				System.out.println("ALERTA TIME TO WORRY");
				process.setTime_to_worry(TIME_TO_WORRY_MOV*2);

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
						if(m.getValorMovMedicao()== 0.0) {
							send_alert=false;
							break;
						}
					
					if(send_alert) {
						System.out.println("ALERTA SOMEONE'S MOVING");
						process.setTime_to_worry(TIME_TO_WORRY_MOV*2);
					}
				}
			
			}else {
				process.setTime_to_worry(TIME_TO_WORRY_MOV);
				
			}
		}
	}

	
	
	public boolean isCurrentRound(LocalDateTime datetime) {
		if(nextOrCurrentRound != null && nextOrCurrentRound.isCurrentRound(datetime) )
			return true;
		else
			return false;
		
		
	}
	
	public static int getTimeToWorryMov() {
		return TIME_TO_WORRY_MOV;
	}
	public static int getTimeToSendEmail() {
		return TIME_TO_SEND_EMAIL;
	}
	
	
	
	
}
