package de.hhu.phil.persien.csinet;

import java.util.Arrays;
import java.util.Random;

public class ContextSim extends AbstractSim {

	private Neighborhood ngh;
	private int step = -1;
	
	private double[] right = new double[Constants.MEANINGS_NUM];
	private double[] wrong = new double[Constants.MEANINGS_NUM];

	private final double[] initPosX = {49.0,89.0};
	private final double[] initPosY = {49.0,89.0};
	
	private int[] contextByMeaning;
	
	private double[] meaningprobs;
	private Prob meaningCDF;
	
	private double[] activations = new double[Constants.TAU+50];
	
	public ContextSim() {
		super();
		this.initTypes();
	}
	
	public double[] getRight() { return this.right; }
	public double[] getWrong() { return this.wrong; }
	
	private void initTypes() {
		this.contextByMeaning = new int[Constants.MEANINGS_NUM];
		
//		this.meaningprobs = this.getWeightedUniformDistribution(Constants.MEANINGS_NUM);
		this.meaningprobs = this.getTwoLabelInverseZipf(Constants.MEANINGS_NUM, 0);
//		this.meaningprobs = this.getDualZipf(Constants.MEANINGS_NUM);
//		this.meaningprobs = this.getMultiZipf(Constants.MEANINGS_NUM, Constants.LABELS_NUM);
		
		System.out.println(Arrays.toString(this.meaningprobs));
		
		this.meaningCDF = new Prob(this.meaningprobs);
	}

	@Override
	public void init() {
		Agent agent1 = new Agent(this.ngh);
		Agent agent2 = new Agent(this.ngh);
		
		Random seed = new Random();
		Random rnd = new Random(seed.nextLong());
		
		agent1.setMaps(Constants.WIDTH, Constants.HEIGHT, Constants.LABELS_NUM, Constants.CONTEXTS_NUM, Constants.MEANINGS_NUM);
		agent2.setMaps(Constants.WIDTH, Constants.HEIGHT, Constants.LABELS_NUM, Constants.CONTEXTS_NUM, Constants.MEANINGS_NUM);
		
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
		
		this.ngh.add(agent1);
		this.ngh.add(agent2);
		this.ngh.addEdge(agent1, agent2);
	}
	
	@Override
	public void update() {
		this.step++;
		
		Agent speaker = this.ngh.getRandomAgent();
		Agent hearer = speaker.getRandomNeighbor();
		
		int meaning = this.meaningCDF.getRandomIndex();
		int context = this.contextByMeaning[meaning];
		Vector production = this.produce(speaker,meaning);
		if (production == null) return;
		
		double score = this.score(hearer.getClustersByContext(context), production, -1, -1, -1);
		int meaninghyp = -1;
		int labelhyp = -1;
		double[] probs = new double[hearer.getClustersByLabel().length];

		if (score > Constants.THRESHOLD) {
			/*
			 * Top-Down
			 */
			meaninghyp = this.evalProb(hearer.getClusterByMeaning(), production, -1, context, -1);
			probs = this.scoresToProbs(hearer.getClustersByLabel(), production, -1, context, meaninghyp);
			labelhyp = this.hypFromProbs(probs);
			production.setCertainty(probs[labelhyp]);
		}
		else {
			/*
			 * Bottom-Up
			 */
			probs = this.scoresToProbs(hearer.getClustersByLabel(), production, -1, context, -1);
			labelhyp = this.hypFromProbs(probs);
			production.setCertainty(probs[labelhyp]);
			meaninghyp = this.evalProb(hearer.getClusterByMeaning(), production, labelhyp, context, -1);
		}
		
		if (labelhyp > -1) {
			if (meaninghyp == meaning) {
				this.setError(meaning, true);
				if (new Random().nextDouble() < production.getCertainty()*2.0-0.4) {
					hearer.addExemplar(production, labelhyp, context, meaninghyp);
				}
			}
			else this.setError(meaning, false);
		}
		
		if (this.step > Constants.TAU) {
			for (Agent agent : this.ngh.getNodes()) {
				agent.tidyUp(Constants.TAU);
				for (Vector vector : agent.getVectors()) {
					vector.incrementTime();
					vector.setActivation(this.activations[vector.getTime()]);
				}
			}
		}
	}
	
	private Vector produce(Agent agent, int meaning) {
		int label;
		
		Cluster cluster = agent.getRandomClusterByMeaning(meaning);
		if (cluster == null) return null;
		Vector production = new Vector(cluster.getVector());
		label = cluster.getLabel();
		int context = cluster.getContext();
		
		
		double score = this.score(agent.getClustersByMeaning(meaning), production, -1, -1, -1);
		double score2 = this.score(agent.getClustersByContext(context),production, -1, -1, -1)
				- this.score(agent.getClustersByContext(context), production, -1, -1, meaning);
		
		if (score > score2 && score > Constants.THRESHOLD2) {
			this.entrench(agent.getClustersByMeaning(meaning), production);
			if (label == Constants.LENITED) {
				if (score > Constants.THRESHOLD2) {
					this.applyLenition(production);
				}
			}
		}
		else {
			this.entrench(agent.getClustersByLabel(label), production);
		}

		
		Random rnd = new Random();
		
		double alpha = rnd.nextDouble()*360.0;
		double x = this.restrain(production.getX() + this.getNoise(rnd)*Math.sin(alpha), Constants.WIDTH);
		double y = this.restrain(production.getY() + this.getNoise(rnd)*Math.cos(alpha), Constants.HEIGHT);

		
		production.setXY(x, y);
		
		return production;
	}
	
	
		
	private void entrench(QuadTree clusters, Vector vector) {
		double x = 0.0;
		double y = 0.0;
		double activation;
		double act_sum = 0.0;
		for (Cluster memorized : clusters.getObjectsInRadius(vector.getX(), vector.getY(), Constants.WINDOW)) {
			activation = memorized.getVector().getActivation()*1.0/(this.distance(vector, memorized.getVector())+1.0);
			x += memorized.getVector().getX()*activation;
			y += memorized.getVector().getY()*activation;
			act_sum += activation;
		}
		x = x/act_sum;
		y = y/act_sum;
		vector.setXY(x, y);
	}
	
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
	
	private double[] getMultiZipf(int len, int cats) {
		double[] out = new double[len];
		int lenpercat = len/cats;
		double[] zipf = this.getZipf(lenpercat);
		double sum = 0.0;
		for (int i=0;i<lenpercat;i++) {
			for (int j=0;j<cats;j++) {
				out[j*lenpercat+i] = zipf[i];
			}
			sum += cats*zipf[i];
		}
		for (int i=0;i<len;i++) {
			out[i] /= sum;
		}
		return out;
	}
	
	private double[] getTwoLabelInverseZipf(int len, int offset) {
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
	
	private double[] getZipf(int len) {
		double[] out = new double[len];
		
		double sum = 0.0;
		for (int i=0;i<len;i++) {
			out[i] = 1.0/(Math.pow((i+1),Constants.ZIPF_EXP));
			sum += out[i];
		}
		return out;
	}
	
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
	
	private double[] getUniformDistribution(int len) {
		double[] out = new double[len];
		double prob = 1.0/len;
		for (int i=0;i<len;i++) {
			out[i] = prob;
		}
		return out;
	}
	
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
	
}
