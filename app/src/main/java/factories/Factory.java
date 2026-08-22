package factories;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import constants.Constants;
import goods.AbstractGood;
import market.Listing;
import world.Pop;
import world.State;

public class Factory {
    public static final double HEALTH_LOSS_PER_TICK = 0.0025;
    public static final int INITIAL_INPUT_STOCK_TICKS = 50;
    private final State state;
    private final FactoryRecipe recipe;
    private int level;
    private Pop owner;
    private double moneyPool;
    private double salesRevenue;
    private double operatingExpenses;
    private double machineryHealth = 1;
    private double cementHealth = 1;
    private double steelHealth = 1;
    private double expansionProgress;
    private boolean expanding;
    private final Map<Pop, Double> workers = new LinkedHashMap<>();
    private final Map<Integer, Double> inputInventory = new LinkedHashMap<>();
    private final Map<Integer, Double> maintenanceInventory = new LinkedHashMap<>();
    private final Map<Integer, Double> lastProduced = new LinkedHashMap<>();
    private final Map<Integer, Double> missingInputs = new LinkedHashMap<>();

    public Factory(State state, FactoryRecipe recipe, int level, Pop owner) {
        if (state == null || recipe == null || level < 1) throw new IllegalArgumentException();
        this.state = state;
        this.recipe = recipe;
        this.level = level;
        this.owner = owner == null ? findDefaultOwner(state) : owner;
        state.addFactory(this);
        this.moneyPool = 1000000 * level;
        initializeInputInventory();
        initializeMaintenanceInventory();
    }

    public State getState() { return state; }
    public String getName() { return recipe.name() + " factory"; }
    public FactoryRecipe getRecipe() { return recipe; }
    public int getLevel() { return level; }
    public Pop getOwner() { return owner; }
    public double getMoneyPool() { return moneyPool; }
    public double getSalesRevenue() { return salesRevenue; }
    public double getOperatingExpenses() { return operatingExpenses; }
    public double getMachineryHealth() { return machineryHealth; }
    public double getCementHealth() { return cementHealth; }
    public double getSteelHealth() { return steelHealth; }
    public double getExpansionProgress() { return expansionProgress; }
    public boolean isExpanding() { return expanding; }
    public Map<Pop, Double> getWorkers() { return Collections.unmodifiableMap(workers); }
    public double getInventoryAmount(int goodConstant) {
        return getInputInventoryAmount(goodConstant) + getMaintenanceInventoryAmount(goodConstant);
    }
    public double getInputInventoryAmount(int goodConstant) { return inputInventory.getOrDefault(goodConstant, 0.0); }
    public double getMaintenanceInventoryAmount(int goodConstant) { return maintenanceInventory.getOrDefault(goodConstant, 0.0); }
    public Map<Integer, Double> getInputInventory() { return Collections.unmodifiableMap(inputInventory); }
    public Map<Integer, Double> getMaintenanceInventory() { return Collections.unmodifiableMap(maintenanceInventory); }
    public Map<Integer, Double> getLastProduced() { return Collections.unmodifiableMap(lastProduced); }
    public Map<Integer, Double> getMissingInputs() { return Collections.unmodifiableMap(missingInputs); }
    public double getWorkerCapacity() { return 1000.0 * level * Math.min(cementHealth, steelHealth); }
    public double getHiringCap() {
        double capacity = getWorkerCapacity();
        if (capacity <= 0 || moneyPool <= 0 || !recipeIsProfitable()) return 0;

        double inputAvailability = 1;
        double throughput = recipe.throughput() * machineryHealth;
        for (int i = 0; i < recipe.inputGoods().length; i++) {
            double perWorker = throughput * recipe.inputAmounts()[i] * recipe.inputModifier();
            double requiredForTenTicks = capacity * perWorker * 10;
            double available = getInputInventoryAmount(recipe.inputGoods()[i]);
            if (requiredForTenTicks > 0) {
                if (available <= 0) return 0;
                inputAvailability = Math.min(inputAvailability, available / requiredForTenTicks);
            }
        }

        double liquidityAvailability = moneyPool / Math.max(1, capacity * getWageValue());
        return capacity * Math.min(1, Math.min(inputAvailability, liquidityAvailability));
    }
    public double getWorkerCount() { return workers.values().stream().mapToDouble(Double::doubleValue).sum(); }
    public double getWageValue() { return 1 + (getWorkerCapacity() - getWorkerCount()) / Math.max(1, getWorkerCapacity()); }
    public void addMoney(double amount) { if (amount > 0) moneyPool += amount; }
    public void pay(double amount) {
        double paid = Math.min(Math.max(0, amount), moneyPool);
        moneyPool -= paid;
        operatingExpenses += paid;
    }
    public void receiveSale(double amount) {
        if (amount > 0) {
            moneyPool += amount;
            salesRevenue += amount;
        }
    }
    public double settleProfit() {
        double profit = Math.max(0, salesRevenue - operatingExpenses);
        if (owner != null && profit > 0) owner.giveCash(profit);
        salesRevenue = 0;
        operatingExpenses = 0;
        moneyPool = Math.max(0, moneyPool - profit);
        return profit;
    }
    public void setOwner(Pop owner) { this.owner = owner; }

