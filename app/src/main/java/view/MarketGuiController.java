package view;

import constants.Constants;
import constants.Functions;
import goods.AbstractGood;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
    @FXML private TableColumn<Map<String, Object>, String> colSeller;
    @FXML private TableColumn<Map<String, Object>, Number> colLAmount;
    @FXML private TableColumn<Map<String, Object>, String> colLPrice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buildTree();
        setupColumns();

        worldMarketTree.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            onTreeSelectionChanged((TreeItem<?>) n);
        });
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
        } else {
            states.addAll(Main.world.getAllStates());
        }

        // for simplicity, show aggregate for first state if multiple selected
        if (states.isEmpty()) return;
        State state = states.get(0);
        populateGoodsTable(state);
        populateListingsTable(state);
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
                    Pop seller = l.getSeller();
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
}
