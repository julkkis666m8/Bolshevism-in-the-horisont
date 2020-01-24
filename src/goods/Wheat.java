package goods;

import constants.Constants;
import goods.AbstractGood;
import world.State;

public class Wheat extends AbstractGood {

	public Wheat(double amount, State originState) {
		super(amount, originState);
		
		baseValue = 3;
		MAX_PRICE = 10;
		MIN_PRICE = 1;
		NON_PRICE = MIN_PRICE-0.01; //must be smaller than MIN_PRICE
		
		this.goodName = "wheat";
		
		this.constant = Constants.WHEAT;
		// TODO Auto-generated constructor stub
	}

}
