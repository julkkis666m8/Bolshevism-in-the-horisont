package goods;


import constants.Constants;
import world.State;

public class Coal extends AbstractGood {
    public Coal(double amount, State originState) {
        super(amount, originState);
        baseValue = 7;
        MAX_PRICE = 20;
        MIN_PRICE = 5;
        //NON_PRICE = MIN_PRICE-0.01; //must be smaller than MIN_PRICE
        this.goodName = "coal";
        this.constant = Constants.COAL;
        // TODO Auto-generated constructor stub
    }
}
