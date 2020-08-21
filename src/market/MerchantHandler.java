package market;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import constants.Constants;
import goods.AbstractGood;
import world.Nation;
import world.Pop;
import world.PopSellHandler;
import world.State;

public class MerchantHandler {

	public static double wrangle(State OGstate, Pop pop, Nation nation) {
		
		//return 1; //Did this to make warangeling work for now. gonna implement it in the goods themselves.
		
		List<State> neigbours = OGstate.getNeigbours();
		
		double income = 0;

		
		double[] needs = OGstate.localMarket.getNeeds();
		
		boolean nothing = false;
		int times = 0;
		for(int goodConst = 0; goodConst < needs.length; goodConst++) {
			
			double needed = needs[goodConst];
			if (needed > 0) {
				System.out.println(needed);
				nothing = true;
				times++;
				income =+ costBenefitAnalysis(OGstate, neigbours, nation, goodConst, needed, pop);
			}
			
		}
				
		if(!nothing) {
			System.out.println("nothing to warangel");
		}else {
			System.out.println(times+" times");
		}
		System.out.println("merchant warangels for " + income);

		return income;
	}

	private static double costBenefitAnalysis(State OGstate, List<State> neigbours, Nation nation, int goodConst, double needed, Pop pop) {

		double income = 0;

		
		List<State> deals = new LinkedList<State>();
		
		for (State neigbour : neigbours) {
			double importPrice = neigbour.localMarket.getGoodMinPrice(goodConst, needed);
			double localPrice = OGstate.localMarket.getGoodMaxPrice(goodConst, needed);
			double profit = importPrice - localPrice;
			if (profit <= 0) {
				if(neigbour.localMarket.goodTotalAmount(goodConst) > 0) {
					deals.add(neigbour);	
				}
			}
		}
		
		//sort everything in "deals"
		Collections.sort(deals, new Comparator<State>() {
			public int compare(State o1, State o2) {
				return o1.localMarket.getGoodMinPrice(goodConst, needed)
						> o2.localMarket.getGoodMinPrice(goodConst, needed)
						? -1 : o1.localMarket.getGoodMinPrice(goodConst, needed)
						== o2.localMarket.getGoodMinPrice(goodConst, needed)
						? 0 : 1;
			}
		});
		
		//List<AbstractGood> wrangelings = new LinkedList<>();

		double[] neededArray = new double[Constants.AMOUNT_OF_GOODS];
		neededArray[goodConst] = needed;
		double neededITEM_TEMP = needed;
		
		System.out.println("fag"+needed);
		
		for(State deal : deals) {
			System.out.println("deal "+deal.localMarket.getAllOfGood(goodConst).toString());
			System.out.println("neededArray"+neededArray[goodConst]);
			neededArray = PopSellHandler.buy(pop, neededArray, 100/*pop.takeTotalCash()*/, deal.localMarket); //deals.get(i).localMarket. //TODO: i think this method is bad, and should be fixed
			System.out.println(neededArray[goodConst]);
			System.out.println("BUY? "+(neededArray[goodConst] - neededITEM_TEMP)+ " " + neededITEM_TEMP);
			
			List<AbstractGood>goods = deal.localMarket.getGood(goodConst, needed);
			
			for (AbstractGood good : goods) {	
				income += PopSellHandler.trade(good, OGstate.localMarket);
			}
			
			
			if (neededArray[goodConst] <= 0) {
				System.out.println("BREAK");
				//break;  
			}
		}
		
		//System.out.println("GOODS"+pop.getGoods().toString());
		//income += PopSellHandler.sell(pop, OGstate.localMarket, nation);
		
		
		
		
		
		
		return income;

		
	}

}
