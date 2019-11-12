package view;


import java.util.ArrayList;
import java.util.List;

import constants.Constants;
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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Group;


public class PopWindow extends Application {

	private static List<Pop> popList;




	public void start(Stage stage) {
		Scene scene = new Scene(new Group());
		stage.setTitle("Population");
		stage.setWidth(500);
		stage.setHeight(500);

		
		getSelectedPops();
		

		
		PieChart jobChart = createChart(popList);
		
		
		
		((Group) scene.getRoot()).getChildren().add(jobChart);
		//((Group) scene.getRoot()).getChildren().add(caption);
		stage.setScene(scene);
		stage.show();
	}

	
	
	
	
	
	
	
	
	
	
	


	private PieChart createChart(List<Pop> pops) {
		
		
		
		ObservableList<PieChart.Data> pieChartData =
			FXCollections.observableArrayList(
					new PieChart.Data("Grapefruit", 13),
					new PieChart.Data("Oranges", 25),
					new PieChart.Data("Plums", 10),
					new PieChart.Data("Pears", 22),
					new PieChart.Data("Apples", 30));
	PieChart chart = new PieChart(pieChartData);
	chart.setTitle("Imported Fruits");
	final Label caption = new Label("");
	caption.setTextFill(Color.BLACK);
	caption.setStyle("-fx-font: 24 arial;");
	for (final PieChart.Data data : chart.getData()) {
		data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
				new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				caption.setTranslateX(e.getSceneX());
				caption.setTranslateY(e.getSceneY());
				caption.setText(data.getName()+"\n"+String.valueOf(data.getPieValue()) + "%");
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

	private String popInfo(List<Pop> pops) {
		String string = "";
		
		

		int population = 0;
		double totalWealth = 0;
		
		double justSpent = 0;
		double incomeTaxable = 0; 
		
		for (int i = 0; i < pops.size(); i++) {
			Pop pop = pops.get(i);
			population += pop.getPopulation();
			totalWealth += pop.totalCash();
			justSpent += pop.getJustSpent();
			incomeTaxable += pop.getIncomeTaxable();
		}

		if(population != 0) {
			string += "\npopulation: "+population+" | ";	
		}
		if(totalWealth != 0) {
			string += "\ntotalWealth: "+totalWealth+"£ | ";	
		}
		if(justSpent != 0) {
			string += "justSpent: "+justSpent+"£ | ";	
		}
		if(incomeTaxable != 0) {
			string += "incomeTaxable: "+incomeTaxable+"£ | ";	
		}
		
		
		
		
		
		
		
		return string;
	}


//END COPYPASTA





	private static void updatePop() {

		getSelectedPops();
		
	}


	private static void getSelectedPops() {

		//temp
		popList = main.Main.germany.getAllPops();
	}

	
	
	
	
	private List<Pop> seperateJobs(List<Pop> pops){
		return pops;
		
		
		
	}
	
	
	public static void tickUpdate() {

		updatePop();
		
		System.out.println("GRAPHICS UPDATED");
		
		//stuff
		
	}




}
