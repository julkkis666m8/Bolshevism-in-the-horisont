package goods;

import constants.Constants;
import world.State;

public class Steel extends AbstractGood {

	public Steel(double amount, State originState) {
		super(amount, originState);

		//MAX_PRICE = 1000000;
		baseValue = 20;
		MAX_PRICE = 50;
		MIN_PRICE = 10;
		NON_PRICE = MIN_PRICE-0.01; //must be smaller than MIN_PRICE
		this.goodName = "steel";
		this.constant = Constants.STEEL;
		// TODO Auto-generated constructor stub
	}


}
