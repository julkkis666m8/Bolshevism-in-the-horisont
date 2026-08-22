package factories;

import constants.Constants;

public class ArtesanCement extends AbstractJob {
    public ArtesanCement() {
        inAmounts = new double[]{1};
        inGoodsConst = new int[]{Constants.COAL};
        outAmounts = new double[]{0.25};
        outGoodsConst = new int[]{Constants.CEMENT};
    }
}