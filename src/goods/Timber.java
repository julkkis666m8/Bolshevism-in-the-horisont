package goods;

import constants.Constants;
import world.State;

public class Timber extends AbstractGood {

	public Timber(double amount, State originState) {
		super(amount, originState);

		baseValue = 3;
		this.goodName = "timber";
		
		this.constant = Constants.TIMBER;
		
	}

}
