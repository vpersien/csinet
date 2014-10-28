package de.hhu.phil.persien.csinet;

/*
 * ContextSim.java
 * 
 * In this simulation, two agents are communicating with each other by exchanging exemplars (Clusters), just like
 * in the first Simulation. This time, however, the speaker does not pick an exemplar with a particular label but
 * a meaning, i.e. another numerical property of clusters. Furthermore, clusters are enriched by a context (a
 * value between 0 and <CONTEXTS>). Every meaning is uniquely identified with a combination of context and label
 * (albeit not every one of such combinations is meaningful). The speaker chooses a meaning, picks a suitable 
 * exemplar and transmits its Vector and its context. The goal of the listener is to identify the intended 
 * meaning.
 * The identification process follows one of two routes, the bottom-up or the top-down route, depending on the
 * activation that the signal receives from similar exemplars. If the activation is high enough, the meaning is
 * evaluated based on the context and exemplars with a similar vector, disregarding the label. If, however, its
 * activation is too low, a label is guessed first and a meaning proposed based on context and this label. If
 * both the intended and the evaluated meaning match up, a new exemplar consisting of the perceived vector,
 * context and the evaluated meaning and label is added to the lexicon of the listener. Otherwise it is
 * discarded.
 * The probability with which a meaning is picked is determined by one out of several probability distributions
 * which can be selected beforehand. For a detailed description refer to the thesis.
 */

import java.util.Arrays;
import java.util.Random;

public class ContextSim extends AbstractSim {
	// Portion of labels that have been guessed right or wrong, respectively.
	// Used for statistics.
	private double[] right = new double[Constants.MEANINGS_NUM];
	private double[] wrong = new double[Constants.MEANINGS_NUM];

	private final double[] initPosX = {49.0,89.0};
	private final double[] initPosY = {49.0,89.0};
	
	private int[] contextByMeaning;
	
	private double[] meaningprobs;
	private Prob meaningCDF;
	
	public ContextSim() {
		super();
		this.initTypes();
	}
	
	public double[] getRight() { return this.right; }
	public double[] getWrong() { return this.wrong; }
	
	private void initTypes() {
		this.contextByMeaning = new int[Constants.MEANINGS_NUM];
		
		// TODO: Make probility distributions selectable in the Constants class
//		this.meaningprobs = this.getWeightedUniformDistribution(Constants.MEANINGS_NUM);
		this.meaningprobs = this.getTwoLabelCrossZipf(Constants.MEANINGS_NUM, 0);
//		this.meaningprobs = this.getDualZipf(Constants.MEANINGS_NUM);
//		this.meaningprobs = this.getMultiZipf(Constants.MEANINGS_NUM, Constants.LABELS_NUM);
		
		System.out.println(Arrays.toString(this.meaningprobs));
		
		this.meaningCDF = new Prob(this.meaningprobs);
	}

	@Override
	public void init() {
		// Add agents to the environment
		Agent agent1 = new Agent(this.ngh);
		Agent agent2 = new Agent(this.ngh);
		
		// New random number generator
		Random rnd = new Random(System.currentTimeMillis());
		
		// Initialize the agents' data structures
		agent1.setMaps(Constants.WIDTH, Constants.HEIGHT, Constants.LABELS_NUM, Constants.CONTEXTS_NUM, Constants.MEANINGS_NUM);
		agent2.setMaps(Constants.WIDTH, Constants.HEIGHT, Constants.LABELS_NUM, Constants.CONTEXTS_NUM, Constants.MEANINGS_NUM);
		
		// For every label and every context add <SEED> exemplars which are dislocated by a random amount from
		// their respective initial position
		int meaning = 0;
		for (int i=0;i<Constants.LABELS_NUM;i++) {
			double x = this.initPosX[i];
			double y = this.initPosY[i];
			
			for (int j=0;j<Constants.CONTEXTS_NUM;j++) {
				if (this.meaningprobs[meaning] > 0.0) {
					this.contextByMeaning[meaning] = j;
					for (int k=0;k<Constants.SEED;k++) {
						agent1.addExemplar(new Vector(this.restrain(x+this.getNoise(rnd), Constants.WIDTH),this.restrain(y+this.getNoise(rnd), Constants.HEIGHT)), i, j, meaning);
						agent2.addExemplar(new Vector(this.restrain(x+this.getNoise(rnd), Constants.WIDTH),this.restrain(y+this.getNoise(rnd), Constants.HEIGHT)), i, j, meaning);
					}
				}
				meaning++;
			}
		}
		
		// Set up neighborhood network, here consisting of only two agents
		this.ngh.add(agent1);
		this.ngh.add(agent2);
		this.ngh.addEdge(agent1, agent2);
	}
	