    private static Pop findDefaultOwner(State state) {
        Pop largestCapitalist = null;
        for (Pop pop : state.getPops()) {
            if (pop.job != Constants.CAPITALIST) continue;
            if (largestCapitalist == null || pop.getPopulation() > largestCapitalist.getPopulation()) {
                largestCapitalist = pop;
            }
        }
        return largestCapitalist;
    }

    @Override
    public String toString() {
        return getName() + " (level " + level + ")";
    }

    public boolean startExpansion() {
        if (expanding || owner == null || owner.job != Constants.CAPITALIST || getWorkerCount() < getWorkerCapacity()) {
            return false;
        }
        expanding = true;
        return true;
    }

    public void tickExpansion() {
        if (!expanding) return;
        int[] goods = {Constants.CEMENT, Constants.STEEL, Constants.MACHINE_PARTS};
        double[] amounts = {10 * level, 10 * level, 5 * level};
        double ratio = 1;
        for (int i = 0; i < goods.length; i++) {
            double bought = buy(goods[i], amounts[i] * 0.0025);
            ratio = Math.min(ratio, bought / (amounts[i] * 0.0025));
        }
        if (ratio < 1) return;
        expansionProgress += 0.0025;
        if (expansionProgress >= 1 - 1e-9) {
            level++;
            expansionProgress = 0;
            expanding = false;
        }
    }

    public void setHealth(double cement, double steel, double machinery) {
        cementHealth = clamp(cement); steelHealth = clamp(steel); machineryHealth = clamp(machinery);
    }

    public void clearWorkers() { workers.clear(); }
    public void assignWorker(Pop pop, double amount) {
        if (pop == null || amount <= 0) return;
        double available = Math.max(0, pop.getPopulation() - workers.values().stream().mapToDouble(Double::doubleValue).sum());
        double assigned = Math.min(amount, Math.min(available, getHiringCap() - getWorkerCount()));
        if (assigned > 0) workers.merge(pop, assigned, Double::sum);
    }

    public double tickProduction() {
        lastProduced.clear();
        missingInputs.clear();
        tickExpansion();
        buyMaintenance();
        double workerAmount = getWorkerCount();
        if (workerAmount <= 0) {
            stockInputInventory();
            return 0;
        }
        payWages(workerAmount);
        double throughput = recipe.throughput() * machineryHealth;
        double baseWork = workerAmount * throughput;
        double inputRatio = buyInputs(baseWork);
        double produced = baseWork * inputRatio * recipe.outputModifier();
        if (produced <= 0) {
            stockInputInventory();
            return 0;
        }
        for (int i = 0; i < recipe.outputGoods().length; i++) {
            int goodConstant = recipe.outputGoods()[i];
            double outputAmount = produced * recipe.outputAmounts()[i];
            lastProduced.merge(goodConstant, outputAmount, Double::sum);
            double reserved = reserveMaintenanceOutput(goodConstant, outputAmount);
            double listedAmount = outputAmount - reserved;
            if (listedAmount > 0) {
                AbstractGood good = Constants.getGood(listedAmount, state, goodConstant);
                state.localMarket.postListing(new Listing(good.getAmount(), state, this, state.localMarket, good));
            }
        }
        stockInputInventory();
        return produced;
    }

    public void stockInputInventory() {
        double capacity = getWorkerCapacity();
        double throughput = recipe.throughput() * machineryHealth;
        for (int i = 0; i < recipe.inputGoods().length; i++) {
            double perWorker = throughput * recipe.inputAmounts()[i] * recipe.inputModifier();
            double target = capacity * perWorker * 10;
            double missing = target - getInputInventoryAmount(recipe.inputGoods()[i]);
            if (missing > 0) {
                double bought = buy(recipe.inputGoods()[i], missing);
                if (bought > 0) inputInventory.merge(recipe.inputGoods()[i], bought, Double::sum);
            }
        }
    }

    private void initializeInputInventory() {
        double throughput = recipe.throughput() * machineryHealth;
        for (int i = 0; i < recipe.inputGoods().length; i++) {
            double amount = getWorkerCapacity() * throughput * recipe.inputAmounts()[i]
                    * recipe.inputModifier() * INITIAL_INPUT_STOCK_TICKS;
            inputInventory.merge(recipe.inputGoods()[i], amount, Double::sum);
        }
    }

    private void initializeMaintenanceInventory() {
        for (int good : maintenanceGoods()) {
            maintenanceInventory.put(good, (double) level * INITIAL_INPUT_STOCK_TICKS);
        }
    }

