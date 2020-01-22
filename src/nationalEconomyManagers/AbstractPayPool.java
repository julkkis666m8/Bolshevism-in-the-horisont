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
	

	
	private double totalBudgetOrigin = 0;
	private double budget = 0;
	
	private int aristocratPop = 0;
	private Nation nation;
	private State state;
	

	
	public AbstractPayPool(Nation nation, State state) {
		//this.totalBudgetOrigin = totalBudget;
		this.nation = nation;
		this.state = state;
	}
	
	
	
	
	public void tick() {
		//pay is handeled per pop
		
		totalBudgetOrigin = budget;
		updateAristocratPop();	
	}
	
	public void giveMoneyToAristocrats(double money) {
		budget += money;
	}
	

	private void updateAristocratPop() {

		
		List<Pop> aristocratPops = state.getJob(Constants.ARISTOCRAT);
		aristocratPop = 0;
		
		for (Pop persons : aristocratPops) {
			aristocratPop += persons.population;
		}
	}


	public double getTotalBudget() {
		return totalBudgetOrigin;
	}

	public void setTotalBudget(double totalBudget) {
		this.totalBudgetOrigin = totalBudget;
	}


	public double payAristocrat(int population) {
		
		double pay = 0;
		
		pay = giveMoneyToAristocrat(population);
		
		return pay;
	}
	
	


	//TODO: make multithreadable by splitting budgets per state
	private synchronized double giveMoneyToAristocrat(int population) {

		double pay = 0;
		
		double toBePayed = payPerAristocrat(population);

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


	private double payPerAristocrat(int population) {
		
		double pay = (totalBudgetOrigin / aristocratPop) * population;
		
		return pay;
		
		
	}
	
	

}
