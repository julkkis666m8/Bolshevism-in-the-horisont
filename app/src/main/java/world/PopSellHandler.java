package world;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import constants.Constants;
import goods.AbstractGood;
import goods.Cotton;
import goods.Iron;
import goods.Steel;
import goods.Timber;
import goods.Wheat;
import market.AbstractMarket;
import market.Listing;

public class PopSellHandler {

    public synchronized static double sell(Pop pop, AbstractMarket market, Nation nation) {

        // In the new listing-based flow, pops post listings to the market
        // rather than receiving immediate payment. This method reserves
        // sellable amounts into listings and returns 0 (no immediate cash).

        List<AbstractGood> goods = pop.getGoods();

        for (AbstractGood good : goods) {
            postListingForGood(good, market, pop);
        }

        // sellers are not paid until listings fill
        return 0;

    }

    private static void postListingForGood(AbstractGood good, AbstractMarket market, Pop pop) {
        double amount = good.getAmount();
        amount = amount - pop.getNeeds()[good.getConstant()];
        if (amount <= 0) {
            return;
        }
        // remove from pop's possession (reserved -> listing owns it)
        good.setAmount(good.getAmount() - amount);
        Listing listing = new Listing(amount, good.originState, pop, market, good);
        market.postListing(listing);
    }


    public static double[] buyFromSelf(Pop pop, double[] buyTheseNeeds) {

        for (int goodConst = 0; goodConst < Constants.AMOUNT_OF_GOODS; goodConst++) {

            List<AbstractGood> goods = pop.getGoods();

            try {
                for (AbstractGood good : goods) {
                    if (good.getAmount() > 0) {

                        double adjustedAmount = buyTheseNeeds[goodConst];

                        if (adjustedAmount > good.getAmount()) {
                            adjustedAmount = good.getAmount();
                        }
                        buyTheseNeeds[goodConst] -= adjustedAmount;
                        good.removeAmount(adjustedAmount);
                    }
                }
            } catch (NullPointerException e) {
                System.out.println(e);
            }
        }
        return buyTheseNeeds;
    }


    public static double[] buy(Pop pop, double[] buyTheseNeeds, double totalMoney, AbstractMarket market) {
        return buy(pop, buyTheseNeeds, totalMoney, market, "Need");
    }

