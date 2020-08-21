package market;

import constants.Constants;
import constants.Functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import goods.AbstractGood;
import world.Pop;
import world.State;

public class AbstractMarket {

	protected List<AbstractGood> stockPile;
	private double[] marketNeeds = new double[Constants.AMOUNT_OF_GOODS];
	private List<AbstractMarket> subMarkets = new LinkedList<>();
	
	public AbstractMarket() {
		stockPile = new ArrayList<>();
	}
	

	public String getStockpileString() {
		
		String marketNeedsString = "";
		int i = 0;
		for(double d : getMarketNeeds()) {
			
			marketNeedsString +="\n"+Constants.GoodToString(i)+": "+d;
			i++;
		}
		
		return "size is "+ Functions.formatNum(stockPile.size())+" and string is: "+stockPile.toString() + 
				"\nneeds are"+ marketNeedsString;
	}
	
	public void tick() {
		for(AbstractGood good : stockPile) {
			
			//if(good.originState)
			
			good.tick();
			
		}
	}
	
	
	
	/**
	 * adds good to state and combines if possible. returns profit.
	 * @param newGood
	 * @param amount
	 * @return
	 */
	public double add(AbstractGood newGood, double amount) {
		int i = 0;
		for(AbstractGood g : stockPile) {
			i++;
			//System.out.println("lol "+i);
			if (newGood.compare(g)) {
				//System.out.println("yes");
				g.addAmount(newGood.getAmount());
				//System.out.println(g.getAmount());
				double money = g.getValue(amount);
				//System.out.println(g.getName()+": "+money);
				return money;
			}
		}
		System.out.println("no");
		stockPile.add(newGood);
		double money = newGood.getValue(amount);
		//System.out.println(money);
		return money;
		
	}
	
	public double addAll(List<AbstractGood> newGoods) {
		double money = 0;
		for (AbstractGood newGood : newGoods) {
			money += this.add(newGood, newGood.getAmount());			
		}
		return money;
	}
	
	
	
	public void updateGoods(){
		
		
		for(AbstractGood g : stockPile) {
			
			g.calculateAviliability();
			
		}
		
		
	}

	/**
	 * minimum price for a good on a market
	 * @param goodConst
	 * @param amount
	 * @return
	 */
	public double getGoodMinPrice(int goodConst, double amount) {
		List<AbstractGood> goods = getAllOfGood(goodConst);
		double newValue;
		double minPrice = -1; //prices can't be negative
		//finds average price for good on market
		for (AbstractGood g : goods) {
			if ((newValue = g.getValue(amount)) < minPrice || minPrice == -1) {
				minPrice = newValue; 
			}
		}
		
		//TODO: what if item not in market yet?
		
		if(minPrice != -1) {
		return minPrice;
		}
		else {
			return 9999; //to make artesans make one of each to start the economic calc. 
		}

	}
	
	/**
	 * find total amount of good constant in a market
	 * @return
	 */
	public double goodTotalAmount(int goodConst) {
		double totalAmount = 0;
		
		for(AbstractGood good : stockPile) {
			
			if (good.getConstant() == goodConst) {
				totalAmount += good.getAmount();
			}
			
		}
		
		return totalAmount;
	}
	
	
	/**
	 * maximum price for a good on a market
	 * @param goodConst
	 * @param amount
	 * @return
	 */
	public double getGoodMaxPrice(int goodConst, double amount) {
		List<AbstractGood> goods = getAllOfGood(goodConst);
		double maxPrice = -1; //prices can't be negative
		//finds average price for good on market
		for (AbstractGood g : goods) {
			if ((g.getValue(amount)) > maxPrice) {
				maxPrice = g.getValue(amount); 
			}
		}

		if(maxPrice != -1) {
			return maxPrice;
		}
		else {
			return 9999; //to make artesans make one of each to start the economic calc.
		}
	}


