package goods;

import constants.Constants;
import world.State;

public class Iron extends AbstractGood {

	public Iron(double amount, State originState) {
		super(amount, originState);

		baseValue = 7;
		MAX_PRICE = 20;
		MIN_PRICE = 5;
		//NON_PRICE = MIN_PRICE-0.01; //must be smaller than MIN_PRICE
		this.goodName = "iron";
		this.constant = Constants.IRON;
		// TODO Auto-generated constructor stub
	}


}
