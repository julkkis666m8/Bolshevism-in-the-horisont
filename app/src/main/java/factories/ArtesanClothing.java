package factories;

import constants.Constants;

public class ArtesanClothing extends AbstractJob {


	public ArtesanClothing() {
		inAmounts = new double[]{2};
		inGoodsConst = new int[]{Constants.COTTON};
		outAmounts = new double[]{1};
		outGoodsConst = new int[]{Constants.CLOTHING};
	}
	

}
