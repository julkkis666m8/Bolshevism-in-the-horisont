package nationalEconomyManagers;

import java.util.List;

import constants.Constants;
import world.Nation;
import world.Pop;

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


	private void paySoldiers() {
		// TODO Auto-generated method stub
		
	}


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
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
