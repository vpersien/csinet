package de.hhu.phil.persien.csinet;

import java.util.Random;

public class FirstSim extends AbstractSim {
	private Neighborhood ngh;
	private int step = -1;
	
	private final double[] initPosX = {10.0,90.0,10.0,90.0,49.0};
	private final double[] initPosY = {10.0,10.0,55.0,55.0,90.0};
	
	private double[] labelprobs = {0.2,0.2,0.2,0.2,0.2};
	private Prob labelCDF;
	
	double[] activations = new double[Constants.TAU+10];
	
	public FirstSim() {
		super();
		this.initProbs();
	}
	
	private void initProbs() {
		this.labelCDF = new Prob(this.labelprobs);
	}

	
	@Override
	public void init() {
		Agent agent1 = new Agent(this.ngh);
		Agent agent2 = new Agent(this.ngh);
		
		Random seed = new Random();
		Random rnd = new Random(seed.nextLong());
		
		
		agent1.setMaps(Constants.WIDTH, Constants.HEIGHT, Constants.LABELS_NUM, 1, 1);
		agent2.setMaps(Constants.WIDTH, Constants.HEIGHT, Constants.LABELS_NUM, 1, 1);
		
		for (int i=0;i<Constants.LABELS_NUM;i++) {
			double x = this.initPosX[i];
			double y = this.initPosY[i];
			for (int j=0;j<Constants.SEED;j++) {
				agent1.addExemplar(new Vector(this.restrain(x+this.getNoise(rnd), Constants.WIDTH),this.restrain(y+this.getNoise(rnd), Constants.HEIGHT)), i, 0, 0);
				agent2.addExemplar(new Vector(this.restrain(x+this.getNoise(rnd), Constants.WIDTH),this.restrain(y+this.getNoise(rnd), Constants.HEIGHT)), i, 0, 0);
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
		int label = labelCDF.getRandomIndex();

		Vector production = this.produce(speaker,label);
		if (production == null) return;
		
		int labelhyp = -1;
		double[] probs = new double[hearer.getClustersByLabel().length];
		
		Random rnd = new Random();
		
		probs = this.scoresToProbs(hearer.getClustersByLabel(), production, -1, -1, -1);
		
		if (rnd.nextDouble() > Constants.SUCCESS) {
			labelhyp = this.hypFromProbs(probs);
		}
		else labelhyp = label;

		if (labelhyp > -1 && labelhyp == label) {
//		if (labelhyp > -1) {
			production.setCertainty(probs[labelhyp]);
			if (rnd.nextDouble() < production.getCertainty()+0.4) {
				hearer.addExemplar(production, labelhyp, 0, 0);
			}
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
	
	private Vector produce(Agent agent, int label) {
		
		Cluster cluster = agent.getRandomClusterByLabel(label);
		if (cluster == null) return null;
		Vector production = new Vector(cluster.getVector());		
		
		this.entrench(agent.getClustersByLabel(label), production);
		
		Random rnd = new Random();
		
		if (label == Constants.LENITED) this.applyLenition(production);

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
		int sum = 0;
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
}
