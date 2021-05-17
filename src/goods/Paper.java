package goods;

import constants.Constants;
import world.State;

public class Paper extends AbstractGood {

    public Paper(double amount, State originState) {
        super(amount, originState);

        baseValue = 10;
        MAX_PRICE = 20;
        MIN_PRICE = 5;
        //NON_PRICE = MIN_PRICE-0.01; //must be smaller than MIN_PRICE
        this.goodName = "paper";

        this.constant = Constants.PAPER;
    }

}
