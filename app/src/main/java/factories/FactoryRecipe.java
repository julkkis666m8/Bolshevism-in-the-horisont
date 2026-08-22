package factories;

import constants.Constants;

public record FactoryRecipe(String name, double[] inputAmounts, int[] inputGoods,
                            double[] outputAmounts, int[] outputGoods,
                            double throughput, double inputModifier, double outputModifier) {
    public static FactoryRecipe cement() {
        return new FactoryRecipe("cement", new double[]{1}, new int[]{Constants.COAL},
                new double[]{0.25}, new int[]{Constants.CEMENT}, 1.0, 1, 1.2);
    }
    public static FactoryRecipe steel() {
        return new FactoryRecipe("steel", new double[]{14.4, 6.93},
                new int[]{Constants.IRON, Constants.COAL}, new double[]{1},
                new int[]{Constants.STEEL}, 5.0, 1, 1.5);
    }
    public static FactoryRecipe paper() {
        return new FactoryRecipe("paper", new double[]{1}, new int[]{Constants.TIMBER},
                new double[]{5}, new int[]{Constants.PAPER}, 2.5, 1, 1);
    }
    public static FactoryRecipe clothing() {
        return new FactoryRecipe("clothing", new double[]{2}, new int[]{Constants.COTTON},
                new double[]{1}, new int[]{Constants.CLOTHING}, 1.0, 1, 1);
    }
    public static FactoryRecipe furniture() {
        return new FactoryRecipe("furniture", new double[]{10, 2},
                new int[]{Constants.TIMBER, Constants.IRON}, new double[]{9},
                new int[]{Constants.FURNUATURE}, 1.0, 1, 1);
    }
    public static FactoryRecipe machineParts() {
        return new FactoryRecipe("machine parts", new double[]{5, 5, 3},
                new int[]{Constants.STEEL, Constants.COAL, Constants.ANIMAL},
                new double[]{1}, new int[]{Constants.MACHINE_PARTS}, 1.0, 1, 1);
    }
}