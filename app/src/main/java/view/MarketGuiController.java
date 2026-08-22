package view;

import constants.Constants;
import constants.Functions;
import factories.Factory;
import goods.AbstractGood;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.control.TreeItem;
import main.Main;
import market.AbstractMarket;
import market.Listing;
import world.Nation;
import world.Pop;
import world.State;

import java.net.URL;
import java.util.*;

public class MarketGuiController implements Initializable {
    @FXML private TreeView<Object> worldMarketTree;

    @FXML private TableView<Map<String, Object>> goodsTable;
    @FXML private TableColumn<Map<String, Object>, String> colGood;
    @FXML private TableColumn<Map<String, Object>, Number> colTotal;
    @FXML private TableColumn<Map<String, Object>, String> colMin;
    @FXML private TableColumn<Map<String, Object>, String> colMax;
    @FXML private TableColumn<Map<String, Object>, String> colAvgLocal;
    @FXML private TableColumn<Map<String, Object>, String> colAvgGlobal;

    @FXML private TableView<Map<String, Object>> listingsTable;
    @FXML private TableColumn<Map<String, Object>, String> colLGood;
    @FXML private TableColumn<Map<String, Object>, String> colLOrigin;
    @FXML private TableColumn<Map<String, Object>, String> colLSaleType;
    @FXML private TableColumn<Map<String, Object>, Number> colLChainLength;
    @FXML private TableColumn<Map<String, Object>, String> colLRoute;
    @FXML private TableColumn<Map<String, Object>, String> colSeller;
    @FXML private TableColumn<Map<String, Object>, Number> colLAmount;
    @FXML private TableColumn<Map<String, Object>, String> colLPrice;

    @FXML private TableView<Map<String, Object>> factoriesTable;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryState;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryName;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryRecipe;
    @FXML private TableColumn<Map<String, Object>, Number> colFactoryLevel;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryOwner;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryWorkers;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryHiringCap;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryWorkerMap;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryCapacity;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryWage;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryMoney;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryRevenue;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryExpenses;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryInputInventory;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryInventory;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryProduced;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryMissingInputs;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryHealth;
    @FXML private TableColumn<Map<String, Object>, String> colFactoryExpansion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.marketGuiController = this;
        buildTree();
        setupColumns();
        setupFactoryColumns();

