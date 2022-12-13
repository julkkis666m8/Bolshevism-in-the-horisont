package factories;

import constants.Constants;

public class LabourTimber extends AbstractJob {

	public LabourTimber() {
		inAmounts = new double[]{0};
		inGoodsConst = new int[]{0};
		outAmounts = new double[]{1};
		outGoodsConst = new int[]{Constants.TIMBER};
	}
	
}
