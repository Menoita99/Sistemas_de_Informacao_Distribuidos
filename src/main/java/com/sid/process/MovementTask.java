package com.sid.process;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.sid.models.Alarm;
import com.sid.models.Measure;
import com.sid.models.MovementAlarm;
import com.sid.models.Round;

public class MovementTask extends Task {

	private static final int TIME_TO_WORRY_MOV = 10;
	private static final int COOLDOWN = 10;

	private int first;

	private Round nextOrCurrentRound;




	public MovementTask(ArrayList<Measure> measuresCopy) {
		super(measuresCopy);
		//System.out.println("before initializing");
		nextOrCurrentRound = process.getNextOrCurrentRound();
		first =measures.size() -1;
		//System.out.println("after initializing");
	}

	



	@Override
	public void run() {
		System.out.println("Movement " + measures);
		alarm = verifyMovementValues();
		super.run();
	}

	



	private Alarm verifyMovementValues() {
		LocalDateTime time = measures.get(first).getDataHoraMedicao();

		if( !isCurrentRound(time)) {
			nextOrCurrentRound = process.setNextOrCurrentRound(time);
		}

		//se estiver a occorer ronda
		if(nextOrCurrentRound!= null && isCurrentRound(time) ) {

			process.increment_counter_to_worry();
			//System.out.println("Inside there is a round Counter " + process.getCounter_to_worry());
			verifyRound(time);
			//System.out.println("On current round counter: " + process.getCounter_to_worry());

			//se não estiver a ocorrer ronda	
		}else {
			verifyNoRound(time);
		}

		//send alarm
		if (alarming) {
			return new MovementAlarm(measure, descricao);
		}else {
			return null;
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




	public static int getCooldown() {
		return COOLDOWN;
	}

	
	



	private void verifyRound(LocalDateTime time) {
		System.out.println("there is a round right now");
		process.resetCooldown();

		//se houver movimento
		if(measures.get(first).getValorMovMedicao() == 1.0) {

			boolean moved = true;
			for(Measure m : measures) 
				if(m.getValorMovMedicao()== 0.0) {
					moved=false;
					break;
				}

			if(moved) {				
				process.reset_counter_to_worry();
				//System.out.println("Counter " + process.getCounter_to_worry());
			}
		}

		//se nao houver movimento por mais de TIME_TO_WORRY  minutos
		else if(measures.get(first).getValorMovMedicao() == 0.0 && process.getCounter_to_worry()>= TIME_TO_WORRY_MOV ) {
			System.out.println("ALERTA TIME TO WORRY");
			double a = (TIME_TO_WORRY_MOV*2)/60;
			descricao += "Alerta ausência de movimento por mais de " + a + " minutos";
			alarming = true;
			process.reset_counter_to_worry();
			System.out.println("Reset Counter " + process.getCounter_to_worry());
		}
	}



	
	

	private void verifyNoRound(LocalDateTime time) {
		process.decreaseCooldown();

		System.out.println("no round right now cooldown "+ process.getCooldown());
		boolean b =process.getCooldown()!=0;
		System.out.println("Cooldown!=0 " + b );
		//there was a movement
		if(measures.get(first).getValorMovMedicao() == 1.0 && process.getCooldown()==0) {

			boolean send_alert = true;
			nextOrCurrentRound = process.setNextOrCurrentRound(time); //making sure there is no round
			System.out.println(" Someone moved, getting round");
			System.out.println("next round "+ nextOrCurrentRound);

			if(!isCurrentRound(time)) {
				for(Measure m : measures) 
					if(m.getValorMovMedicao()== 0.0) {
						send_alert=false;
						break;
					}

				if(send_alert) {
					System.out.println("ALERTA SOMEONE'S MOVING");
					descricao += "Alerta detetado movimento, intruso";
					alarming = true;
					process.activateCooldown();
					System.out.println("Set Counter to cooldown " + process.getCooldown());

				}
			}
		}
	}
}
