package de.hhu.phil.persien.csinet;

import java.util.List;
import java.util.Random;

public abstract class AbstractSim {
	private Neighborhood ngh;
	private int step = -1;
	
	private final double[] initPosX = {49.0,89.0};
	private final double[] initPosY = {49.0,89.0};
	
	private double[] activations = new double[Constants.TAU+50];
	
	public AbstractSim() {
		this.calculateActivations();
		this.ngh = new Neighborhood();
	}
	
	public abstract void init();
	public abstract void update();
	
	/*
	 * Getters
	 */
	
	public Neighborhood getNeighborhood() { return this.ngh; }
	public int getStep() { return this.step; }
	public double getWidth() { return Constants.WIDTH; }
	public double getHeight() { return Constants.HEIGHT; }
		
	
	/*
	 * Most commponly used methods in the models
	 */
	
	protected double distance(Vector vector1, Vector vector2) {
		return Math.sqrt(Math.pow(vector1.getX()-vector2.getX(), 2.0) 
				+ Math.pow(vector1.getY()-vector2.getY(), 2.0));
	}

	private double window(Vector vector1, Vector vector2) {
		double sq_dist = this.distance(vector1,vector2);
		if (sq_dist < Constants.WINDOW) return 1.0;
		return 0.0;
	}

	private double decay(Vector vector) {
		return Math.exp(-(vector.getTime())/(double)Constants.TAU);
	}

	protected void calculateActivations() {
		for (int i=0;i<this.activations.length;i++) {
			this.activations[i] = Math.exp(-i/(double)Constants.TAU);
		}
	}

	protected double score(QuadTree clusters, Vector vector, int label, int context,
			int meaning) {
				double score = 0.0;
				boolean l = label == -1;
				boolean c = context == -1;
				boolean m = meaning == -1;
				
				for (Cluster memorized : clusters.getObjectsInRadius(vector.getX(), vector.getY(), Constants.WINDOW)) {
					if (l || memorized.getLabel() == label) {
						if (c || memorized.getContext() == context) {
							if (m || memorized.getMeaning() == meaning) {
								score += memorized.getVector().getActivation()/(this.distance(memorized.getVector(), vector)+1.0);
							}
						}
					}
				}
				return score;
			}

	protected void applyLenition(Vector vector) {
		double x = vector.getX();
		double y = vector.getY();
		
		if (x > Constants.TARGET_X) vector.setX(Math.max(Constants.TARGET_X, x-Constants.LENITION));
		else if (x < Constants.TARGET_X) vector.setX(Math.min(Constants.TARGET_X, x+Constants.LENITION));
		if (y > Constants.TARGET_Y) vector.setY(Math.max(Constants.TARGET_Y, y-Constants.LENITION));
		else if (y < Constants.TARGET_Y) vector.setY(Math.min(Constants.TARGET_Y, y+Constants.LENITION));
	}

	protected double[] scoresToProbs(QuadTree[] map, Vector vector, int label,
			int context, int meaning) {
				int size = map.length;
				double[] probs = new double[size];
				double sum = 0.0;
				
				for (int i=0;i<size;i++) {
					probs[i] = this.score(map[i], vector, label, context, meaning);
					sum += probs[i];
				}
				for (int i=0;i<size;i++) {
					probs[i] = probs[i]/sum;
				}
				return probs;
			}

	protected int hypFromProbs(double[] probs) {
		double F = 0.0;
		double rnd = new Random().nextDouble();
		for (int i=0;i<probs.length;i++) {
			F += probs[i];
			if (rnd <= F) return i;
		}
		return -1;
	}

	protected int evalProb(QuadTree[] map, Vector vector, int label,
			int context, int meaning) {
				double[] probs = this.scoresToProbs(map, vector, label, context, meaning);
				
				int hyp = this.hypFromProbs(probs);		
				
				return hyp;
			}

	protected void entrench(List<Vector> clusters, Vector vector) {
		double x = 0.0;
		double y = 0.0;
		double activation;
		double act_sum = 0.0;
		double dist;
		for (Vector memorized : clusters) {
			dist = this.distance(vector, memorized);
			if (dist <= Constants.WINDOW) {
				activation = memorized.getActivation()*1.0/(dist+1.0);
				x += memorized.getX()*activation;
				y += memorized.getY()*activation;
				act_sum += activation;
			}
		}
		x = x/act_sum;
		y = y/act_sum;
		vector.setXY(x, y);
	}

	protected double getNoise(Random rnd) {
			double noise = (double) (rnd.nextInt(Constants.NOISE2)-Constants.NOISE2/2);
			noise += rnd.nextDouble();
			return noise;
		}

	protected double restrain(double x, double max) {
		if (x < 0.0) x = 0.0;
		else if (x > max) x = max;
		return x;
	}

	
}
