package factories;

import java.util.ArrayList;
import java.util.List;

import constants.Constants;
import goods.AbstractGood;
import world.Pop;
import world.State;

public class AbstractJobDoer {

	public List<AbstractGood> doJob(Pop pop, State state, AbstractJob job) {

		int population = pop.getPopulation();

		List<AbstractGood> goodList = new ArrayList<AbstractGood>();
		
		double inputInEfficency = 1;
		double outputEfficency = 0.9;

		double adjustedAmountBougth = 0;
		double amountNeeded = 0;


		for(int i = 0; i < job.inGoodsConst.length; i++) { //for each item needed


			double amount = population * job.inAmounts[i];
			amountNeeded += amount;
			List<AbstractGood> buyGoods = state.localMarket.getGood(constants.Constants.IRON, job.inAmounts[i]);

			for(AbstractGood good : buyGoods) {
				
				if(amount > 0) {
					double bougth = good.buyForMaxMoney(amount, pop.totalCash());

					//System.out.println("BROUGTH: "+bougth+", amount: "+amount+",population: "+population);

					Pop.takeMoney(pop.getSelfList(), good.getValue(bougth));

					adjustedAmountBougth += bougth;
				}
			}
		}

		double inputPercentage;
		if(amountNeeded > 0) {
			inputPercentage = adjustedAmountBougth / amountNeeded;
		}else {
			inputPercentage = 1; //incase no input is needed.
		}
		//System.out.println("INPERC:"+inputPercentage);


		for(int i = 0; i < job.outGoodsConst.length; i++) { //for each item made

			double adjustedAmount = population * job.outAmounts[i] * outputEfficency * inputPercentage;

			goodList.add(Constants.getGood(adjustedAmount, state, job.outGoodsConst[i]));
		}

		pop.addGoods(goodList);
		
		return goodList;
		
	}
	
	
}
