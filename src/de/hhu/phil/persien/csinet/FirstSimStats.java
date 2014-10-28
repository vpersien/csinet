package de.hhu.phil.persien.csinet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

public class FirstSimStats extends AbstractStats {

	public FirstSimStats(AbstractSim sim) {
		super(sim);
	}
	
	
	public void update() {
		if (!this.recording) return;
		String str;
		
		str = this.getInitString();
		double[][] meanCoordinates = this.getMeanCoordinates(this.agent);
		str += this.Array2DToString(meanCoordinates);
		this.appendToFile("data/meancoordinates.csv", str);
		
		str = this.getInitString();
		str += this.arrayToString(this.getQuantities(agent));
		this.appendToFile("data/quantities.csv", str);
		
//		double[][] coordinates = this.getCoordinates(this.agent);
//		str = this.Array2DToString(coordinates);
//		this.appendToFile(String.format("data/coordinates%06d.csv",this.sim.getStep()), str);
		
	}
	
	private String Array2DToString(double[][] arr) {
		String str = "";
		for (int i=0;i<arr.length;i++) {
			str += this.arrayToString(arr[i])+" ";
		}
		str = str.trim();
		return str;
	}
	
	private double[][] getMeanCoordinates(Agent agent) {
		int labels = agent.getClustersByLabel().length;
		double[][] out = new double[labels][2];
		double activation;
		for (int i=0;i<labels;i++) {
			double act_sum = 0.0;
			double x = 0.0;
			double y = 0.0;
			for (Cluster cluster : agent.getClustersByLabel(i)) {
				activation = cluster.getVector().getActivation();
				x += cluster.getVector().getX()*activation;
				y += cluster.getVector().getY()*activation;
				act_sum += activation;
			}
			out[i][0] = x/act_sum;
			out[i][1] = y/act_sum;
		}
		return out;
	}
	
	private double[][] getCoordinates(Agent agent) {
		int labels = agent.getClustersByLabel().length;
		double[][] out = new double[labels*3][3];
		for (int i=0;i<labels;i++) {
			List<Cluster> clusters = agent.getClustersByLabel(i).flatten();
			for (int j=0;j<clusters.size();j++) {
				out[i][0] = clusters.get(j).getVector().getX();
				out[i][1] = clusters.get(j).getVector().getY();
				out[i][2] = clusters.get(j).getVector().getActivation();
			}
		}
		return out;
	}

}
