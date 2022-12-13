package world;

import java.util.LinkedList;

import constants.Constants;

public class PopParameters {
	
	public static LinkedList<Pop> createPops(Nation nation, int totalStatePop, RaceParameters rp, JobParameters jp, State state) {
		
		
		LinkedList<Pop> pops = new LinkedList<>();

		//System.out.println(totalStatePop+"");
		
		pops.addAll(raceDefiner((int)(jp.getSerfPercentage() * totalStatePop), rp, Constants.SERF, state));
		pops.addAll(raceDefiner((int)(jp.getSlavePercentage() * totalStatePop), rp, Constants.SLAVE, state));
		pops.addAll(raceDefiner((int)(jp.getFarmerPercentage() * totalStatePop), rp, Constants.FARMER, state));
		pops.addAll(raceDefiner((int)(jp.getLaborerPercentage() * totalStatePop), rp, Constants.LABORER, state));
		pops.addAll(raceDefiner((int)(jp.getSoldierPercentage() * totalStatePop), rp, Constants.SOLDIER, state));
		pops.addAll(raceDefiner((int)(jp.getArtisanPercentage() * totalStatePop), rp, Constants.ARTISAN, state));
		pops.addAll(raceDefiner((int)(jp.getCraftsmanPercentage() * totalStatePop), rp, Constants.CRAFTSMAN, state));
		pops.addAll(raceDefiner((int)(jp.getClerkPercentage() * totalStatePop), rp, Constants.MERCHANT, state));
		pops.addAll(raceDefiner((int)(jp.getCapitalistPercentage() * totalStatePop), rp, Constants.CAPITALIST, state));
		pops.addAll(raceDefiner((int)(jp.getClergymanPercentage() * totalStatePop), rp, Constants.CLERGYMAN, state));
		pops.addAll(raceDefiner((int)(jp.getAristocratPercentage() * totalStatePop), rp, Constants.ARISTOCRAT, state));
		pops.addAll(raceDefiner((int)(jp.getOfficerPercentage() * totalStatePop), rp, Constants.OFFICER, state));
		
		
		//TODO: create wives
		
		
		
		
		
		return pops;
		
	}
	
	private static LinkedList<Pop> raceDefiner(int totalPop, RaceParameters rp, int job, State state) {
		
		int sexAdjustedPop = totalPop/2; //for wives
		
		LinkedList<Pop> pops = new LinkedList<Pop>();
		

		Pop pop = new Pop(sexAdjustedPop, rp.getReligion(), rp.getRace(), job, State.wealthCalculator(job), state);
		
		
		
		pops.add(pop);
		
		return pops;
	}
	
}
