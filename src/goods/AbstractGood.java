package goods;

import market.AbstractMarket;
import world.State;

public abstract class AbstractGood {

	public static final double baseValue = 20;
	public double valueMultiplyer = 1;
	public static double MAX_PRICE = 1000000000;
	public static double MIN_PRICE = 0.01;
	public State originState;
	private double amount;
	protected String goodName;
	protected int constant = -1;
	
	
	public AbstractGood(double amount, State originState) {
		this.amount = amount;
		this.originState = originState;
	}
	
	
	@Override
	public String toString() {
		return goodName+": "+getValue(1)+" * "+amount;
	}
	
	
	public double sellGood(double amount, AbstractMarket market) {
		
		return market.add(this,amount);
		
		
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
	
	private void setValueMultiplyer(double newMultiplyer) {
		System.out.println(getName()+": "+newMultiplyer);
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
	
	public double getValue(double amount) {
		setValueMultiplyer(valueMultiplyer);
		return amount*(baseValue*valueMultiplyer);
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
		setValueMultiplyer(valueMultiplyer+(amount/this.amount));
		this.amount += amount;
	}


	public void removeAmount(double amount) {
		setValueMultiplyer(valueMultiplyer-(amount/this.amount));
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
