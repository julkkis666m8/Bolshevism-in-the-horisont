package controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import constants.Constants;
import main.Main;
import market.Taxes;
import world.Nation;
import world.Pop;
import world.State;
import world.World;

public class Controller {

	public World world;
	
	public Controller(World world) {
		this.world = world;
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
				
				
				ExecutorService es = Executors.newCachedThreadPool();
				for(State state : states) es.execute(new Runnable() {

						@Override
						public void run() {
							//try {	
								tickState(nation, state);
							/*	
							}catch (Exception e) {
								System.out.println("!!!!!!ERROR IN STATE MULTITRHEADDING!!!!!!\n"+e);
								JOptionPane.showInternalMessageDialog(null, "!!!!!!ERROR IN STATE MULTITRHEADDING!!!!!!\n"+e);
							}*/
							
						}
				    	
				    });
				es.shutdown();
				try {
					boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// all tasks have finished or the time has been reached.
				//System.out.println("Simnum " + itteration);
			}
			
			
			
		}
	}

	/**
	 * set pop needs and wants to market
	 */
	private void updatePopNeeds() {
		
		for (Pop p : main.Main.world.getAllPops()) {
			double[] allNeeds = new double[Constants.AMOUNT_OF_GOODS];
			double[] needs1 = p.getNeeds();
			double[] needs2 = p.getWants();
			//TODO: add luxury wants etc here too
			
			int i = 0;
			for (double d : allNeeds) {
				d = needs1[i];
				d += needs2[i];
				//d += needs3[i];

				allNeeds[i] += d;
				
				i++;
			}
			
			
			p.getState().localMarket.modMarketNeeds(allNeeds);

			p.setNeedsFurfilled(0);
			p.setWantsFurfilled(0);
			//Luxury
		}
		
		
		
	}
	

	private void tickState(Nation nation, State state) {
		
		//START STATE METHOD
		
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

		ArrayList<Pop> popsA = new ArrayList<Pop>();
		
		popsA.addAll(pops);

		Collections.sort(popsA, new Comparator<Pop>() {
			public int compare(Pop o1, Pop o2) {
				return o1.getAverageWealth() > o2.getAverageWealth() ? -1 : o1.getAverageWealth() == o2.getAverageWealth() ? 0 : 1;
			}
		});

		for(Pop pop : popsA) {
			pop.buyTick(nation);
		}



		//END METHOD
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
