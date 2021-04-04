package view;

import controller.BattleController;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.TextField;


public class BattleGuiController {
    @FXML
    private TextField battleInfo;
    @FXML
    private ScatterChart battleChart;
    @FXML
    private Axis xAxis;
    @FXML
    private Axis yAxis;
    private BattleController bc;

    public BattleGuiController(BattleController bc) {
        this.bc = bc;
    }


    public void init() {
        //battleChart = new ScatterChart(xAxis, yAxis);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        battleChart.getData().addAll(bc.getDefenders());
        battleChart.getData().addAll(bc.getAttackers());



        updater();
    }

    private void updater() {
        if (bc.ongoing()){
            updateData(bc);
            updater();
        }
    }

    private void updateData(BattleController bc) {
        battleChart.getData().clear();
        battleChart.getData().addAll(bc.getDefenders());
        battleChart.getData().addAll(bc.getAttackers());


        battleChart.autosize();
    }
}
