package goods;

import constants.Constants;
import world.State;

public class Furnuature extends AbstractGood {

	public Furnuature(double amount, State originState) {
		super(amount, originState);

		baseValue = 17;
		this.goodName = "furnature";
		
		this.constant = Constants.FURNUATURE;
	}

}
