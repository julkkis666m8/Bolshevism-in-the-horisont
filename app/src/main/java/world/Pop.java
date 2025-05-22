package world;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import constants.Constants;
import goods.AbstractGood;
import market.Taxes;

public class Pop {



	private static final double POPULATION_FLUIDITY_CONSTANT = 0.01;
	public int population;
	public int religion;
	public int race;
	public float age;
	public int job;
	public double averageWealth;
	public Ideology ideology;
	private double taxEvasion;
	List<AbstractGood> goods;
	private PopNeeds popNeeds;
	private PopWants popWants;
	private PopLuxury popLuxury;
	
	private double justSpent = 0;
	private double incomeTaxable = 0;
	
	private State state;
	private double fertility = 0.001;
	private double growthOMatic = 0;

	
	@Override
	public String toString() {
		return Constants.raceToString(race)+" "+Constants.ReligionToString(religion)+
				" "+Constants.JobToString(job)+" from "+state.toString();
	}
	public Pop(int population, int race, int religion, float age, int job, Ideology ideology,
			double averageWealth, State state) {
		this.state = state;
		goods = new LinkedList<AbstractGood>();
		this.population = population;
		this.race = race;
		this.religion = religion;
		this.age = age;
		this.job = job;
		this.averageWealth = averageWealth;
		popNeeds = new PopNeeds(job);
		popWants = new PopWants(job);
		popLuxury = new PopLuxury(job);
		if(job == Constants.CAPITALIST) {
			taxEvasion = 0.5;
		}
		else {
			taxEvasion = 0.01;
		}
	}
	public Pop(int population, int religion, int race, int job, double averageWealth, State state) {
		this.state = state;
		goods = new LinkedList<AbstractGood>();
		this.population = population;
		this.religion = religion;
		this.race = race;
		this.job = job;
		this.averageWealth = averageWealth;
		popNeeds = new PopNeeds(job);
		popWants = new PopWants(job);
		popLuxury = new PopLuxury(job);
		if(job == Constants.CAPITALIST) {
			taxEvasion = 0.5;
		}
		else {
			taxEvasion = 0.01;
		}
	}

	/**
	 * gives information about a pop
	 * @return
	 */
	public String getInfo() {
		String string = "";

		string += getPopulationString()+", ";
		string += getReligionString()+", ";
		string += getRaceString()+", ";
		string += getJobString()+", ";
		string += getAverageWealthString()+"�, ";


		return string;
	}
	private String getAverageWealthString() {
		return ""+averageWealth;
	}
	private String getRaceString() {
		return Constants.raceToString(race);
	}
	private String getJobString() {
		return Constants.JobToString(job);
	}
	private String getReligionString() {
		return Constants.ReligionToString(religion);
	}
	private String getPopulationString() {
		return "population: "+population;
	}
	public int getPopulation() {
		return population;
	}
	
	
	
	public void tick(Nation nation) {


		popStockpileRotter();
		cleanGoods(); //removes items with size 0.000000001
		
		
		//System.out.println(pop.toString());

		jobCounter(nation, state);//has sell for RGO
		Taxes.taxMe(this, nation); //income tax

		/* People buy thier goods in the begining of the turn in state calculation
		setNeedsFurfilled(buy(nation, state, getNeeds()));
		if(getNeedsFurfilled() > 0.6) {
			setWantsFurfilled(buy(nation, state, getWants()));	
			if(getWantsFurfilled() > 0.6) {
				setLuxuryFurfilled(buy(nation, state, getLuxury()));			
			}else {
				setLuxuryFurfilled(0);
			}
		}else {
			setWantsFurfilled(0);
			setLuxuryFurfilled(0);
		*/
		
		
		

		
		//System.out.println(itteration);
		//pop.removePeople( (( (int)(Math.round((pop.getPopulation())*0.1)) ) + 150) );
			
		
	}

