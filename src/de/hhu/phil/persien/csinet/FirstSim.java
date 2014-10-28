package de.hhu.phil.persien.csinet;

/*
 * FirstSim.java
 * 
 * This is a simple agent-based simulation largely based on Pierrehumbert (2001). Here, two agents communicate with one another
 * by exchanging exemplars (here instances of the class Cluster). Every round one agent is assigned the role of the speaker
 * and the other one the role of the listener. The speaker randomly chooses a category label, based on the probability
 * distribution given by <labelprobs>, and picks an exemplar from its inventory which is marked for this label uniformly at
 * random. It then passes the perceivable content of this exemplar, a Vector instance, to the listener. The latter now compares
 * the incoming signal to already existing exemplars in its lexicon in order to find a suitable category label. Three factors
 * determine the likelihood of a label to be assigned: The relative frequency of exemplars marked for the label in the vicinity
 * of the percept, their activation and their distance to the percept. For every label a probability is determined and one of
 * these is assigned to the percept randomly based on this probability.
 * However, in some cases, depending on a success rate <SUCCESS> defined in the class Constants, the estimation of a label is
 * skipped and the percept is assigned the original label, e.g. if the success rate is set to 0.7, the original label is
 * assigned in 70% of all cases.
 * Furthermore, if a percept is added to the exemplar space of the listener is determined by one out of three "categorization
 * regimes" of which exactly one is activated during each simulation run. These are:
 * * No Competition: The exemplar is always added and always assigned the original label (i.e. success rate is 1.0),
 * * Pure Competition: The exemplar is always added and the label is evaluated by the procedure described above,
 * * Competition with Discards: The exemplar is only added if the evaluation picked out the original label.
 * Altering the success rate yields hybrids between No Competition and one of the two remaining regimes.
 */

import java.util.Random;

public class FirstSim extends AbstractSim {
	// (Approx. mean) initial x and y coords for the members of each label
	private final double[] initPosX = { 10.0, 90.0, 10.0, 90.0, 49.0 };
	private final double[] initPosY = { 10.0, 10.0, 55.0, 55.0, 90.0 };

	// Probability distribution of the labels
	private double[] labelprobs = { 0.2, 0.2, 0.2, 0.2, 0.2 };
	private Prob labelCDF;

	public FirstSim() {
		super();
		this.initProbs();
	}

	// Initialize the probability distribution of the labels
	private void initProbs() {
		this.labelCDF = new Prob(this.labelprobs);
	}

	@Override
	public void init() {
		// Add agents to the environment
		Agent agent1 = new Agent(this.ngh);
		Agent agent2 = new Agent(this.ngh);

		// New random number generator
		Random rnd = new Random(System.currentTimeMillis());

		// Initialize the agents' data structures
		agent1.setMaps(Constants.WIDTH, Constants.HEIGHT, Constants.LABELS_NUM,
				1, 1);
		agent2.setMaps(Constants.WIDTH, Constants.HEIGHT, Constants.LABELS_NUM,
				1, 1);

		// For every label add <SEED> exemplars which are dislocated by a random
		// amount from their respective initial position.
		for (int i = 0; i < Constants.LABELS_NUM; i++) {
			double x = this.initPosX[i];
			double y = this.initPosY[i];
			for (int j = 0; j < Constants.SEED; j++) {
				agent1.addExemplar(
						new Vector(this.restrain(x + this.getNoise(rnd),
								Constants.WIDTH), this.restrain(
								y + this.getNoise(rnd), Constants.HEIGHT)), i,
						0, 0);
				agent2.addExemplar(
						new Vector(this.restrain(x + this.getNoise(rnd),
								Constants.WIDTH), this.restrain(
								y + this.getNoise(rnd), Constants.HEIGHT)), i,
						0, 0);
			}
		}

		// Set up the neighborhood network, here consisting of only two agents.
		this.ngh.add(agent1);
		this.ngh.add(agent2);
		this.ngh.addEdge(agent1, agent2);
	}

	@Override
	public void update() {
		// Increase the step count
		this.step++;

		// Assign the roles of speaker and hearer randomly
		Agent speaker = this.ngh.getRandomAgent();
		Agent hearer = speaker.getRandomNeighbor();

		/*
		 * Production
		 */
		// Get random label
		int label = labelCDF.getRandomIndex();

		// Get a corresponding signal (vector) from the speaker's lexicon
		Vector production = this.produce(speaker, label);

		// Skip if null
		if (production == null)
			return;

		/*
		 * Perception
		 */
		int labelhyp = -1;
		double[] probs = new double[hearer.getClustersByLabel().length];

		Random rnd = new Random();

		// Get label probabilities based on their scores
		probs = this.scoresToProbs(hearer.getClustersByLabel(), production, -1,
				-1, -1);

		// Draw a number between 0 and 1. If it is greater than <SUCCESS>, the
		// hearer has to guess the label identity considering the just
		// calculated probabilities (first case), otherwise the label 
		// hypothesis will equal the category label it originally stemmed from.
		if (rnd.nextDouble() > Constants.SUCCESS) {
			labelhyp = this.hypFromProbs(probs);
		} else
			labelhyp = label;

		// Add exemplar based on the current categorization regime
		// TODO: Make regime selectable in the Constants class
		if (labelhyp > -1 && labelhyp == label) { // Competition with Discards
			// if (labelhyp > -1) { // Pure Competition
			// Save certainty of the label assignment
			production.setCertainty(probs[labelhyp]);
			// Decide whether the exemplar is to be added to the hearer's
			// exemplar space
			// based on its certainty (plus 0.4 to weaken its influence
			// somewhat)
			if (rnd.nextDouble() < production.getCertainty() + 0.4) {
				hearer.addExemplar(production, labelhyp, 0, 0);
			}
		}

		// Tidy up the agents' exemplar spaces
		this.tidyUp();

	}

	// Findsand returns a production target
	private Vector produce(Agent agent, int label) {
		// Get random cluster by label
		Cluster cluster = agent.getRandomClusterByLabel(label);

		// Return null if null (no cluster found)
		if (cluster == null)
			return null;

		// Build production target from this cluster
		Vector production = new Vector(cluster.getVector());

		// Entrench in surrounding exemplars of the same label
		this.entrench(agent.getClustersByLabel(label), production);

		Random rnd = new Random();

		// If its label is marked for lenition, apply lenition
		if (label == Constants.LENITED)
			this.applyLenition(production);

		// Apply noise in a random angle around the (entrenched) production
		// target
		double alpha = rnd.nextDouble() * 360.0;
		double x = this.restrain(
				production.getX() + this.getNoise(rnd) * Math.sin(alpha),
				Constants.WIDTH);
		double y = this.restrain(
				production.getY() + this.getNoise(rnd) * Math.cos(alpha),
				Constants.HEIGHT);

		// Set new coordinates
		production.setXY(x, y);

		// Return production target
		return production;
	}

}
