package goods;

import constants.Constants;
import world.State;

public class Timber extends AbstractGood {

	public Timber(double amount, State originState) {
		super(amount, originState);

		baseValue = 3;
		MAX_PRICE = 10;
		MIN_PRICE = 1;
		//NON_PRICE = MIN_PRICE-0.01; //must be smaller than MIN_PRICE
		this.goodName = "timber";
		
		this.constant = Constants.TIMBER;
		
	}

}
