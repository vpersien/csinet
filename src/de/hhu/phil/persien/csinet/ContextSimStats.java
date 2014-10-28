package de.hhu.phil.persien.csinet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ContextSimStats extends AbstractStats {

	public ContextSimStats(AbstractSim sim) {
		super(sim);
	}
	
	
	private class ClusterXComparator implements Comparator<Cluster> {
		@Override
		public int compare(Cluster o1, Cluster o2) {
			return Double.compare(o1.getVector().getX(), o2.getVector().getX());
		}
	}
	
	private class ClusterYComparator implements Comparator<Cluster> {
		@Override
		public int compare(Cluster o1, Cluster o2) {
			return Double.compare(o1.getVector().getY(), o2.getVector().getY());
		}
	}
	
	
	public void update() {
		if (!this.recording) return;
		
		String str;
		
		str = this.getInitString();
		str += this.arrayToString(this.getErrorRate());
		this.appendToFile("data/errorrates.csv", str);
		
		str = this.getInitString();
		str += this.arrayToString(this.getQuartiles(this.agent));
		this.appendToFile("data/quantiles.csv", str);
		
		str = this.getInitString();
		str += this.arrayToString(this.getMeanCoordinates(this.agent.getClustersByLabel()));
		this.appendToFile("data/mean_labels.csv", str);
		
		str = this.getInitString();
		str += this.arrayToString(this.getMeanCoordinates(this.agent.getClusterByMeaning()));
		this.appendToFile("data/mean_meanings.csv", str);
	}
	

	private double[] getMeanCoordinates(QuadTree[] set) {
		int size = set.length;
		double[] out = new double[2*size];
		
		for (int i=0;i<size;i++) {
			double activation = 0.0;
			double act_sum = 0.0;
			double x = 0.0;
			double y = 0.0;
			for (Cluster cluster : set[i]) {
				activation = cluster.getVector().getActivation();
				x += cluster.getVector().getX()*activation;
				y += cluster.getVector().getY()*activation;
				act_sum += activation;
			}
			out[2*i] = x/act_sum;
			out[2*i+1] = y/act_sum;
		}
		
		return out;
	}
	
	public double[] getErrorRate() {
		double[] out = new double[Constants.MEANINGS_NUM];
		ContextSim cSim = (ContextSim) this.sim;
		for (int i=0;i<Constants.MEANINGS_NUM;i++) {
			out[i] = cSim.getRight()[i]/(cSim.getRight()[i]+cSim.getWrong()[i]);
		}
		return out;
	}
	
	public double[] getQuartiles(Agent agent) {
		double[] out = new double[6*Constants.LABELS_NUM];
		List<Cluster> current;
		
		for (int i=0;i<Constants.LABELS_NUM;i++) {
			current = agent.getClustersByLabel(i).flatten();
			current.sort(new ClusterXComparator());
			out[6*i+0] = this.getQuantileX(current, 6, 1);
			out[6*i+1] = this.getQuantileX(current, 6, 3);
			out[6*i+2] = this.getQuantileX(current, 6, 5);
			current.sort(new ClusterYComparator());
			out[6*i+3] = this.getQuantileY(current, 6, 1);
			out[6*i+4] = this.getQuantileY(current, 6, 3);
			out[6*i+5] = this.getQuantileY(current, 6, 5);
		}
		
		return out;
	}
	
	private double getQuantileX(List<Cluster> list, int q, int n) {
		int len = list.size();
		int index = n*len/q;
		if (len % q == 0) return list.get(index).getVector().getX();
		return (list.get(index).getVector().getX()+list.get(index+1).getVector().getX())/2.0;
	}
	
	private double getQuantileY(List<Cluster> list, int q, int n) {
		int len = list.size();
		int index = n*len/q;
		if (len % q == 0) return list.get(index).getVector().getY();
		return (list.get(index).getVector().getY()+list.get(index+1).getVector().getY())/2.0;
	}

}
