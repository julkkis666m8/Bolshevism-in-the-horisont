package goods;

import constants.Constants;
import world.State;

public class Iron extends AbstractGood {

	public Iron(double amount, State originState) {
		super(amount, originState);

		baseValue = 20;
		//MAX_PRICE = 20;
		this.goodName = "iron";
		this.constant = Constants.IRON;
		// TODO Auto-generated constructor stub
	}


}
