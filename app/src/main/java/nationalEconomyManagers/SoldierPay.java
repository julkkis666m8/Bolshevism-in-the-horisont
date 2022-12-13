package nationalEconomyManagers;

import java.util.List;

import constants.Constants;
import lombok.Getter;
import lombok.Setter;
import world.Nation;
import world.Pop;


/**
 * should be used as a sort of ministery of soldier wages
 * @author julkkis666
 *
 */
public class SoldierPay {
	
	
	@Getter
	@Setter
	private double perTickBudget = 0;

	private double budget = 0;
	
	private int soldierPop;
	private Nation nation;
	

	public SoldierPay(double totalBudget, Nation nation) {
		this.nation = nation;
		this.perTickBudget = totalBudget;
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


	public double paySoldier(int population) {
		
		double pay = 0;
		
		pay = giveMoneyToSoldier(population);
		
		return pay;
	}
	
	


	//TODO: make multithreadable by splitting budgets per state
	private synchronized double giveMoneyToSoldier(int population) {

		double pay = 0;
		
		double toBePayed = payPerSoldier(population);

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
		
		double pay = (perTickBudget / soldierPop) * population;
		
		return pay;
		
		
	}
	
	

}
