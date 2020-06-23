package goods;

import constants.Constants;
import world.State;

public class Furnuature extends AbstractGood {

	public Furnuature(double amount, State originState) {
		super(amount, originState);

		baseValue = 10;
		MAX_PRICE = 20;
		MIN_PRICE = 5;
		//NON_PRICE = MIN_PRICE-0.01; //must be smaller than MIN_PRICE
		this.goodName = "furnature";
		
		this.constant = Constants.FURNUATURE;
	}

}
