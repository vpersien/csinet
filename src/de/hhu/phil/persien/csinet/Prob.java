package de.hhu.phil.persien.csinet;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Prob {

	private double[] probs;
	private double[] cdf;
	private Integer[] indices;
	private IndexComparator comparator;
	private Random rnd;
	
	public Prob(double[] probs) {
		this.probs = probs;
		this.cdf = new double[probs.length];
		this.comparator = new IndexComparator();
		this.indices = comparator.createIndexArray();
		this.calcCDF();
//		Arrays.sort(this.indices,this.comparator);
		System.out.println();
		this.rnd = new Random(new Random().nextLong());
	}
	
	private class IndexComparator implements Comparator<Integer> {

		public IndexComparator() { }
		
		public Integer[] createIndexArray() {
			Integer[] indices = new Integer[Prob.this.probs.length];
			for (int i=0;i<indices.length;i++) {
				indices[i] = i;
			}
			return indices;
		}
		
		@Override
		public int compare(Integer i1, Integer i2) {
			return -Double.compare(Prob.this.probs[i1],Prob.this.probs[i2]);
		}
		
	}
	
	public double[] getProbs() { return this.probs; }
	public double[] getCDF() { return this.cdf; }
	public double getProbOf(int i) { return this.probs[i]; }
	public int getRandomIndex() { 
		double die = this.rnd.nextDouble();
		for (int i=0;i<this.cdf.length;i++) {
			if (die < this.cdf[i]) return this.indices[i];
		}
		return this.indices[this.cdf.length-1];
	}
	
	public void setProbs(double[] probs) { 
		this.probs = probs;
		this.calcCDF();
		Arrays.sort(this.indices,this.comparator);
	}
	public void calcCDF() {
		double F = 0.0;
		for (int i=0;i<this.probs.length;i++) {
			F += this.probs[this.indices[i]];
			this.cdf[i] = F;
		}
	}

}
