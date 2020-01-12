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
	
	
	
	private double totalBudget;
	
	private int soldierPop;
	private Nation nation;
	

	public SoldierPay(double totalBudget, Nation nation) {
		this.totalBudget = totalBudget;
		this.nation = nation;
	}
	
	
	public void tick() {
		//pay is handeled per pop
		
		getSoldierPay();
		
		updateSoldierPop();
		
		
	}
	

	private void updateSoldierPop() {

		List<Pop> soldierPops = nation.getJob(Constants.SOLDIER);
		
		
		
	}


	/**
	 * used once a tick to get how much a state wants to pay to its soliders
	 */
	private void getSoldierPay() {
		// TODO Auto-generated method stub
		
	}


	public double getTotalBudget() {
		return totalBudget;
	}

	public void setTotalBudget(double totalBudget) {
		this.totalBudget = totalBudget;
	}


	public double paySoldier(int population) {
		
		double pay = 0;
		
		pay = nation.getTotalMoney();
		
		return pay;
	}
	
	

}
