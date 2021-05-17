package factories;

import constants.Constants;

public class LabourCoal extends AbstractJob{
    public LabourCoal(){
        inAmounts = new double[]{0};
        inGoodsConst = new int[]{0};
        outAmounts = new double[]{1};
        outGoodsConst = new int[]{Constants.COAL};
    }
}
