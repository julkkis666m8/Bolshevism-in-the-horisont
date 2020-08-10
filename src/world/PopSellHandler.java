package world;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import constants.Constants;
import goods.AbstractGood;
import goods.Cotton;
import goods.Iron;
import goods.Steel;
import goods.Timber;
import goods.Wheat;
import market.AbstractMarket;

public class PopSellHandler {

	
	
	public synchronized static double sell(Pop pop, AbstractMarket market, Nation nation) {
		
		
		List<AbstractGood> goods = pop.getGoods();
		
		double money = 0;
		/*
		for (int i = 0; i < goods.size(); i++) {
			AbstractGood good = goods.get(i);
			
			double amount = good.getAmount();
			
			
			money += good.sellGood(amount, market);
			//System.out.println(good.getAmount());
		
		}*/
		
		
		for(AbstractGood good : goods) {
			
			if(true/*market.getMarketNeed(good.getConstant()) > 0*/) {
				//if (market.getMarketNeed(good.getConstant()) > good.getAmount()) {
				money = sellAllButNeeds(good, market, pop);	
				
				
				
				//}
				//else {
				//	money = sellXButNeeds(good, market, pop, market.getMarketNeed(good.getConstant()));
				//}		
			}
			else {
				//System.out.println(good.toString());
			}
		}
		
		
		
		
		
		//nation.coffers -= money; //TODO: TEMPORARY TEST WHATEVER

		return money;
		
	}


	private static double sellXButNeeds(AbstractGood good, AbstractMarket market, Pop pop, double x) {
		double money = 0;
		double amount = x;
		amount = amount - pop.getNeeds()[good.getConstant()];
		
		if (amount < 0) {
			amount = 0;
		}
		
		money = money += good.sellGood(amount, market);
		good.setAmount(good.getAmount() - amount);
		
		return money;
	}


	private static double sellAllButNeeds(AbstractGood good, AbstractMarket market, Pop pop) {
		double money = 0;
		double amount = good.getAmount();
		amount = amount - pop.getNeeds()[good.getConstant()];
		
		if (amount < 0) {
			amount = 0;
		}
		
		money = money += good.sellGood(amount, market);
		good.setAmount(good.getAmount() - amount);
		
		return money;
	}


	public static double[] buyFromSelf(Pop pop, double[] buyTheseNeeds) {

		for(int goodConst = 0; goodConst < Constants.AMOUNT_OF_GOODS; goodConst++) {

			List<AbstractGood> goods = pop.getGoods();

			try {
				for(AbstractGood good : goods) {
					if (good.getAmount() > 0) {

						double adjustedAmount = buyTheseNeeds[goodConst];

						if(adjustedAmount > good.getAmount()) {
							adjustedAmount = good.getAmount();
						}
						buyTheseNeeds[goodConst] -= adjustedAmount;
						good.removeAmount(adjustedAmount);
					}
				}
			}
			catch(NullPointerException e) {
				System.out.println(e);
			}	
		}
		return buyTheseNeeds;
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
							//System.out.println("adjustedAmount "+adjustedAmount);
							if(goodPrice < money) {
								money -= goodPrice;
								//System.out.println("goodConst b4"+buyTheseNeeds[goodConst]);
								buyTheseNeeds[goodConst] -= adjustedAmount;
								//System.out.println("after"+buyTheseNeeds[goodConst]);
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
					JOptionPane.showMessageDialog(null, e);
				}	
			}
		}
		pop.addJustSpent(totalMoney - money);
		pop.returnTotalCash(money);
		return buyTheseNeeds;
	}
	

	//TODO: FINNISH
	public double trade(AbstractGood good, AbstractMarket target) {
		double price = 0;
		
		
		return price;
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
		double amount = population * 0.025;
		
		goods.add(new Iron(amount, state));
		goods.add(new Timber(amount, state));
		
		
		
		return goods;
	}


	public static List<AbstractGood> farmerJob(Pop pop, State state) {
		
		List<AbstractGood> goods = new LinkedList<AbstractGood>();
		
		int population = pop.getPopulation();
		
		double aristocratModifier = 1 + state.jobPercentage(Constants.ARISTOCRAT);
		
		//efficency calculator
		double amount = population * 0.025 * aristocratModifier * state.getFertility();

		goods.add(new Wheat(amount, state));
		goods.add(new Cotton(amount, state));
		
		
		
		return goods;
	}


	public static List<AbstractGood> serfJob(Pop pop, State state) {
		
		List<AbstractGood> goods = new LinkedList<AbstractGood>();
		
		int population = pop.getPopulation();
		
		
		//efficency calculator
		double amount = population * 0.025 * state.getFertility();

		goods.add(new Wheat(amount*2, state));
		//goods.add(new Cotton(amount, state));
		
		
		
		return goods;
	}



	
	/**
	 * OLD LEGACY NOT IN USE. SEE ARTESAN JOB CLASS
	 * @param pop
	 * @param state
	 * @return
	 */
	public static List<AbstractGood> artesanJob(Pop pop, State state) {

		
		List<AbstractGood> goods = new LinkedList<AbstractGood>();
		
		int population = pop.getPopulation();
		
		
		//efficency calculator
		double amount = population * 0.01;
		
		List<AbstractGood> buyGoods = state.localMarket.getGood(constants.Constants.IRON, amount);
		
		double adjustedAmountBougth = 0;
		
		for(AbstractGood good : buyGoods) {
			double bougth = good.buyForMaxMoney(amount, pop.totalCash());
			
			Pop.takeMoney(pop.getSelfList(), good.getValue(bougth));
			
			adjustedAmountBougth += bougth;
		}

		
		double EFFICENCY = 0.5;
		
		double adjustedAmount = EFFICENCY * adjustedAmountBougth;
				 
				 
		goods.add(new Steel(adjustedAmount, state));
		
		
		
		return goods;
	}


	
	
}
