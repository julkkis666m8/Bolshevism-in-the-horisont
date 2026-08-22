package factories;

import constants.Constants;

public class ArtesanMachineParts extends AbstractJob {
    public ArtesanMachineParts() {
        inAmounts = new double[]{5, 5, 3};
        inGoodsConst = new int[]{Constants.STEEL, Constants.COAL, Constants.ANIMAL};
        outAmounts = new double[]{0.25};
        outGoodsConst = new int[]{Constants.MACHINE_PARTS};
    }
}
