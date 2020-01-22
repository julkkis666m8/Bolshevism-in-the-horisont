package world;
import java.util.LinkedList;
import java.util.List;

import constants.Constants;
import constants.Functions;
import goods.AbstractGood;
import main.Main;
import market.NationalMarket;
import nationalEconomyManagers.SoldierPay;

public class Nation {
	
	private String name;
	private String nameADJ;
	private List<State> states;
	private List<Integer> coreRaces;
	private List<Integer> acceptedRaces;
	private List<Integer> hatedRaces;
	public double taxEfficency;
	public double coffers;
	public double taxPercentage;
	
	private NationalMarket nationalMarket;
	private double cleregymanPay;
	//private double soldierPay;
	private SoldierPay soldierPay;
	public double babyPrice = 500;
	public int births = 0; //for statistics
	
	//TODO: add lists for discriminated races/etc and perfered races/etc
	
	
	
	/**
	 * constructor takes name and nameADJ (flag to be added)
	 * @param name
	 * @param nameADJ
	 */
	public Nation(String name, String nameADJ) {
		
		
		
		//temp
		cleregymanPay = 0.9;
		soldierPay = new SoldierPay(10000, this);
		
		setNationalMarket(new NationalMarket(this, main.Main.world.getGlobalMarket()));
		
		
		coffers = 1000000000;
		taxEfficency = 0.9;
		taxPercentage = 0.024;
		states = new LinkedList<State>();
		coreRaces = new LinkedList<Integer>();
		acceptedRaces = new LinkedList<Integer>();
		hatedRaces = new LinkedList<Integer>();
		this.name = name;
		this.nameADJ = nameADJ;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	

	public void addCoreRace(int race){
		coreRaces.add(race);
	}
	public void addAcceptedRace(int race){
		acceptedRaces.add(race);
	}
	public void addHatedRace(int race){
		hatedRaces.add(race);
	}
	
	
	
	
	
	
	public void addState(State state) {
		states.add(state);
	}
	
	public List<State> getStates(){
		return states;
	}



	private int getTotalPop() {

		LinkedList<Pop> pops = getAllPops();

		int corePop = 0;

		for (int i = 0; i < pops.size(); i++) {
			Pop pop = pops.get(i);
			corePop += pop.getPopulation();
		}

		return corePop;
	}

	private int getCorePop() {

		LinkedList<Pop> pops = getAllPops();

		int corePop = 0;

		for (int i = 0; i < pops.size(); i++) {
			Pop pop = pops.get(i);
			if(coreRaces.contains(pop.getRace())) {
				corePop += pop.getPopulation();
			}
		}
		
		return corePop;
	}
	
	private int getAcceptedPop() {
		
		LinkedList<Pop> pops = getAllPops();
		
		int acceptedPop = 0;
		
		for (int i = 0; i < pops.size(); i++) {
			Pop pop = pops.get(i);
			if(acceptedRaces.contains(pop.getRace())) {
				acceptedPop += pop.getPopulation();
			}
		}
		
		return acceptedPop;
	}
	
	private int getHatedPop() {
		
		LinkedList<Pop> pops = getAllPops();
		
		int hatedPop = 0;
		
		for (int i = 0; i < pops.size(); i++) {
			Pop pop = pops.get(i);
			if(hatedRaces.contains(pop.getRace())) {
				hatedPop += pop.getPopulation();
			}
		}
		
		return hatedPop;
	}
	
	private int getOtherPop() {
		
		LinkedList<Pop> pops = getAllPops();
		
		int otherPop = 0;
		
		for (int i = 0; i < pops.size(); i++) {
			Pop pop = pops.get(i);
			if(		!coreRaces.contains(pop.getRace())     &&
					!acceptedRaces.contains(pop.getRace()) &&
					!hatedRaces.contains(pop.getRace())    ) {
				otherPop += pop.getPopulation();
			}
		}
		
		return otherPop;
	}
	

	public LinkedList<Pop> getAllPops() {

		LinkedList<Pop> allPops = new LinkedList<Pop>();
		
		for (int i = 0; i < states.size(); i++) {
			State state = states.get(i);
			allPops.addAll(state.getPops());
		}
		
		return allPops;
	}
	

	public double getTotalMoney() {
		
		LinkedList<Pop> pops = getAllPops();
		double money = 0;
		
		for (int i = 0; i < pops.size(); i++) {
			Pop pop = pops.get(i);

			money += pop.getTotalWealth();
			
		}
		
		return money;
	}
	
	private int getBirths() {
		int temp = births;
		//births = 0;
		return temp;
	}

	public String getInfo() {
		String string = nameADJ+" information:";

		string += "\nTotal Pop:    "+getTotalPop();
		string += "\nCore Pop:     "+getCorePop();
		string += "\nAccepted Pop: "+getAcceptedPop();
		string += "\nHated Pop:    "+getHatedPop();
		string += "\nOther Pop:    "+getOtherPop();
		string += "\ngrowth Pop:   "+getBirths();
		string += "\npop wealth: "+Functions.formatNum(getTotalMoney())+"£";
		string += "\nstate weal:  "+Functions.formatNum(coffers)+"£";
		double totpop = getTotalMoney();
		string += "\nTotal weth:  "+Functions.formatNum((coffers+totpop))+"£";
		
//		for (int i = 0; i < states.size(); i++) {
//			string += states.get(i).getInfo();
//		}
		

		string += getPopInfo();
		
		
		return string;
	}
	
	public String getPopInfo() {
		String string = "";
		
		for (int job = 0; job < 12; job++) {
			string += "\n | "+Constants.JobToString(job)+" | "+popInfo(getPopsJob(getAllPops(), job));
		}
		
		
		
		
		return string;
	}

	private String popInfo(List<Pop> pops) {
		String string = "";
		
		

		int population = 0;
		double totalWealth = 0;
		
		double justSpent = 0;
		double incomeTaxable = 0; 
		int itterations = 0;
		double needsFurfilled = 0;
		double wantsFurfilled = 0;
		
		for (int i = 0; i < pops.size(); i++) {
			itterations++;
			Pop pop = pops.get(i);
			population += pop.getPopulation();
			totalWealth += pop.getAverageWealth();
			justSpent += pop.getJustSpent()*population;
			incomeTaxable += pop.getIncomeTaxable();
			needsFurfilled += pop.getNeedsFurfilled();
			wantsFurfilled += pop.getWantsFurfilled();
		}

		needsFurfilled = (needsFurfilled / itterations)*100;//%
		wantsFurfilled = (wantsFurfilled / itterations)*100;//%
		
		

		if(population != 0) {
			string += "population: "+population+" | ";	
		}
		if(totalWealth != 0) {
			string += "totalWealth: "+Functions.formatNum(totalWealth)+"£ | ";	
		}
		if(justSpent != 0) {
			string += "justSpent: "+Functions.formatNum(justSpent)+"£ | ";	
		}
		if(incomeTaxable != 0) {
			string += "incomeTaxable: "+Functions.formatNum(incomeTaxable)+"£ | ";	
		}
		if(itterations != 0) {
			string += "needsFurfilled: "+Functions.formatNum(needsFurfilled)+"% | ";	
		}
		if(itterations != 0) {
			string += "wantsFurfilled: "+Functions.formatNum(wantsFurfilled)+"% | ";	
		}
		
		
		
		
		
		
		
		return string;
	}

	public static List<Pop> getPopsJob(List<Pop> allPops, int job) {
		List<Pop> popsOfJob = new LinkedList<>();
		
		for (int i = 0; i < allPops.size(); i++) {
			Pop pop = allPops.get(i);
			if(pop.job == job) {
				popsOfJob.add(pop);
			}
		}
		
		return popsOfJob;
	}
	
	/**
	 * gets all pops of nation with specific job type
	 * @param job
	 * @return
	 */
	public List<Pop> getJob(int job) {
		List<Pop> popsOfJob = new LinkedList<>();
		
		List<Pop> allPops = getAllPops();
		
		for (int i = 0; i < allPops.size(); i++) {
			Pop pop = allPops.get(i);
			if(pop.job == job) {
				popsOfJob.add(pop);
			}
		}
		
		return popsOfJob;
	}

	public void addToCoffers(double taxMoney) {
		coffers += taxMoney;
	}

	public double getCleregymanPay() {
		return cleregymanPay;
	}

	public void setCleregymanPay(double cleregymanPay) {
		this.cleregymanPay = cleregymanPay;
	}

	public double getNationCash(double money) {
		if (coffers >= money) {
			coffers -= money;
			return money;			
		}
		else if (coffers > 0) {
			double adjustedMoney = coffers;
			coffers = 0;
			return adjustedMoney;
		}
		else {
			return 0;
		}
		

	}

	/*
	public double getSoldierPay() {
		return soldierPay;
	}
	*/


	public NationalMarket getNationalMarket() {
		return nationalMarket;
	}



	private void setNationalMarket(NationalMarket nationalMarket) {
		this.nationalMarket = nationalMarket;
	}



	public double getBabyPrice() {
		return babyPrice;
	}



	public SoldierPay getSoldierPay() {
		return soldierPay;
	}



	public void tick() {
		soldierPay.tick();
		
	}

	
}
