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

		
		double[] needs = OGstate.localMarket.getDemands();
		
		boolean nothing = false;
		int times = 0;
		for(int goodConst = 0; goodConst < needs.length; goodConst++) {
			
			double needed = needs[goodConst];
			if (needed > 0) {
				System.out.println(needed);
				nothing = true;
				times++;
				income += costBenefitAnalysis(OGstate, neigbours, nation, goodConst, needed, pop);
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
		double neededRemaining = needed;

		// First, look for private listings in neighbouring markets (preferred)
		for (State neighbour : neigbours) {
			if (neededRemaining <= 0) break;
			List<AbstractGood> available = new LinkedList<>(neighbour.localMarket.getAllOfGood(goodConst));
			// sort available listings by unit price ascending so cheapest first
			Collections.sort(available, new Comparator<AbstractGood>() {
				public int compare(AbstractGood a, AbstractGood b) {
					double pa = a.getValue(1);
					double pb = b.getValue(1);
					return Double.compare(pa, pb);
				}
			});

			for (AbstractGood aGood : available) {
				if (neededRemaining <= 0) break;
				if (!(aGood instanceof Listing)) continue; // prefer private listings
				Listing listing = (Listing) aGood;
				double unitPrice = listing.getValue(1);
				double localPrice = OGstate.localMarket.getGoodMaxPrice(goodConst, 1);
				// only trade if there's profit margin
				if (localPrice <= unitPrice) continue;

				double qty = Math.min(neededRemaining, listing.getAmount());
				if (qty <= 0) continue;

				// remove from listing (pays seller)
				try {
					listing.removeAmount(qty);
				} catch (Exception e) {
					continue;
				}

				// build a transferable good representing what was bought and give it the unit price
				AbstractGood bought = Constants.getGood(qty, listing.originState, goodConst);
				try {
					bought.setCurrentPrice(unitPrice);
				} catch (Exception ignored) {}

				double profit = PopSellHandler.trade(bought, OGstate.localMarket);
				if (OGstate.isForigen(bought.originState)) {
					profit = nation.payTarrif(profit);
				}
				income += profit;
				neededRemaining -= qty;
			}
		}

		// If still need goods, fall back to buying from public stockpiles (non-listing goods)
		if (neededRemaining > 0) {
			for (State neighbour : neigbours) {
				if (neededRemaining <= 0) break;
				List<AbstractGood> available = new LinkedList<>(neighbour.localMarket.getAllOfGood(goodConst));
				Collections.sort(available, new Comparator<AbstractGood>() {
					public int compare(AbstractGood a, AbstractGood b) {
						return Double.compare(a.getValue(1), b.getValue(1));
					}
				});
				for (AbstractGood aGood : available) {
					if (neededRemaining <= 0) break;
					if (aGood instanceof Listing) continue; // already handled
					double unitPrice = aGood.getValue(1);
					double localPrice = OGstate.localMarket.getGoodMaxPrice(goodConst, 1);
					if (localPrice <= unitPrice) continue;
					double qty = Math.min(neededRemaining, aGood.getAmount());
					if (qty <= 0) continue;
					// remove from market
					aGood.removeAmount(qty);
					AbstractGood bought = Constants.getGood(qty, aGood.originState, goodConst);
					try { bought.setCurrentPrice(unitPrice); } catch (Exception ignored) {}
					double profit = PopSellHandler.trade(bought, OGstate.localMarket);
					if (OGstate.isForigen(bought.originState)) profit = nation.payTarrif(profit);
					income += profit;
					neededRemaining -= qty;
				}
			}
		}

		return income;

	}

}
