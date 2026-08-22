import constants.Constants;
import factories.Factory;
import factories.FactoryManager;
import factories.FactoryRecipe;
import factories.AbstractJobDoer;
import factories.ArtesanCement;
import goods.AbstractGood;
import goods.Cotton;
import goods.Cement;
import goods.MachineParts;
import market.Listing;
import org.junit.jupiter.api.Test;
import world.JobParameters;
import world.Nation;
import world.Pop;
import world.PopSellHandler;
import world.RaceParameters;
import world.State;
import world.World;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryTest {
    private State state() {
        World world = new World();
        Nation nation = new Nation("Testia", "Testian", world);
        return new State("Testonia", nation,
                new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC),
                new JobParameters(0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0), 1);
    }

    private Pop seller(State state) {
        return new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.MERCHANT, 0, state);
    }

    private void list(State state, int constant, double amount, double price) {
        AbstractGood good = Constants.getGood(amount, state, constant);
        good.setCurrentPrice(price);
        state.localMarket.postListing(new Listing(amount, state, seller(state), state.localMarket, good));
    }

    @Test
    public void newMaintenanceGoodsAreRegistered() {
        assertEquals(Constants.CEMENT, new Cement(1, null).getConstant());
        assertEquals(Constants.MACHINE_PARTS, new MachineParts(1, null).getConstant());
        assertEquals(12, Constants.AMOUNT_OF_GOODS);
    }

        @Test
        public void factoryDefaultsToTheLargestCapitalistGroup() {
        State state = state();
        Pop smaller = new Pop(10, Constants.PROTESTANT, Constants.GERMANIC,
            Constants.CAPITALIST, 100, state);
        Pop larger = new Pop(20, Constants.CATHOLIC, Constants.GERMANIC,
            Constants.CAPITALIST, 100, state);
        state.addPop(smaller);
        state.addPop(larger);

        Factory factory = new Factory(state, FactoryRecipe.paper(), 1, null);

        assertSame(larger, factory.getOwner());
        }

        @Test
        public void explicitFactoryOwnerOverridesDefaultCapitalist() {
        State state = state();
        Pop defaultOwner = new Pop(20, Constants.PROTESTANT, Constants.GERMANIC,
            Constants.CAPITALIST, 100, state);
        Pop explicitOwner = new Pop(1, Constants.CATHOLIC, Constants.GERMANIC,
            Constants.CAPITALIST, 100, state);
        state.addPop(defaultOwner);
        state.addPop(explicitOwner);

        Factory factory = new Factory(state, FactoryRecipe.paper(), 1, explicitOwner);

        assertSame(explicitOwner, factory.getOwner());
        }

        @Test
        public void artisanCementUsesCoalAndKeepsItsStateOrigin() {
        State state = state();
        Pop artisan = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
            Constants.ARTISAN, 100, state);
        Pop seller = seller(state);
        AbstractGood coal = Constants.getGood(1, state, Constants.COAL);
        state.localMarket.postListing(new Listing(1, state, seller, state.localMarket, coal));

        AbstractGood produced = new AbstractJobDoer()
            .doJob(artisan, state, new ArtesanCement()).get(0);

        assertSame(state, produced.getOriginState());
        PopSellHandler.sell(artisan, state.localMarket, state.nation);
        Listing listing = (Listing) state.localMarket.getAllOfGood(Constants.CEMENT).get(0);
        assertSame(state, listing.getOriginState());
        }

    @Test
    public void cementAndSteelHealthReduceWorkerCapacity() {
        Factory factory = new Factory(state(), FactoryRecipe.paper(), 2, null);
        factory.setHealth(0.5, 0.25, 1);

        assertEquals(500, factory.getWorkerCapacity(), 1e-9);
    }

    @Test
    public void craftsmenCanBeFractionallyAssignedAcrossFactories() {
        State state = state();
        Pop craftsmen = new Pop(1500, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CRAFTSMAN, 0, state);
        state.addPop(craftsmen);
        list(state, Constants.TIMBER, 25000, 1);
        list(state, Constants.COTTON, 20000, 1);
        Factory first = new Factory(state, FactoryRecipe.paper(), 1, null);
        Factory second = new Factory(state, FactoryRecipe.clothing(), 1, null);
        first.stockInputInventory();
        second.stockInputInventory();

        FactoryManager.tick(state);

        assertEquals(1000, first.getWorkerCount(), 1e-9);
        assertEquals(500, second.getWorkerCount(), 1e-9);
    }

    @Test
    public void missingMaintenanceDecaysHealthByPointTwentyFivePercent() {
        Factory factory = new Factory(state(), FactoryRecipe.paper(), 1, null);

        for (int tick = 0; tick < Factory.INITIAL_INPUT_STOCK_TICKS; tick++) {
            factory.tickProduction();
        }

        factory.tickProduction();

        assertEquals(1 - Factory.HEALTH_LOSS_PER_TICK, factory.getMachineryHealth(), 1e-9);
    }

    @Test
    public void halfAvailableInputProducesHalfOutputAndFactoryOwnsListing() {
        State state = state();
        Pop owner = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CAPITALIST, 0, state);
        Factory factory = new Factory(state, FactoryRecipe.clothing(), 1, owner);
        factory.addMoney(100000);
        list(state, Constants.CEMENT, 1, 1);
        list(state, Constants.STEEL, 1, 1);
        list(state, Constants.MACHINE_PARTS, 1, 1);
        list(state, Constants.COTTON, 1000, 1);
        factory.stockInputInventory();
        factory.assignWorker(new Pop(100, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CRAFTSMAN, 0, state), 100);

        factory.tickProduction();

        Listing output = (Listing) state.localMarket.getAllOfGood(Constants.CLOTHING).get(0);
        assertEquals(100, output.getAmount(), 1e-9);
        assertSame(factory, output.getSellerObject());
        assertSame(state, output.getOriginState());
    }

    @Test
    public void factoryKeepsProducedMaintenanceGoodsForTheNextTick() {
        State state = state();
        Factory factory = new Factory(state, FactoryRecipe.steel(), 1, null);
        factory.addMoney(100000);
        list(state, Constants.IRON, 1000, 1);
        list(state, Constants.COAL, 1000, 1);
        list(state, Constants.STEEL, 1000, 1000);
        factory.stockInputInventory();
        factory.assignWorker(new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CRAFTSMAN, 0, state), 1);

        factory.tickProduction();

        assertTrue(factory.getInventoryAmount(Constants.STEEL) > 0);
        Listing output = state.localMarket.getAllOfGood(Constants.STEEL).stream()
            .map(good -> (Listing) good)
            .filter(listing -> listing.getSellerObject() == factory)
            .findFirst()
            .orElseThrow();
        assertTrue(output.getAmount() > 0);
        assertTrue(output.getAmount() < 50 * 1.5);
    }

    @Test
    public void factoryReportsGoodsProducedDuringTheLastTick() {
        State state = state();
        Factory factory = new Factory(state, FactoryRecipe.paper(), 1, null);
        factory.addMoney(100000);
        list(state, Constants.CEMENT, 2, 1);
        list(state, Constants.STEEL, 2, 1);
        list(state, Constants.MACHINE_PARTS, 2, 1);
        list(state, Constants.TIMBER, 25000, 1);
        factory.stockInputInventory();
        factory.assignWorker(new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CRAFTSMAN, 0, state), 1);

        factory.tickProduction();

        assertEquals(12.5, factory.getLastProduced().get(Constants.PAPER), 1e-9);
        assertTrue(factory.getMissingInputs().isEmpty());
    }

    @Test
    public void factoryReportsMissingInputGoodsAfterPartialPurchase() {
        State state = state();
        Factory factory = new Factory(state, FactoryRecipe.clothing(), 1, null);
        factory.addMoney(100000);
        list(state, Constants.CEMENT, 2, 1);
        list(state, Constants.STEEL, 2, 1);
        list(state, Constants.MACHINE_PARTS, 2, 1);
        list(state, Constants.COTTON, 10, 1);
        factory.stockInputInventory();
        factory.assignWorker(new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CRAFTSMAN, 0, state), 1);

        factory.tickProduction();

        assertTrue(factory.getMissingInputs().isEmpty());
        assertTrue(factory.getLastProduced().get(Constants.CLOTHING) > 0);
    }

    @Test
    public void factoryWithNoLiquidityDoesNotHireWorkers() {
        State state = state();
        Factory factory = new Factory(state, FactoryRecipe.paper(), 1, null);
        factory.pay(factory.getMoneyPool());
        factory.assignWorker(new Pop(100, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CRAFTSMAN, 0, state), 100);

        assertEquals(0, factory.getHiringCap(), 1e-9);
        assertEquals(0, factory.getWorkerCount(), 1e-9);
    }

    @Test
    public void factoryHiringCapScalesWithAvailableInputGoods() {
        State state = state();
        Factory factory = new Factory(state, FactoryRecipe.clothing(), 1, null);
        list(state, Constants.COTTON, 10, 1);
        factory.stockInputInventory();
        factory.assignWorker(new Pop(1000, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CRAFTSMAN, 0, state), 1000);

        assertEquals(1000, factory.getHiringCap(), 1e-9);
        assertEquals(1000, factory.getWorkerCount(), 1e-9);
    }

    @Test
    public void factoryInputPurchasesIncreaseMarketDemand() {
        State state = state();
        Factory factory = new Factory(state, FactoryRecipe.paper(), 1, null);
        list(state, Constants.TIMBER, 100, 1);

        factory.stockInputInventory();

        assertEquals(0, state.localMarket.getMarketDemand(Constants.TIMBER), 1e-9);
        assertTrue(factory.getInventoryAmount(Constants.TIMBER) > 0);
    }

    @Test
    public void factoryStartsWithFiftyTicksOfRecipeInputs() {
        Factory factory = new Factory(state(), FactoryRecipe.paper(), 1, null);

        assertEquals(FactoryRecipe.paper().throughput() * 1000
                * FactoryRecipe.paper().inputAmounts()[0] * 50,
                factory.getInventoryAmount(Constants.TIMBER), 1e-9);
    }

    @Test
    public void factoryStartsWithSeparateFiftyTickMaintenanceStock() {
        Factory factory = new Factory(state(), FactoryRecipe.paper(), 1, null);

        assertEquals(50, factory.getMaintenanceInventoryAmount(Constants.CEMENT), 1e-9);
        assertEquals(50, factory.getMaintenanceInventoryAmount(Constants.STEEL), 1e-9);
        assertEquals(50, factory.getMaintenanceInventoryAmount(Constants.MACHINE_PARTS), 1e-9);
        assertEquals(0, factory.getInputInventoryAmount(Constants.CEMENT), 1e-9);
        assertTrue(factory.getInputInventoryAmount(Constants.TIMBER) > 0);
    }

    @Test
    public void factoryPaysAssignedWorkersFromItsPool() {
        State state = state();
        Factory factory = new Factory(state, FactoryRecipe.paper(), 1, null);
        Pop craftsmen = new Pop(10, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CRAFTSMAN, 0, state);
        list(state, Constants.TIMBER, 25000, 1);
        factory.stockInputInventory();
        factory.addMoney(1000);
        factory.assignWorker(craftsmen, 10);

        factory.tickProduction();

        assertTrue(craftsmen.totalCash() > 0);
    }

    @Test
    public void factorySalesCanBeSettledAsOwnerProfitAfterExpenses() {
        State state = state();
        Pop owner = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CAPITALIST, 0, state);
        Factory factory = new Factory(state, FactoryRecipe.clothing(), 1, owner);
        factory.receiveSale(20);
        factory.pay(5);

        assertEquals(15, factory.settleProfit(), 1e-9);
        assertEquals(15, owner.totalCash(), 1e-9);
    }

    @Test
    public void capitalistCanExpandAFullFactoryWhenConstructionGoodsAreAvailable() {
        State state = state();
        Pop owner = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CAPITALIST, 0, state);
        Factory factory = new Factory(state, FactoryRecipe.paper(), 1, owner);
        factory.addMoney(100000);
        list(state, Constants.TIMBER, 25000, 1);
        factory.stockInputInventory();
        factory.assignWorker(new Pop(1000, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.CRAFTSMAN, 0, state), 1000);
        list(state, Constants.CEMENT, 10, 1);
        list(state, Constants.STEEL, 10, 1);
        list(state, Constants.MACHINE_PARTS, 5, 1);

        assertTrue(factory.startExpansion());
        for (int tick = 0; tick < 400; tick++) {
            list(state, Constants.CEMENT, 0.025, 1);
            list(state, Constants.STEEL, 0.025, 1);
            list(state, Constants.MACHINE_PARTS, 0.0125, 1);
            factory.tickExpansion();
        }

        assertEquals(2, factory.getLevel());
        assertFalse(factory.isExpanding());
    }
}