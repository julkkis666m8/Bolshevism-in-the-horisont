package market;
import world.Nation;
import world.Pop;

public class Taxes {

	
	/**
	 * tax pop -> nation.
	 * @param pop
	 * @param nation
	 */
	public static void taxMe(Pop pop, Nation nation) {
		
		double taxPercentage = nation.taxPercentage;
		int population = pop.population;
		//double averageWealth = pop.getAverageWealth();
		double taxEvasion = getTaxEvasion(pop, getTaxEvasionReducer(nation.taxEfficency));
		
		
		
		double toTax = taxPercentage * nation.taxEfficency * taxEvasion;
		
		
		
		//double taxMoney = (averageWealth*population)*toTax;
		
		double taxMoney = pop.getIncomeTaxable() * toTax;
		
		
		nation.addToCoffers(pop.pay(taxMoney));
	}
	
	
	
	
	
	private static double getTaxEvasion(Pop pop, double evasionReducer){
		
		double taxEvasion = 1-pop.getTaxEvasion() - evasionReducer;
		
		if (taxEvasion <= 0) {
			return 0;
		}
		else if(taxEvasion >= 1) {
			return 1;
		}
		else {
			return taxEvasion;
		}
	}
	
	private static double getTaxEvasionReducer(double taxEfficency) {
		double reducer = taxEfficency - 1;
		return reducer;
	}
	
}
