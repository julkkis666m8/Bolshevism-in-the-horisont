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

        // notify market to clean up listing if empty
        if (ownerMarket != null) {
            ownerMarket.onListingSold(this);
        }
    }

}
