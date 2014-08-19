package de.hhu.phil.persien.csinet;

public interface ISimulation {
	public double getWidth();
	public double getHeight();
	public int getStep();
	public Neighborhood getNeighborhood();
	
	public void init();
	public void update();
}
