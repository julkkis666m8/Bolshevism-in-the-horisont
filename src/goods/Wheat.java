package goods;

import constants.Constants;
import goods.AbstractGood;
import world.State;

public class Wheat extends AbstractGood {

	public Wheat(double amount, State originState) {
		super(amount, originState);
		
		//MAX_PRICE = 10;
		
		this.goodName = "wheat";
		
		this.constant = Constants.WHEAT;
		// TODO Auto-generated constructor stub
	}

}
