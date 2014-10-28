package de.hhu.phil.persien.csinet;

/*
 * Agent.java
 * 
 * An agent is basically a container for clusters, i.e. exemplars. These are organized into three arrays of QuadTrees relating to
 * labels, contexts and meanings, respectively, for quicker access. The QuadTree data structure allows for a faster search (log n
 * on average) but insertion is rather slow (also log n on average). The list of vectors seems superfluous but proves convenient
 * when all vectors have to be accessed in a row (e.g. when incrementing their age at the and of every simulation step).
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.awt.Rectangle;

public class Agent {
	
	private List<Agent> neighbors;			// List of neighbors
	private Neighborhood neighborhood;		// The agent's neighborhood network
	private List<Vector> vectors;			// List of all vectors
	private QuadTree[] clustersByLabel;		// One QuadTree per label
	private QuadTree[] clustersByContext;	// One QuadTree per context
	private QuadTree[] clustersByMeaning;	// One QuadTree per meaning
	private int labels;						// number of labels
	private int contexts;					// number of contexts
	private int meanings;					// number of meanings
	
	
	public Agent(Neighborhood neighborhood) {
		this.neighborhood = neighborhood;
		this.neighbors = new ArrayList<Agent>();
		this.neighborhood.add(this);
		this.vectors = new LinkedList<Vector>();
	}
	
	/*
	 * Getters
	 */
	
	public Neighborhood getNeighborhood() { return this.neighborhood; }
	public List<Agent> getNeighbors() { return this.neighbors; }
	public Agent getNeighbor(int i) { return this.neighbors.get(i); }
	public Agent getRandomNeighbor() {
		int index = new Random().nextInt(this.neighbors.size());
		return this.neighbors.get(index);
	}
	
	public int getLabelNum() { return this.labels; }
	public int getContextNum() { return this.contexts; }
	public int getMeaningNum() { return this.meanings; }
	public List<Vector> getVectors() { return this.vectors; }
	public QuadTree[] getClustersByLabel() { return this.clustersByLabel; }
	public QuadTree getClustersByLabel(int label) { return this.clustersByLabel[label]; }
	public QuadTree[] getClusterByContext() { return this.clustersByContext; }
	public QuadTree getClustersByContext(int context) { return this.clustersByContext[context]; }
	public QuadTree[] getClusterByMeaning() { return this.clustersByMeaning; }
	public QuadTree getClustersByMeaning(int meaning) { return this.clustersByMeaning[meaning]; }
	public Cluster getRandomClusterByLabel(int label) {
		List<Cluster> flat = this.clustersByLabel[label].flatten();
		if (flat.size() == 0) return null;
		return flat.get(new Random().nextInt(flat.size()));
	}
	public Cluster getRandomClusterByContext(int context) {
		List<Cluster> flat = this.clustersByContext[context].flatten();
		if (flat.size() == 0) return null;
		return flat.get(new Random().nextInt(flat.size()));
	}
	public Cluster getRandomClusterByMeaning(int meaning) {
		List<Cluster> flat = this.clustersByMeaning[meaning].flatten();
		if (flat.size() == 0) return null;
		return flat.get(new Random().nextInt(flat.size()));
	}
	
	public boolean hasNeighbor(Agent agent) { return this.neighbors.contains(agent); }
	 
	
	/*
	 * Setters
	 */
	
	public void setMeanings(Rectangle bounds, int num) {
//		this.clustersByMeaning = (List<Cluster>[]) new LinkedList<?>[num];
		this.clustersByMeaning = new QuadTree[num];
		this.meanings = num;
		for (int i=0;i<num;i++) {
			this.clustersByMeaning[i] = new QuadTree(null,bounds);
		}
	}
	public void setContexts(Rectangle bounds, int num) {
//		this.clustersByContext = (List<Cluster>[]) new LinkedList<?>[num];
		this.clustersByContext = new QuadTree[num];
		this.contexts = num;
		for (int i=0;i<num;i++) {
			this.clustersByContext[i] = new QuadTree(null,bounds);
		}
	}
	public void setLabels(Rectangle bounds, int num) {
//		this.clustersByLabel = (List<Cluster>[]) new LinkedList<?>[num];
		this.clustersByLabel = new QuadTree[num];
		this.labels = num;
		for (int i=0;i<num;i++) {
			this.clustersByLabel[i] = new QuadTree(null,bounds);
		}
	}
	public void setMaps(double width, double height, int labels, int contexts, int meanings) {
		Rectangle bounds = new Rectangle(0,0,(int) width+1, (int) height+1);
		this.setLabels(bounds,labels);
		this.setContexts(bounds,contexts);
		this.setMeanings(bounds,meanings);
	}
	
	public void addNeighbor(Agent agent) {
		if (!this.neighbors.contains(agent)) {
			this.neighbors.add(agent);
		}
	}
	public void removeNeighbor(Agent agent) {
		if (this.neighbors.contains(agent)) this.neighbors.remove(agent);
	}
	public void addExemplar(Vector vector, int label, int context, int meaning) {
		Cluster cluster = new Cluster(vector,label,context,meaning);
		if (this.clustersByLabel[label].contains(cluster)) {
			return;
		}
		this.clustersByLabel[label].add(cluster);
		this.clustersByContext[context].add(cluster);
		this.clustersByMeaning[meaning].add(cluster);
		this.vectors.add(vector);
	}
	
	// Removes all vectors and clusters that are older than <maxtime>
	public void tidyUp(int maxtime) {
		for (int i=0;i<this.vectors.size();i++) {
			if (this.vectors.get(i).getTime() > maxtime) this.vectors.remove(i);
		}
		for (int i=0;i<this.labels;i++) {
			this.clustersByLabel[i].tidyUpByTime(maxtime);
		}
		for (int i=0;i<this.meanings;i++) {
			this.clustersByMeaning[i].tidyUpByTime(maxtime);
		}
		for (int i=0;i<this.contexts;i++) {
			this.clustersByContext[i].tidyUpByTime(maxtime);
		}
	}
	
}
