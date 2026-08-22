import constants.Constants;
import factories.Factory;
import factories.FactoryRecipe;
import goods.AbstractGood;
import market.Listing;
import org.junit.jupiter.api.Test;
import world.JobParameters;
import world.Nation;
import world.Pop;
import world.RaceParameters;
import world.State;
import world.World;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactoryMaintenanceTest {
    private State state() {
        World world = new World();
        Nation nation = new Nation("Testia", "Testian", world);
        return new State("Testonia", nation,
                new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC),
                new JobParameters(0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0), 1);
    }

    @Test
    public void buyingTwoTicksOfMaintenanceHealsOneTickOfDamage() {
        State state = state();
        Factory factory = new Factory(state, FactoryRecipe.paper(), 1, null);
        for (int tick = 0; tick < Factory.INITIAL_INPUT_STOCK_TICKS; tick++) {
            factory.tickProduction();
        }
        factory.setHealth(0.5, 1, 1);
        Pop seller = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.MERCHANT, 0, state);
        AbstractGood cement = Constants.getGood(2, state, Constants.CEMENT);
        state.localMarket.postListing(new Listing(2, state, seller, state.localMarket, cement));
        factory.addMoney(100);

        factory.tickProduction();

        assertEquals(0.5 + Factory.HEALTH_LOSS_PER_TICK, factory.getCementHealth(), 1e-9);
    }
}