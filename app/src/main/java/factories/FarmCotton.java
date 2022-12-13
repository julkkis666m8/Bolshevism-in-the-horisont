package factories;

import constants.Constants;

public class FarmCotton extends AbstractJob {

	public FarmCotton() {
		inAmounts = new double[]{0};
		inGoodsConst = new int[]{0};
		outAmounts = new double[]{1};
		outGoodsConst = new int[]{Constants.COTTON};
	}
}
