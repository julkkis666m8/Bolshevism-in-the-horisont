package main;
import javax.swing.JOptionPane;

import constants.Constants;
import controller.Controller;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.NationGuiController;
import view.PopWindow;
import world.JobParameters;
import world.Nation;
import world.PopParameters;
import world.RaceParameters;
import world.State;
import world.World;

import java.io.IOException;
import java.util.Objects;

public class Main{

	public static Controller controller;
	public static World world;
	public static int tickAmount;
	public static Nation germany;
	public static PopWindow popWindow;
	public static NationGuiController nationGuiController;
	
	
	//temp
	public static RaceParameters germanRace = new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC);
	public static JobParameters germanJob = new JobParameters(0, 0, 30, 10, 5, 15, 1, 2, 2, 2, 3, 1);
	public static RaceParameters jewishRace = new RaceParameters(Constants.JEWISH, Constants.ASHKERNAZI);
	public static JobParameters jewishJob = new JobParameters(0, 100, 10, 10, 0, 20, 0, 1, 5, 1, 0, 0);
	public static RaceParameters polishRace = new RaceParameters(Constants.CATHOLIC, Constants.SLAV);
	public static JobParameters polishJob = new JobParameters(20, 0, 20, 5, 2, 1, 0, 1, 1, 2, 1, 1);
	public static Nation poland;
	
	//public State nullState = new State(null, null);
	
	
	public static void main() {
		
		long last_time = System.nanoTime(); //for calculating performance and such :^)


		
		
		world = new World();
		controller = new Controller(world);
		tickAmount = 1;
		
		
		
		//NATIONS--------------------
		
		germany = new Nation("Germany", "German",world);

		germany.addCoreRace(Constants.GERMANIC);
		germany.addAcceptedRace(Constants.NORDIC);
		germany.addHatedRace(Constants.ASHKERNAZI);
		
		world.addNation(germany);
		

		poland = new Nation("Poland", "Polish",world);

		poland.addCoreRace(Constants.SLAV);
		poland.addAcceptedRace(Constants.JEWISH);
		poland.addHatedRace(Constants.GERMANIC);
		
		world.addNation(poland);
		
		//--------------------------
		



		State state1 = new State("State "+1, germany, germanRace, germanJob, 2000);
		
		state1.addPop(PopParameters.createPops(germany, 500, polishRace, polishJob, state1));
		
		//State state2 = new State("State POL "+1, germany, polishRace, polishJob, 100);
		//State state2 = new State("State "+2, germany, germanRace, germanJob, 3250);
		//State state3 = new State("State "+3, germany, germanRace, germanJob, 5000);
		germany.addState(state1);
		//germany.addState(state2);
		//germany.addState(state3);
		
		//state1.addNeigbour(state2);
		//state2.addNeigbour(state3);
		
		
		State statep1 = new State("State "+1, poland, polishRace, polishJob, 10000/*(int)(Math.random() * 10000 + 1000)*/);
		State statep2 = new State("State "+2, poland, polishRace, polishJob, 100000/*(int)(Math.random() * 10000 + 1000)*/);
		State statep3 = new State("State "+3, poland, polishRace, polishJob, 100000/*(int)(Math.random() * 10000 + 1000)*/);
		poland.addState(statep1);
		poland.addState(statep2);
		poland.addState(statep3);



		statep1.addNeigbour(statep2);
		statep2.addNeigbour(statep3);
		
		
		statep1.addNeigbour(state1);



		/*
		new Thread("PopWindow"){
			public void run(){
				//Preloader.launch(view.PopWindow.class);
				popWindow = new PopWindow();
				popWindow.start(new Stage());
			}
		}.start();
		*/

	      
	      try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	      /*
	      new Thread("PopWindowUpdater"){
				public void run(){
					try {
						while(true) {
							view.PopWindow.tickUpdate();
						}
										
					}catch (Exception e) {
						System.out.println("PopWindowGuiNotLoaded");
						//e.printStackTrace();
					}
				}
			}.start();*/

		      try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		while (true) {
			
			//FIRST PRINT FROM THIS IS TIME TO SETUP
			//FOR CALCULATING TIME BETWEEN TICKS
		    long time = System.nanoTime();
		    int delta_time = (int) ((time - last_time) / 1000000);
		    //last_time = time; //we want to skip gui
		    //System.out.println(delta_time); //i want to see "fps" last on prints
			//FOR CALCULATING TIME BETWEEN TICKS

		    System.out.println("\n-----------------previous happenings above-----------");

			for(State s : germany.getStates()) {
				System.out.println("GERMAN MARKET: "+s.localMarket.getStockpileString());
				System.out.println(s.pops.size());
				//break;
			}
			System.out.println("POLISH MARKET: "+statep1.localMarket.getStockpileString());
			/*
			for(State s : poland.getStates()) {
				System.out.println("POLISH MARKET: "+s.localMarket.getStockpileString());
				System.out.println(s.pops.size());
				//break;
			}*/
			
			System.out.println("Delta time: "+delta_time+"ms"); //to show "fps"


		    System.out.println("-----------------new happenings below-----------\n");
			
			
			//for(State s : poland.getStates()) {
			//	System.out.println("POLISH MARKET: "+s.localMarket.getStockpileString());				
			//}

			try{
				nationGuiController.setTickUpdate(true);
			}catch (NullPointerException e){

			}
			JOptionPane.showMessageDialog(null, germany.getInfo());

			/*
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			//JOptionPane.showMessageDialog(null, poland.getInfo());
					
			last_time = System.nanoTime();
			controller.tick(tickAmount);
		}
	}


	public void setNationGui(NationGuiController nationGuiController) {
		this.nationGuiController = nationGuiController;
	}
}
