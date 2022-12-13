package world;
import java.util.LinkedList;
import java.util.List;

import constants.*;
import market.LocalMarket;
import nationalEconomyManagers.AristocratCashPool;





public class State {
	public LinkedList<Pop> pops;
	public String name;
	public LocalMarket localMarket;
	public Nation nation;
	private double clerkWage;
	private double craftsmanWage;
	private double fertility;
	private AristocratCashPool aristocratCashPool;
	public List<State> neigbours = new LinkedList<>();
	public List<Army> armies = new LinkedList<>();

	@Override
	public String toString() {
		return name+" of "+nation.toString();
	}

	public String getInfo() {
		String string = "\n"+name+": ";

		for (int i = 0; i < this.pops.size(); i++) {
			string += "\n";
			string += this.pops.get(i).getInfo();
			if (i % 5 == 0) {
				string += "\n";
			}
			this.pops.iterator();
		}
		return string;
	}

	public State(String name, Nation nation, RaceParameters mainRace, JobParameters mainJob, int population) {
		
		this.nation = nation;
		
		//TODO: temp
		clerkWage = 50;
		craftsmanWage = 15;
		
		
		fertility = 1;//(Math.random() * 10 + 1);
		
		
		
		pops = new LinkedList<Pop>();
		localMarket = new LocalMarket();
		setAristocratCashPool(new AristocratCashPool(nation, this));
		
		this.name = name;
		
		//DEV_MAKE_POP();
		//pops.add(new Pop(100, Constants.CATHOLIC, Constants.GERMANIC, Constants.CAPITALIST, wealthCalculator(Constants.CAPITALIST)));
		

		//int pop = (int)(Math.random() * 10000 + 1000);
		
		pops.addAll(PopParameters.createPops(nation, population, mainRace, mainJob, this));
		//pops.addAll(PopParameters.createPops(nation, 1000, main.Main.jewishRace, main.Main.jewishJob, this));
		
	}
	
	public void addPop(List<Pop> pops) {
		pops.addAll(pops);
	}
	
	/**
	 * NULL STATE? USE PARAMETERS: NAME, NATION, RACE, JOB, POPnum TO MAKE A REAL STATE
	 */
	public State() {
		this.nation = new Nation("fake", "fakist",new World());

	}

	public boolean isForigen(State other){
		try{
			if(this.nation.equals(other.nation)){
				return false;
			}
			return true;
		}catch(NullPointerException e){
			return true;
		}

	}

	public void addNeigbour(State state){
		state.neigbours.add(this);
		this.neigbours.add(state);
	}
	
	public void addNeigbours(List<State> states){

		for (State state : states) {
			state.neigbours.add(this);
			state.neigbours.addAll(states);
			state.neigbours.remove(state);
		}
		this.neigbours.addAll(states);
		
	}
	
	private void DEV_MAKE_POP() {

		int random = (int)(Math.random() * 20 + 1);
		for (int i = 0; i < random; i++) {
			int pop = (int)(Math.random() * 1000 + 1);
			int[] religionList = {
					Constants.PROTESTANT, Constants.CATHOLIC, Constants.CATHOLIC,Constants.PROTESTANT,
					Constants.CATHOLIC, Constants.PROTESTANT, Constants.CATHOLIC,
					Constants.PROTESTANT, Constants.CATHOLIC, Constants.JEWISH};
			int randomRel = (int)(Math.random() * 8 + 0);
			int religion = religionList[randomRel];
			int[] raceList = {
					Constants.NORDIC, Constants.SLAV, Constants.ASHKERNAZI,
					Constants.GERMANIC, Constants.GERMANIC,Constants.GERMANIC, Constants.GERMANIC};
			int randomRac = (int)(Math.random() * 7 + 0);
			int race = raceList[randomRac];
			if (race == Constants.SLAV) {
				religion = Constants.CATHOLIC;
			}
			if (race == Constants.ASHKERNAZI) {
				religion = Constants.JEWISH;
			}
			int[] jobList = {
					0,1,2,3,4,5,6,7,8
					};
			int randomJob = (int)(Math.random() * 9 + 0);
			int job = jobList[randomJob];
			if (race == Constants.SLAV && job == Constants.CAPITALIST) {
				job = Constants.FARMER;
			}
			if (race == Constants.ASHKERNAZI && job == Constants.SOLDIER) {
				job = Constants.ARTISAN;
			}
			double averageWealth = wealthCalculator(job);
			
			
			pops.add(new Pop(pop, religion, race, job, averageWealth, this));
		}
	}
	
	
	
	
	public LinkedList<Pop> getPops(){
		return pops;
	}
	