	@Override
	public void update() {
		// Increase step count
		this.step++;
		
		// Assign the roles of speaker and hearer randomly
		Agent speaker = this.ngh.getRandomAgent();
		Agent hearer = speaker.getRandomNeighbor();
		
		/*
		 * Production
		 */
		// Get random meaning and corresponding context (which is unique to the meaning)
		int meaning = this.meaningCDF.getRandomIndex();
		int context = this.contextByMeaning[meaning];
		
		// Get a corresponding signal (vector) from the speaker's lexicon
		Vector production = this.produce(speaker,meaning);
		
		// Skip if null
		if (production == null) return;
		
		/*
		 * Perception
		 */
		int meaninghyp = -1;
		int labelhyp = -1;
		double[] probs = new double[hearer.getClustersByLabel().length];
		
		// Evaluate the signal's score, i.e. how much activation does it receive at all by exemplars with
		// the same context?
		double score = this.score(hearer.getClustersByContext(context), production, -1, -1, -1);
		
		// If the score is greater than <THRESHOLD> do top-down processing, otherwise bottom-up
		if (score > Constants.THRESHOLD) {
			// Bottom-up processing
			// Evaluate a probable meaning considering the context of the signal
			meaninghyp = this.evalProb(hearer.getClusterByMeaning(), production, -1, context, -1);
			// Corroborate a label hypothesis based on context and the hypothesized meaning
			probs = this.scoresToProbs(hearer.getClustersByLabel(), production, -1, context, meaninghyp);
			labelhyp = this.hypFromProbs(probs);
		}
		else {
			// Top-down processing
			// Evaluate probable label considering the context of the signal
			probs = this.scoresToProbs(hearer.getClustersByLabel(), production, -1, context, -1);
			labelhyp = this.hypFromProbs(probs);
			// Corroborate a meaning hypothesis based on context and the hypothesized label
			meaninghyp = this.evalProb(hearer.getClusterByMeaning(), production, labelhyp, context, -1);
		}
		
		// If the label hypothesis is valid (i.e. >-1), add a new exemplar with the hypothesized meaning,
		// label and context to the inventory of the listener.
		if (labelhyp > -1) {
			if (meaninghyp == meaning) {
				this.setError(meaning, true);
				hearer.addExemplar(production, labelhyp, context, meaninghyp);
			}
			else this.setError(meaning, false);
		}
		
		// Tidy up the agents' exemplar spaces
		this.tidyUp();
	}
	
	// Finds and returns a production target
	private Vector produce(Agent agent, int meaning) {
		// Get random cluster by label
		Cluster cluster = agent.getRandomClusterByMeaning(meaning);
		
		// Return null if null (no cluster found)
		if (cluster == null) return null;
		
		// Build production target from this cluster
		Vector production = new Vector(cluster.getVector());
		
		// Get label and context
		int label = cluster.getLabel();
		int context = cluster.getContext();
		
		// Evaluate score considering exemplars of the same meaning
		double score = this.score(agent.getClustersByMeaning(meaning), production, -1, -1, -1);
		
		// Evaluate score of nearby exemplars with the same context but a different meaning
		double score2 = this.score(agent.getClustersByContext(context), production, -1, -1, -1)
				- this.score(agent.getClustersByContext(context), production, -1, -1, meaning);
		
		// If the chosen vector receives sufficient activation (both total and relative)
		// entrench in clusters of the same meaning (and apply lenition if the label is
		// marked for it), otherwise entrench in clusters of the same label.
		if (score > score2 && score > Constants.THRESHOLD) {
			this.entrench(agent.getClustersByMeaning(meaning), production);
			if (label == Constants.LENITED) {
				if (score > Constants.THRESHOLD) {
					this.applyLenition(production);
				}
			}
		}
		else {
			this.entrench(agent.getClustersByLabel(label), production);
		}

		// Apply noise
		Random rnd = new Random();
		
		double alpha = rnd.nextDouble()*360.0;
		double x = this.restrain(production.getX() + this.getNoise(rnd)*Math.sin(alpha), Constants.WIDTH);
		double y = this.restrain(production.getY() + this.getNoise(rnd)*Math.cos(alpha), Constants.HEIGHT);

		// Set new x and y values
		production.setXY(x, y);
		
		// Return production target
		return production;
	}
	
