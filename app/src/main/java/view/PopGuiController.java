package view;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import constants.Constants;
import constants.Functions;
import goods.AbstractGood;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.TreeItem;
import javafx.concurrent.Task;
import javafx.application.Platform;
import main.Main;
import main.GuiMain;
import world.Nation;
import world.Pop;
import world.State;

public class PopGuiController implements Initializable {

    @FXML private TreeView<Object> worldTree;
    @FXML private TableView<Pop> popTable;
    @FXML private TableColumn<Pop, String> colName;
    @FXML private TableColumn<Pop, String> colJob;
    @FXML private TableColumn<Pop, String> colProduced;
    @FXML private TableColumn<Pop, Integer> colPopulation;
    @FXML private TableColumn<Pop, Double> colAvgWealth;
    @FXML private TableColumn<Pop, Double> colNeeds;
    @FXML private TableColumn<Pop, Double> colWants;
    @FXML private TableColumn<Pop, Double> colLuxury;
    @FXML private TableColumn<Pop, Double> colJustSpent;
    @FXML private TableColumn<Pop, Double> colIncomeTaxable;
    @FXML private TableColumn<Pop, String> colInventory;
    @FXML private PieChart raceChart;
    @FXML private PieChart jobChart;
    @FXML private TextArea detailsArea;
    @FXML private Label dateLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buildTree();
        setupTableColumns();

