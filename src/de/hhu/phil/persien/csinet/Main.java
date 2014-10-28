package de.hhu.phil.persien.csinet;

import java.util.Date;

public class Main {

	public static void main(String[] args) {
		/*
		 * Choose simulation to be run
		 */
//		String activeSim = "first"; 
		String activeSim = "context";
		
		/*
		 * Do you want to take screenshots?
		 */
		boolean recording = true;
		
		
		/*
		 * Initialization of the simulation, its panel (graphical output), and statistics. 
		 */
		AbstractSim sim;
		AbstractPanel panel;
		AbstractStats stats;
		
		if (activeSim.equals("first")) {
			sim = new FirstSim();
			sim.init();
			panel = new FirstSimPanel(sim);
			stats = new FirstSimStats(sim);
		}
		else {
			sim = new ContextSim();
			sim.init();
			panel = new ContextSimPanel(sim);
			stats = new ContextSimStats(sim);
		}
		
		panel.setRecording(recording);
		
		MyWindow window = new MyWindow(panel);
		window.setVisible(true);
		
		long startTime;
		startTime = System.currentTimeMillis();
		
		/*
		 * Main loop
		 */
		while (true) {
			/*
			 * Update the simulation itself
			 */
			sim.update();
			
			/*
			 * Record statistics every 1000 steps
			 */
			if (sim.getStep() % 1000 == 0) stats.update();
			
			/*
			 * Output to screen every 40ms (approx.) or when the current step equals 1000
			 */
			if (System.currentTimeMillis()-startTime > 40 || sim.getStep() % 1000 == 0) {
				window.update();
				startTime = System.currentTimeMillis();
			}
		}

	}

}