	public int populationRandom(double modifier) {
		
		return (int) (Math.random() * (population * modifier * POPULATION_FLUIDITY_CONSTANT) + POPULATION_FLUIDITY_CONSTANT);
		
	}
	public int populationRandom() {
		
		return (int) (Math.random() * (population * POPULATION_FLUIDITY_CONSTANT) + POPULATION_FLUIDITY_CONSTANT);
		
	}
	
	public void buyTick(Nation nation) {
		setNeedsFurfilled(buy(nation, state, getNeeds()));
		if(getNeedsFurfilled() > 0.6) {
			setWantsFurfilled(buy(nation, state, getWants()));
			if(getWantsFurfilled() > 0.2){
				setLuxuryFurfilled(buy(nation, state, getLuxury()));
			}
		}
	}

	public void buyExtraTick(Nation nation) {

		double firstMoney = totalCash();

		//spend extra money
		//while(totalCash() > (firstMoney * 0.5) || Math.random() > 0.5){
			System.out.println(population +" "+ totalCash());
			//if(getNeedsFurfilled() > 0.8){
				setNeedsFurfilled(getNeedsFurfilled() + buy(nation, state, getNeeds()));
			//}
			//if(getWantsFurfilled() > 0.8){
				setWantsFurfilled(getWantsFurfilled() + buy(nation, state, getNeeds()));
			//}
			//if(getLuxuryFurfilled() > 0.8){
				setLuxuryFurfilled(getLuxuryFurfilled() + buy(nation, state, getNeeds()));
			//}
		//}
	}

	public void promoteTick(Nation nation) {
		if(job == Constants.FARMER || job == Constants.LABORER){
			farmerLaborControll(populationRandom());
		}
		promotionControll(populationRandom());
		demotionControll(populationRandom(1-getNeedsFurfilled())); //TODO: make number go reverse.
		birthControll(populationRandom());

	}


	public int getRace() {
		return race;
	}


	public void removePeople(int numToKill) {
		
		if (numToKill < 0) {
			return;
		}
		
		population = population - numToKill;
		if (population <= 0) {
			population = 0;
		}
	}
	
	public double getTotalWealth() {
		return population * averageWealth;
	}
	
	public void taxMe(double percentage, Nation nation) {
		
		double taxMoney = (averageWealth*population)*percentage * nation.taxEfficency * (1-this.taxEvasion);
		
		averageWealth -= taxMoney/population;
		
		
		nation.addToCoffers(taxMoney);
	}
	
	public double getAverageWealth(){
		//wealth/population
		
		return averageWealth;
		
		
	}


	public double getTaxEvasion() {
		return taxEvasion;
	}


	public double getJustSpent() {
		double temp = justSpent;
		justSpent = 0;
		return temp/population;
	}


//	public void setJustSpent(double justSpent) {
//		this.justSpent = justSpent;
//	}

	public void addJustSpent(double justSpent) {
		this.justSpent += justSpent;
		
	}


	public List<AbstractGood> getGoods() {
		return goods;
	}
	
	/**
	 * call me at start of each tick
	 */
	public void popStockpileRotter() {
		if(goods.size() > Constants.AMOUNT_OF_GOODS) { //for easy bug cheking
			System.out.println("if you see this, then there's a leak of some sort, or maybe global trade is working :^)");
			System.out.println(goods.size());
			System.out.println(goods);
		}
	
		for(AbstractGood good : goods) {
			good.rot();
		}
	}

	/**
	 * "ticks" a pop to make them do what their "jobs" do.
	 * @param nation
	 * @param state
	 */
	public void jobCounter(Nation nation, State state) {
		main.Main.controller.popJobHandler.jobCounter(nation, state, this); //to save space in Pop so easier to read
	}
	



	public static int countTotalPopulation(List<Pop> pops) {
		int popul = 0;
		
		for (int i = 0; i < pops.size(); i++) {
			Pop pop = pops.get(i);
			popul += pop.getPopulation();
		}
		
		return popul;
	}


