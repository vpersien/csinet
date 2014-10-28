package de.hhu.phil.persien.csinet;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public class FirstSimPanel extends AbstractPanel {

	private int blocksize = 4;
	private final AbstractSim sim;
	private final double stretch = 1.0/Math.E;
	
	private final String[] contextLabels = {"p","t","c","k","q","b","d","g","f","s","x","h","v","z","r","m","n","l","j","w"};
	private final String[] labelLabels = {"a","i","o","e","u","y"};
	
	Agent agent;
	
	public FirstSimPanel(AbstractSim sim) {
		super();
		this.sim = sim;
		this.setWidth(this.blocksize*((int) sim.getWidth()+1));
		this.setHeight(this.blocksize*((int) sim.getWidth()+1));
		this.init();
		this.graphics.setFont(new Font("Arial",Font.PLAIN,8));
		this.setBackground(Color.WHITE);
		
		this.agent = sim.getNeighborhood().getAgent(0);
	}

	
	@Override
	public void update() {
//		if (!(this.sim.getStep() % 1000 == 0)) return;
//		this.graphics.clearRect(0, 0, this.width, this.height);
		this.graphics.setColor(new Color(255,255,255,255));
		this.graphics.fillRect(0, 0, this.width, this.height);
		for (int i=0;i<Constants.LABELS_NUM;i++) {
			for (Cluster cluster : this.agent.getClustersByLabel(i)) {
				this.graphics.setColor(this.assignColor(i, cluster.getVector().getActivation()));
				int x = (int) (cluster.getVector().getX()*this.blocksize);
				int y = (int) (cluster.getVector().getY()*this.blocksize);
				this.graphics.fillRect(x, y, this.blocksize*2, this.blocksize*2);
			}
		}
		
		int step = this.sim.getStep();
		if (step % 1000 == 0) {
			System.out.println(step);
			if (this.recording && (step < 10000 || step % 10000 == 0)) {
				this.saveIMG();
			}
		}
	}
	
	private void saveIMG() {
		try {
			ImageIO.write(this.getBuffer(), "png", new File(String.format("res/snapshot%03d.png", this.sim.getStep())));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String wordToString(int context, int label) {
		int second = context%10;
		int first = 0;
		if (context > 9) {
			context = context/10;
			first = context%10;
		}
		
		return this.contextLabels[first]+this.labelLabels[label]+this.contextLabels[second+10];
	}
	
	private Color assignColor(int label, double activation) {
//		int transparency = (int) (activation*255);
		int transparency = (int) (((activation-this.stretch)/(1.0-this.stretch))*255);
		switch(label) {
		case 0:
			return new Color(214,96,77,transparency);
		case 1:
			return new Color(5,48,97,transparency);
		case 2:
			return new Color(67,147,195,transparency);
		case 3:
			return new Color(64,0,75,transparency);
		case 4:
			return new Color(103,0,31,transparency);
		default:
			return new Color(255,0,0,transparency);
//		case 0:
//			return new Color(142,1,82,transparency);
//		case 1:
//			return new Color(197,27,125,transparency);
//		case 2:
//			return new Color(222,119,174,transparency);
//		case 3:
//			return new Color(241,182,218,transparency);
//		case 4:
//			return new Color(253,224,239,transparency);
//		case 5:
//			return new Color(247,247,247,transparency);
//		case 6:
//			return new Color(230,245,208,transparency);
//		case 7:
//			return new Color(184,225,134,transparency);
//		case 8:
//			return new Color(127,188,65,transparency);
//		case 9:
//			return new Color(77,146,33,transparency);
//		case 10:
//			return new Color(39,100,25,transparency);
//		default:
//			return new Color(255,255,255,transparency);
		}
	}
	
	

}