    public static double[] buy(Pop pop, double[] buyTheseNeeds, double totalMoney,
                               AbstractMarket market, String needType) {

        double money = Math.max(0, totalMoney);

        for (int goodConst = 0; goodConst < Constants.AMOUNT_OF_GOODS; goodConst++) {
            if (money > 0) {

                List<AbstractGood> goods = market.getGood(goodConst, buyTheseNeeds[goodConst]);

                try {
                    for (AbstractGood good : goods) {
                        if (money > 0 && good.getAmount() > 0) {

                            double adjustedAmount = buyTheseNeeds[goodConst];

                            if (adjustedAmount > good.getAmount()) {
                                adjustedAmount = good.getAmount();
                            }

                            if (!(good instanceof Listing)) continue;
                            Listing listing = (Listing) good;
                            double goodPrice = listing.getValue(adjustedAmount);
                            double purchaseMoney = Math.min(money, goodPrice);
                            double bought = listing.purchase(adjustedAmount, pop, purchaseMoney);
                            money -= bought * listing.getCurrentPrice();
                            buyTheseNeeds[goodConst] -= bought;
                            pop.recordPurchase(listing, bought, needType);
                            if (money <= 0) money = 0;
                        }
                    }
                } catch (NullPointerException e) {
                    System.out.println(e);
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
        pop.addJustSpent(totalMoney - money);
        pop.returnTotalCash(money);
        return buyTheseNeeds;
    }


    public static double dropship(AbstractGood good, AbstractMarket target, Pop merchant) {
        if (merchant != null && merchant.getState() != null) {
            target = merchant.getState().localMarket;
            if (good.getOriginState() == merchant.getState()) return 0;
        }

        double minPrice = target.getGoodMinPrice(good.getConstant(), 1);

        double myPrice = good.getValue(1);

        double targetPrice = Math.max(myPrice, minPrice);
        double finalPrice = Math.min(targetPrice, myPrice * 1.10);
        good.setCurrentPrice(finalPrice);
        target.postListing(new Listing(good.getAmount(), good.originState, merchant, target, good));
        return 0;
    }

    public static double dropship(AbstractGood good, AbstractMarket target, Pop merchant,
                                  Listing supplierListing, double supplierUnitPrice,
                                  boolean supplierAlreadySettled) {
        if (merchant != null && merchant.getState() != null) {
            target = merchant.getState().localMarket;
            if (supplierListing.hasVisitedState(merchant.getState())) return 0;
        }

        double salePrice = Math.min(Math.max(good.getValue(1), target.getGoodMinPrice(good.getConstant(), 1)),
                supplierUnitPrice * 1.10);
        good.setCurrentPrice(salePrice);
        target.postListing(new Listing(good.getAmount(), good.originState, merchant, target, good,
                supplierListing, supplierUnitPrice, supplierAlreadySettled));
        return 0;
    }

    public static double dropship(AbstractGood good, AbstractMarket target, Pop merchant,
                                  Listing supplierListing, double supplierUnitPrice) {
        if (merchant != null && merchant.getState() != null) {
            target = merchant.getState().localMarket;
            if (supplierListing.hasVisitedState(merchant.getState())) return 0;
        }

        double salePrice = Math.min(Math.max(good.getValue(1), target.getGoodMinPrice(good.getConstant(), 1)),
                supplierUnitPrice * 1.05);
        good.setCurrentPrice(salePrice);
        target.postListing(new Listing(good.getAmount(), good.originState, merchant, target, good,
                supplierListing, supplierUnitPrice));
        return (salePrice - supplierUnitPrice) * good.getAmount();
    }

    @Deprecated
    public static double trade(AbstractGood good, AbstractMarket target, Pop merchant) {
        return dropship(good, target, merchant);
    }

    @Deprecated
    public static double brokerTrade(AbstractGood good, AbstractMarket target, Pop merchant,
                                      Listing supplierListing, double supplierUnitPrice) {
        return dropship(good, target, merchant, supplierListing, supplierUnitPrice);
    }


    public static List<AbstractGood> labourerJob(Pop pop, State state) {

        List<AbstractGood> goods = new LinkedList<AbstractGood>();

        int population = pop.getPopulation();

        double amount = population * 0.025;

        goods.add(new Iron(amount, state));
        goods.add(new Timber(amount, state));

        return goods;
    }


    public static List<AbstractGood> farmerJob(Pop pop, State state) {

        List<AbstractGood> goods = new LinkedList<AbstractGood>();

        int population = pop.getPopulation();

        double aristocratModifier = 1 + state.jobPercentage(Constants.ARISTOCRAT);

        double amount = population * 0.025 * aristocratModifier * state.getFertility();

        goods.add(new Wheat(amount, state));
        goods.add(new Cotton(amount, state));

        return goods;
    }


    public static List<AbstractGood> serfJob(Pop pop, State state) {

        List<AbstractGood> goods = new LinkedList<AbstractGood>();

        int population = pop.getPopulation();

        double amount = population * 0.025 * state.getFertility();

        goods.add(new Wheat(amount * 2, state));

        return goods;
    }


    public static List<AbstractGood> artesanJob(Pop pop, State state) {

        List<AbstractGood> goods = new LinkedList<AbstractGood>();

        int population = pop.getPopulation();

        double amount = population * 0.01;

        List<AbstractGood> buyGoods = state.localMarket.getGood(constants.Constants.IRON, amount);

        double adjustedAmountBougth = 0;

        for (AbstractGood good : buyGoods) {
            double bougth = good.buyForMaxMoney(amount, pop.totalCash());

            Pop.takeMoney(pop.getSelfList(), good.getValue(bougth));

            adjustedAmountBougth += bougth;
        }

        double EFFICENCY = 0.5;

        double adjustedAmount = EFFICENCY * adjustedAmountBougth;

        goods.add(new Steel(adjustedAmount, state));

        return goods;
    }

}
