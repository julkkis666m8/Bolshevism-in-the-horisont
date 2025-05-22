import constants.Constants;
import controller.Controller;
import file.readers.MapReader;
import org.junit.jupiter.api.Test;
import world.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class PriceTest {

    public static RaceParameters germanRace = new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC);
    public static JobParameters germanJob = new JobParameters(0, 0, 30, 10, 5, 15, 1, 2, 2, 2, 3, 1);


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

    }
}

