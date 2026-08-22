package goods;

import constants.Constants;
import world.State;

public class Cement extends AbstractGood {
    public Cement(double amount, State originState) {
        super(amount, originState);
        baseValue = 8;
        MAX_PRICE = 30;
        //MIN_PRICE = 2;
        goodName = "cement";
        constant = Constants.CEMENT;
    }
}