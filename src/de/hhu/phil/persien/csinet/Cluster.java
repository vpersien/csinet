package de.hhu.phil.persien.csinet;

public class Cluster {
	private Vector vector;
	private int label;
	private int context;
	private int meaning;
	
	public Cluster(Vector vector, int label, int context, int meaning) {
		this.vector = vector;
		this.label = label;
		this.context = context;
		this.meaning = meaning;
	}

	/*
	 * Getters
	 */
	
	public Vector getVector() {
		return vector;
	}

	public int getLabel() {
		return label;
	}

	public int getContext() {
		return context;
	}

	public int getMeaning() {
		return meaning;
	}

	/*
	 * Setters
	 */
	
	public void setVector(Vector vector) {
		this.vector = vector;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public void setContext(int context) {
		this.context = context;
	}

	public void setMeaning(int meaning) {
		this.meaning = meaning;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + context;
//		result = prime * result + label;
//		result = prime * result + meaning;
//		result = prime * result + ((vector == null) ? 0 : vector.hashCode());
//		return result;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Cluster))
			return false;
		Cluster other = (Cluster) obj;
		if (context != other.context)
			return false;
		if (label != other.label)
			return false;
		if (meaning != other.meaning)
			return false;
		if (vector == null) {
			if (other.vector != null)
				return false;
		} else if (vector.equals(other.vector))
			return true;
		return false;
	}
	
	/*
	 * Overrides
	 */
	
	

}
