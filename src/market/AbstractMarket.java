package market;

import constants.Functions;

import java.util.ArrayList;
import java.util.List;

import goods.AbstractGood;

public class AbstractMarket {

	protected List<AbstractGood> stockPile;
	
	public AbstractMarket() {
		stockPile = new ArrayList<>();
	}
	

	public String getStockpileString() {
		return "size is "+ Functions.formatNum(stockPile.size())+" and string is: "+stockPile.toString();
	}
	
	
	

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
	
	public void updateGoods(){
		
		
		for(AbstractGood g : stockPile) {
			
			g.calculateAviliability();
			
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
		
		marketNeed(goodConst, stillNeeded);
		return goods;
	}


	private void marketNeed(int goodConst, double stillNeeded) {
		// TODO Auto-generated method stub
		
	}
	
}
