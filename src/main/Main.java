package main;
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
	public static JobParameters germanJob = new JobParameters(0, 0, 50, 50, 5, 5, 10, 2, 1, 2, 1, 1);
	public static RaceParameters jewishRace = new RaceParameters(Constants.JEWISH, Constants.ASHKERNAZI);
	public static JobParameters jewishJob = new JobParameters(10, 10, 50, 50, 0, 20, 0, 1, 0, 1, 0, 0);
	
	
	public static void main(String[] args) {
		controller = new Controller();
		world = new World();
		tickAmount = 100;
		
		
		
		
		
		germany = new Nation("Germany", "German");

		germany.addCoreRace(Constants.GERMANIC);
		germany.addAcceptedRace(Constants.NORDIC);
		germany.addHatedRace(Constants.ASHKERNAZI);
		
		world.addNation(germany);
		
		
		//testing nation w/ 3 states
		for (int i = 0; i < 3; i++) {
			germany.addState(new State("State "+i, germany));
		}



	      /*new Thread("PopWindow"){
	        public void run(){
	    		Application.launch(view.PopWindow.class, args);
	        }
	      }.start();*/
		
		while (true) {
			
			//JOptionPane.showMessageDialog(null, germany.getInfo());
			
			JOptionPane.showMessageDialog(null, germany.getInfo());
			
			view.PopWindow.tickUpdate();
			
			//JOptionPane.showMessageDialog(null, germany.getPopInfo());
			controller.tick(tickAmount);
			
			for(State s : germany.getStates()) {
				System.out.println("GERMAN MARKET: "+s.localMarket.getStockpileString());				
			}

		}
	}

}
