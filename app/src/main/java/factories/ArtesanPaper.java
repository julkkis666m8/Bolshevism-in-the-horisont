package factories;

import constants.Constants;

public class ArtesanPaper extends AbstractJob {


    public ArtesanPaper() {
        inAmounts = new double[]{1};
        inGoodsConst = new int[]{Constants.TIMBER};
        outAmounts = new double[]{5};
        outGoodsConst = new int[]{Constants.PAPER};
    }


}
