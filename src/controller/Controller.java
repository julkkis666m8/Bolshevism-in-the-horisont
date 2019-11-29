package controller;
import java.util.List;

import main.Main;
import market.Taxes;
import world.Nation;
import world.Pop;
import world.State;

public class Controller {

	public Controller() {
		
	}
	
	/**
	 * 
	 */
	public void tick(int simNum){
		
		//simulates all states
		
		
		for(int itteration = 0; itteration < simNum; itteration++) {
			
			//tickStates(Main.world.getAllStates());

			//List<State> states = Main.world.getAllStates();
			
			List<Nation> nations = Main.world.getNations();
			
			for(int nationInt = 0; nationInt < nations.size(); nationInt++) {
				Nation nation = nations.get(nationInt);
				List<State> states = (nation.getStates());
				

				for(int stateIndex = 0; stateIndex < states.size(); stateIndex++) {
					//tickPops(states.get(i).pops);
					State state = states.get(stateIndex);
					List<Pop> pops = state.pops;
					
					for(int i = 0; i < pops.size(); i++) {
						
						Pop pop = pops.get(i);
						
						if (pop.getPopulation() <= 0) {
							pops.remove(i);
							i--;
						}
						
						//PopTicker.tick(pop);
						
						//temp:
						
						pop.jobCounter(nation, state);//has sell for RGO
						Taxes.taxMe(pop, nation);
						//supplyHandler();
						pop.buy(nation, state);
						//demandHandler();
						
						//System.out.println(itteration);
						
						
						//pop.removePeople( (( (int)(Math.round((pop.getPopulation())*0.1)) ) + 150) );
						
					}
				}
				
			}
			
			
			
		}
	}
	
	
	
/*
	private void tickStates(List<State> states) {
		//run tick on every state
		for(int i = 0; i < states.size(); i++) {
			tickPops(states.get(i).pops);
		}
	}
	
	
	

	private void tickPops(LinkedList<Pop> pops) {
		//use itterator here to run over all pops in for-loop
		//remove empty pops from list
		
		
		
		for(int i = 0; i < pops.size(); i++) {
			
			Pop pop = pops.get(i);
			
			if (pop.getPopulation() <= 0) {
				pops.remove(i);
				i--; //TODO: BUGCHEK IF THIS WORKS AS INTENDED
			}
			
			
			PopTicker.tick(pop);
			
		}
	}
*/
	
}
