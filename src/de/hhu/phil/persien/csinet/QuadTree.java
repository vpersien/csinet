package de.hhu.phil.persien.csinet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.awt.Rectangle;


public class QuadTree implements Collection<Cluster> {

	private final int capacity = 20;
	private final int level;
	private final List<Cluster> objects;
	private final QuadTree[] children;
	private final Rectangle bounds;
	private int size = 0;
	
	public QuadTree(QuadTree parent, Rectangle bounds) {
		this.level = (parent == null) ? 0 : parent.getLevel()+1;
		this.bounds = bounds;
		this.children = new QuadTree[4];
		this.objects = new ArrayList<Cluster>();
	}
	
	public int getLevel() { return this.level; }
	public List<Cluster> getObjects() { return this.objects; }
	
	@Override
	public int size() {
//		int size = this.objects.size();
//		for (int i=0;i<this.children.length;i++) {
//			if (this.children[i] != null) size += this.children[i].size();
//		}
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		if (this.objects.isEmpty() && this.children[0] == null) return true;
		return false;
	}

	@Override
	public boolean contains(Object o) {
		if (!(o instanceof Cluster)) return false;
		Cluster p = (Cluster) o;
		if (!this.bounds.contains(p.getVector().getX(),p.getVector().getY())) return false;
		if (this.objects.contains(o)) {
			return true;
		}
		if (this.children[0] == null) return false;
		for (int i=0;i<4;i++) {
			if (this.children[i].contains(p)) return true;
		}
		return false;
	}

	@Override
	public Iterator<Cluster> iterator() {
		return this.flatten().iterator();
	}

	@Override
	public Object[] toArray() {
		return this.flatten().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.flatten().toArray(a);
	}

	@Override
	public boolean add(Cluster e) {
		if (!this.bounds.contains((int) e.getVector().getX(), (int) e.getVector().getY())) {
			return false;
		}
//		if (this.objects.size() < this.capacity) {
//			if (this.objects.contains(e)) return false;
//			int index = this.objects.indexOf(e);
//			if (index > -1) this.objects.get(index).getVector().setTime(0);
//			else {
//				this.objects.add(e);
//				this.size++;
//			}
//			return true;
//		}
		int index = this.objects.indexOf(e);
		if (index > -1) {
			this.objects.get(index).getVector().setTime(0);
			return true;
		}
		else if (this.objects.size() < this.capacity) {
				this.objects.add(e);
				this.size++;
				return true;
		}
		if (this.children[0] == null) {
			int halfW = this.bounds.width/2;
			int halfH = this.bounds.height/2;
			int x = this.bounds.x;
			int y = this.bounds.y;
			this.children[0] = new QuadTree(this,new Rectangle(x,y,halfW,halfH));
			this.children[1] = new QuadTree(this,new Rectangle(x+halfW,y,halfW+1,halfH));
			this.children[2] = new QuadTree(this,new Rectangle(x,y+halfH,halfW,halfH+1));
			this.children[3] = new QuadTree(this,new Rectangle(x+halfW,y+halfH,halfW+1,halfH+1));
		}	
		for (int i=0;i<4;i++) {
			if (this.children[i].add(e)) {
//				this.size++;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean remove(Object o) {
		if (!(o instanceof Cluster)) return false;
		Cluster p = (Cluster) o;
		if (!(this.bounds.contains(p.getVector().getX(),p.getVector().getY()))) return false;
		for (int i=0;i<this.objects.size();i++) {
			if (this.objects.get(i).equals(p)) {
				boolean remove = this.objects.remove(p);
				if (remove) this.size--;
				return remove;
			}
		}
		if (this.children[0] == null) return false;
		boolean remove = false;
		for (int i=0;i<4;i++) {
			remove = this.children[i].remove(p);
			return remove;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends Cluster> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		this.objects.clear();
		for (int i=0;i<4;i++) {
			if (this.children[i] != null) {
				this.children[i].clear();
				this.children[i] = null;
				this.size = 0;
			}
		}
	}
	
	public List<Cluster> getObjectsInRange(Rectangle range) {
		List<Cluster> out = new LinkedList<Cluster>();
		if (!this.bounds.intersects(range)) {
			return out;
		}
		for (Cluster p : this.objects) {
			if (range.contains(p.getVector().getX(), p.getVector().getY())) {
				out.add(p);
			}
		}
		if (this.children[0] == null) return out;
		for (int i=0;i<4;i++) {
			out.addAll(this.children[i].getObjectsInRange(range));
		}
		
		return out;
	}
	
	public List<Cluster> getObjectsInRadius(double x, double y, double r) {
		Rectangle range = new Rectangle(
				Math.max(0,(int) (x-r)),
				Math.max(0,(int) (y-r)),
				Math.min((int) this.bounds.getMaxX(),(int) (x+r)),
				Math.min((int) this.bounds.getMaxY(),(int) (y+r)));
		List<Cluster> out = new LinkedList<Cluster>();
		for (Cluster p : this.getObjectsInRange(range)) {
			double vx = p.getVector().getX();
			double vy = p.getVector().getY();
			if (Math.sqrt(Math.pow(x-vx, 2.0)+Math.pow(y-vy, 2.0)) <= r) {
				out.add(p);
			}
		}
		return out;
	}
	
	public List<Cluster> flatten() {
		List<Cluster> out = new LinkedList<Cluster>();
		out.addAll(this.objects);
		if (this.children[0] != null) {
			for (int i=0;i<4;i++) {
				out.addAll(this.children[i].flatten());
			}
		}
		return out;
	}
	
	public void tidyUpByTime(int maxtime) {
		for (int i=this.objects.size()-1;i>=0;i--) {
			if (this.objects.get(i).getVector().getTime() > maxtime) this.objects.remove(i);
		}
		if (this.children[0] != null) {
			for (int i=0;i<4;i++) {
				this.children[i].tidyUpByTime(maxtime);
			}
		}
	}

}
