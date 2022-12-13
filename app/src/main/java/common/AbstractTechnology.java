package common;

public class AbstractTechnology {

	//production factors
	private double farmerInputModifier = 1;
	private double farmerThroughputModifier = 1;
	private double farmerOutputModifier = 1;
	private double labourerInputModifier = 1;
	private double labourerThroughputModifier = 1;
	private double labourerOutputModifier = 1;
	private double artesanInputModifier = 1;
	private double artesanThroughputModifier = 1;
	private double artesanOutputModifier = 1;
	private double factoryInputModifier = 1;
	private double factoryThroughputModifier = 1;
	private double factoryOutputModifier = 1;
	
	public double getFarmerInputModifier() {
		return farmerInputModifier;
	}
	public void setFarmerInputModifier(double farmerInputModifier) {
		this.farmerInputModifier = farmerInputModifier;
	}
	public double getFarmerOutputModifier() {
		return farmerOutputModifier;
	}
	public void setFarmerOutputModifier(double farmerOutputModifier) {
		this.farmerOutputModifier = farmerOutputModifier;
	}
	public double getLabourerInputModifier() {
		return labourerInputModifier;
	}
	public void setLabourerInputModifier(double labourerInputModifier) {
		this.labourerInputModifier = labourerInputModifier;
	}
	public double getLabourerOutputModifier() {
		return labourerOutputModifier;
	}
	public void setLabourerOutputModifier(double labourerOutputModifier) {
		this.labourerOutputModifier = labourerOutputModifier;
	}
	public double getArtesanInputModifier() {
		return artesanInputModifier;
	}
	public void setArtesanInputModifier(double artesanInputModifier) {
		this.artesanInputModifier = artesanInputModifier;
	}
	public double getArtesanOutputModifier() {
		return artesanOutputModifier;
	}
	public void setArtesanOutputModifier(double artesanOutputModifier) {
		this.artesanOutputModifier = artesanOutputModifier;
	}
	public double getFactoryInputModifier() {
		return factoryInputModifier;
	}
	public void setFactoryInputModifier(double factoryInputModifier) {
		this.factoryInputModifier = factoryInputModifier;
	}
	public double getFactoryOutputModifier() {
		return factoryOutputModifier;
	}
	public void setFactoryOutputModifier(double factoryOutputModifier) {
		this.factoryOutputModifier = factoryOutputModifier;
	}
	public double getFarmerThroughputModifier() {
		return farmerThroughputModifier;
	}
	public void setFarmerThroughputModifier(double farmerThroughputModifier) {
		this.farmerThroughputModifier = farmerThroughputModifier;
	}
	public double getLabourerThroughputModifier() {
		return labourerThroughputModifier;
	}
	public void setLabourerThroughputModifier(double labourerThroughputModifier) {
		this.labourerThroughputModifier = labourerThroughputModifier;
	}
	public double getArtesanThroughputModifier() {
		return artesanThroughputModifier;
	}
	public void setArtesanThroughputModifier(double artesanThroughputModifier) {
		this.artesanThroughputModifier = artesanThroughputModifier;
	}
	public double getFactoryThroughputModifier() {
		return factoryThroughputModifier;
	}
	public void setFactoryThroughputModifier(double factoryThroughputModifier) {
		this.factoryThroughputModifier = factoryThroughputModifier;
	}
	
}
