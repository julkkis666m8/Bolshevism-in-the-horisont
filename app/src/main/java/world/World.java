package world;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import market.AbstractMarket;
import market.GlobalMarket;

public class World {

	private List<Nation> nations;
	//private List<State> states;
	private GlobalMarket globalMarket;
	
	public World() {
		globalMarket = new GlobalMarket();
		nations = new ArrayList<Nation>();
		//states = new ArrayList<State>();
	}

	public void addNation(Nation nation) {
		nations.add(nation);
	}
//	public void addState(State state) {
//		states.add(state);
//	}

	public List<State> getAllStates() {
		List<State> allStates = new LinkedList<State>();
		for(int i = 0; i < nations.size(); i++) {
			allStates.addAll((nations.get(i).getStates()));	
		}
		return allStates;
	}
	
	public List<Pop> getAllPops() {
		List<Pop> pops = new ArrayList<Pop>();
		for (State s : getAllStates()) {
			pops.addAll(s.getPops());
		}
		return pops;
	}


	public List<Nation> getNations() {
		return nations;
	}

	public GlobalMarket getGlobalMarket() {
		return globalMarket;
	}
	
}
