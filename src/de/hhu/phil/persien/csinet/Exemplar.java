package de.hhu.phil.persien.csinet;

public class Exemplar {

	protected double x;
	protected double y;
	private double activation;
	private String label;
	private int time;
	
	public Exemplar(double x, double y, String label, int time) {
		this.x = x;
		this.y = y;
		this.activation = 1;
		this.label = label;
		this.time = time;
	}
	
	// TODO VORLÄUFIG!
	public Exemplar(Exemplar exemplar) {
		this(exemplar.x, exemplar.y, exemplar.label, exemplar.time);
	}
	
	/*
	 * Getters
	 */
	
	public double getX() { return this.x; }
	public double getY() { return this.y; }
	public double[] getVector() {
		double[] vector = new double[2];
		return vector;
	}
	public double getActivation() { return this.activation; }
	public String getLabel() { return this.label; }
	public int getTime() { return this.time; }
	
	/*
	 * Setters
	 */
	
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setXY(double x, double y) {
		this.setX(x);
		this.setY(y);
	}
	public void setVector(double[] vector) {
		this.setX(vector[0]);
		this.setY(vector[1]);
	}
	public void addToX(double dx) { this.x += dx; }
	public void addToY(double dy) { this.y += dy; }
	public void addToXY(double dx, double dy) {
		this.addToX(dx);
		this.addToY(dy);
	}
	public void setActivation(double activation) { this.activation = activation; }
	public void setLabel(String label) { this.label = label; }
	public void setTime(int time) { this.time = time; }
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Exemplar)) return false;
		if (((Exemplar) o).x == this.x && ((Exemplar) o).y == this.y) return true;
		return false;
	}
}
