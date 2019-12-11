package factories;

import java.util.ArrayList;
import java.util.List;

import constants.Constants;
import goods.AbstractGood;
import world.Pop;
import world.State;

public class ArtesanJobs {


	//Steel
	private static double[] inSteelAmounts = {0.01};
	private static int[] inSteelConst = {Constants.IRON};
	private static double[] outSteelAmounts = {0.01};
	private static int[] outSteelConst = {Constants.STEEL};
	
	//Furnuature
	private static double[] inFurnuatureAmounts = {0.01};
	private static int[] inFurnuatureConst = {Constants.TIMBER};
	private static double[] outFurnuatureAmounts = {0.01};
	private static int[] outFurnuatureConst = {Constants.FURNUATURE};
	
	//Clothing
	private static double[] inClothingAmounts = {0.01};
	private static int[] inClothingConst = {Constants.COTTON};
	private static double[] outClothingAmounts = {0.01};
	private static int[] outClothingConst = {Constants.CLOTHING};
	
	
	public static List<AbstractGood> artesanJob(Pop pop, State state) {
		
		int population = pop.getPopulation();

		double inputInEfficency = 10;
		double outputEfficency = 0.9;
		
		
		List<AbstractGood> goodList = new ArrayList<AbstractGood>();
		
		
		double[] costBenefitList = {0,0,0}; //steel, chiars, clothes.

		
		double[][] inAmounts = {inSteelAmounts, inFurnuatureAmounts, inClothingAmounts};
		int[][] inGoodsConst = {inSteelConst, inFurnuatureConst, inClothingConst};
		double[][] outAmounts = {outSteelAmounts, outFurnuatureAmounts, outClothingAmounts};
		int[][] outGoodsConst = {outSteelConst, outFurnuatureConst, outClothingConst};
		
		for(int i = 0; i < costBenefitList.length; i++) {
			costBenefitList[i] = costBenefit( inAmounts[i], inGoodsConst[i], outAmounts[i], outGoodsConst[i], state);			
		}

		//to find most profitable item index
		double max = costBenefitList[0];
		int index = 0;
		for (int i = 0; i < costBenefitList.length; i++) {
			
			//System.out.println("["+i+"]ARTESAN CALC: "+max+" vs "+costBenefitList[i]);
			
			if (max < costBenefitList[i]) 
			{
				//System.out.println("["+i+"]ARTESAN CALC: "+max+" < "+costBenefitList[i]);
				max = costBenefitList[i];
				index = i;
			}
		}
		//System.out.println("ARTESAN INDEX: "+index);

		double adjustedAmountBougth = 0;
		double amountNeeded = 0;
		
		
		for(int i = 0; i < inGoodsConst[index].length; i++) { //for each item needed
			

			double amount = population * inAmounts[index][i];
			amountNeeded += amount;
			List<AbstractGood> buyGoods = state.localMarket.getGood(constants.Constants.IRON, inAmounts[index][i]);
			
			for(AbstractGood good : buyGoods) {
				double bougth = good.buyForMaxMoney(amount, pop.totalCash());
				
				Pop.takeMoney(pop.getSelfList(), good.getValue(bougth));
				
				adjustedAmountBougth += bougth;
			}
		}
		
		double inputPercentage = adjustedAmountBougth / amountNeeded;
		
		
		for(int i = 0; i < outGoodsConst[index].length; i++) { //for each item made

			double adjustedAmount = population * outAmounts[index][i] * outputEfficency * inputPercentage;
			
			goodList.add(Constants.getGood(adjustedAmount, state, outGoodsConst[index][i]));
		}

		return goodList;
	}
	
	
	
	
	
	
	
	
	/**
	 * gives cost-benefit calculation by finding worst-case sales situation for product and worst-case input cost
	 * returns net profit from sales of out-goods subtracted by cost of in-goods.
	 * @param inAmounts ordered array of amount of items needed
	 * @param inGoods ordered array of item-consts to make
	 * @param outAmounts ordered array of amount of items made
	 * @param outGoods ordered array of item-consts made
	 * @param state state which's market it's sold at.
	 * @return
	 */
	public static double costBenefit(double[] inAmounts, int[] inGoodsConst, double[] outAmounts, int[] outGoodsConst, State state) {
		
		double inCost = 0;
		double outCost = 0;
		
		//get maximum price for needed goods
		for(int i = 0; i < inGoodsConst.length; i++) {
			inCost += state.localMarket.getGoodMaxPrice(inGoodsConst[i], inAmounts[i]);			
		}
		//get minimum profit from selling out-good
		for(int i = 0; i < outGoodsConst.length; i++) {
			outCost += state.localMarket.getGoodMinPrice(outGoodsConst[i], outAmounts[i]);			
		}
		if(Math.random() >0.5) {
			return outCost - inCost;	
		}
		else {
			return -inCost;
		}
		
	}
	
	
	
	
	
	
	
	
	

}
