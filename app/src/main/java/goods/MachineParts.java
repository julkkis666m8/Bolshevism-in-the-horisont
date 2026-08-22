package goods;

import constants.Constants;
import world.State;

public class MachineParts extends AbstractGood {
    public MachineParts(double amount, State originState) {
        super(amount, originState);
        baseValue = 35;
        MAX_PRICE = 100;
        //MIN_PRICE = 10;
        goodName = "machine parts";
        constant = Constants.MACHINE_PARTS;
    }
}