	// Adds 1 to <right[meaning]> if meaning <meaning> was transmitted truthfully,
	// i.e. <right> is true, adds 1 to <wrong[meaning]> otherwise.
	// Resets the crounters every 1000th step.
	private void setError(int meaning, boolean right) {
		if (this.step % 1000 == 1) {
			for (int i=0;i<Constants.MEANINGS_NUM;i++) {
				this.right[i] = 0;
				this.wrong[i] = 0;
			}
		}
		if (right) this.right[meaning] += 1;
		else this.wrong[meaning] += 1;
	}
	
	/*
	 * Utterance probability distributions
	 */
	
	// Returns a random distribution of length <len>. The highest possible
	// value is <a>/<len>.
	private double[] getRandomDistribution(int len, int a) {
		double[] out = new double[len];
		double limit = (1.0/len)*a;
		double F = 0.0;
		Random rnd = new Random();
		for (int i=0;i<len;i++) {
			if (i == len-1) {
				out[i] = F;
				break;
			}
			out[i] = rnd.nextDouble()*limit*(1.0-F);
			F -= out[i];
		}
		return out;
	}
	
	// Returns a Zipfian distribution for <cats> categories of length <len>
	// in such a way that the index for rank i of category j is j*l+i, where
	// l is the number of ranks per category.
	private double[] getMultiZipf(int len, int cats) {
		double[] out = new double[len];
		// Number of ranks per category
		int lenpercat = len/cats;
		// Get Zipfian distribution for <lenpercat> ranks
		double[] zipf = this.getZipf(lenpercat);
		
		double sum = 0.0;
		
		// Fill output array with Zipfian distribution for every category
		for (int i=0;i<lenpercat;i++) {
			for (int j=0;j<cats;j++) {
				out[j*lenpercat+i] = zipf[i];
			}
			sum += cats*zipf[i];
		}
		
		// Normalize
		for (int i=0;i<len;i++) {
			out[i] /= sum;
		}
		
		// Return distribution
		return out;
	}
	
	
	// Returns a Multi-Zipfian distribution (see above) for two categories,
	// the first category being <QUOTIENT> times as probable as the second
	// one.
	private double[] getDualZipf(int len) {
		double[] out = this.getMultiZipf(len, 2);
		double sum = 0.0;
		for (int i=0;i<len;i++) {
			if (i >= len/2) out[i] *= Constants.QUOTIENT;
			sum += out[i];
		}
		for (int i=0;i<len;i++) {
			out[i] /= sum;
		}
		return out;
	}
	
	// Returns a distribution consisting of two Zipfian distributions, one
	// for each category (out of two). Both distributions are arranged in
	// such a way that the indices of the second category are reversed and
	// entries of the first category are <QUOTIENT> times as probable as
	// ones of the second.
	private double[] getTwoLabelCrossZipf(int len, int offset) {
		double[] out = new double[len];
		int half = len/2;
		int limit = half-offset;
		double[] zipf = this.getZipf(half);
		double sum = 0.0;
		for (int i=0;i<half;i++) {
			double value = 0.0;
			if (i<limit) value = zipf[i];
			out[i] = value;
			out[len-1-i] = value*Constants.QUOTIENT;
			sum += out[i]+out[len-1-i];
		}
		for (int i=0;i<len;i++) {
			out[i] /= sum;
		}
		return out;
	}
	
	// Returns an array of length <len> including a Zipfian distribution.
	// Beware: This is not a probability distribution since the values
	// are not being normalized.
	private double[] getZipf(int len) {
		double[] out = new double[len];
		
		for (int i=0;i<len;i++) {
			out[i] = 1.0/(Math.pow((i+1),Constants.ZIPF_EXP));
		}
		return out;
	}
	
	// Returns an array of length <len> containing a uniform
	// distribution, i.e. every entry is set to 1/<len>.
	private double[] getUniformDistribution(int len) {
		double[] out = new double[len];
		double prob = 1.0/len;
		for (int i=0;i<len;i++) {
			out[i] = prob;
		}
		return out;
	}
	
	// Returns a distribution for two categories where the probabilities
	// within each category are uniformly distributed and entries of the
	// first category are <QUOTIENT> times as probable as ones of the
	// second.
	private double[] getWeightedUniformDistribution(int len) {
		double[] out = new double[len];
		double sum = 0.0;
		for (int i=0;i<len;i++) {
			out[i] = 1.0;
			if (i >= len/2) {
				out[i] *= Constants.QUOTIENT;
			}
			sum += out[i];
		}
		for (int i=0;i<len;i++) {
			out[i] /= sum;
		}
		return out;
	}
	
}
