package goods;

import constants.Constants;
import constants.Functions;
import market.AbstractMarket;
import world.State;

import static java.util.Objects.nonNull;

public abstract class AbstractGood {

	private static final double ROTTING_COEFICENT = 0.35;
	public double baseValue = 5;
	public double lastValue = 5;
	public double valueMultiplyer = 1;
	public double sumModifier = 0;
	public double MAX_PRICE = 1000;
	public double MIN_PRICE = 1;
	public double NON_PRICE = 0.99; //must be smaller than MIN_PRICE
	public State originState;
	private double amount;
	public String goodName;
	protected int constant = -1;
	private double supply = 1;
	private double demand = 1;

	public int getConstant() {
		return constant;
	}
	private double daysOnNeg = 0;
	private double daysOnPos = 0;
	private double maxDaysOnNeg = 10;
	private double maxDaysOnPos = 10;
	
	
	
	
	public AbstractGood(double amount, State originState) {
		this.amount = amount;
		//this.originState = nonNull(originState) ? originState : new State(); //fake state when origal good is traded
		try{
			if(originState != null){
				this.originState = originState;
			}
		}catch(NullPointerException e){
			this.originState = new State(); //fake state when origal good is traded
		}
	}

	public void marketPriceAdder(double factor) { //factor is legacy/oldtest
		daysOnPos = 0;
		daysOnNeg++;
	}
	public void marketPriceLowerer(double factor) {//factor is legacy/oldtest
		daysOnNeg = 0;
		daysOnPos++;
	}
	
	/**
	 * constructor that gives specific item according to it's constant
	 * @param amount
	 * @param originState
	 * @param constant
	 */
	public AbstractGood getAbstractGoodOfConst(double amount, State originState, int constant) {
		return Constants.getGood(amount, originState, constant);
	}
	
	
	@Override
	public String toString() {
		return "\n---"+goodName+": "+Functions.formatNum(getValue(1))+"£ price, "
				+Functions.formatNum(amount)+" units";
	}

	public double getValue(double amount) {
		//setValueMultiplyer(valueMultiplyer);
		//return amount*(baseValue*valueMultiplyer);
		return amount* (baseValue * (demand/supply));//(baseValue + sumModifier )/**valueMultiplyer*/;
	}
	
	public double sellGood(double amount, AbstractMarket market) {
		
		market.modMarketNeed(amount*(-1), this.getConstant());
		
		return market.add(getAbstractGoodOfConst(amount, this.originState, this.getConstant()),amount);
		
		
	}
	
	public void calculateAviliability() {
		if (daysOnPos == 0) {
			//daysOnPos = 0;
			//daysOnNeg++;
			if (getValue(1) < baseValue) {
				//setValueMultiplyer(valueMultiplyer+(/*valueMultiplyer*0.01**/(daysOnNeg*0.0001)));
			}
			setValueSumModifier(sumModifier+(0.1/*daysOnNeg*/));
			
		}
		else {
			//daysOnNeg = 0;
			//daysOnPos++;
			if (getValue(1) > baseValue) {
				//setValueMultiplyer(valueMultiplyer-(/*valueMultiplyer*0.01**/(daysOnPos*0.0001)));
			}
			if(amount > 1) {
				setValueSumModifier(sumModifier-(0.1/*daysOnPos*/));
			}
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
	
	private void setValueSumModifier(double newModifier) {
		//System.out.println(getName()+": "+newMultiplyer);
		if((baseValue+sumModifier) >= MAX_PRICE) {
			sumModifier = (MAX_PRICE - baseValue);
			return;
		}
		if((baseValue+newModifier) <= MIN_PRICE) {
			sumModifier = (MIN_PRICE - baseValue);
			return;
		}
		
		sumModifier = newModifier;
	}

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
			valueMultiplyer = (NON_PRICE / baseValue);
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

		/*
		if(this.originState.equals(obj)) {
			return true;
		}*/


		if (this.goodName.equals(g.goodName)/* && this.originState.equals(g.originState)*/) {
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
		//this.supply += amount;
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

	/**
	 * TODO: make a rotting value for each item?
	 */
	public void rot() {
		amount = amount * ROTTING_COEFICENT;
	}
	public void rot(double coeficient) {
		amount = amount * coeficient;
	}

	public void setAmount(double d) {
		amount = d;
		
	}

	public void tick() {
		//rot();
		
	}

	
	/**
	 * TODO: works?
	 * @param newPrice
	 */
	public void setValue(double newPrice) {
		
		double curValue = this.getValue(1);
		
		double takeThis = curValue - newPrice;
		
		setValueSumModifier(sumModifier - (takeThis));
		
	}


	public void setSupplyAndDemand(double demand, double supply) {

		//lastValue = getValue(1); //TODO: continue here

		this.demand = demand > 1 ? demand : 1;
		this.supply = supply > 1 ? supply : 1;
	}

	//public void setSupply(double supply) {
	//	this.supply = supply;
	//}
}
