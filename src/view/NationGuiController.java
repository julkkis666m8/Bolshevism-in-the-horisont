package view;

import constants.Functions;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import main.GuiMain;
import main.Main;
import world.Nation;
import world.World;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NationGuiController {


    private World world;
    private Nation mainNation;
    private Main main;

    @FXML
    private Text nationName;
    @FXML
    private Slider taxesSlider;
    @FXML
    private Slider tarrifSlider;
    @FXML
    private Button setButton;
    @FXML
    private Label incomeLabel;
    @FXML
    private LineChart moneyChart;
    @FXML
    private LineChart incomeChart;

    private XYChart.Series<Integer, Double> moneySeries = new XYChart.Series();
    private XYChart.Series<Integer, Double> incomeSeries = new XYChart.Series();
    private double income;


    public void initialize(){
        System.out.println("LOLOLOLOL");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        this.main = GuiMain.main;
        main.setNationGui(this);
        this.world = main.world;
        mainNation = world.getNations().get(0);
        nationName.setText(mainNation.getName());

        //moneySeries.getData().add(new XYChart.Data(1,mainNation.coffers));
        moneyChart.getData().add(moneySeries);
        incomeChart.getData().add(incomeSeries);

        tickUpdate();
    }

    public synchronized void update(){
        this.income = mainNation.getIncome();
        incomeLabel.setText(Functions.formatNum(income));

        int moneyIndex = moneySeries.getData().size()+1;
        moneySeries.getData().add(new XYChart.Data(Integer.toString(moneyIndex),mainNation.coffers));
        incomeSeries.getData().add(new XYChart.Data(Integer.toString(moneyIndex),income));


    }
    public synchronized void tickUpdate() {

        Task dynamicTimeTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    //update();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
                return null;
            }
        };
        //dynamicTimeDisplayLabel2.textProperty().bind(dynamicTimeTask.messageProperty());
        Thread t2 = new Thread(dynamicTimeTask);
        t2.setName("Tesk Time Updater");
        t2.setDaemon(true);
        t2.start();
    }


    @FXML
    private void setButtonPress(){
        double val = this.taxesSlider.getValue()/100;
        mainNation.setTaxPercentage(val);
        val = this.tarrifSlider.getValue()/100;
        mainNation.setTarrifPercentage(val);
        update();
    }

}
