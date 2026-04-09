package market;

import goods.AbstractGood;
import world.Pop;
import world.State;

/**
 * Listing is a market-held representation of a seller's offer.
 * It behaves like an AbstractGood so buyers can interact with it,
 * but it knows its seller and the owning market so proceeds are
 * only transferred to the seller when the listing is bought.
 */
public class Listing extends AbstractGood {

    private Pop seller;
    private AbstractMarket ownerMarket;

    public Listing(double amount, State originState, Pop seller, AbstractMarket ownerMarket, AbstractGood prototype) {
        super(amount, originState);
        this.seller = seller;
        this.ownerMarket = ownerMarket;
        // copy metadata from prototype
        this.constant = prototype.getConstant();
        this.goodName = prototype.getName();
        this.baseValue = prototype.baseValue;
        this.MAX_PRICE = prototype.MAX_PRICE;
        this.MIN_PRICE = prototype.MIN_PRICE;
        try {
            this.setCurrentPrice(prototype.getCurrentPrice());
        } catch (Exception e) {
            // ignore
        }
    }

    public Pop getSeller() {
        return seller;
    }

    @Override
    public void advancedCalculatePrice(AbstractMarket market) {
        // Base market-driven price movement
        super.advancedCalculatePrice(market);

        // Additionally nudge listing price based on seller's personal surplus
        try {
            if (seller != null) {
                double personalNeed = 0;
                double[] needs = seller.getNeeds();
                if (needs != null && constant >= 0 && constant < needs.length) {
                    personalNeed = needs[constant];
                }

                double surplus = this.getAmount() - personalNeed;

                if (surplus > 0) {
                    double discountRatio = Math.min(0.5, 0.2 * surplus / (personalNeed + 1));
                    double newPrice = getCurrentPrice() * (1 - discountRatio);
                    if (newPrice < MIN_PRICE) newPrice = MIN_PRICE;
                    setCurrentPrice(newPrice);
                } else if (surplus < 0) {
                    double premiumRatio = Math.min(0.2, 0.1 * (-surplus) / (personalNeed + 1));
                    double newPrice = getCurrentPrice() * (1 + premiumRatio);
                    if (newPrice > MAX_PRICE) newPrice = MAX_PRICE;
                    setCurrentPrice(newPrice);
                }
            }
        } catch (Exception e) {
            // best-effort; don't break the tick
        }
    }

    @Override
    public void removeAmount(double amount) {
        double before = getAmount();
        if (amount > before) {
            amount = before;
        }
        super.removeAmount(amount);

        // pay the seller for what was sold
        double money = amount * getCurrentPrice();
        if (seller != null) {
            seller.giveCash(money);
        }

        // decrement market supply for the sold amount
        if (ownerMarket != null) {
            ownerMarket.adjustMarketSupply(this.getConstant(), -amount);
        }

        // notify market to clean up listing if empty
        if (ownerMarket != null) {
            ownerMarket.onListingSold(this);
        }
    }

}
