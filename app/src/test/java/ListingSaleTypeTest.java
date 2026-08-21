import constants.Constants;
import goods.Wheat;
import market.AbstractMarket;
import market.Listing;
import org.junit.jupiter.api.Test;
import world.Pop;
import world.State;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListingSaleTypeTest {

    private Pop pop(int job) {
        return new Pop(1, Constants.PROTESTANT, Constants.GERMANIC, job, 0, null);
    }

    @Test
    public void creatorListingIsIdentified() {
        Listing listing = new Listing(1, null, pop(Constants.FARMER), new AbstractMarket(), new Wheat(1, null));

        assertEquals("Original creator", listing.getSaleType());
    }

    @Test
    public void merchantOwnedListingIsIdentified() {
        Listing listing = new Listing(1, null, pop(Constants.MERCHANT), new AbstractMarket(), new Wheat(1, null));

        assertEquals("Merchant-owned", listing.getSaleType());
    }

    @Test
    public void chainedDropshipListingIsIdentified() {
        Listing supplier = new Listing(1, null, pop(Constants.FARMER), new AbstractMarket(), new Wheat(1, null));
        Listing listing = new Listing(1, null, pop(Constants.MERCHANT), new AbstractMarket(),
                new Wheat(1, null), supplier, 1);

        assertEquals("Dropship chain (1 hops)", listing.getSaleType());
        assertEquals(1, listing.getDropshipChainLength());
    }

    @Test
    public void chainedDropshipListingShowsRoute() {
        State origin = namedState("Origin");
        State firstStop = namedState("First stop");
        State secondStop = namedState("Second stop");
        Pop creator = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC, Constants.FARMER, 0, origin);
        Pop firstMerchant = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC, Constants.MERCHANT, 0, firstStop);
        Pop secondMerchant = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC, Constants.MERCHANT, 0, secondStop);
        Listing supplier = new Listing(1, origin, creator, origin.localMarket, new Wheat(1, origin));
        Listing first = new Listing(1, origin, firstMerchant, firstStop.localMarket, new Wheat(1, origin), supplier, 1);
        Listing second = new Listing(1, origin, secondMerchant, secondStop.localMarket, new Wheat(1, origin), first, 1);

        assertEquals(2, second.getDropshipChainLength());
        assertEquals("Origin -> First stop -> Second stop", second.getDropshipRoute());
    }

    @Test
    public void merchantOwnedResaleKeepsThePreviousRoute() {
        State origin = namedState("Origin");
        State merchantState = namedState("Merchant state");
        Pop creator = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC, Constants.FARMER, 0, origin);
        Pop merchant = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC, Constants.MERCHANT, 0, merchantState);
        Listing original = new Listing(1, origin, creator, origin.localMarket, new Wheat(1, origin));
        Listing merchantOwned = new Listing(1, origin, merchant, merchantState.localMarket,
                new Wheat(1, origin), original, 1, true);

        assertEquals("Dropship chain (1 hops)", merchantOwned.getSaleType());
        assertEquals("Origin -> Merchant state", merchantOwned.getDropshipRoute());
    }

    private State namedState(String name) {
        State state = new State();
        state.setName(name);
        return state;
    }
}