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
		double remainingCapacity = merchantCapacity(pop, OGstate);
		
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
				if (remainingCapacity <= 0) break;
				DropshipResult result = costBenefitAnalysis(OGstate, neigbours, nation,
						goodConst, Math.min(needed, remainingCapacity), pop);
				income += result.income;
				remainingCapacity -= result.amount;
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

	private static DropshipResult costBenefitAnalysis(State OGstate, List<State> neigbours, Nation nation, int goodConst, double needed, Pop pop) {

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
				if (pop.totalCash() < qty * unitPrice) {
					double reserved = listing.reserveForDropshipping(qty);
					if (reserved <= 0) continue;
					AbstractGood brokered = Constants.getGood(reserved, listing.originState, goodConst);
					brokered.setCurrentPrice(unitPrice);
					income += PopSellHandler.dropship(brokered, OGstate.localMarket, pop, listing, unitPrice);
					neededRemaining -= reserved;
					continue;
				}
				qty = Math.min(qty, pop.totalCash() / unitPrice);
				if (qty <= 0) continue;

					pop.pay(qty * unitPrice);
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

					double profit = PopSellHandler.dropship(bought, OGstate.localMarket, pop,
						listing, unitPrice, true);
				if (OGstate.isForigen(bought.originState)) {
					profit = nation.payTarrif(profit);
				}
				income += profit;
				neededRemaining -= qty;
			}
		}

		return new DropshipResult(income, needed - neededRemaining);

	}

	private static double merchantCapacity(Pop pop, State state) {
		// TODO: Add infrastructure, roads, ports, and state logistics modifiers.
		// TODO: Add merchant skills, transport technology, and transport costs.
		// TODO: Replace the linear population scaling with the state's eventual
		// infrastructure capacity once that system exists.
		return Constants.MERCHANT_DROPSHIPPING_CAPACITY_PER_TURN * pop.getPopulation();
	}

	private static class DropshipResult {
		private final double income;
		private final double amount;

		private DropshipResult(double income, double amount) {
			this.income = income;
			this.amount = amount;
		}
	}

}
