package world;

import constants.Constants;

public class PopNeeds {

	private double wheat = 0.005;
	private double iron = 0.0001;
	private double steel = 0.001;
	
	
	public PopNeeds(int job) {
		// TODO: make diference on job
	}
	
	
	/**
	 * returns a double array, with the indexes representing the different goods
	 * 
	 * adjusts need with population number
	 * 
	 * @return
	 */
	public double[] getNeeds(int population){
		
		double[] needs = new double[Constants.AMOUNT_OF_GOODS];
		
		needs[Constants.WHEAT] = wheat * population;
		needs[Constants.IRON] = iron * population;
		needs[Constants.STEEL] = steel * population;
		
		return needs;	
	}

}
