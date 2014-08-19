package de.hhu.phil.persien.csinet;

import java.util.Date;

public class Main {

	public static void main(String[] args) {
//		ISimulation sim;
		AbstractSim sim;
		sim = new ContextSim();
//		sim = new FirstSim();
		
		
		sim.init();
		
		MyPanel panel;
		panel = new ContextSimPanel(sim);
//		panel = new FirstSimPanel(sim);
		MyWindow window = new MyWindow(panel);
		window.setVisible(true);
		
		ContextSimStats contextSimStats = new ContextSimStats(sim);
//		FirstSimStats firstSimStats = new FirstSimStats(sim);
		
		Date date = new Date();
		long startTime;
		long timeDiff;
		long delay = 1000/25;
		startTime = System.currentTimeMillis();
		
		while (true) {
			
			sim.update();
			
//			if (sim.getStep() % 1000 == 0) contextSimStats.update();
//			if (sim.getStep() % 1000 == 0) firstSimStats.update();
			
			if (System.currentTimeMillis()-startTime > 40 || sim.getStep() % 1000 == 0) {
				window.update();
				startTime = System.currentTimeMillis();
			}
//			if (sim.getStep() % 100 == 0) window.update();
			
//			timeDiff = System.currentTimeMillis()-startTime;
//			if (timeDiff-delay > 0) {
//				window.update();
//				startTime = System.currentTimeMillis();
//			}
			
			
//			try {
//				if (50-timeDiff>0)	Thread.sleep(50-timeDiff);
//			}
//			catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}

	}

}
