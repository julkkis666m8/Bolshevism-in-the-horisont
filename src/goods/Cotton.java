package goods;

import constants.Constants;
import world.State;

public class Cotton extends AbstractGood {

	public Cotton(double amount, State originState) {
		super(amount, originState);

		baseValue = 5;
		this.goodName = "cotton";
		
		this.constant = Constants.COTTON;
	}

}
