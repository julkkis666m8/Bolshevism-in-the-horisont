package factories;

import constants.Constants;

public class ArtesanSteel extends AbstractJob {


	public ArtesanSteel() {
		inAmounts = new double[]{16, 7.7};
		inGoodsConst = new int[]{Constants.IRON, Constants.COAL};
		outAmounts = new double[]{1};
		outGoodsConst = new int[]{Constants.STEEL};
	}
	

}
