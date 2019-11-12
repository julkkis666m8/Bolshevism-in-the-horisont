package world;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import constants.Constants;
import goods.AbstractGood;
import goods.Iron;
import goods.Steel;
import goods.Wheat;
import market.AbstractMarket;
import market.NationalMarket;

public class PopSellHandler {

	public static double sell(Pop pop, List<AbstractGood> goods, AbstractMarket market) {
		
		double money = 0;
		
		for (int i = 0; i < goods.size(); i++) {
			AbstractGood good = goods.get(i);
			double amount = good.getAmount();
			money += good.sellGood(amount, market);
			
		}

		return money;
		
	}
	
	

	public static double[] buy(Pop pop, double[] buyTheseNeeds, double totalMoney, AbstractMarket market) {
		
		double money = totalMoney;
		
		for(int goodConst = 0; goodConst < Constants.AMOUNT_OF_GOODS; goodConst++) {
			if (money > 0) {
			
				List<AbstractGood> goods = market.getGood(goodConst, buyTheseNeeds[goodConst]);
				
				try {
					for(AbstractGood good : goods) {
						if (money > 0 && good.getAmount() > 0) {
							
							double adjustedAmount = buyTheseNeeds[goodConst];
							
							if(adjustedAmount > good.getAmount()) {
								adjustedAmount = good.getAmount();
							}
							
							double goodPrice = good.getValue(adjustedAmount);
							
							if(goodPrice < money) {
								money -= goodPrice;
								buyTheseNeeds[goodConst] -= adjustedAmount;
								good.removeAmount(adjustedAmount);
							}
							else {
								buyTheseNeeds[goodConst] -= good.buyForMoney(money);
								money = 0;
							}
						}
					}
				}
				catch(NullPointerException e) {
					System.out.println(e);
				}	
			}
		}
		pop.addJustSpent(totalMoney - money);
		pop.returnTotalCash(money);
		return buyTheseNeeds;
	}
	

	
	
	
	
	
	
	
	/**
	 * initially makes only iron/steel. TODO: make better
	 * @param pop
	 * @param state
	 * @return
	 */
	public static List<AbstractGood> labourerJob(Pop pop, State state) {
		
		
		List<AbstractGood> goods = new LinkedList<AbstractGood>();
		
		int population = pop.getPopulation();
		
		
		//efficency calculator
		double amount = population * 0.01;
		
		goods.add(new Iron(amount, state));
		
		
		
		return goods;
	}


	public static List<AbstractGood> farmerJob(Pop pop, State state) {
		
		List<AbstractGood> goods = new LinkedList<AbstractGood>();
		
		int population = pop.getPopulation();
		
		
		//efficency calculator
		double amount = population * 0.1;
		
		goods.add(new Wheat(amount, state));
		
		
		
		return goods;
	}



	public static List<AbstractGood> artesanJob(Pop pop, State state) {

		
		List<AbstractGood> goods = new LinkedList<AbstractGood>();
		
		int population = pop.getPopulation();
		
		
		//efficency calculator
		double amount = population * 0.001;
		
		List<AbstractGood> buyGoods = state.localMarket.getGood(constants.Constants.IRON, amount);
		
		double adjustedAmountBougth = 0;;
		
		for(AbstractGood good : buyGoods) {
			double bougth = good.buyForMaxMoney(amount, pop.totalCash());
			
			Pop.takeMoney(pop.getSelfList(), good.getValue(bougth));
			
			adjustedAmountBougth += bougth;
		}

		
		double adjustedAmount =0;
				 
				 
		goods.add(new Steel(adjustedAmount, state));
		
		
		
		return goods;
	}
	
	
}
