package world;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import constants.Constants;
import constants.Functions;
import factories.ArtesanJobs;
import goods.AbstractGood;
import market.Taxes;

public class Pop {



	public int population;
	public int religion;
	public int sex;
	public int race;
	public float age;
	public int job;
	public double averageWealth;
	public Ideology ideology;
	private double taxEvasion;
	List<AbstractGood> goods;
	private PopNeeds popNeeds;
	private PopWants popWants;
	
	private double justSpent = 0;
	private double incomeTaxable = 0;
	
	private State state;
	private double fertility = 0.0001;
	private double growthOMatic = 0;

	
	
	
	
	public Pop(int population, int sex, int race, int religion, float age, int job, Ideology ideology,
			double averageWealth, State state) {

		this.state = state;

		goods = new LinkedList<AbstractGood>();

		this.population = population;
		this.sex = sex;
		this.race = race;
		this.religion = religion;
		this.age = age;
		this.job = job;
		this.averageWealth = averageWealth;
		

		popNeeds = new PopNeeds(job);
		popWants = new PopWants(job);
		
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
		string += getAverageWealthString()+"£, ";


		return string;
	}
	private String getAverageWealthString() {
		// TODO Auto-generated method stub
		return ""+averageWealth;
	}
	private String getGenderString() {
		// TODO Auto-generated method stub
		return Constants.sexToString(sex);
	}
	private String getRaceString() {
		// TODO Auto-generated method stub
		return Constants.raceToString(race);
	}
	private String getJobString() {
		// TODO Auto-generated method stub
		return Constants.JobToString(job);
	}
	private String getReligionString() {
		// TODO Auto-generated method stub
		return Constants.ReligionToString(religion);
	}
	private String getPopulationString() {
		// TODO Auto-generated method stub
		return "population: "+population;
	}
	public int getPopulation() {
		return population;
	}
	public void tick() {
		// TODO Auto-generated method stub
		
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
	 * "ticks" a pop to make them do what their "jobs" do.
	 * @param nation
	 * @param state
	 */
	public void jobCounter(Nation nation, State state) { 
		
		double income = 0;
		
		if (job == Constants.CAPITALIST) {
			income = 100*population;
		}
		else if (job == Constants.CLERGYMAN) {
			double neededInc = nation.getCleregymanPay()*population;
			income = nation.getNationCash(nation.getCleregymanPay()*population);
			double effect = income/neededInc;
			state.AddEducation(population, effect);
			state.convert(population, effect);
		}
		else if (job == Constants.CLERK) {
			income = state.getClerkPay(state.getClerkWage()*population);
		}
		else if (job == Constants.CRAFTSMAN) {
			income = state.getCraftsmanPay(state.getCraftsmankWage()*population);
		}
		else if (job == Constants.ARTISAN) {
			goods.addAll(ArtesanJobs.artesanJob(this, state));
			income = PopSellHandler.sell(this, goods, state.localMarket, nation);
			goods.removeAll(goods);
		}
		else if (job == Constants.SOLDIER) {
			income =  nation.getNationCash(nation.getSoldierPay()*population);
		}
		else if (job == Constants.LABORER) {
			goods.addAll(PopSellHandler.labourerJob(this, state));
			income = PopSellHandler.sell(this, goods, state.localMarket, nation);
			goods.removeAll(goods);
		}
		else if (job == Constants.FARMER) {
			goods.addAll(PopSellHandler.farmerJob(this, state));
			income = PopSellHandler.sell(this, goods, state.localMarket, nation);
			goods.removeAll(goods);
		}
		
		this.setIncomeTaxable(income);
		averageWealth = income/population;
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
			double percentage = pop.getPopulation() / populationThatPays;
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


	public double getIncomeTaxable() {
		return incomeTaxable;
	}


	public void setIncomeTaxable(double incomeTaxable) {
		this.incomeTaxable = incomeTaxable;
	}


	public void buy(Nation nation, State state) {
		
		double[] needs = popNeeds.getNeeds(population, job);
		double[] buyTheseNeeds = new double[needs.length];
		for(int i = 0; i < needs.length; i++) {
			buyTheseNeeds[i] = needs[i];
		}
		
		
		//"buy" from self
		buyTheseNeeds = PopSellHandler.buyFromSelf(this, buyTheseNeeds);
		
		//buy from local market
		buyTheseNeeds = PopSellHandler.buy(this, buyTheseNeeds, takeTotalCash(), state.localMarket);
		
		
		//TODO: add global market
		
		
		setNeedsFurfilled(constants.Functions.divideArrays(buyTheseNeeds, needs));
		
	}


	public List<Pop> getSelfList() {
		List<Pop> pops = new ArrayList<>();
		pops.add(this);
		return pops;
	}


	public double getNeedsFurfilled() {
		return popNeeds.getNeedsFurfilled();
	}


	public void setNeedsFurfilled(double needsFurfilled) {
		popNeeds.setNeedsFurfilled(needsFurfilled);
	}


	public void birthControll() {

		//if (getStrata() == Constants.LOWER_STRATA) {
			if (getNeedsFurfilled() >= 1) {
				int growth = (int)((fertility*population)+1);
				double toPay = growth * (5000);
				System.out.println(growth+" BIRTHs of "+Constants.JobToString(job));
				takeMoney(getSelfList(), toPay);
				state.nation.births += growth;
				population += growth;
			}
			else if (getNeedsFurfilled() < 0.5 && (job == Constants.FARMER || job == Constants.LABORER)) {
				int growth = (int)((-fertility)-1);
				double toPay = growth * (5000);
				System.out.println(growth+" DEATHs of "+Constants.JobToString(job));
				takeMoney(getSelfList(), toPay);
				state.nation.births += growth;
				population += growth;
			}
		//}
		
	}


	private int getStrata() {
		return Constants.jobToClass(job);

	}


	
	
	
	

}
