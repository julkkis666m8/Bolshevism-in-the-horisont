import constants.Constants;
import controller.Controller;
import main.Main;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import world.*;


/**
 * TODO: So, currently i want to modify the market mechanisms, as it's really bad that there's
 * a phantom market with infinite money, that buys products, that no-one really buys, then the proiducts change prices dynamically,
 * but the market has infinite money, so there's an infinite money glitch there, or an infinite money sink. prices are also too fluid.
 *
 * If merchants were the only people who buy cross-border, or for non-consumption needs, then the merchants will handle this.
 * The job of the market should really be to just connect buyers to sellers, and for sellers to put their goods to.
 */
public class PriceTest {

    public static RaceParameters germanRace = new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC);
    public static JobParameters germanJob = new JobParameters(0, 0, 30, 10, 5, 15, 1, 2, 2, 2, 3, 1);
/*
    @Test
    public void getStatesTest() {

        World world = new World();
        Controller controller = new Controller(world);
        Nation testia = new Nation("Testia", "Testian", world);
        world.addNation(testia);
        State s = new State("testonia", testia, germanRace, germanJob, 10000);
        testia.addState(s);

        System.out.println(s.name+" - MARKET: "+s.localMarket.getStockpileString());
        System.out.println(s.pops.size());

        System.out.println(testia.getInfo());
        int ticks = 100;
        System.out.println(ticks+" TICKS!!!!");
        controller.tick(ticks);

        System.out.println(s.name+" - MARKET: "+s.localMarket.getStockpileString());
        System.out.println(s.pops.size());

        System.out.println(testia.getInfo());

    }*/

    @Test
    public void economyStabilityTest() {

        World world = new World();
        Controller controller = new Controller(world);
        Main.controller = controller;
        Main.world = world;
        Nation testia = new Nation("Testia", "Testian", world);
        world.addNation(testia);
        State s = new State("testonia", testia, germanRace, germanJob, 1000);
        testia.addState(s);

        double beforeCash = 0;
        for (Pop p : world.getAllPops()) {
            beforeCash += p.totalCash();
        }

        int ticks = 1000;

        // check stability each tick to catch transient instabilities
        double prevCash = beforeCash;
        for (int t = 0; t < ticks; t++) {
            controller.tick(1);

            if(t % 100 == 0 || t >= ticks - 1) {
                System.out.println(t+" TICKS!!!!");
                System.out.println(s.name+" - MARKET: "+s.localMarket.getStockpileString());
                System.out.println(s.pops.size());
                System.out.println(testia.getInfo());
            }
            

            double currentCash = 0;
            for (Pop p : world.getAllPops()) {
                currentCash += p.totalCash();
            }

            // sanity checks
            assertFalse(Double.isNaN(currentCash) || Double.isInfinite(currentCash));

            if (beforeCash <= 0) {
                // if nothing to start with, ensure economy doesn't explode
                assertTrue(currentCash < 1e8, "Economy exploded at tick " + t + " to " + currentCash);
            } else {
                double ratio = (prevCash == 0) ? (currentCash / beforeCash) : (currentCash / prevCash);
                assertTrue(ratio > 0.1 && ratio < 10.0, "Economy unstable at tick " + t + ": cash ratio=" + ratio);
            }

            prevCash = currentCash;
        }

    }
}

