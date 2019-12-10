package goods;

import constants.Constants;
import world.State;

public class Clothing extends AbstractGood {

	public Clothing(double amount, State originState) {
		super(amount, originState);

		baseValue = 15;
		this.goodName = "clothing";
		
		this.constant = Constants.CLOTHING;
	}

}
