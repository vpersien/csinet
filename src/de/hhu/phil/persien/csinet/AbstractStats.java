package de.hhu.phil.persien.csinet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public abstract class AbstractStats {
	protected AbstractSim sim;
	protected Agent agent;
	protected boolean recording = Constants.RECORDSTATISTICS;
	
	public AbstractStats(AbstractSim sim) {
		this.sim = sim;
		this.agent = this.sim.getNeighborhood().getAgent(0);
	}
	
	abstract public void update();
	
	
	protected String arrayToString(double[] arr) {
		String str = Arrays.toString(arr);
		str = this.prepareArrayString(str);
		return str;
	}
	
	protected String prepareArrayString(String str) {
		String out = str.replaceAll("\\[|\\]|,", "");
		out = out.replaceAll("\\s", "\t");
		return out;
	}
	
	protected void appendToFile(String filename, String string) {
		Writer writer = null;
		try {
			writer = new FileWriter(filename,true);
			writer.write(string);
			writer.append("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected String getInitString() {
		return String.format("%s\t",this.sim.getStep());
	}
	
	protected double[] getQuantities(Agent agent) {
		int labels = agent.getClustersByLabel().length;
		double[] out = new double[labels];
		
		for (int i=0;i<labels;i++) {
			out[i] = agent.getClustersByLabel(i).flatten().size();
		}
		return out;		
	}
}
