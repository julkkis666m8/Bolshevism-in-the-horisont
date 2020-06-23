package factories;

import world.State;

public class AbstractJob {

	
	public double[] inAmounts;
	public int[] inGoodsConst;
	public double[] outAmounts;
	public int[] outGoodsConst;
	


	//TODO: to better performance; make this into a per-tick thing!
	public double returnOnInvestment(State state){
		return costBenefit(state);
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
	public double costBenefit(State state) {
		
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

		return outCost - inCost;

	}
}
