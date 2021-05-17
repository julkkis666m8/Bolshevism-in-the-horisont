package goods;

import constants.Constants;
import world.State;

public class Animal extends AbstractGood {
    public Animal(double amount, State originState) {
        super(amount, originState);

        baseValue = 15;
        MAX_PRICE = 50;
        MIN_PRICE = 3;
        //NON_PRICE = MIN_PRICE-0.01; //must be smaller than MIN_PRICE

        this.goodName = "animal";

        this.constant = Constants.ANIMAL;
        // TODO Auto-generated constructor stub
    }

}
