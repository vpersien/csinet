package de.hhu.phil.persien.csinet;

/*
 * This class is where the constants (including parameters) are put, in essence all project wide data.
 * Constants that are unique to a specific model or differ between models are organized informally 
 * into two blocks that can be easily commented out. 
 */

public final class Constants {
	
	/*
	 * Colors used for visualization
	 */
	public static final float H_CYAN = 0.5f;
	public static final float H_MAGENTA = 300.0f/360.0f;
	public static final float H_YELLOW = 60.0f/360.0f;
	public static final float H_RED = 0.0f;
	public static final float H_GREEN = 120.0f/360.0f;
	public static final float H_BLUE = 240.0f/360.0f;
	
	public static final double JND = 0.06;		// just noticeable difference
	public static final double WIDTH = 100.0;	// width of the exemplar space
	public static final double HEIGHT = 100.0;	// height of the exemplar space
	
	public static final int NOISE = 40;		// amount of noise (diameter)
	
	public static final boolean TAKESCREENSHOTS = false;
	public static final boolean RECORDSTATISTICS = false;
	
	/*
	 * Model 2
	 */
	
//	public static final double WINDOW = 25.0;	// window radius
//	public static final double LENITION = 5.0;	// amount of lenition per step
//
//	public static final double THRESHOLD = 7.0;	// top-down-bottom-up threshold
//	public static final double TARGET_X = 49.0;	// target x-position of the lenited category
//	public static final double TARGET_Y = 49.0;	// target y-position of the lenited category
	public static final double ZIPF_EXP = 2.0;	// exponent of the distribution
	public static final double QUOTIENT = 1.0;	// coefficient of the distribution
	
//	public static final double[] INITPOSX = {49.0,89.0};	// Mean initial x position for exemplars of each label
//	public static final double[] INITPOSY = {49.0,89.0};	// Mean initial y position for exemplars of each label
//	
//	public static final int TAU = 4000;			// maximum exepmlar age
//	public static final int LABELS_NUM = 2;		// number of different labels
	public static final int CONTEXTS_NUM = 5;	// number of different contexts
	public static final int MEANINGS_NUM = 10;	// number of different meanings (should be #contexts*#labels)
//	public static final int LENITED = 1;		// category to be shifted
//	public static final int SEED = 10;			// number of initial exemplars per category
	
	// Meaning utterance probability distribution. Possible values:
	// crosszipf, dualzipf, multizipf, uniform, weighteduniform
	public static final String DISTRIBUTION = "crosszipf";
	
	
	
	/*
	 * These are graphical representations of contexts and labels used in the output screen. "words" (refer to the text for clarification)
	 * are represented as "CVD", where "C" and "D" are drawn from <CONTEXTLABELS> and "V" is drawn from <LABELLABELS>. "C" is drawn from
	 * the first 10 characters, "D" from the subsequent 10.  
	 */
	public static final String[] CONTEXTLABELS = {"p","t","c","k","q","b","d","g","f","s","x","h","v","z","r","m","n","l","j","w"};
	public static final String[] LABELLABELS = {"i","u","e","o","a","y"};
	
	/*
	 * Model 1
	 */
	public static final double WINDOW = 40.0;
	public static final double LENITION = 2.0;
	public static final double THRESHOLD = 80.0;
	public static final double SUCCESS = 0.0;	// fraction of exemplars to be categorized right per default
	public static final double TARGET_X = 10.0;
	public static final double TARGET_Y = 10.0;
	
	public static final double[] INITPOSX = { 10.0, 90.0, 10.0, 90.0, 49.0 };	// Mean initial x position for exemplars of each label
	public static final double[] INITPOSY = { 10.0, 10.0, 55.0, 55.0, 90.0 };	// Mean initial y position for exemplars of each label
	public static final double[] LABELPROBS = { 0.13333, 0.26667, 0.2, 0.2, 0.2 };		// Utterance probabilities for each label
	
	public static final int TAU = 4000;
	public static final int LABELS_NUM = 5;
	public static final int LENITED = 1;
	public static final int SEED = 10;
	
	// The categorization regime. Possible values:
	// competitionwithdiscards, purecompetition
	// (No Competition) is equivalent to "purecompetition" and a succes rate of 1.0)
	public static final String REGIME = "competitionwithdiscards";
}
