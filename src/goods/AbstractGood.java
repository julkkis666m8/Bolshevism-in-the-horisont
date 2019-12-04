package goods;

import constants.Functions;
import market.AbstractMarket;
import world.State;

public abstract class AbstractGood {

	public double baseValue = 1;
	public double valueMultiplyer = 1;
	public static double MAX_PRICE = 100000;
	public static double MIN_PRICE = 0.000001;
	public State originState;
	private double amount;
	protected String goodName;
	protected int constant = -1;
	private int daysOnNeg = 0;
	private int daysOnPos = 0;
	
	
	public AbstractGood(double amount, State originState) {
		this.amount = amount;
		this.originState = originState;
	}
	
	
	@Override
	public String toString() {
		return "\n---"+goodName+": "+Functions.formatNum(getValue(1))+"£ price, "+Functions.formatNum(amount)+" units, multiplyer: "+Functions.formatNum(valueMultiplyer);
	}

	public double getValue(double amount) {
		//setValueMultiplyer(valueMultiplyer);
		return amount*(baseValue*valueMultiplyer);
	}
	
	public double sellGood(double amount, AbstractMarket market) {
		
		return market.add(this,amount);
		
		
	}
	
	public void calculateAviliability() {
		if (amount < 1) {
			daysOnPos = 0;
			daysOnNeg++;
			setValueMultiplyer(valueMultiplyer+(valueMultiplyer*0.001/**(daysOnNeg*0.001)*/));
			
		}
		else {
			daysOnNeg = 0;
			daysOnPos++;
			setValueMultiplyer(valueMultiplyer-(valueMultiplyer*0.001/**(daysOnPos*0.001)*/));
			
		}
	}
	
	
//	private AbstractGood(AbstractMarket targetMarket, State originState, double amount) {
//		
//		
//		
//		if (targetMarket.hasThis(this)) {
//			targetMarket.
//		}
//		
//		
//	}
	
	/**
	 * used to get new price with maximum and minimum prices in mind
	 * @param newMultiplyer
	 */
	private void setValueMultiplyer(double newMultiplyer) {
		//System.out.println(getName()+": "+newMultiplyer);
		if((baseValue*newMultiplyer) > MAX_PRICE) {
			valueMultiplyer = (MAX_PRICE / baseValue);
			return;
		}
		if((baseValue*newMultiplyer) < MIN_PRICE) {
			valueMultiplyer = (MIN_PRICE / baseValue);
			return;
		}
		
		valueMultiplyer = newMultiplyer;
	}
	
	
	public double getAmount() {
		return amount;
	}
	public String getName() {
		return goodName;
	}

	/**
	 * if good's name and origin state match, then say true.
	 * @param g
	 * @return
	 */
	public boolean compare(AbstractGood g) {



		if (this.goodName.equals(g.goodName) /*&& this.originState.equals(g.originState)*/) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * to compare two goods if it is the same good.
	 * @param constant
	 * @return
	 */
	public boolean isGoodToBuy(int constant) {
		if(this.constant == constant) {
			return true;	
		}
		return false;
	}
	
	

	public void addAmount(double amount) {
		//setValueMultiplyer(valueMultiplyer+(amount/this.amount));
		this.amount += amount;
	}


	public void removeAmount(double amount) {
		//setValueMultiplyer(valueMultiplyer-(amount/this.amount));
		this.amount -= amount;
		if(amount < 0) {
			amount = 0;
		}
	}


	public double buyForMoney(double money) {
		
		double canAfordAmount = money/getValue(1);
		removeAmount(canAfordAmount);

		return canAfordAmount;
	}
	public double buyForMaxMoney(double amount, double money) {
		
		
		double canAfordAmount = money/getValue(1);
		
		if(canAfordAmount > amount) {

			if(amount > this.amount) {
				amount = this.amount;
			}
			canAfordAmount = amount;
		}
		
		removeAmount(canAfordAmount);

		return canAfordAmount;
	}
	
	
	
	
}
