package factories;

import constants.Constants;

public class FarmAnimal extends AbstractJob{
    public FarmAnimal() {
        inAmounts = new double[]{5};
        inGoodsConst = new int[]{Constants.WHEAT};
        outAmounts = new double[]{0.25};
        outGoodsConst = new int[]{Constants.ANIMAL};
    }
}
