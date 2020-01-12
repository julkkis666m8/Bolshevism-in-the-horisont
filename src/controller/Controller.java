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
			
			updateWorldsGoodPrices();
			
			updatePopNeeds();
			
			
			List<Nation> nations = Main.world.getNations();
			
			for(int nationInt = 0; nationInt < nations.size(); nationInt++) {
				
				Nation nation = nations.get(nationInt);
				nation.tick();
				
				
				List<State> states = (nation.getStates());
				for(int stateIndex = 0; stateIndex < states.size(); stateIndex++) {
					State state = states.get(stateIndex);
					state.tick();
					
					
					List<Pop> pops = state.pops;
					for(int i = 0; i < pops.size(); i++) {
						
						Pop pop = pops.get(i);
						
						if (pop.getPopulation() <= 0) {
							System.out.println("REMOVE DEAD PEOPLE "+pop.toString());
							pops.remove(i);
							i--;
						}
						else {

							pop.tick(nation);
							
						}
						
					}
				}
				
			}
			
			
			
		}
	}

	/**
	 * sets pop-needs to default for start of itteration
	 */
	private void updatePopNeeds() {
		
		for (Pop p : main.Main.world.getAllPops()) {
			
		}
		
	}

	/**
	 * prices go up if no goods left in market, down if
	 */
	private void updateWorldsGoodPrices() {
		for (State s : main.Main.world.getAllStates()){
			s.localMarket.updateGoods();
			s.localMarket.resetMarketNeedForTheTurn();
		}
	}
	
	
}