        worldMarketTree.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            onTreeSelectionChanged((TreeItem<?>) n);
        });
        tickUpdate();
    }

    private volatile boolean tickRequested;

    public synchronized void setTickUpdate(boolean update) {
        tickRequested = update;
    }

    private void tickUpdate() {
        Task<Void> updateTask = new Task<>() {
            @Override
            protected Void call() {
                while (!isCancelled()) {
                    if (tickRequested) {
                        setTickUpdate(false);
                        Platform.runLater(() -> {
                            TreeItem<?> selected = worldMarketTree.getSelectionModel().getSelectedItem();
                            if (selected == null) selected = worldMarketTree.getRoot();
                            onTreeSelectionChanged(selected);
                        });
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException exception) {
                        cancel();
                        break;
                    }
                }
                return null;
            }
        };
        Thread updater = new Thread(updateTask, "MarketGuiTickUpdater");
        updater.setDaemon(true);
        updater.start();
    }

    private void buildTree() {
        TreeItem<Object> root = new TreeItem<>("World");
        root.setExpanded(true);
        for (Nation nation : Main.world.getNations()) {
            TreeItem<Object> nationItem = new TreeItem<>(nation);
            for (State state : nation.getStates()) {
                TreeItem<Object> stateItem = new TreeItem<>(state);
                nationItem.getChildren().add(stateItem);
                for (Factory factory : state.getFactories()) {
                    stateItem.getChildren().add(new TreeItem<>(factory));
                }
            }
            root.getChildren().add(nationItem);
        }
        worldMarketTree.setRoot(root);
        worldMarketTree.getSelectionModel().select(root);
        onTreeSelectionChanged(root);
    }

    private void setupColumns() {
        colGood.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("good")));
        colTotal.setCellValueFactory(c -> new SimpleIntegerProperty(((Number) c.getValue().getOrDefault("total", 0)).intValue()));
        colMin.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("min", "")));
        colMax.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("max", "")));
        colAvgLocal.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("avgLocal", "")));
        colAvgGlobal.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("avgGlobal", "")));

        colLGood.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("good")));
        colLOrigin.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("origin", "Unknown")));
        colLSaleType.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("saleType", "Unknown")));
        colLChainLength.setCellValueFactory(c -> new SimpleIntegerProperty(((Number) c.getValue().getOrDefault("chainLength", 0)).intValue()));
        colLRoute.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("route", "Unknown")));
        colSeller.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("seller")));
        colLAmount.setCellValueFactory(c -> new SimpleIntegerProperty(((Number) c.getValue().getOrDefault("amount", 0)).intValue()));
        colLPrice.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("price", "")));
    }

    private void onTreeSelectionChanged(TreeItem<?> item) {
        if (item == null) return;
        Object ud = item.getValue();
        List<State> states = new ArrayList<>();
        if (ud instanceof Nation) {
            states.addAll(((Nation) ud).getStates());
        } else if (ud instanceof State) {
            states.add((State) ud);
        } else if (ud instanceof Factory) {
            Factory factory = (Factory) ud;
            State state = factory.getState();
            populateGoodsTable(state);
            populateListingsTable(state);
            populateFactoryTable(state, Collections.singletonList(factory));
            return;
        } else {
            states.addAll(Main.world.getAllStates());
        }

        // for simplicity, show aggregate for first state if multiple selected
        if (states.isEmpty()) return;
        State state = states.get(0);
        populateGoodsTable(state);
        populateListingsTable(state);
        populateFactoryTable(state, state.getFactories());
    }
    private void populateGoodsTable(State state) {
        AbstractMarket market = state.localMarket;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (int i = 0; i < Constants.AMOUNT_OF_GOODS; i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("good", Constants.GoodToString(i));
            double total = market.goodTotalAmount(i);
            row.put("total", (int) Math.round(total));

            double min = market.getGoodMinPrice(i, 1);
            double max = market.getGoodMaxPrice(i, 1);
            row.put("min", Functions.formatNum(min));
            row.put("max", Functions.formatNum(max));

            // compute average local price
            List<AbstractGood> localGoods = market.getAllOfGood(i);
            double avgLocal = 0;
            if (!localGoods.isEmpty()) {
                double sum = 0;
                for (AbstractGood g : localGoods) sum += g.getValue(1);
                avgLocal = sum / localGoods.size();
            }
            row.put("avgLocal", Functions.formatNum(avgLocal));

            // compute average global price
            List<AbstractGood> globalGoods = Main.world.getGlobalMarket().getAllOfGood(i);
            double avgGlobal = 0;
            if (!globalGoods.isEmpty()) {
                double sum = 0;
                for (AbstractGood g : globalGoods) sum += g.getValue(1);
                avgGlobal = sum / globalGoods.size();
            }
            row.put("avgGlobal", Functions.formatNum(avgGlobal));

            rows.add(row);
        }

        ObservableList<Map<String, Object>> items = FXCollections.observableArrayList(rows);
        goodsTable.setItems(items);
    }

    private void populateListingsTable(State state) {
        AbstractMarket market = state.localMarket;
        List<Map<String, Object>> rows = new ArrayList<>();

        for (int i = 0; i < Constants.AMOUNT_OF_GOODS; i++) {
            List<AbstractGood> goods = market.getAllOfGood(i);
            for (AbstractGood g : goods) {
                if (g instanceof Listing) {
                    Listing l = (Listing) g;
                    Map<String, Object> row = new HashMap<>();
                    row.put("good", Constants.GoodToString(i));
                    State originState = l.getOriginState();
                    row.put("origin", originState == null ? "Unknown" : originState.name);
                    row.put("saleType", l.getSaleType());
                    row.put("chainLength", l.getDropshipChainLength());
                    row.put("route", l.getDropshipRoute());
                    Object seller = l.getSellerObject();
                    row.put("seller", seller == null ? "(unknown)" : seller.toString());
                    row.put("amount", (int) Math.round(l.getAmount()));
                    double unit = l.getValue(1);
                    row.put("price", Functions.formatNum(unit));
                    rows.add(row);
                }
            }
        }

        ObservableList<Map<String, Object>> items = FXCollections.observableArrayList(rows);
        listingsTable.setItems(items);
    }
    private void setupFactoryColumns() {
        colFactoryState.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("state")));
        colFactoryName.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("name")));
        colFactoryRecipe.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("recipe")));
        colFactoryLevel.setCellValueFactory(c -> new SimpleIntegerProperty(((Number) c.getValue().get("level")).intValue()));
        colFactoryOwner.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("owner")));
        colFactoryWorkers.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("workers")));
        colFactoryHiringCap.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("hiringCap")));
        colFactoryWorkerMap.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("workerMap")));
        colFactoryCapacity.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("capacity")));
        colFactoryWage.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("wage")));
        colFactoryMoney.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("money")));
        colFactoryRevenue.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("revenue")));
        colFactoryExpenses.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("expenses")));
        colFactoryInputInventory.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("inputInventory")));
        colFactoryInventory.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("inventory")));
        colFactoryProduced.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("produced")));
        colFactoryMissingInputs.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("missingInputs")));
        colFactoryHealth.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("health")));
        colFactoryExpansion.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().get("expansion")));
    }

    private void populateFactoryTable(State state, List<Factory> factories) {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Factory factory : factories) {
            Map<String, Object> row = new HashMap<>();
            row.put("state", factory.getState().name);
            row.put("name", factory.getName());
            row.put("recipe", factory.getRecipe().name());
            row.put("level", factory.getLevel());
            row.put("owner", factory.getOwner() == null ? "State" : factory.getOwner().toString());
            row.put("workers", Functions.formatNum(factory.getWorkerCount()));
            row.put("hiringCap", Functions.formatNum(factory.getHiringCap()));
                row.put("workerMap", factory.getWorkers().entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + Functions.formatNum(entry.getValue()))
                    .collect(java.util.stream.Collectors.joining(", ")));
            row.put("capacity", Functions.formatNum(factory.getWorkerCapacity()));
            row.put("wage", Functions.formatNum(factory.getWageValue()));
            row.put("money", Functions.formatNum(factory.getMoneyPool()));
            row.put("revenue", Functions.formatNum(factory.getSalesRevenue()));
            row.put("expenses", Functions.formatNum(factory.getOperatingExpenses()));
                    row.put("inputInventory", factory.getInputInventory().entrySet().stream()
                        .map(entry -> Constants.GoodToString(entry.getKey()) + "=" + Functions.formatNum(entry.getValue()))
                        .collect(java.util.stream.Collectors.joining(", ")));
                    row.put("inventory", factory.getMaintenanceInventory().entrySet().stream()
                    .map(entry -> Constants.GoodToString(entry.getKey()) + "=" + Functions.formatNum(entry.getValue()))
                    .collect(java.util.stream.Collectors.joining(", ")));
                    row.put("produced", factory.getLastProduced().entrySet().stream()
                        .map(entry -> Constants.GoodToString(entry.getKey()) + "=" + Functions.formatNum(entry.getValue()))
                        .collect(java.util.stream.Collectors.joining(", ")));
                    row.put("missingInputs", factory.getMissingInputs().entrySet().stream()
                        .map(entry -> Constants.GoodToString(entry.getKey()) + "=" + Functions.formatNum(entry.getValue()))
                        .collect(java.util.stream.Collectors.joining(", ")));
            row.put("health", "cement " + Functions.formatNum(factory.getCementHealth())
                    + ", steel " + Functions.formatNum(factory.getSteelHealth())
                    + ", machinery " + Functions.formatNum(factory.getMachineryHealth()));
            row.put("expansion", factory.isExpanding()
                    ? Functions.formatNum(factory.getExpansionProgress() * 100) + "%"
                    : "inactive");
            rows.add(row);
        }
        factoriesTable.setItems(FXCollections.observableArrayList(rows));
    }
}
