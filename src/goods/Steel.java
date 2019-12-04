package goods;

import constants.Constants;
import world.State;

public class Steel extends AbstractGood {

	public Steel(double amount, State originState) {
		super(amount, originState);

		MAX_PRICE = 8000000;
		baseValue = 100;
		this.goodName = "steel";
		this.constant = Constants.STEEL;
		// TODO Auto-generated constructor stub
	}


}
