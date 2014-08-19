package de.hhu.phil.persien.csinet;

import java.util.Random;

public class Vector {
	
	private double x;
	private double y;
	private double activation;
	private int time;
	private double certainty;
	private long id;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
		this.time = 0;
		this.activation = 1.0;
		this.certainty = -1.0;
		this.id = new Random().nextLong();
	}
	
	public Vector(Vector vector) {
		this(vector.x,vector.y);
	}
	
	public double getX() { return this.x; }
	public double getY() { return this.y; }
	public double[] getVector() {
		double[] vector = new double[2];
		return vector;
	}
	public double getActivation() { return this.activation; }
	public int getTime() { return this.time; }
	public double getCertainty() { return this.certainty; }
	
	
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setXY(double x, double y) {
		this.setX(x);
		this.setY(y);
	}
	public void setActivation(double activation) { this.activation = activation; }
	public void setTime(int time) { this.time = time; }
	public void setCertainty(double certainty) { this.certainty = certainty; }
	public void incrementTime() { this.time++; }

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		long temp;
//		temp = Double.doubleToLongBits(x);
//		result = prime * result + (int) (temp ^ (temp >>> 32));
//		temp = Double.doubleToLongBits(y);
//		result = prime * result + (int) (temp ^ (temp >>> 32));
//		return result;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vector))
			return false;
		Vector other = (Vector) obj;
//		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
//			return false;
//		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
//			return false;
		if (Math.sqrt(Math.pow(this.x-other.x, 2.0)+Math.pow(this.y-other.y, 2.0)) < Constants.JND
//				&& this.id == other.id
				) {
			return true;
		}
		return false;
	}
	

}
