package de.hhu.phil.persien.csinet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Neighborhood implements Collection<Agent> {
	
	List<Agent> nodes;
	
	public Neighborhood() {
		this.nodes = new ArrayList<Agent>();
	}
	
	/*
	 * Getters
	 */
	
	public List<Agent> getNodes() { return this.nodes; }
	public Agent getAgent(int index) { return this.nodes.get(index); }
	public Agent getRandomAgent() {
		int index = new Random().nextInt(this.nodes.size());
		return this.nodes.get(index);
	}
	public boolean hasNode(Agent node) { return this.nodes.contains(node); }
	
	/*
	 * Setters
	 */
	
	public void addNode(Agent node) { this.nodes.add(node); }
	public void addEdge(Agent node1, Agent node2) {
		if (!(this.contains(node1) && this.contains(node2))) return;
		node1.addNeighbor(node2);
		node2.addNeighbor(node1);
	}
	public void removeEdge(Agent node1, Agent node2) {
		if (!(this.contains(node1) && this.contains(node2))) return;
		node1.removeNeighbor(node2);
		node2.removeNeighbor(node1);
	}

	
	/*
	 * Overrides
	 */
	
	@Override
	public int size() {
		return this.nodes.size();
	}

	@Override
	public boolean isEmpty() {
		return this.nodes.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.nodes.contains(o);
	}

	@Override
	public Iterator<Agent> iterator() {
		return this.nodes.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.nodes.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.nodes.toArray(a);
	}

	@Override
	public boolean add(Agent e) {
		return this.nodes.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.nodes.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.nodes.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Agent> c) {
		return this.nodes.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.nodes.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.nodes.retainAll(c);
	}

	@Override
	public void clear() {
		this.nodes.clear();
	}

	

}
