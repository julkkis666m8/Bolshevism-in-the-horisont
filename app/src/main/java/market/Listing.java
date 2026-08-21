package market;

import goods.AbstractGood;
import world.Pop;
import world.State;
import constants.Constants;

/**
 * Listing is a market-held representation of a seller's offer.
 * It behaves like an AbstractGood so buyers can interact with it,
 * but it knows its seller and the owning market so proceeds are
 * only transferred to the seller when the listing is bought.
 */
public class Listing extends AbstractGood {

    private Pop seller;
    private AbstractMarket ownerMarket;
    private Listing supplierListing;
    private double supplierUnitPrice;
    private boolean supplierAlreadySettled;

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

    public Listing(double amount, State originState, Pop seller, AbstractMarket ownerMarket,
                   AbstractGood prototype, Listing supplierListing, double supplierUnitPrice) {
        this(amount, originState, seller, ownerMarket, prototype, supplierListing, supplierUnitPrice, false);
    }

    public Listing(double amount, State originState, Pop seller, AbstractMarket ownerMarket,
                   AbstractGood prototype, Listing supplierListing, double supplierUnitPrice,
                   boolean supplierAlreadySettled) {
        this(amount, originState, seller, ownerMarket, prototype);
        this.supplierListing = supplierListing;
        this.supplierUnitPrice = supplierUnitPrice;
        this.supplierAlreadySettled = supplierAlreadySettled;
    }

    public Pop getSeller() {
        return seller;
    }

    public String getSaleType() {
        if (supplierListing != null) return "Dropship chain (" + getDropshipChainLength() + " hops)";
        if (seller != null && seller.job == Constants.MERCHANT) return "Merchant-owned";
        if (seller != null) return "Original creator";
        return "Unknown";
    }

    public int getDropshipChainLength() {
        return supplierListing == null ? 0 : supplierListing.getDropshipChainLength() + 1;
    }

    public boolean hasVisitedState(State state) {
        if (state == null) return false;
        if (originState == state || (seller != null && seller.getState() == state)) return true;
        return supplierListing != null && supplierListing.hasVisitedState(state);
    }

    public String getDropshipRoute() {
        String route;
        if (supplierListing != null) {
            route = supplierListing.getDropshipRoute();
        } else {
            route = stateName(originState);
            if (seller != null && seller.getState() != null) {
                route = appendRoute(route, stateName(seller.getState()));
            }
        }

        if (supplierListing != null && seller != null && seller.getState() != null) {
            route = appendRoute(route, stateName(seller.getState()));
        }
        return route;
    }

    private static String stateName(State state) {
        return state == null || state.name == null ? "Unknown" : state.name;
    }

    private static String appendRoute(String route, String nextState) {
        if (route.equals(nextState) || route.endsWith(" -> " + nextState)) return route;
        return route + " -> " + nextState;
    }

    public synchronized double purchase(double requestedAmount, Pop buyer, double availableMoney) {
        if (buyer == null || requestedAmount <= 0 || availableMoney <= 0 || getAmount() <= 0) {
            return 0;
        }

        double quantity = Math.min(requestedAmount, getAmount());
        quantity = Math.min(quantity, availableMoney / getCurrentPrice());
        if (quantity <= 0) return 0;

        double paid = quantity * getCurrentPrice();
        double actualPayment = buyer.pay(paid);
        if (actualPayment <= 0) return 0;
        removeAmount(quantity);
        return quantity;
    }

    public synchronized double reserveForDropshipping(double requestedAmount) {
        double reserved = Math.min(requestedAmount, getAmount());
        if (reserved <= 0) return 0;
        super.removeAmount(reserved);
        if (ownerMarket != null) ownerMarket.adjustMarketSupply(getConstant(), -reserved);
        if (ownerMarket != null) ownerMarket.onListingSold(this);
        return reserved;
    }

    @Deprecated
    public double reserveForBrokerage(double requestedAmount) {
        return reserveForDropshipping(requestedAmount);
    }

    @Override
    public void advancedCalculatePrice(AbstractMarket market) {
        double priceAtStart = getCurrentPrice();
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
                    newPrice = Math.max(priceAtStart * 0.95, newPrice);
                    if (newPrice < MIN_PRICE) newPrice = MIN_PRICE;
                    setCurrentPrice(newPrice);
                } else if (surplus < 0) {
                    double premiumRatio = Math.min(0.2, 0.1 * (-surplus) / (personalNeed + 1));
                    double newPrice = getCurrentPrice() * (1 + premiumRatio);
                    newPrice = Math.min(priceAtStart * 1.05, newPrice);
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

        // compute sale proceeds and allocate aristocrat share based on seller's status
        double money = amount * getCurrentPrice();
        double toSeller = money;
        try {
            if (supplierListing != null) {
                if (!supplierAlreadySettled) {
                    supplierListing.settleDropshipSale(amount, supplierUnitPrice);
                }
                toSeller = Math.max(0, money - amount * supplierUnitPrice);
            }
            if (seller != null) {
                int job = seller.job;
                double ariFraction = 0.0;
                if (job == Constants.LABORER || job == Constants.FARMER) {
                    ariFraction = 0.10;
                } else if (job == Constants.SERF) {
                    ariFraction = 0.75;
                } else if (job == Constants.SLAVE) {
                    ariFraction = 0.99;
                }

                if (ariFraction > 0 && seller.getState() != null) {
                    double ariMoney = money * ariFraction;
                    seller.getState().getAristocratCashPool().giveMoneyToAristocrats(ariMoney);
                    toSeller = money - ariMoney;
                }

                seller.giveCash(toSeller);
                seller.recordSale(this, amount, getCurrentPrice());
            }
        } catch (Exception e) {
            // best-effort, ensure seller still gets paid if possible
            if (seller != null) seller.giveCash(money);
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

    private void settleDropshipSale(double amount, double saleUnitPrice) {
        double upstreamMoney = 0;
        if (supplierListing != null) {
            upstreamMoney = amount * supplierUnitPrice;
            if (!supplierAlreadySettled) {
                supplierListing.settleDropshipSale(amount, supplierUnitPrice);
            }
        }
        if (seller != null) {
            seller.giveCash(Math.max(0, amount * saleUnitPrice - upstreamMoney));
            seller.recordSale(this, amount, saleUnitPrice);
        }
    }

}