	public static double takeMoney(List<Pop> pops, double payThis) {
		
		
		int populationThatPays = countTotalPopulation(pops);
		double money = 0;
		
		for(int i = 0; i < pops.size(); i++) {
			Pop pop = pops.get(i);
			double percentage = 0;
			try {
				percentage = pop.getPopulation() / populationThatPays;
			} catch (Exception e) {
				System.out.println("someone's dead");
			}
			

			double toPay = percentage * payThis;
			double payed = 0;
			
			if (toPay > pop.totalCash()) {
				payed += pop.bankrupt();
			}
			else {
				payed += pop.pay(toPay);
			}

			pop.justSpent += payed;
			money += payed;
		}
		
		return money;
	}


	/**
	 * assumes payer has the money to spend
	 * @param toPay
	 * @return
	 */
	public double pay(double toPay) {
		
		double money = averageWealth * population;
		money -= toPay;
		averageWealth = money * population;

		justSpent += toPay;
		return toPay;
	}


	/**
	 * pop pays all money it has
	 * @return
	 */
	public double bankrupt() {
		System.out.println(getJobString()+" HAS NO MONEY");
		
		double money = totalCash();
		
		return pay(money);
	}


	public double totalCash() {
		return averageWealth * population;
	}
	
	public double takeTotalCash() {
		double temp = averageWealth;
		averageWealth = 0;
		return temp * population;
	}
	public void returnTotalCash(double totalCash) {
		averageWealth = totalCash/population;
	}
	
	public void giveCash(double totalCash) {
		averageWealth = averageWealth + (totalCash/population); //TODO: CHEK IF THIS ACTUALLY WORKS
	}


	public double getIncomeTaxable() {
		return incomeTaxable;
	}


	public void setIncomeTaxable(double incomeTaxable) {
		this.incomeTaxable = incomeTaxable;
	}


	public double buy(Nation nation, State state, double[] needs) {
		
		
		double[] buyTheseNeeds = new double[needs.length];
		for(int i = 0; i < needs.length; i++) {
			buyTheseNeeds[i] = needs[i];
		}
		
		
		//"buy" from self
		buyTheseNeeds = PopSellHandler.buyFromSelf(this, buyTheseNeeds);
		
		//buy from local market
		buyTheseNeeds = PopSellHandler.buy(this, buyTheseNeeds, takeTotalCash(), state.localMarket);
		

		//buy from national market
		buyTheseNeeds = PopSellHandler.buy(this, buyTheseNeeds, takeTotalCash(), nation.getNationalMarket());
		
		
		//TODO: add global market
		//buyTheseNeeds = PopSellHandler.buy(this, buyTheseNeeds, takeTotalCash(), );
		

		
		return constants.Functions.divideArrays(buyTheseNeeds, needs);
		
	}


	public List<Pop> getSelfList() {
		List<Pop> pops = new ArrayList<>();
		pops.add(this);
		return pops;
	}


	public double getNeedsFurfilled() {
		return popNeeds.getNeedsFurfilled();
	}

	public double getWantsFurfilled() {
		return popWants.getWantsFurfilled();
	}

	public double getLuxuryFurfilled() {
		return popLuxury.getLuxuryFurfilled();
	}
	public void setNeedsFurfilled(double needsFurfilled) {
		popNeeds.setNeedsFurfilled(needsFurfilled);
	}

	public void setWantsFurfilled(double wantsFurfilled) {
		popWants.setWantsFurfilled(wantsFurfilled);
	}

	public void setLuxuryFurfilled(double luxuryFurfilled) {
		popLuxury.setLuxuryFurfilled(luxuryFurfilled);
	}

