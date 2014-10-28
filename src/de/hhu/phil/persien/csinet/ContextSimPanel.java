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

public class ContextSimPanel extends AbstractPanel {

	private int blocksize = 4;
	private final AbstractSim sim;
	private final double stretch = 1.0/Math.E;
	
	Agent agent;
	
	public ContextSimPanel(AbstractSim sim) {
		super();
		this.sim = sim;
		this.setWidth(this.blocksize*((int) sim.getWidth()+1));
		this.setHeight(this.blocksize*((int) sim.getWidth()+1));
		this.init();
		this.graphics.setFont(new Font("Arial",Font.PLAIN,8));
		this.setBackground(Color.WHITE);
		
		this.agent = sim.getNeighborhood().getRandomAgent();
	}

	
	@Override
	public void update() {
		this.graphics.clearRect(0, 0, this.width, this.height);

		int meanings = this.agent.getMeaningNum();
		for (int i=0;i<meanings;i++) {
			for (Cluster cluster : this.agent.getClustersByMeaning(i)) {
				this.graphics.setColor(this.assignColor(i%11, cluster.getVector().getActivation()));
				int x = (int) (cluster.getVector().getX()*this.blocksize);
				int y = (int) (cluster.getVector().getY()*this.blocksize);
				this.graphics.drawString(this.wordToString(cluster.getContext(), cluster.getLabel()), x, y);
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
		int second = context%10+10;
		int first = 0;
		if (context > 9) {
			context = context/10;
			first = context%10;
		}

		return Constants.CONTEXTLABELS[first]+Constants.LABELLABELS[label]+Constants.CONTEXTLABELS[second];
	}
	
	private Color assignColor(int label, double activation) {
		int transparency = (int) (((activation-this.stretch)/(1.0-this.stretch))*255);
		switch(label) {
		case 0:
			return new Color(142,1,82,transparency);
		case 1:
			return new Color(197,27,125,transparency);
		case 2:
			return new Color(222,119,174,transparency);
		case 3:
			return new Color(241,182,218,transparency);
		case 4:
			return new Color(253,224,239,transparency);
		case 5:
			return new Color(247,247,247,transparency);
		case 6:
			return new Color(230,245,208,transparency);
		case 7:
			return new Color(184,225,134,transparency);
		case 8:
			return new Color(127,188,65,transparency);
		case 9:
			return new Color(77,146,33,transparency);
		case 10:
			return new Color(39,100,25,transparency);
		default:
			return new Color(255,255,255,transparency);
		}
	}
	
	

}
