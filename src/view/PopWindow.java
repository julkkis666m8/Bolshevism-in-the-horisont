package view;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import constants.Constants;
import constants.Functions;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.stage.Stage;
import world.Nation;
import world.Pop;
import world.State;
import javafx.scene.chart.*;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Group;


public class PopWindow extends Application {

	private static List<Pop> popList;
	static PieChart jobChart;
	static Scene scene;
	static ObservableList<PieChart.Data> pieChartData;
	static Stage stage;
	static Tooltip tooltip = new Tooltip();
	static PieChart.Data lastData;



	public void start(Stage stage) {
		this.stage = stage;
		scene = new Scene(new Group());
		stage.setTitle("Population");
		stage.setWidth(500);
		stage.setHeight(500);

		
		getSelectedPops();
		

		
		jobChart = createChart(popList);
		
		
		
		((Group) scene.getRoot()).getChildren().add(jobChart);
		//((Group) scene.getRoot()).getChildren().add(caption);
		stage.setScene(scene);
		stage.show();
		
	}

	
	
	
	
	
	
	

	private static double popInfo(List<Pop> pops) {
		
		int population = 0;
		double totalWealth = 0;
		
		double justSpent = 1;
		double incomeTaxable = 1; 
		int itterations = 0;
		double needsFurfilled = 1;
		
		for (int i = 0; i < pops.size(); i++) {
			itterations++;
			Pop pop = pops.get(i);
			population += pop.getPopulation();
			totalWealth += pop.getAverageWealth();
			justSpent += pop.getJustSpent();
			incomeTaxable += pop.getIncomeTaxable();
			needsFurfilled += pop.getNeedsFurfilled();
		}
		
		return totalWealth;
	}
	private static double popInfoTotalWealth(List<Pop> pops) {
		
		double totalWealth = 0;
		
		for (int i = 0; i < pops.size(); i++) {
		
			Pop pop = pops.get(i);
			totalWealth += pop.getAverageWealth();
		}
		
		return totalWealth;
	}
	
	
	private static double popInfoNeedsPercentage(List<Pop> pops) {
		
		int itterations = 0;
		double needsFurfilled = 0.01;
		
		for (int i = 0; i < pops.size(); i++) {
			itterations++;
			Pop pop = pops.get(i);
			needsFurfilled += pop.getNeedsFurfilled();
		}
		
		
		return needsFurfilled/itterations;
	}
	private static double popInfoPop(List<Pop> pops) {
		
		int population = 0;
		
		for (int i = 0; i < pops.size(); i++) {
			Pop pop = pops.get(i);
			population += pop.getPopulation();
		}
		
		return population;
	}

	
	
	


	private static PieChart createChart(List<Pop> pops) {
		


		pieChartData = FXCollections.observableArrayList();
		updatePop();
		PieChart chart = new PieChart(pieChartData);
		chart.setTitle("Population");
		final Label caption = new Label("");
		//caption.setTextFill(Color.BLACK);
		//caption.setStyle("-fx-font: 42 arial;");
		for (final PieChart.Data data : chart.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
					new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent e)
				{
					if (tooltip.isShowing())
					{
						tooltip.hide();
					}
					lastData = data; //so that we can update our tooltip with new pie-updates!
					
					//caption.setText(data.getName()+"s have "+data.getPieValue() + " people");
					tooltip.setText(data.getName()+"s have "+data.getPieValue() + " people");
					tooltip.show(stage, e.getScreenX(), e.getScreenY());
					//updatePop();
					System.out.println("123");
				}
			});
		}
		return chart;
	}





	//START COPYPASTA


	public String getPopInfo() {
		String string = "";
		
		for (int job = 0; job < 12; job++) {
			Constants.JobToString(job);
			popInfo(Nation.getPopsJob(main.Main.germany.getAllPops(), job));
		}
		
		
		
		
		return string;
	}
//END COPYPASTA





	private static void updatePop() {

		//List<Pop> items = getSelectedPops2();
		
		//pieChartData.
		List<PieChart.Data> jobs = seperateJobs(getSelectedPops2());
		for(int i = 0; i < jobs.size(); i++) {
			
			String name = jobs.get(i).getName();
			double value = jobs.get(i).getPieValue();

			addData(name, value);
			
		}
		/*
		try {
			tooltip.setText(lastData.getName()+"s have "+lastData.getPieValue() + " people");
			//tooltip.show(stage, e.getScreenX(), e.getScreenY());
		}
		catch(Exception e) {
			System.out.println(e);
		}*/

	}


	private static void getSelectedPops() {

		//temp
		popList = main.Main.germany.getAllPops();
	}

	private static LinkedList<Pop> getSelectedPops2() {

		return main.Main.germany.getAllPops();
	}

	
	
	
	
	private static List<PieChart.Data> seperateJobs(List<Pop> pops){
		

		List<PieChart.Data> list = new ArrayList<PieChart.Data>();

		for (int job = 0; job < 12; job++) {
			list.add(new PieChart.Data(Constants.JobToString(job), popInfo(main.Main.germany.getPopsJob(pops, job))));
		}
		return list;
		
		
		
	}
	

    public static void naiveAddData(String name, double value)
    {
        pieChartData.add(new javafx.scene.chart.PieChart.Data(name, value));
    }

    //updates existing Data-Object if name matches
    public static void addData(String name, double value)
    {
        for(javafx.scene.chart.PieChart.Data d : pieChartData)
        {
            if(d.getName().equals(name))
            {
                d.setPieValue(value);
                return;
            }
        }
        naiveAddData(name, value);
    }
	
	
	public synchronized static void tickUpdate() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		updatePop();


		
		//System.out.println("GRAPHICS UPDATED");
		
		//stuff
		
	}




}