	//TODO: make work with inparameter
	public void birthControll(int popModifier) {

		//if (getStrata() == Constants.LOWER_STRATA) {
			if (growthOMatic > 1) {
				int growth = (int)growthOMatic;
				growthOMatic -= growth;
				double toPay = growth * (5000);
				System.out.println(growth+" BIRTHs of "+Constants.JobToString(job));
				takeMoney(getSelfList(), toPay);
				state.nation.births += growth;
				population += growth;
			}
			else if (getNeedsFurfilled() < 0.25 && population > 0 && (job == Constants.FARMER || job == Constants.LABORER)) {
				int growth = -1;
				double toPay = growth * (5000);
				System.out.println(growth+" DEATHs of "+Constants.JobToString(job));
				takeMoney(getSelfList(), toPay);
				state.nation.births += growth;
				population += growth;
			}
			else if(getWantsFurfilled() >= 0.75) {
				//System.out.println(growthOMatic+" growths of "+Constants.JobToString(job));
				growthOMatic += fertility*population* getWantsFurfilled();
			}
		//}
	
	}

	public void farmerLaborControll(int toPromote){

		if(toPromote == 0) {
			return;
		}

		if(getNeedsFurfilled() < 0.25) {
			promote(toPromote, job == Constants.FARMER ? Constants.LABORER : Constants.FARMER);
		}

	}

	public void promotionControll(int toPromote) {
		
		if(toPromote == 0) {
			return;
		}
		
		//int toPromote = 1;
		
		if (getWantsFurfilled() >= 0.1 && Constants.jobToClass(job) == Constants.LOWEST_STRATA && Math.random() > 0.9) {
			
			if(Math.random() > 0.34) {
				promote(toPromote, Constants.FARMER);
			}
			else if(Math.random() > 0.34) {
				promote(toPromote, Constants.LABORER);
			}
			else { 
				promote(toPromote, Constants.SOLDIER);
			}
			
		}
		else if (getNeedsFurfilled() >= 1 && job == Constants.FARMER && state.localMarket.getMarketDemand(Constants.WHEAT) <= 0) { //TODO: change this to supoply/demand

			if(Math.random() > 0.5) {
				promote(toPromote, Constants.ARTISAN);
			}
			else if(Math.random() > 0.2) {
				promote(toPromote, Constants.OFFICER);
			}
			else if(Math.random() > 0.5) {
				promote(toPromote, Constants.MERCHANT);
			}
			else {
				promote(toPromote, Constants.CLERGYMAN);
			}
		}
		else if (getWantsFurfilled() > 0.33 && Constants.jobToClass(job) == Constants.LOWER_STRATA && job != Constants.FARMER && Math.random() > 0.9) {

			if(Math.random() > 0.5) {
				promote(toPromote, Constants.ARTISAN);
			}
			else if(Math.random() > 0.2) {
				promote(toPromote, Constants.OFFICER);
			}
			else if(Math.random() > 0.5) {
				promote(toPromote, Constants.MERCHANT);
			}
			else {
				promote(toPromote, Constants.CLERGYMAN);
			}
		}
		else if (getWantsFurfilled() > 0.75 && Constants.jobToClass(job) == Constants.MIDDLE_STRATA && Math.random() > 0.9 && getAverageWealth() > 100000) {
			if(Math.random() > 0.5) {
				promote(toPromote, Constants.CAPITALIST);
			}
			else {
				promote(toPromote, Constants.ARISTOCRAT);
			}
			
		}
	}
	

	public void demotionControll(int toDemote) {
		
		if(toDemote == 0) {
			return;
		}
		
		//int toDemote = 1;
		
		if (Constants.jobToClass(job) == Constants.LOWER_STRATA) {
			
			demote(toDemote, Constants.SERF);
			
		}
		else if ( Constants.jobToClass(job) == Constants.MIDDLE_STRATA) {
			if(Math.random() > 0.34) {
				demote(toDemote, Constants.FARMER);
			}
			else if(Math.random() > 0.5) {
				demote(toDemote, Constants.LABORER);
			}
			else { 
				demote(toDemote, Constants.SOLDIER);
			}
			
		}
		else if (Constants.jobToClass(job) == Constants.UPPER_STRATA && Math.random() > 0.9) {
			
			if(Math.random() > 0.5) {
				demote(toDemote, Constants.ARTISAN);
			}
			else if(Math.random() > 0.2) {
				demote(toDemote, Constants.OFFICER);
			}
			else if(Math.random() > 0.5) {
				demote(toDemote, Constants.MERCHANT);
			}
			else {
				demote(toDemote, Constants.CLERGYMAN);
			}
			
		}
	}
	
