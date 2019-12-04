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
				List<State> states = (nation.getStates());
				

				for(int stateIndex = 0; stateIndex < states.size(); stateIndex++) {
					State state = states.get(stateIndex);
					List<Pop> pops = state.pops;
					
					for(int i = 0; i < pops.size(); i++) {
						
						Pop pop = pops.get(i);
						
						if (pop.getPopulation() <= 0) {
							pops.remove(i);
							i--;
						}
						
						pop.jobCounter(nation, state);//has sell for RGO
						Taxes.taxMe(pop, nation); //income tax
						pop.buy(nation, state);
						
						pop.birthControll();
						
						//System.out.println(itteration);
						
						
						//pop.removePeople( (( (int)(Math.round((pop.getPopulation())*0.1)) ) + 150) );
						
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
		}
	}
	
	
}
