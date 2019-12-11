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
		String string = "";
		
		

		int population = 0;
		double totalWealth = 0;
		
		double justSpent = 0;
		double incomeTaxable = 1; 
		int itterations = 0;
		double needsFurfilled = 0;
		
		for (int i = 0; i < pops.size(); i++) {
			itterations++;
			Pop pop = pops.get(i);
			population += pop.getPopulation();
			totalWealth += pop.getTotalWealth();
			justSpent += pop.getJustSpent();
			incomeTaxable += pop.getIncomeTaxable();
			needsFurfilled += pop.getNeedsFurfilled();
		}
		
		needsFurfilled = (needsFurfilled / itterations)*100;//%
		
		

		if(population != 0) {
			string += "population: "+population+" | ";	
		}
		if(totalWealth != 0) {
			string += "totalWealth: "+Functions.formatNum(totalWealth)+"£ | ";	
		}
		if(justSpent != 0) {
			string += "justSpent: "+Functions.formatNum(justSpent)+"£ | ";	
		}
		if(incomeTaxable != 0) {
			string += "incomeTaxable: "+Functions.formatNum(incomeTaxable)+"£ | ";	
		}
		if(itterations != 0) {
			string += "needsFurfilled: "+Functions.formatNum(needsFurfilled)+"% | ";	
		}
		
		
		
		
		
		
		
		return incomeTaxable;
	}

	
	
	


	private static PieChart createChart(List<Pop> pops) {
		Tooltip container = new Tooltip();


		pieChartData = FXCollections.observableArrayList();
		updatePop();
		PieChart chart = new PieChart(pieChartData);
		chart.setTitle("Population");
		final Label caption = new Label("");
		caption.setTextFill(Color.BLACK);
		caption.setStyle("-fx-font: 24 arial;");
		for (final PieChart.Data data : chart.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
					new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent e)
				{
					if (container.isShowing())
					{
						container.hide();
					}
					caption.setText(String.valueOf(data.getPieValue()) + "%");
					container.show(stage, e.getScreenX(), e.getScreenY());
					updatePop();
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
	
	
	public static void tickUpdate() {

		//updatePop();


		
		System.out.println("GRAPHICS UPDATED");
		
		//stuff
		
	}




}