	/**
	 * make people into lower class
	 */
	private void promote(int promotablePopulation, int job) {
		
		if (population < promotablePopulation) {
			promotablePopulation = population;
		}
		
		population = population - promotablePopulation;
		
		System.out.println(promotablePopulation+" "+ Constants.JobToString(this.job) + " promote to "+ Constants.JobToString(job));
		//JOptionPane.showMessageDialog(null, "PROMOTION");
		state.addPop(new Pop(promotablePopulation, race, religion, age, job, ideology, averageWealth, state));
		
	}


	/**
	 * make people into lower class
	 */
	private void demote(int demotablePopulation, int job) {
		
		if (population < demotablePopulation) {
			demotablePopulation = population;
		}
		
		population = population - demotablePopulation;
		
		System.out.println(demotablePopulation+" "+ Constants.JobToString(this.job) + " demote to "+ Constants.JobToString(job));
		
		state.addPop(new Pop(demotablePopulation, race, religion, age, job, ideology, averageWealth, state));
		
	}




	private int getStrata() {
		return Constants.jobToClass(job);

	}




	public double[] getNeeds() {
		return popNeeds.getNeeds(population, job);
	}




	public double[] getWants() {
		return popWants.getWants(population, job);
	}

	
	public double[] getLuxury() {
		return popLuxury.getLuxury(population, job);
	}



	public void cleanGoods() {
		List<AbstractGood> removeList = new ArrayList<>();
		for (AbstractGood good : goods) {
			if (good.getAmount() < 0.000000001){
				removeList.add(good);
			}
		}
		for (AbstractGood removableGood : removeList) {
			goods.remove(removableGood);			
		}

	}




	public State getState() {
		return state;
	}


	//state.addPop(new Pop(demotablePopulation, sex, race, religion, age, job, ideology, averageWealth, state));
	
	/**
	 * compare if pop is combinable
	 * @param pop
	 * @return
	 */
	public boolean compare(Pop pop) {
		if(this.race == pop.race && this.religion == pop.religion && this.age == pop.age && this.job == pop.job) {
			return true;
		}
		return false;
	}


	//state.addPop(new Pop(demotablePopulation, ideology, averageWealth, state));
	

	/**
	 * combine pops who are compatable to save on operations.
	 * @param pop
	 */
	public void combine(Pop pop) {
		
		//TODO: handle ideology
		
		this.population += pop.population;
		
		this.giveCash(pop.getTotalWealth());
		
	}

	//LEGACY?
	/*
	//to add stuff to people's pockets
	public void addGoods(List<AbstractGood> goodList) {
		goods.addAll(goodList);
	}*/

	/**
	 * adds goods to pop taking into account similar goods lrdy in pops pocket
	 * @param newGoods
	 */
	public void addGoods(List<AbstractGood> newGoods) {
		List<AbstractGood> newGoodsAddList = new ArrayList<>();
		
		for (AbstractGood newGood : newGoods) {
			boolean has = false;
			for (AbstractGood good : goods) {
				if (newGood.compare(good)) {
					good.addAmount(newGood.getAmount());
					has = true;
					break;
				}
			}
			
			if (!has) {
				newGoodsAddList.add(newGood);				
			}
		}
		goods.addAll(newGoodsAddList);
	}

	public double getStanding(Nation nation) {
		double standing = Constants.jobToClass(job);
		
		if(nation.hatedRaces.contains(this.race)) {
			standing++;
			standing = standing * 2;
		}
		
		standing = standing - 100;
		standing = standing * -1;
		
		return standing;
		
		//replaces this from controller:
		//return o1.getAverageWealth() > o2.getAverageWealth() ? -1 : o1.getAverageWealth() == o2.getAverageWealth() ? 0 : 1;
	}
}
