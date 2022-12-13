package factories;

import constants.Constants;

public class FarmWheat extends AbstractJob {

	public FarmWheat() {
		inAmounts = new double[]{0};
		inGoodsConst = new int[]{0};
		outAmounts = new double[]{1};
		outGoodsConst = new int[]{Constants.WHEAT};
	}
	
}