    private void buyMaintenance() {
        int[] goods = new int[]{Constants.CEMENT, Constants.STEEL, Constants.MACHINE_PARTS};
        Map<Integer, Double> ratios = new java.util.HashMap<>();
        for (int good : goods) {
            double required = level;
            double fromInventory = takeMaintenanceInventory(good, required);
            double bought = buy(good, required * 20 - fromInventory);
            double available = fromInventory + bought;
            double excess = available - required;
            if (excess > 0) maintenanceInventory.merge(good, excess, Double::sum);
            ratios.put(good, available / required);
        }
        cementHealth = updateHealth(cementHealth, ratios.getOrDefault(Constants.CEMENT, 1.0));
        steelHealth = updateHealth(steelHealth, ratios.getOrDefault(Constants.STEEL, 1.0));
        machineryHealth = updateHealth(machineryHealth, ratios.getOrDefault(Constants.MACHINE_PARTS, 1.0));
    }

    private double reserveMaintenanceOutput(int goodConstant, double amount) {
        if (!maintenanceGoods().contains(goodConstant)) return 0;
        double reserved = Math.min(amount, level);
        maintenanceInventory.merge(goodConstant, reserved, Double::sum);
        return reserved;
    }

    private double takeInputInventory(int goodConstant, double amount) {
        double available = inputInventory.getOrDefault(goodConstant, 0.0);
        double taken = Math.min(available, amount);
        if (taken > 0) {
            double remaining = available - taken;
            if (remaining <= 1e-9) inputInventory.remove(goodConstant);
            else inputInventory.put(goodConstant, remaining);
        }
        return taken;
    }

    private double takeMaintenanceInventory(int goodConstant, double amount) {
        double available = maintenanceInventory.getOrDefault(goodConstant, 0.0);
        double taken = Math.min(available, amount);
        if (taken > 0) {
            double remaining = available - taken;
            if (remaining <= 1e-9) maintenanceInventory.remove(goodConstant);
            else maintenanceInventory.put(goodConstant, remaining);
        }
        return taken;
    }

    private java.util.Set<Integer> maintenanceGoods() {
        if (recipe.name().equals("machine parts")) {
            return java.util.Set.of(Constants.STEEL, Constants.ANIMAL, Constants.TIMBER);
        }
        return java.util.Set.of(Constants.CEMENT, Constants.STEEL, Constants.MACHINE_PARTS);
    }
    private double buyInputs(double amount) {
        double ratio = 1;
        for (int i = 0; i < recipe.inputGoods().length; i++) {
            double required = amount * recipe.inputAmounts()[i] * recipe.inputModifier();
            double available = takeInputInventory(recipe.inputGoods()[i], required);
            if (available + 1e-9 < required) {
                missingInputs.merge(recipe.inputGoods()[i], required - available, Double::sum);
            }
            ratio = Math.min(ratio, available / Math.max(required, 1e-9));
        }
        return ratio;
    }

    private boolean recipeIsProfitable() {
        return true;/*
        double inputCost = 0;
        double throughput = recipe.throughput() * machineryHealth;
        for (int i = 0; i < recipe.inputGoods().length; i++) {
            double inputPrice = state.localMarket.getAllOfGood(recipe.inputGoods()[i]).isEmpty()
                ? Constants.getGood(1, state, recipe.inputGoods()[i]).getCurrentPrice()
                : state.localMarket.getGoodMaxPrice(recipe.inputGoods()[i], 1);
                inputCost += throughput * recipe.inputAmounts()[i] * recipe.inputModifier()
                * inputPrice;
        }
        double outputRevenue = 0;
        for (int i = 0; i < recipe.outputGoods().length; i++) {
            double outputPrice = state.localMarket.getAllOfGood(recipe.outputGoods()[i]).isEmpty()
                ? Constants.getGood(1, state, recipe.outputGoods()[i]).getCurrentPrice()
                : state.localMarket.getGoodMinPrice(recipe.outputGoods()[i], 1);
            outputRevenue += throughput * recipe.outputAmounts()[i] * recipe.outputModifier() * outputPrice;
        }
        return outputRevenue > inputCost + getWageValue();*/
    }
    private double buy(int constant, double amount) {
        double bought = 0;
        for (AbstractGood good : state.localMarket.getGood(constant, amount)) {
            if (!(good instanceof Listing listing)) continue;
            bought += listing.purchase(amount - bought, this, moneyPool);
            if (bought >= amount) break;
        }
        return bought;
    }
    private double updateHealth(double health, double purchasedRatio) {
        if (purchasedRatio > 1) return Math.min(1, health + (purchasedRatio - 1) * HEALTH_LOSS_PER_TICK);
        if (purchasedRatio == 1) return health;
        return Math.max(0, health - HEALTH_LOSS_PER_TICK);
    }
    private void payWages(double workerCount) {
        double wage = getWageValue();
        for (Map.Entry<Pop, Double> entry : this.workers.entrySet()) {
            double payment = entry.getValue() * wage;
            pay(payment);
            entry.getKey().giveCash(payment);
        }
    }
    private static double clamp(double value) { return Math.max(0, Math.min(1, value)); }
}