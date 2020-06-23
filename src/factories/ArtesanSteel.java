package factories;

import constants.Constants;

public class ArtesanSteel extends AbstractJob {


	public ArtesanSteel() {
		inAmounts = new double[]{2};
		inGoodsConst = new int[]{Constants.IRON};
		outAmounts = new double[]{1};
		outGoodsConst = new int[]{Constants.STEEL};
	}
	

}
