import constants.Constants;
import goods.Wheat;
import market.AbstractMarket;
import market.Listing;
import org.junit.jupiter.api.Test;
import world.Pop;
import world.PopSellHandler;

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

        PopSellHandler.trade(imported, market, merchant);

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

        PopSellHandler.trade(imported, market, merchant);

        Listing listing = (Listing) market.getAllOfGood(Constants.WHEAT).get(0);
        assertEquals(22, listing.getCurrentPrice(), 1e-9);
    }
}