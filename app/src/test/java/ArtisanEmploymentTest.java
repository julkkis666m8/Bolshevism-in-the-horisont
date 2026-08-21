import constants.Constants;
import factories.AbstractJob;
import factories.AbstractJobChoser;
import factories.ArtesanFurnature;
import factories.ArtesanSteel;
import goods.Steel;
import goods.Furnuature;
import market.Listing;
import org.junit.jupiter.api.Test;
import world.JobParameters;
import world.Nation;
import world.Pop;
import world.RaceParameters;
import world.State;
import world.World;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArtisanEmploymentTest {

    private State state() {
        World world = new World();
        Nation nation = new Nation("Testia", "Testian", world);
        return new State("Testonia", nation,
                new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC),
                new JobParameters(0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0), 1);
    }

    private Pop artisan(State state) {
        return new Pop(100, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.ARTISAN, 100, state);
    }

    @Test
    public void artisanCanChooseAnotherRecipeWhenOneHasHundredDaysUnsold() {
        State state = state();
        Pop artisan = artisan(state);
        AbstractJob steel = new ArtesanSteel();
        AbstractJob furniture = new ArtesanFurnature();
        double hundredDays = artisan.getPopulation() * steel.outAmounts[0] * 0.9 * 100;
        state.localMarket.postListing(new Listing(hundredDays, state, artisan,
                state.localMarket, new Steel(hundredDays, state)));

        AbstractJob selected = new AbstractJobChoser().chooseArtisanJob(
                artisan, state, List.of(steel, furniture));

        assertTrue(selected instanceof ArtesanFurnature);
    }

    @Test
    public void artisanBecomesUnemployedWhenEveryRecipeHasHundredDaysUnsold() {
        State state = state();
        Pop artisan = artisan(state);
        AbstractJob steel = new ArtesanSteel();
        AbstractJob furniture = new ArtesanFurnature();
        double steelLimit = artisan.getPopulation() * steel.outAmounts[0] * 0.9 * 100;
        double furnitureLimit = artisan.getPopulation() * furniture.outAmounts[0] * 0.9 * 100;
        state.localMarket.postListing(new Listing(steelLimit, state, artisan,
                state.localMarket, new Steel(steelLimit, state)));
        state.localMarket.postListing(new Listing(furnitureLimit, state, artisan,
                state.localMarket, new Furnuature(furnitureLimit, state)));

        AbstractJob selected = new AbstractJobChoser().chooseArtisanJob(
                artisan, state, List.of(steel, furniture));

        assertTrue(selected.unemployed);
        assertEquals(Constants.ARTISAN, artisan.job);
    }

    @Test
    public void listingsFromOtherSellersDoNotBlockArtisan() {
        State state = state();
        Pop artisan = artisan(state);
        Pop otherSeller = new Pop(100, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.ARTISAN, 100, state);
        AbstractJob steel = new ArtesanSteel();
        double limit = artisan.getPopulation() * steel.outAmounts[0] * 0.9 * 100;
        state.localMarket.postListing(new Listing(limit, state, otherSeller,
                state.localMarket, new Steel(limit, state)));

        AbstractJob selected = new AbstractJobChoser().chooseArtisanJob(
                artisan, state, List.of(steel));

        assertSame(steel, selected);
    }
}