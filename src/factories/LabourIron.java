package factories;

import constants.Constants;

public class LabourIron extends AbstractJob {

	public LabourIron() {
		inAmounts = new double[]{0};
		inGoodsConst = new int[]{0};
		outAmounts = new double[]{1};
		outGoodsConst = new int[]{Constants.IRON};
	}
	
}