        worldTree.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            onTreeSelectionChanged((TreeItem) n);
        });

        popTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) detailsArea.setText(n.getInfo());
            else detailsArea.setText("");
        });
        // disable chart animations and register with main for tick-driven updates
        if (jobChart != null) jobChart.setAnimated(false);
        if (raceChart != null) raceChart.setAnimated(false);
        // register with main and use tick-driven updates
        try {
            if (GuiMain.main != null) {
                this.main = GuiMain.main;
                this.main.setPopGui(this);
            } else {
                // fallback: set static field so Main loop can see controller
                Main.popGuiController = this;
            }
        } catch (Exception ignored) {}
        tickUpdate();
    }

    private void buildTree() {
        TreeItem<Object> root = new TreeItem<>("World");
        root.setExpanded(true);

        for (Nation nation : Main.world.getNations()) {
            TreeItem<Object> nationItem = new TreeItem<>(nation);
            for (State state : nation.getStates()) {
                TreeItem<Object> stateItem = new TreeItem<>(state);
                nationItem.getChildren().add(stateItem);
            }
            root.getChildren().add(nationItem);
        }

        worldTree.setRoot(root);
        worldTree.getSelectionModel().select(root);
        onTreeSelectionChanged(root);
    }

    private boolean toTick = false;
    private Main main;

    private void tickUpdate() {
        Task dynamicTimeTask = new Task<Void>() {
            @Override
            protected Void call() {
                while (true) {
                    if (toTick) {
                        setTickUpdate(false);
                        Platform.runLater(() -> {
                            TreeItem<?> selected = worldTree.getSelectionModel().getSelectedItem();
                            if (selected == null) selected = worldTree.getRoot();
                            onTreeSelectionChanged((TreeItem<?>) selected);
                        });
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
                return null;
            }
        };
        Thread t2 = new Thread(dynamicTimeTask);
        t2.setName("PopGuiTickUpdater");
        t2.setDaemon(true);
        t2.start();
    }

    public synchronized void setTickUpdate(boolean bool) {
        this.toTick = bool;
    }

    private void onTreeSelectionChanged(TreeItem<?> item) {
        if (item == null) return;
        Object ud = item.getValue();
        List<Pop> pops;
        if (ud instanceof Nation) {
            pops = ((Nation) ud).getAllPops();
        } else if (ud instanceof State) {
            pops = ((State) ud).getPops();
        } else {
            pops = Main.world.getAllPops();
        }

        populateTable(pops);
        updateCharts(pops);
        detailsArea.setText(buildSummary(pops));
        if (worldTree != null) worldTree.refresh();
        try {
            if (dateLabel != null) dateLabel.setText("Date: " + Main.world.getCurrentDateString());
        } catch (Exception ignored) {}
    }

    private String buildProducedString(Pop p) {
        List<AbstractGood> goodsSnapshot = p.getLastProducedGoods();
        if (goodsSnapshot == null || goodsSnapshot.isEmpty()) return "";
        return goodsSnapshot.stream()
                .map(g -> g.getName() + ":" + Functions.formatNum(g.getAmount()))
                .collect(Collectors.joining(", "));
    }

    private void setupTableColumns() {
        colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().toString()));
        colJob.setCellValueFactory(c -> new SimpleStringProperty(Constants.JobToString(c.getValue().job)));
        colProduced.setCellValueFactory(c -> new SimpleStringProperty(buildProducedString(c.getValue())));

        colPopulation.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPopulation()).asObject());
        colPopulation.setCellFactory(tc -> new TableCell<Pop, Integer>() {
            @Override protected void updateItem(Integer v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : Integer.toString(v));
            }
        });

        colAvgWealth.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getAverageWealth()).asObject());
        colAvgWealth.setCellFactory(tc -> new TableCell<Pop, Double>() {
            @Override protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : Functions.formatNum(v) + "$" );
            }
        });

        colNeeds.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getNeedsFurfilled() * 100).asObject());
        colNeeds.setCellFactory(tc -> new TableCell<Pop, Double>() {
            @Override protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : Functions.formatNum(v) + "%" );
            }
        });

        colWants.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getWantsFurfilled() * 100).asObject());
        colWants.setCellFactory(colNeeds.getCellFactory());

        colLuxury.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getLuxuryFurfilled() * 100).asObject());
        colLuxury.setCellFactory(colNeeds.getCellFactory());

        colJustSpent.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getJustSpent()).asObject());
        colJustSpent.setCellFactory(tc -> new TableCell<Pop, Double>() {
            @Override protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : Functions.formatNum(v) + "$" );
            }
        });

        colIncomeTaxable.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getIncomeTaxable()).asObject());
        colIncomeTaxable.setCellFactory(colJustSpent.getCellFactory());

        colInventory.setCellValueFactory(c -> new SimpleStringProperty(buildInventoryString(c.getValue())));

        popTable.setRowFactory(tv -> {
            TableRow<Pop> row = new TableRow<>();
            Tooltip tt = new Tooltip();
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem == null) row.setTooltip(null);
                else {
                    tt.setText(buildDetailedInfo(newItem));
                    row.setTooltip(tt);
                }
            });
            return row;
        });
        
        // render tree nodes with counts
        worldTree.setCellFactory(tv -> new TreeCell<Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item instanceof Nation) {
                    Nation n = (Nation) item;
                    int popCount = n.getAllPops().stream().mapToInt(Pop::getPopulation).sum();
                    setText(n.toString() + " (" + popCount + ")");
                } else if (item instanceof State) {
                    State s = (State) item;
                    int popCount = s.getPops().stream().mapToInt(Pop::getPopulation).sum();
                    setText(s.name + " (" + popCount + ")");
                } else {
                    setText(item.toString());
                }
            }
        });
    }

    private void populateTable(List<Pop> pops) {
        // take a snapshot and filter out zero-population pops to avoid clutter
        List<Pop> snapshot = new ArrayList<>(pops).stream().filter(p -> p.getPopulation() > 0).collect(Collectors.toList());
        ObservableList<Pop> items = FXCollections.observableArrayList(snapshot);
        popTable.setItems(items);
    }

    private Map<String, String> jobColorMap = new LinkedHashMap<>();
    private Map<String, String> raceColorMap = new LinkedHashMap<>();
    private List<String> fixedColors = Arrays.asList("#3366CC", "#DC3912", "#FF9900", "#109618", "#990099", "#0099C6", "#DD4477", "#66AA00", "#B82E2E", "#316395");
    private int colorIndex = 0;

    private String nextColor() {
        String color = fixedColors.get(colorIndex % fixedColors.size());
        colorIndex++;
        return color;
    }

    private void updateCharts(List<Pop> pops) {
        // snapshot and filter zero-population pops
        List<Pop> snapshot = new ArrayList<>(pops).stream().filter(p -> p.getPopulation() > 0).collect(Collectors.toList());
        Map<String, Integer> jobCounts = new LinkedHashMap<>();
        Map<String, Integer> raceCounts = new LinkedHashMap<>();

        for (Pop p : snapshot) {
            jobCounts.merge(Constants.JobToString(p.job), p.getPopulation(), Integer::sum);
            raceCounts.merge(Constants.raceToString(p.getRace()), p.getPopulation(), Integer::sum);
        }

        ObservableList<PieChart.Data> jobData = FXCollections.observableArrayList();
        jobCounts.forEach((k, v) -> jobData.add(new PieChart.Data(k, v)));
        jobChart.setData(jobData);
        // assign stable colors
        for (PieChart.Data d : jobData) {
            String key = d.getName();
            String color = jobColorMap.computeIfAbsent(key, k -> nextColor());
            d.nodeProperty().addListener((obs, oldN, newN) -> {
                if (newN != null) newN.setStyle("-fx-pie-color: " + color + ";");
            });
            if (d.getNode() != null) d.getNode().setStyle("-fx-pie-color: " + color + ";");
        }

        ObservableList<PieChart.Data> raceData = FXCollections.observableArrayList();
        raceCounts.forEach((k, v) -> raceData.add(new PieChart.Data(k, v)));
        raceChart.setData(raceData);
        for (PieChart.Data d : raceData) {
            String key = d.getName();
            String color = raceColorMap.computeIfAbsent(key, k -> nextColor());
            d.nodeProperty().addListener((obs, oldN, newN) -> {
                if (newN != null) newN.setStyle("-fx-pie-color: " + color + ";");
            });
            if (d.getNode() != null) d.getNode().setStyle("-fx-pie-color: " + color + ";");
        }
    }

    private String buildInventoryString(Pop p) {
        List<AbstractGood> goodsSnapshot = new ArrayList<>(p.getGoods());
        return goodsSnapshot.stream()
            .map(g -> g.getName() + ":" + Functions.formatNum(g.getAmount()))
            .collect(Collectors.joining(", "));
    }

    private String buildDetailedInfo(Pop p) {
        StringBuilder sb = new StringBuilder();
        sb.append(p.getInfo()).append("\nInventory: ").append(buildInventoryString(p));
        return sb.toString();
    }

    private String buildSummary(List<Pop> pops) {
        int totalPop = pops.stream().mapToInt(Pop::getPopulation).sum();
        double totalWealth = pops.stream().mapToDouble(Pop::getTotalWealth).sum();
        return "Summary\nTotal population: " + totalPop + "\nTotal wealth: " + Functions.formatNum(totalWealth) + "$";
    }
}
