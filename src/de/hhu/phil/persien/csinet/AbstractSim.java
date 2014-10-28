package de.hhu.phil.persien.csinet;

import java.util.Random;

public abstract class AbstractSim {
	protected Neighborhood ngh;		// the agent network
	protected int step = -1;		// initial step number
	
	// activations by time step for quicker access
	protected double[] activations = new double[Constants.TAU+50];
	
	
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
	
	// Calculates the euclidean distance between two vectors/points
	protected double distance(Vector vector1, Vector vector2) {
		return Math.sqrt(Math.pow(vector1.getX()-vector2.getX(), 2.0) 
				+ Math.pow(vector1.getY()-vector2.getY(), 2.0));
	}
	
	// Window function that returns 1.0 iff the distance between two vectors is <WINDOW>
	// and 0.0 otherwise
	// (never used)
	protected double window(Vector vector1, Vector vector2) {
		double sq_dist = this.distance(vector1,vector2);
		if (sq_dist < Constants.WINDOW) return 1.0;
		return 0.0;
	}

	// Calculates the activation of a vector from its age
	// (never used)
	protected double decay(Vector vector) {
		return Math.exp(-(vector.getTime())/(double)Constants.TAU);
	}

	// Calculates all possible activation values
	protected void calculateActivations() {
		for (int i=0;i<this.activations.length;i++) {
			this.activations[i] = Math.exp(-i/(double)Constants.TAU);
		}
	}

	// Returns a score for a vector <vector> given a label, a context, and a meaning. Refer
	// to the text for an explanation of its usage.
	protected double score(QuadTree clusters, Vector vector, int label, int context,
			int meaning) {
				double score = 0.0;
				// if the value for label, context or meaning equals -1, it will not be considered in the calculation
				// of the score.
				boolean l = label == -1;
				boolean c = context == -1;
				boolean m = meaning == -1;
				
				// browse through all clusters within a radius of <WINDOW> around <vector>.
				for (Cluster memorized : clusters.getObjectsInRadius(vector.getX(), vector.getY(), Constants.WINDOW)) {
					// if <label> is not to be considered (i.e. == -1) or matches the label of the current cluster and
					if (l || memorized.getLabel() == label) {
						// if ... (analogous) and
						if (c || memorized.getContext() == context) {
							// if ... (anologous)
							if (m || memorized.getMeaning() == meaning) {
								// add to <score> the activation-weighted distance of the two vectors.
								score += memorized.getVector().getActivation()/(this.distance(memorized.getVector(), vector)+1.0);
							}
						}
					}
				}
				return score;
			}
	
	// Given a vector <vector>, this method calculates its score for all categories that are represented by the quadtrees
	// in <map> (c.f. class Agent for clarification). Then the probability for each category is calculated by normalization
	// with the summed scores. 
	protected double[] scoresToProbs(QuadTree[] map, Vector vector, int label,
			int context, int meaning) {
				int size = map.length;
				double[] probs = new double[size];
				double sum = 0.0;
				
				// calculate all scores
				for (int i=0;i<size;i++) {
					probs[i] = this.score(map[i], vector, label, context, meaning);
					sum += probs[i];
				}
				// normalize
				for (int i=0;i<size;i++) {
					probs[i] = probs[i]/sum;
				}
				return probs;
			}
	
	// Given a probability distribution stored in <probs>, this method returns a random index based on this probability
	// representing a hypothesis in the model.
	protected int hypFromProbs(double[] probs) {
		double F = 0.0;
		double rnd = new Random().nextDouble();
		for (int i=0;i<probs.length;i++) {
			F += probs[i];
			if (rnd <= F) return i;
		}
		return -1;
	}
	
	// Calls scoresToProbs and hypFromProbs.
	protected int evalProb(QuadTree[] map, Vector vector, int label,
			int context, int meaning) {
				double[] probs = this.scoresToProbs(map, vector, label, context, meaning);
				
				int hyp = this.hypFromProbs(probs);		
				
				return hyp;
			}

	// Relocates a given vector by an amount of <LENITION> towards a target position defined by <TARGET_X> and
	// <TARGET_Y>
	protected void applyLenition(Vector vector) {
		double x = vector.getX();
		double y = vector.getY();
		
		if (x > Constants.TARGET_X) vector.setX(Math.max(Constants.TARGET_X, x-Constants.LENITION));
		else if (x < Constants.TARGET_X) vector.setX(Math.min(Constants.TARGET_X, x+Constants.LENITION));
		if (y > Constants.TARGET_Y) vector.setY(Math.max(Constants.TARGET_Y, y-Constants.LENITION));
		else if (y < Constants.TARGET_Y) vector.setY(Math.min(Constants.TARGET_Y, y+Constants.LENITION));
	}
	
	// Entrenches a vector <vector> in a cloud of other vectors stored in <clusters>
	protected void entrench(QuadTree clusters, Vector vector) {
		double x = 0.0;			// new x coordinate
		double y = 0.0;			// new y coordinate
		double activation;
		double act_sum = 0.0;	// sum of all observed distance-weighted activations
		
		// For each cluster in a radius of <WINDOW> around the vectors position
		for (Cluster memorized : clusters.getObjectsInRadius(vector.getX(), vector.getY(), Constants.WINDOW)) {
			// calculate its distance-weighted activation
			activation = memorized.getVector().getActivation()*1.0/(this.distance(vector, memorized.getVector())+1.0);
			// update x and y weighted by this activation
			x += memorized.getVector().getX()*activation;
			y += memorized.getVector().getY()*activation;
			// update sum of all activations
			act_sum += activation;
		}
		// calculate average x and y values
		x = x/act_sum;
		y = y/act_sum;
		// update the vector
		vector.setXY(x, y);
	}
	
	// Calculate noise (random double) within a window of -<NOISE2>/2 and <NOISE2>
	protected double getNoise(Random rnd) {
			double noise = (double) (rnd.nextInt(Constants.NOISE)-Constants.NOISE/2);
			noise += rnd.nextDouble();
			return noise;
		}

	// Returns 0.0 iff x < 0.0, max if x > max, and x otherwise
	protected double restrain(double x, double max) {
		if (x < 0.0) x = 0.0;
		else if (x > max) x = max;
		return x;
	}

	protected void tidyUp() {
		for (Agent agent : this.ngh.getNodes()) {
			agent.tidyUp(Constants.TAU);
			for (Vector vector : agent.getVectors()) {
				vector.incrementTime();
				vector.setActivation(this.activations[vector.getTime()]);
			}
		}
	}
}
