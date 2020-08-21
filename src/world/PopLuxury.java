package world;

import constants.Constants;

public class PopLuxury {

	private double wheat = 0.01;
	private double clothing = 0.1000;
	private double furnuature = 0.120000;
	private double iron = 0.00000;
	private double steel = 0.100000;
	private double cotton = 0.010000;
	
	
	private double modifier = 1;
	
	private double luxuryFurfilled = 1;
	
	public PopLuxury(int job) {
		// TODO: make diference on job
		int strata = Constants.jobToClass(job);
		
		//strata needs:
		
		
		switch(Constants.jobToClass(job)) {
		case Constants.UPPER_STRATA:
			//rich people needs
			//wheat = wheat * 1.49;
			//steel += 0.01;
			//iron += 0.49;
			break;
		case Constants.MIDDLE_STRATA:
			//middle ppl needs
			//wheat += 0.549;
			//steel += 0.001;
			break;
		case Constants.LOWER_STRATA:
			//low ppl needs
			//iron += +0.0001;
			break;
		case Constants.LOWEST_STRATA:
			//lowest ppl needs
			modifier = 0.1;
			break;
		}
	}
	
	
	/**
	 * returns a double array, with the indexes representing the different goods
	 * 
	 * adjusts need with population number
	 * 
	 * @return
	 */
	public double[] getLuxury(int population, int job){
		
		double[] needs = new double[Constants.AMOUNT_OF_GOODS];

		
		needs[Constants.WHEAT] = wheat * population * modifier;
		needs[Constants.IRON] = iron * population * modifier;
		needs[Constants.STEEL] = steel * population * modifier;
		needs[Constants.CLOTHING] = clothing * population * modifier;
		needs[Constants.FURNUATURE] = furnuature * population * modifier;
		needs[Constants.COTTON] = cotton * population * modifier;
		
		return needs;	
	}


	public double getLuxuryFurfilled() {
		return luxuryFurfilled;
	}


	public void setLuxuryFurfilled(double luxuryFurfilled) {
		this.luxuryFurfilled = luxuryFurfilled;
	}

}
