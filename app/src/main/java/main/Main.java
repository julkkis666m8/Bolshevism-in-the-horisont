package main;
import javax.swing.JOptionPane;

import constants.Constants;
import controller.Controller;
import view.NationGuiController;
import view.PopWindow;
import world.JobParameters;
import world.Nation;
import world.PopParameters;
import world.RaceParameters;
import world.State;
import world.World;


public class Main{

	public static World world = new World();
	public static Controller controller = new Controller(world);
	public static int tickAmount = 10;
	public static PopWindow popWindow;
	public static Nation germany;
	public static NationGuiController nationGuiController;
	//public static PopWindow popGuiController;
	
	
	//temp
	public static RaceParameters germanRace = new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC);
	public static JobParameters germanJob = new JobParameters(0, 0, 30, 10, 5, 15, 1, 2, 2, 2, 3, 1);
	public static RaceParameters nordicRace = new RaceParameters(Constants.PROTESTANT, Constants.NORDIC);
	public static JobParameters nordicJob = new JobParameters(0, 0, 30, 30, 5, 15, 1, 2, 2, 2, 3, 1);
	public static RaceParameters finnicRace = new RaceParameters(Constants.PROTESTANT, Constants.FINNIC);
	public static JobParameters finnicJob = new JobParameters(0, 0, 40, 30, 5, 15, 1, 1, 1, 1, 1, 1);
	public static RaceParameters jewishRace = new RaceParameters(Constants.JEWISH, Constants.ASHKERNAZI);
	public static JobParameters jewishJob = new JobParameters(0, 0, 10, 10, 0, 20, 0, 1, 5, 1, 0, 0);
	public static RaceParameters polishRace = new RaceParameters(Constants.CATHOLIC, Constants.SLAV);
	public static JobParameters polishJob = new JobParameters(20, 0, 20, 5, 2, 1, 0, 0, 1, 2, 1, 1);
	public static RaceParameters russianRace = new RaceParameters(Constants.ORTHODOX, Constants.SLAV);
	public static JobParameters russianJob = new JobParameters(40, 0, 10, 10, 5, 1, 0, 0, 1, 2, 3, 1);

	//public State nullState = new State(null, null);
	
	
	public static void main() {
		
		long last_time = System.nanoTime(); //for calculating performance and such :^)


		

		
		
		
		//NATIONS--------------------
		
		germany = new Nation("Germany", "German",world);

		germany.addCoreRace(Constants.GERMANIC);
		germany.addAcceptedRace(Constants.NORDIC);
		germany.addHatedRace(Constants.ASHKERNAZI);


		Nation poland = new Nation("Poland", "Polish",world);

		poland.addCoreRace(Constants.SLAV);
		poland.addAcceptedRace(Constants.JEWISH);
		poland.addHatedRace(Constants.GERMANIC);


		Nation finland = new Nation("Finland", "Finnish",world);

		finland.addCoreRace(Constants.FINNIC);
		finland.addAcceptedRace(Constants.NORDIC);
		finland.addHatedRace(Constants.SLAV);


		Nation sweden = new Nation("Sweden", "Swedish",world);

		sweden.addCoreRace(Constants.NORDIC);


		Nation norway = new Nation("Norway", "Norwegian",world);

		norway.addCoreRace(Constants.NORDIC);


		Nation denmark = new Nation("Denmark", "Danish",world);

		denmark.addCoreRace(Constants.NORDIC);
		denmark.addAcceptedRace(Constants.GERMANIC);


		Nation russia = new Nation("Russia", "Russian",world);

		russia.addCoreRace(Constants.SLAV);

		//--------------------------


		/*
		 * create states here
		 */

		State norway1 = new State("norway", norway, nordicRace, nordicJob, 20000);
		State sweden1 = new State("sweden", sweden, nordicRace, nordicJob, 40000);
		State denmark1 = new State("denmark", denmark, nordicRace, nordicJob, 20000);

		State finland1 = new State("denmark", finland, finnicRace, finnicJob, 20000);
		finland1.addPop(PopParameters.createPops(500, nordicRace, nordicJob, finland1));




		State ostproisen = new State("ostproisen", germany, germanRace, germanJob, 20000);
		ostproisen.addPop(PopParameters.createPops(500, jewishRace, jewishJob, ostproisen));
		ostproisen.addPop(PopParameters.createPops(1000, polishRace, polishJob, ostproisen));

		State selicia = new State("selicia", germany, germanRace, germanJob, 2000);
		selicia.addPop(PopParameters.createPops(500, jewishRace, jewishJob, selicia));
		selicia.addPop(PopParameters.createPops(5000, polishRace, polishJob, selicia));

		State berlin = new State("berlin", germany, germanRace, germanJob, 5000);
		berlin.addPop(PopParameters.createPops(1000, jewishRace, jewishJob, berlin));
		berlin.addPop(PopParameters.createPops(100, polishRace, polishJob, berlin));

		//State state2 = new State("State POL "+1, germany, polishRace, polishJob, 100);
		//State state2 = new State("State "+2, germany, germanRace, germanJob, 3250);
		//State state3 = new State("State "+3, germany, germanRace, germanJob, 5000);

		//germany.addState(state2);
		//germany.addState(state3);

		//state1.addNeigbour(state2);
		//state2.addNeigbour(state3);
		
		
		State westpoland = new State("west poland", poland, polishRace, polishJob, 10000);
		westpoland.addPop(PopParameters.createPops(1000, jewishRace, jewishJob, westpoland));
		State warsawa = new State("warsawa", poland, polishRace, polishJob, 100000);
		warsawa.addPop(PopParameters.createPops(1000, jewishRace, jewishJob, warsawa));
		State krakow = new State("krakow", poland, polishRace, polishJob, 100000);
		krakow.addPop(PopParameters.createPops(1000, jewishRace, jewishJob, krakow));
		State eastpoland = new State("east poland", poland, polishRace, polishJob, 10000);
		eastpoland.addPop(PopParameters.createPops(1000, jewishRace, jewishJob, eastpoland));


		State stpetersburg = new State("st. petersburg", russia, russianRace, russianJob, 10000);
		stpetersburg.addPop(PopParameters.createPops(5000, finnicRace, finnicJob, stpetersburg));
		stpetersburg.addPop(PopParameters.createPops(2500, jewishRace, jewishJob, stpetersburg));
		stpetersburg.addPop(PopParameters.createPops(500, germanRace, germanJob, stpetersburg));
		State karelia = new State("karelia", russia, russianRace, russianJob, 2500);
		karelia.addPop(PopParameters.createPops(2500, finnicRace, finnicJob, karelia));
		State northernrussia = new State("northernrussia", russia, russianRace, russianJob, 2500);
		northernrussia.addPop(PopParameters.createPops(2500, finnicRace, finnicJob, northernrussia));
		State moskova = new State("moskova", russia, russianRace, russianJob, 10000);
		moskova.addPop(PopParameters.createPops(1000, jewishRace, jewishJob, moskova));
		State volgograd = new State("volgograd", russia, russianRace, russianJob, 10000);
		State siberia = new State("volgograd", russia, russianRace, russianJob, 10000);
		siberia.addPop(PopParameters.createPops(5000, finnicRace, finnicJob, siberia));
		State ukraine = new State("ukraine", russia, russianRace, russianJob, 10000);
		State belarus = new State("belarus", russia, russianRace, russianJob, 10000);
		State baltics = new State("baltics", russia, russianRace, russianJob, 2500);
		baltics.addPop(PopParameters.createPops(5000, finnicRace, finnicJob, baltics));
		baltics.addPop(PopParameters.createPops(2500, jewishRace, jewishJob, baltics));
		baltics.addPop(PopParameters.createPops(500, germanRace, germanJob, baltics));


		//nordics
		 sweden1.addNeigbour(norway1);
		 sweden1.addNeigbour(denmark1);
		 denmark1.addNeigbour(berlin);

		finland1.addNeigbour(sweden1);
		finland1.addNeigbour(stpetersburg);
		finland1.addNeigbour(karelia);

		//russia
		stpetersburg.addNeigbour(karelia);
		karelia.addNeigbour(northernrussia);
		stpetersburg.addNeigbour(baltics);
		stpetersburg.addNeigbour(karelia);

		moskova.addNeigbour(stpetersburg);
		moskova.addNeigbour(northernrussia);
		moskova.addNeigbour(ukraine);
		moskova.addNeigbour(belarus);
		moskova.addNeigbour(volgograd);

		ukraine.addNeigbour(volgograd);
		ukraine.addNeigbour(belarus);
		ukraine.addNeigbour(eastpoland);

		belarus.addNeigbour(baltics);
		belarus.addNeigbour(eastpoland);

		baltics.addNeigbour(eastpoland);

		siberia.addNeigbour(northernrussia);
		siberia.addNeigbour(moskova);
		siberia.addNeigbour(volgograd);

		//germany
		westpoland.addNeigbour(ostproisen);
		westpoland.addNeigbour(selicia);
		selicia.addNeigbour(berlin);

		//poland
		westpoland.addNeigbour(warsawa);
		warsawa.addNeigbour(krakow);
		krakow.addNeigbour(eastpoland);




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
				System.out.println(s.name+" - MARKET: "+s.localMarket.getStockpileString());
				System.out.println(s.pops.size());
				//break;
			}
			//System.out.println("POLISH MARKET: "+statep1.localMarket.getStockpileString());
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
				//popWindow.tickUpdate();
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
/*
	public void setPopGui(PopGuiController popGuiController) {
		this.popGuiController = popGuiController;
	}*/
}
