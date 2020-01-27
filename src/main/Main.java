package main;
import java.sql.Time;

import javax.swing.JOptionPane;

import constants.Constants;
import controller.Controller;
import javafx.application.Application;
import view.PopWindow;
import world.JobParameters;
import world.Nation;
import world.RaceParameters;
import world.State;
import world.World;

public class Main {

	public static Controller controller;
	public static World world;
	public static int tickAmount;
	public static Nation germany;
	public static PopWindow popWindow;
	
	
	//temp
	public static RaceParameters germanRace = new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC);
	public static JobParameters germanJob = new JobParameters(0, 0, 30, 10, 5, 5, 1, 2, 2, 2, 3, 1);
	public static RaceParameters jewishRace = new RaceParameters(Constants.JEWISH, Constants.ASHKERNAZI);
	public static JobParameters jewishJob = new JobParameters(0, 100, 10, 10, 0, 20, 0, 1, 5, 1, 0, 0);
	public static RaceParameters polishRace = new RaceParameters(Constants.CATHOLIC, Constants.SLAV);
	public static JobParameters polishJob = new JobParameters(200, 0, 0, 5, 2, 1, 0, 1, 1, 2, 1, 1);
	public static Nation poland;
	
	//public State nullState = new State(null, null);
	
	
	public static void main(String[] args) {
		world = new World();
		controller = new Controller(world);
		tickAmount = 1;
		
		
		
		
		
		germany = new Nation("Germany", "German");

		germany.addCoreRace(Constants.GERMANIC);
		germany.addAcceptedRace(Constants.NORDIC);
		germany.addHatedRace(Constants.ASHKERNAZI);
		
		world.addNation(germany);
		

		poland = new Nation("Poland", "Polish");

		poland.addCoreRace(Constants.SLAV);
		poland.addAcceptedRace(Constants.JEWISH);
		poland.addHatedRace(Constants.GERMANIC);
		
		world.addNation(poland);
		

		//testing nation w/ 3 states
		for (int i = 0; i < 1; i++) {
			//germany.addState(new State("State "+i, germany));
		}

		State state1 = new State("State "+1, germany, germanRace, germanJob, (int)(Math.random() * 10000 + 1000));
		//State state2 = new State("State "+2, germany);
		//State state3 = new State("State "+3, germany);
		germany.addState(state1);
		//germany.addState(state2);
		//germany.addState(state3);
		
		//state1.addNeigbour(state2);
		//state2.addNeigbour(state3);
		
		
		State statep1 = new State("State "+1, poland, polishRace, polishJob, 1000000000/*(int)(Math.random() * 10000 + 1000)*/);
		//State statep2 = new State("State "+2, poland);
		//State statep3 = new State("State "+3, poland);
		poland.addState(statep1);
		//poland.addState(statep2);
		//poland.addState(statep3);
		
		//statep1.addNeigbour(statep2);
		//statep2.addNeigbour(statep3);
		
		
		statep1.addNeigbour(state1);
		
		
		//testing nation w/ 3 states
		//for (int i = 0; i < 3; i++) {
			//poland.addState(new State("State "+i, poland));
		//}



	      new Thread("PopWindow"){
	        public void run(){
	    		Application.launch(view.PopWindow.class, args);
	        }
	      }.start();
	      
	      try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      
	      new Thread("PopWindowUpdater"){
				public void run(){
					try {
						while(true) {
							view.PopWindow.tickUpdate();	
						}
										
					}catch (Exception e) {
						System.out.println("GuiNotLoaded");
					}
				}
			}.start();

		      try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		while (true) {


			for(State s : germany.getStates()) {
				System.out.println("GERMAN MARKET: "+s.localMarket.getStockpileString());
				System.out.println(s.pops.size());
				break;
			}
			for(State s : poland.getStates()) {
				System.out.println("POLISH MARKET: "+s.localMarket.getStockpileString());
				System.out.println(s.pops.size());
				break;
			}
			
			
			//for(State s : poland.getStates()) {
			//	System.out.println("POLISH MARKET: "+s.localMarket.getStockpileString());				
			//}
			
			
			JOptionPane.showMessageDialog(null, germany.getInfo());

			JOptionPane.showMessageDialog(null, poland.getInfo());
					
			
			controller.tick(tickAmount);
		}
	}

}
