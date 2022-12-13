package nationalEconomyManagers;

import java.util.List;

import constants.Constants;
import world.Nation;
import world.Pop;


/**
 * should be used as a sort of ministery of soldier wages
 * @author julkkis666
 *
 */
public class SoldierPay {


	private static final int OFFICER_WAGE_MULTIPLIER = 2;
	private double perTickBudget = 0;

	private double budget = 0;
	
	private int soldierPop;
	private int officerPop;
	private int totalMilitaryPop;

	private Nation nation;
	

	public SoldierPay(double totalBudget, Nation nation) {
		this.nation = nation;
		this.perTickBudget = totalBudget;
		budget = 0;//totalBudget;
	}

	public double getPerTickBudget() {
		return perTickBudget;
	}

	public void setPerTickBudget(double perTickBudget) {
		this.perTickBudget = perTickBudget;
	}

	public void tick() {
		//pay is handeled per pop
		
		getSoldierPay();
		
		updateSoldierPop();
	}

	private void updateSoldierPop() {

		List<Pop> soldierPops = nation.getJob(Constants.SOLDIER);
		List<Pop> officerPops = nation.getJob(Constants.OFFICER);

		soldierPop = 0;
		for (Pop pop : soldierPops) {
			soldierPop = soldierPop + pop.population;
		}
		officerPop = 0;
		for (Pop pop : officerPops) {
			officerPop = officerPop + pop.population;
		}

		totalMilitaryPop = soldierPop + (officerPop * OFFICER_WAGE_MULTIPLIER);
		
	}

	private void getSoldierPay() {
		
		budget += nation.getNationCash(perTickBudget);
		
	}

	public double getTotalBudget() {
		return perTickBudget;
	}

	public void setTotalBudget(double totalBudget) {
		this.perTickBudget = totalBudget;
	}


	public double paySoldier(int population, boolean isOfficer) {
		
		double pay = 0;


		double toBePayed = payPerSoldier(population);

		if(isOfficer) {
			toBePayed = toBePayed * OFFICER_WAGE_MULTIPLIER;
		}

		if(budget > toBePayed) {
			this.budget = budget - toBePayed;
			pay = toBePayed;
		}
		else {
			pay = budget;
			budget = 0;
		}
		
		return pay;
	}

	private double payPerSoldier(int population) {
		double pay = (perTickBudget / totalMilitaryPop) * population;
		return pay;
	}
	
	

}
