package world;

import constants.Constants;

public class PopWants {

	private double wheat = 0.01;
	private double clothing = 0.005;
	private double furnuature = 0.001;
	private double iron = 0.0001;
	private double steel = 0.0001;
	private double cotton = 0.0001;
	
	private double wantsFurfilled = 1;
	
	
	public PopWants(int job) {
		// TODO: make diference on job
		int strata = Constants.jobToClass(job);
		
		//strata needs:
		
		
		switch(Constants.jobToClass(job)) {
		case Constants.UPPER_STRATA:
			//rich people needs
			wheat = wheat * 1.49;
			steel += 0.01;
			furnuature += 0.001;
			clothing += 0.001;
			//iron += 0.49;
			break;
		case Constants.MIDDLE_STRATA:
			//middle ppl needs
			//wheat += 0.549;
			steel += 0.001;
			furnuature += 0.0001;
			clothing += 0.001;
			break;
		case Constants.LOWER_STRATA:
			//low ppl needs
			//iron += +0.0001;
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
	public double[] getWants(int population, int job){
		
		double[] needs = new double[Constants.AMOUNT_OF_GOODS];

		needs[Constants.WHEAT] = wheat * population;
		needs[Constants.IRON] = iron * population;
		needs[Constants.STEEL] = steel * population;
		needs[Constants.CLOTHING] = clothing * population;
		needs[Constants.FURNUATURE] = furnuature * population;
		needs[Constants.COTTON] = cotton * population;
		
		return needs;	
	}


	public double getWantsFurfilled() {
		return wantsFurfilled;
	}


	public void setWantsFurfilled(double wantsFurfilled) {
		this.wantsFurfilled = wantsFurfilled;
	}



}
