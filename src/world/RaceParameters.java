package world;

public class RaceParameters {
	
	private int religion;
	private int race;
	private Ideology ideology;
	

	/**
	 * 
	 * @param religion
	 * @param race
	 */
	public RaceParameters(int religion, int race) {
		this.religion = religion;
		this.race = race;
		
		ideology = new Ideology();
	}
	
	/**
	 * 
	 * @param religion
	 * @param race
	 * @param ideology
	 */
	public RaceParameters(int religion, int race, Ideology ideology) {
		this.religion = religion;
		this.race = race;
		this.ideology = ideology;
	}

	
	public int getReligion() {
		return religion;
	}
	public int getRace() {
		return race;
	}


	public Ideology getIdeology() {

		
		return ideology;
	}
	
}
