package goods;

import constants.Constants;
import world.State;

public class Clothing extends AbstractGood {

	public Clothing(double amount, State originState) {
		super(amount, originState);

		baseValue = 15;
		MAX_PRICE = 25;
		MIN_PRICE = 5;
		//NON_PRICE = MIN_PRICE-0.01; //must be smaller than MIN_PRICE
		this.goodName = "clothing";
		
		this.constant = Constants.CLOTHING;
	}

}
