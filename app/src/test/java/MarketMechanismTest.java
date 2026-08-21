import constants.Constants;
import goods.Wheat;
import market.AbstractMarket;
import market.Listing;
import market.MerchantHandler;
import org.junit.jupiter.api.Test;
import world.Pop;
import world.PopSellHandler;
import world.World;
import world.Nation;
import world.State;
import world.RaceParameters;
import world.JobParameters;

import static org.junit.jupiter.api.Assertions.*;

public class MarketMechanismTest {

    private Pop pop(double cash) {
        return new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
                Constants.LABORER, cash, null);
    }

    @Test
    public void marketDoesNotExposeItsOwnStockpileAsSupply() {
        AbstractMarket market = new AbstractMarket();
        market.add(new Wheat(5, null), 5);

        assertTrue(market.getGood(Constants.WHEAT, 1).isEmpty());
        assertTrue(market.getAllOfGood(Constants.WHEAT).isEmpty());
    }

    @Test
    public void purchaseTransfersExistingMoneyFromBuyerToSeller() {
        AbstractMarket market = new AbstractMarket();
        Pop seller = pop(0);
        Pop buyer = pop(10);
        Listing listing = new Listing(2, null, seller, market, new Wheat(2, null));
        market.postListing(listing);
        double price = listing.getCurrentPrice();

        double[] needs = new double[Constants.AMOUNT_OF_GOODS];
        needs[Constants.WHEAT] = 1;
        PopSellHandler.buy(buyer, needs, buyer.totalCash(), market);

        assertEquals(10 - price, buyer.totalCash(), 1e-9);
        assertEquals(price, seller.totalCash(), 1e-9);
        assertEquals(1, listing.getAmount(), 1e-9);
        assertEquals(1, buyer.getRecentPurchases().size());
        assertEquals(1, buyer.getRecentPurchases().get(0).getAmount(), 1e-9);
        assertEquals("Need", buyer.getRecentPurchases().get(0).getNeedType());
        assertEquals(1, seller.getRecentSales().size());
        assertEquals(1, seller.getRecentSales().get(0).getAmount(), 1e-9);
    }

    @Test
    public void buyerCannotPurchaseMoreThanItsCash() {
        AbstractMarket market = new AbstractMarket();
        Pop seller = pop(0);
        Pop buyer = pop(0.5);
        Listing listing = new Listing(2, null, seller, market, new Wheat(2, null));
        listing.setCurrentPrice(1);
        market.postListing(listing);

        double[] needs = new double[Constants.AMOUNT_OF_GOODS];
        needs[Constants.WHEAT] = 2;
        PopSellHandler.buy(buyer, needs, buyer.totalCash(), market);

        assertEquals(0, buyer.totalCash(), 1e-9);
        assertEquals(1.5, listing.getAmount(), 1e-9);
        assertEquals(0.5, seller.totalCash(), 1e-9);
    }

    @Test
    public void listingPriceMovesNoMoreThanFivePercentPerUpdate() {
        AbstractMarket market = new AbstractMarket();
        Listing listing = new Listing(1000, null, pop(0), market, new Wheat(1000, null));
        listing.setCurrentPrice(100);
        market.postListing(listing);
        market.modMarketNeed(10000, Constants.WHEAT);

        market.updateGoods();

        assertTrue(listing.getCurrentPrice() >= 95);
        assertTrue(listing.getCurrentPrice() <= 105);
    }

    @Test
    public void demandIsResetAfterPriceUpdate() {
        AbstractMarket market = new AbstractMarket();
        market.modMarketNeed(7, Constants.WHEAT);

        market.updateGoods();

        assertEquals(0, market.getMarketDemand(Constants.WHEAT), 1e-9);
    }

    @Test
    public void merchantTradePostsMerchantOwnedListing() {
        AbstractMarket market = new AbstractMarket();
        Pop merchant = pop(10);
        Wheat imported = new Wheat(3, null);

        PopSellHandler.dropship(imported, market, merchant);

        assertEquals(1, market.getAllOfGood(Constants.WHEAT).size());
        Listing listing = (Listing) market.getAllOfGood(Constants.WHEAT).get(0);
        assertSame(merchant, listing.getSeller());
    }

    @Test
    public void merchantPriceIsCappedWhenDestinationMarketIsEmpty() {
        AbstractMarket market = new AbstractMarket();
        Pop merchant = pop(10);
        Wheat imported = new Wheat(3, null);
        imported.setCurrentPrice(20);

        PopSellHandler.dropship(imported, market, merchant);

        Listing listing = (Listing) market.getAllOfGood(Constants.WHEAT).get(0);
        assertEquals(22, listing.getCurrentPrice(), 1e-9);
    }

    @Test
    public void cashPoorMerchantCanBrokerGoodsWithoutFundingThePurchase() {
        AbstractMarket sourceMarket = new AbstractMarket();
        AbstractMarket targetMarket = new AbstractMarket();
        Pop sourceSeller = pop(0);
        Pop merchant = pop(0);
        Pop buyer = pop(20);
        Listing source = new Listing(1, null, sourceSeller, sourceMarket, new Wheat(1, null));
        source.setCurrentPrice(10);
        sourceMarket.postListing(source);

        double reserved = source.reserveForDropshipping(1);
        Wheat brokered = new Wheat(reserved, null);
        brokered.setCurrentPrice(10);
        PopSellHandler.dropship(brokered, targetMarket, merchant, source, 10);

        Listing destination = (Listing) targetMarket.getAllOfGood(Constants.WHEAT).get(0);
        double[] needs = new double[Constants.AMOUNT_OF_GOODS];
        needs[Constants.WHEAT] = 1;
        PopSellHandler.buy(buyer, needs, buyer.totalCash(), targetMarket);

        assertEquals(0.5, merchant.totalCash(), 1e-9);
        assertEquals(10, sourceSeller.totalCash(), 1e-9);
        assertEquals(9.5, buyer.totalCash(), 1e-9);
        assertEquals(0, destination.getAmount(), 1e-9);
    }

        @Test
        public void merchantDropshippingIsLimitedPerTurn() {
        World world = new World();
        Nation nation = new Nation("Testia", "Testian", world);
        State destination = new State("Destination", nation,
            new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC),
            new JobParameters(0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0), 1);
        State source = new State("Source", nation,
            new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC),
            new JobParameters(0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0), 1);
        destination.addNeigbour(source);
        Pop merchant = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
            Constants.MERCHANT, 0, destination);
        Pop sourceSeller = pop(0);
        Listing sourceListing = new Listing(250, source, sourceSeller, source.localMarket,
            new Wheat(250, source));
        sourceListing.setCurrentPrice(1);
        source.localMarket.postListing(sourceListing);
        destination.localMarket.modMarketNeed(200, Constants.WHEAT);

        MerchantHandler.wrangle(destination, merchant, nation);

        assertEquals(Constants.MERCHANT_DROPSHIPPING_CAPACITY_PER_TURN,
            destination.localMarket.listedAmount(Constants.WHEAT, merchant), 1e-9);
        }

        @Test
        public void chainedDropshippingPaysBothMerchantMargins() {
        AbstractMarket finalMarket = new AbstractMarket();
        Pop originalSeller = pop(0);
        Pop firstMerchant = pop(0);
        Pop secondMerchant = pop(0);
        Listing original = new Listing(1, null, originalSeller, new AbstractMarket(), new Wheat(1, null));
        original.setCurrentPrice(10);
        original.reserveForDropshipping(1);
        Wheat firstGoods = new Wheat(1, null);
        firstGoods.setCurrentPrice(10);
        PopSellHandler.dropship(firstGoods, finalMarket, firstMerchant, original, 10);
        Listing firstListing = (Listing) finalMarket.getAllOfGood(Constants.WHEAT).get(0);

        AbstractMarket secondMarket = new AbstractMarket();
        firstListing.reserveForDropshipping(1);
        Wheat secondGoods = new Wheat(1, null);
        secondGoods.setCurrentPrice(firstListing.getCurrentPrice());
        PopSellHandler.dropship(secondGoods, secondMarket, secondMerchant, firstListing,
            firstListing.getCurrentPrice());
        Listing finalListing = (Listing) secondMarket.getAllOfGood(Constants.WHEAT).get(0);
        Pop buyer = pop(20);
        double[] needs = new double[Constants.AMOUNT_OF_GOODS];
        needs[Constants.WHEAT] = 1;
        PopSellHandler.buy(buyer, needs, buyer.totalCash(), secondMarket);

        assertEquals(0.5, firstMerchant.totalCash(), 1e-9);
        assertEquals(0.525, secondMerchant.totalCash(), 1e-9);
        assertEquals(10, originalSeller.totalCash(), 1e-9);
        assertEquals(8.975, buyer.totalCash(), 1e-9);
        assertEquals(0, finalListing.getAmount(), 1e-9);
        }

        @Test
        public void dropshipCannotReturnGoodsToTheirOriginState() {
        World world = new World();
        Nation nation = new Nation("Testia", "Testian", world);
        State origin = new State("Origin", nation,
            new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC),
            new JobParameters(0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0), 1);
        Pop merchant = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
            Constants.MERCHANT, 0, origin);
        Wheat good = new Wheat(1, origin);

        PopSellHandler.dropship(good, origin.localMarket, merchant);

        assertTrue(origin.localMarket.getAllOfGood(Constants.WHEAT).isEmpty());
        }

        @Test
        public void dropshipCannotReturnGoodsToAnEarlierChainState() {
        World world = new World();
        Nation nation = new Nation("Testia", "Testian", world);
        State origin = new State("Origin", nation,
            new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC),
            new JobParameters(0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0), 1);
        State firstStop = new State("First stop", nation,
            new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC),
            new JobParameters(0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0), 1);
        Pop creator = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
            Constants.FARMER, 0, origin);
        Pop firstMerchant = new Pop(1, Constants.PROTESTANT, Constants.GERMANIC,
            Constants.MERCHANT, 0, firstStop);
        Listing supplier = new Listing(1, origin, creator, origin.localMarket, new Wheat(1, origin));
        Listing firstListing = new Listing(1, origin, firstMerchant, firstStop.localMarket,
            new Wheat(1, origin), supplier, 1);

        PopSellHandler.dropship(new Wheat(1, origin), origin.localMarket, firstMerchant,
            firstListing, 1);

        assertTrue(origin.localMarket.getAllOfGood(Constants.WHEAT).isEmpty());
        }
}