	/**
	 * initial wealth per pop
	 * @param job
	 * @return
	 */
	public static double wealthCalculator(int job) { 
		
		int socialClass = Constants.jobToClass(job);
		
		if (job == Constants.UPPER_STRATA) {
			return 100;
		}
		else if (job == Constants.MIDDLE_STRATA) {
			return 10;
		}
		else if (job == Constants.LOWER_STRATA) {
			return 1;
		}
		else if (job == Constants.LOWEST_STRATA) {
			return 0.001;
		}
		else {
			return 0;
		}
		
		
		/*
		if (job == Constants.CAPITALIST) {
			return 100;
		}
		else if (job == Constants.CLERGYMAN) {
			return 100;
		}
		else if (job == Constants.MERCHANT) { //TEMPORARY BILLION
			return 5000;
		}
		else if (job == Constants.CRAFTSMAN) {
			return 1;
		}
		else if (job == Constants.ARTISAN) {
			return 600;
		}
		else if (job == Constants.SOLDIER) {
			return 1;
		}
		else if (job == Constants.LABORER) {
			return 1;
		}
		else if (job == Constants.FARMER) {
			return 1;
		}
		return 0;*/
	}

	public double getClerkWage() {
		return clerkWage;
	}

	public double getClerkPay(double payThis) {
		return Pop.takeMoney(findCapitalists(), payThis);
	}

	private List<Pop> findCapitalists() {

		List<Pop> capitalists = new LinkedList<>();
		
		for (int i = 0; i < pops.size(); i++) {
			Pop pop = pops.get(i);
			if (pop.job == Constants.CAPITALIST) {
				capitalists.add(pop);
			}
		}
		
		return capitalists;
	}
	
	public double jobPercentage(int job) {
		int ofJob = countPopulation(getJob(job));
		return ofJob/countPopulation(getPops());
	}
	
	public int countPopulation(List<Pop> pops) {
		int total = 0;
		for (Pop pop : pops) {
			total += pop.population;
		}
		return total;
	}
	

	public void AddEducation(int population, double effect) {
		// TODO Auto-generated method stub
		
	}

	public void convert(int population, double effect) {
		// TODO Auto-generated method stub
		
	}

	public double getCraftsmankWage() {
		return craftsmanWage;
	}

	public double getCraftsmanPay(double payThis) {
		System.out.println("CRAFTSMEN WANT MONEY: "+payThis);
		return Pop.takeMoney(findCapitalists(), payThis);
	}

	public double getFertility() {
		return fertility;
	}

	public void setFertility(double fertility) {
		this.fertility = fertility;
	}

	public void setName(String string) {
		name = string;
		
	}

	public void tick() {
		localMarket.tick();
		aristocratCashPool.tick();
		
	}

	public AristocratCashPool getAristocratCashPool() {
		return aristocratCashPool;
	}

	public void setAristocratCashPool(AristocratCashPool aristocratCashPool) {
		this.aristocratCashPool = aristocratCashPool;
	}

	public void addPop(Pop pop) {

		//TODO: add something that tests if such a pop exists lrdy. IMPORTANT!!!!!
		
		List<Pop> popJobs = this.getJob(pop.job);
		
		System.out.println(Constants.JobToString(pop.job)+" people of that job "+popJobs.size());
		
		for (Pop exPop : popJobs) {
			if (exPop.comparePop(pop)) {
				exPop.combinePop(pop);
				return;
			}
		}
		
		pops.add(pop);
		
		
	}

	/**
	 * gets all pops of nation with specific job type
	 * @param job
	 * @return
	 */
	public List<Pop> getJob(int job) {
		List<Pop> popsOfJob = new LinkedList<>();
		
		List<Pop> allPops = getPops();
		
		for (int i = 0; i < allPops.size(); i++) {
			Pop pop = allPops.get(i);
			if(pop.job == job) {
				popsOfJob.add(pop);
			}
		}
		System.out.println("getJob size: "+allPops.size()+" to "+popsOfJob.size());
		return popsOfJob;
	}

	public List<State> getNeigbours() {
		return neigbours;
	}
	

}
