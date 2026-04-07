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

        double money = totalMoney;

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

                            double goodPrice = good.getValue(adjustedAmount);
                            if (goodPrice < money) {
                                money -= goodPrice;
                                buyTheseNeeds[goodConst] -= adjustedAmount;
                                good.removeAmount(adjustedAmount);
                            } else {
                                buyTheseNeeds[goodConst] -= good.buyForMoney(money);
                                money = 0;
                            }
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


    //TODO: FINNISH
    public static double trade(AbstractGood good, AbstractMarket target) {

        double minPrice = target.getGoodMinPrice(good.getConstant(), 1);

        double myPrice = good.getValue(1);

        double income = (minPrice - myPrice) * Constants.TRADE_MARGIN_MERCHANT_CONSTANT;

        double finalPrice = myPrice + income;
        good.setValue(finalPrice);

        target.add(good, good.getAmount());
        //TODO: NEEDED STUFF

        target.modMarketNeed(good.getAmount(), good.getConstant());

        income = income * good.getAmount();
        return income;
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
