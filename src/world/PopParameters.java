package world;

import java.util.LinkedList;

public class PopParameters {
	
	public static LinkedList<Pop> createPops(Nation nation, int totalStatePop, RaceParameters rp, JobParameters jp, State state) {
		
		
		LinkedList<Pop> pops = new LinkedList<>();

		//System.out.println(totalStatePop+"");
		
		pops.addAll(raceDefiner((int)(jp.getSerfPercentage() * totalStatePop), rp, 9, state));
		pops.addAll(raceDefiner((int)(jp.getSlavePercentage() * totalStatePop), rp, 10, state));
		pops.addAll(raceDefiner((int)(jp.getFarmerPercentage() * totalStatePop), rp, 0, state));
		pops.addAll(raceDefiner((int)(jp.getLaborerPercentage() * totalStatePop), rp, 1, state));
		pops.addAll(raceDefiner((int)(jp.getSoldierPercentage() * totalStatePop), rp, 2, state));
		pops.addAll(raceDefiner((int)(jp.getArtisanPercentage() * totalStatePop), rp, 3, state));
		pops.addAll(raceDefiner((int)(jp.getCraftsmanPercentage() * totalStatePop), rp, 4, state));
		pops.addAll(raceDefiner((int)(jp.getClerkPercentage() * totalStatePop), rp, 5, state));
		pops.addAll(raceDefiner((int)(jp.getCapitalistPercentage() * totalStatePop), rp, 6, state));
		pops.addAll(raceDefiner((int)(jp.getClergymanPercentage() * totalStatePop), rp, 7, state));
		pops.addAll(raceDefiner((int)(jp.getAristocratPercentage() * totalStatePop), rp, 8, state));
		pops.addAll(raceDefiner((int)(jp.getOfficerPercentage() * totalStatePop), rp, 9, state));
		
		
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