	public List<AbstractGood> getGood(int goodConst, double d) {
		
		List<AbstractGood> goods = new ArrayList<>();
		
		double stillNeeded = d;
		
		for(AbstractGood good : stockPile) {
			if(good.isGoodToBuy(goodConst)) {
				
				stillNeeded -= good.getAmount();
				
				goods.add(good);
				
				if(stillNeeded <= 0) {
					return goods;
				}
			}
		}
		
			//if no more goods in market then look at sub markets
		
		if(stillNeeded > 0) {
			List<AbstractGood> goods2Sort = this.getSubGoodType(goodConst);
			Collections.sort(goods2Sort, new Comparator<AbstractGood>() {
				public int compare(AbstractGood o1, AbstractGood o2) {
					return o1.getValue(1) < o2.getValue(1) ? -1 : o1.getValue(1) == o2.getValue(1) ? 0 : 1;
				}
			});
			
			for(AbstractGood good : goods2Sort) {
				if(good.isGoodToBuy(goodConst)) {
					
					stillNeeded -= good.getAmount();
					
					goods.add(good);
					
					if(stillNeeded <= 0) {
						return goods;
					}
				}
			}
			
		}
		
		
		
		marketNeed(goodConst, stillNeeded);
		return goods;
	}
	
	public List<AbstractGood> getAllOfGood(int goodConst){

		List<AbstractGood> goods = new ArrayList<>();
		for(AbstractGood good : stockPile) {
			if(good.isGoodToBuy(goodConst)) {
				goods.add(good);
			}
		}
		
		
		return goods;
	}


	private void marketNeed(int goodConst, double stillNeeded) {
		getMarketNeeds()[goodConst] += stillNeeded;
		//System.out.println(goodConst +" "+ marketNeeds[goodConst]);
	}
	
	public void resetMarketNeedForTheTurn() {
		int i = 0;
		for (double thisNeed : getMarketNeeds()) {
			
			//to fluidify the economy
			
			if (thisNeed > 0) {
				//JOptionPane.showMessageDialog(null, thisNeed+" IS THIS ZERO?");
				for (AbstractGood good : getAllOfGood(i)) {
					good.marketPriceAdder(thisNeed);//factor is legacy/oldtest
				}	
			}
			else {
				for (AbstractGood good : getAllOfGood(i)) {
					good.marketPriceLowerer(thisNeed);//factor is legacy/oldtest
				}
			}
			///////////////////////
			//actual reset of need:
			/////////////////////
			
			double needNudger = 0;
			
			for (AbstractGood good : getAllOfGood(i)) {
				
				needNudger -= good.getAmount();
				
				//good.marketPriceLowerer(thisNeed);//factor is legacy/oldtest
			}
					
			getMarketNeeds()[i] = needNudger*0.01; //to set default need to a fraction of total stockpile to prevent prices staying stable while stockpiles are rising.
			i++;
		}
	}

//ALL SAME //TODO: ONLY HAVE two!!
	public double[] getNeeds() {
		return marketNeeds;
	}
	public double getMarketNeed(int goodIndex) {
		return marketNeeds[goodIndex];
	}
	public double[] getMarketNeeds() {
		return marketNeeds;
	}
//_______________________________________
	
	

	/**
	 * change for need, and then
	 * @param marketNeedChange
	 * @param index
	 */
	public void modMarketNeed(double marketNeedChange, int index) {
		this.marketNeeds[index] = marketNeedChange;
	}
	
	public void modMarketNeeds(double[] marketNeedChanges) {
		for(int i = 0; i < marketNeeds.length; i++) {
			this.marketNeeds[i] += marketNeedChanges[i];
			//System.out.println(marketNeeds[i]);
		}
	}


	public List<AbstractMarket> getSubMarkets() {
		return subMarkets;
	}
	
	/**
	 * get all goods of type from submarkets
	 * @param goodConst
	 * @return
	 */
	public List<AbstractGood> getSubGoodType(int goodConst){
		
		List<AbstractGood> goods = new LinkedList<>();
		
		for (AbstractMarket market : subMarkets) {
			market.getAllOfGood(goodConst);
		}
		
		return goods;
	}


	public void addSubMarkets(List<AbstractMarket> subMarkets) {
		this.subMarkets = subMarkets;
	}
	
	public void removeSubMarkets(List<AbstractMarket> subMarkets) {
		this.subMarkets = subMarkets;
	}
	
	
}
