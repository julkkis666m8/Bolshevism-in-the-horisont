package factories;

import constants.Constants;

public class Unemployed extends AbstractJob {

    public Unemployed() {
        unemployed = true;
        inAmounts = new double[]{0};
        inGoodsConst = new int[]{0};
        outAmounts = new double[]{0};
        outGoodsConst = new int[]{Constants.WHEAT};
    }
}
