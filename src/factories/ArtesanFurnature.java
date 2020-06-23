package factories;

import constants.Constants;

public class ArtesanFurnature extends AbstractJob {


	public ArtesanFurnature() {
		inAmounts = new double[]{2};
		inGoodsConst = new int[]{Constants.TIMBER};
		outAmounts = new double[]{1};
		outGoodsConst = new int[]{Constants.FURNUATURE};
	}
	

}
