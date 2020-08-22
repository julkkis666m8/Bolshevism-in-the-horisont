package nationalEconomyManagers;

import java.util.List;

import constants.Constants;
import world.Nation;
import world.Pop;
import world.State;


/**
 * copied from SoldierPay
 * @author julkkis666
 *
 */
public abstract class AbstractPayPool {
	

	
	protected double totalBudgetOrigin = 0;
	protected double budget = 0;
	
	protected int targetPop = 0;
	protected Nation nation;
	protected State state;
	

	
	public AbstractPayPool(Nation nation, State state) {
		//this.totalBudgetOrigin = totalBudget;
		this.nation = nation;
		this.state = state;
	}
	
	private Nation getNation() {
		this.nation = state.nation;
		return nation;
	}
	
	
	
	public void tick() {
		//pay is handeled per pop
		
		totalBudgetOrigin = budget;
		updateTargetPop();	
	}
	
	public void giveMoneyToAristocrats(double money) {
		budget += money;
	}
	

	abstract void updateTargetPop();
	/*
	List<Pop> targetPops = state.getJob(Constants.ARISTOCRAT);
	targetPop = 0;
	
	for (Pop persons : targetPops) {
		targetPop += persons.population;
	}*/

	public double getTotalBudget() {
		return totalBudgetOrigin;
	}

	public void setTotalBudget(double totalBudget) {
		this.totalBudgetOrigin = totalBudget;
	}


	public double payTarget(int population) {
		
		double pay = 0;
		
		pay = giveMoneyToTarget(population);
		
		return pay;
	}
	
	


	//TODO: make multithreadable by splitting budgets per state
	private synchronized double giveMoneyToTarget(int population) {

		double pay = 0;
		
		double toBePayed = payPerPop(population);

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


	private double payPerPop(int population) {
		
		double pay = (totalBudgetOrigin / targetPop) * population;
		
		return pay;
		
		
	}
	
	

}
