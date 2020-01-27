package world;

public class JobParameters {

	private	double serf;
	private	double slave;
	private	double farmer;
	private	double laborer;
	private	double soldier;
	private	double artisan;
	private	double craftsman;
	private	double clerk;
	private	double capitalist;
	private	double clergyman;
	private	double aristocrat;
	private	double officer;
	
	/**
	 * 
	 * @param serf
	 * @param slave
	 * @param farmer
	 * @param laborer
	 * @param soldier
	 * @param artisan
	 * @param craftsman
	 * @param clerk
	 * @param capitalist
	 * @param clergyman
	 * @param aristocrat
	 * @param officer
	 */
	public JobParameters(int serf, int slave, int farmer, int laborer, int soldier, int artisan,
			int craftsman, int clerk, int capitalist, int clergyman, int aristocrat, int officer) {
		
		double total = serf + slave + farmer + laborer + soldier + artisan
				+ craftsman + clerk	+ capitalist + clergyman + aristocrat + officer;

		
		this.serf = serf/total;
		this.slave = slave/total;
		this.farmer = farmer/total;
		this.laborer = laborer/total;
		this.soldier = soldier/total;
		this.artisan = artisan/total;
		this.craftsman = craftsman/total;
		this.clerk = clerk/total;
		this.capitalist = capitalist/total;
		this.clergyman = clergyman/total;
		this.aristocrat = aristocrat/total;
		this.officer = officer/total;
	}


	public double getSerfPercentage() {
		return serf;
	}
	public double getSlavePercentage() {
		return slave;
	}
	public double getFarmerPercentage() {
		return farmer;
	}
	public double getLaborerPercentage() {
		return laborer;
	}
	public double getSoldierPercentage() {
		return soldier;
	}
	public double getArtisanPercentage() {
		return artisan;
	}
	public double getCraftsmanPercentage() {
		return craftsman;
	}
	public double getClerkPercentage() {
		return clerk;
	}
	public double getClergymanPercentage() {
		return clergyman;
	}
	public double getCapitalistPercentage() {
		return capitalist;
	}
	public double getAristocratPercentage() {
		return aristocrat;
	}
	public double getOfficerPercentage() {
		return officer;
	}
}
