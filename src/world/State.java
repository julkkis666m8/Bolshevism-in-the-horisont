package world;
import java.util.LinkedList;
import java.util.List;

import constants.*;
import market.LocalMarket;





public class State {
public LinkedList<Pop> pops;
public String name;
public LocalMarket localMarket;
public Nation nation;
private double clerkWage;
private double craftsmanWage;
private double fertility;

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

	public State(String name, Nation nation) {
		
		this.nation = nation;
		
		//TODO: temp
		clerkWage = 50;
		craftsmanWage = 15;
		
		
		fertility = 1;//(Math.random() * 10 + 1);
		
		
		
		pops = new LinkedList<Pop>();
		localMarket = new LocalMarket();
		
		this.name = name;
		
		//DEV_MAKE_POP();
		//pops.add(new Pop(100, Constants.CATHOLIC, Constants.GERMANIC, Constants.CAPITALIST, wealthCalculator(Constants.CAPITALIST)));
		

		int pop = (int)(Math.random() * 10000 + 1000);
		
		pops.addAll(PopParameters.createPops(main.Main.germany, pop, main.Main.germanRace, main.Main.germanJob, this));
		pops.addAll(PopParameters.createPops(main.Main.germany, 1000, main.Main.jewishRace, main.Main.jewishJob, this));
		
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
		if (job == Constants.CAPITALIST) {
			return 100;
		}
		else if (job == Constants.CLERGYMAN) {
			return 0;
		}
		else if (job == Constants.CLERK) {
			return 5;
		}
		else if (job == Constants.CRAFTSMAN) {
			return 0;
		}
		else if (job == Constants.ARTISAN) {
			return 60;
		}
		else if (job == Constants.SOLDIER) {
			return 1;
		}
		else if (job == Constants.LABORER) {
			return 0;
		}
		else if (job == Constants.FARMER) {
			return 0;
		}
		return 0;
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
		return Pop.takeMoney(findCapitalists(), payThis);
	}

	public double getFertility() {
		return fertility;
	}

	public void setFertility(double fertility) {
		this.fertility = fertility;
	}
	

}
