package factories;

import java.util.ArrayList;
import java.util.List;

import constants.Constants;
import goods.AbstractGood;
import world.Pop;
import world.State;



//Money should be given using a pop.function.

public class AbstractJobChoser {
	
	
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
	public double costBenefit(double[] inAmounts, int[] inGoodsConst, double[] outAmounts, int[] outGoodsConst, State state) {
		
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

	//--------------------------------------------------------------
	//------------important stuff below-----------------------------
	//--------------------------------------------------------------
	
	
	
	

	public AbstractJob choseEfficentJob(Pop pop, State state, List<AbstractJob>  jobList){
		double[] costBenefitList = new double[jobList.size()]; //{0,0,0}; //steel, chiars, clothes.
		
		//List<Double> costBenefitList = new ArrayList<Double>(jobList.size());
		
		for(int i = 0; i < jobList.size(); i++) {
			costBenefitList[i] = costBenefit( jobList.get(i).inAmounts, jobList.get(i).inGoodsConst, jobList.get(i).outAmounts, jobList.get(i).outGoodsConst, state);			
		}
		//to find most profitable item index
		double max = costBenefitList[0];
		int index = 0;
		for (int i = 0; i < costBenefitList.length; i++) {
			if (max < costBenefitList[i]) 
			{
				max = costBenefitList[i];
				index = i;
			}
		}
		return jobList.get(index);
	}
	
	
	
	/**
	 * 
	 * @param pop
	 * @param state
	 * @param jobList
	 * @param stupidity is used to add to by how much a pop will work an inefficent job. stupidity of 1 gives an efficency lessening effect of
	 * exactly STUPIDITY_EFFECT_CONSTANT, while zero stupidity gives same result as "choseEfficentJob()".
	 * @return
	 */
	public AbstractJob choseLessEfficentJob(Pop pop, State state, List<AbstractJob>  jobList, double stupidity) {
		//System.out.println(jobList.toString());
		double[] costBenefitList = new double[jobList.size()]; //{0,0,0}; //steel, chiars, clothes.
		
		for(int i = 0; i < jobList.size(); i++) {
			costBenefitList[i] = costBenefit( jobList.get(i).inAmounts, jobList.get(i).inGoodsConst, jobList.get(i).outAmounts, jobList.get(i).outGoodsConst, state);			
		}
		
		//obscure results
		for(int i = 0; i < jobList.size(); i++) {
			double randDouble = Math.pow(Math.random() * Constants.STUPIDITY_EFFECT_CONSTANT, stupidity);
			costBenefitList[i] = costBenefitList[i] * randDouble;			
		}
		
		//to find "most" profitable item index
		double max = costBenefitList[0];
		int index = 0;
		for (int i = 0; i < costBenefitList.length; i++) {
			if (max < costBenefitList[i]) 
			{
				max = costBenefitList[i];
				index = i;
			}
		}
		//System.out.println(index);
		//System.out.println(pop.population+" PEOPLE MAKE: "+jobList.get(index).toString());
		
		return jobList.get(index);
	}
	
	public AbstractJob choseRandomJob(Pop pop, State state, List<AbstractJob>  jobList) {
		int index = (int)Math.random() * jobList.size() + 1;
		if(index > jobList.size()) {
			index = jobList.size();
		}
		return jobList.get(index);
	}
	
}





