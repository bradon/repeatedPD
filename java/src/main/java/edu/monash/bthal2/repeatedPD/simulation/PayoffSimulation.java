package edu.monash.bthal2.repeatedPD.simulation;

import java.util.Map;
import java.util.TreeMap;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentBasedPayoffCalculator;
import com.evolutionandgames.agentbased.AgentBasedPopulationFactory;
import com.evolutionandgames.agentbased.AgentBasedSimulation;
import com.evolutionandgames.agentbased.AgentMutator;
import com.evolutionandgames.agentbased.extensive.AgentBasedWrightFisherProcessWithAssortment;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulation;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGame;
import com.evolutionandgames.repeatedgames.utils.MapBasedValueComparator;

/**
 * @author bradon
 * 
 */
public abstract class PayoffSimulation {
	// Set up with JSON
	protected int samplesPerEstimate;
	protected int reportEveryTimeSteps;
	protected Long seed;
	protected int burningTimePerEstimate;
	protected int numberOfEstimates;

	protected double r;
	protected double intensityOfSelection;
	protected PayoffToFitnessMapping mapping;
	protected double continuationProbability;
	protected double mistakeProbability;
	protected double reward;
	protected double sucker;
	protected double temptation;
	protected double punishment;
	protected String outputFile;
	protected int populationSize;

	//Set up by init
	public transient AgentBasedPopulationFactory factory;
	protected transient AgentMutator mutator;
	protected transient RepeatedGame repeatedGame;
	protected transient AgentBasedPayoffCalculator payoffCalculator;
	protected transient AgentBasedWrightFisherProcessWithAssortment process;
	protected transient AgentBasedSimulation simulation;
	protected transient ExtensivePopulation population;
	protected int timeStepsPerEstimate;

	public int getSamplesPerEstimate() {
		return samplesPerEstimate;
	}

	public int getReportEveryTimeSteps() {
		return reportEveryTimeSteps;
	}

	public Long getSeed() {
		return seed;
	}

	public int getBurningTimePerEstimate() {
		return burningTimePerEstimate;
	}

	public int getNumberOfEstimates() {
		return numberOfEstimates;
	}

	public AgentBasedPopulationFactory getFactory() {
		return factory;
	}

	public AgentMutator getMutator() {
		return mutator;
	}

	public double getR() {
		return r;
	}

	public double getIntensityOfSelection() {
		return intensityOfSelection;
	}

	public PayoffToFitnessMapping getMapping() {
		return mapping;
	}

	public double getContinuationProbability() {
		return continuationProbability;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public static String buildString(Map<Agent, Double> stat,
			int numberOfStrategiesToReport) {
		StringBuffer buffer = new StringBuffer();
		// order the map by frequency
		TreeMap<Agent, Double> orderedMap = new TreeMap<Agent, Double>(
				new MapBasedValueComparator(stat));
		orderedMap.putAll(stat);
		int i = 0;
		for (Map.Entry<Agent, Double> entry : orderedMap.entrySet()) {
			buffer.append(entry.getKey().toString() + "," + entry.getValue()
					+ "\n");
			i++;
			if (i >= numberOfStrategiesToReport)
				break;
		}
		return buffer.toString();
	}
	protected static void prepareJson(PayoffSimulation app) {
		app.samplesPerEstimate = 1000;
		app.reportEveryTimeSteps = 1000;
		app.seed = System.currentTimeMillis();
		app.burningTimePerEstimate = 1000;
		app.numberOfEstimates = 10;
		app.r = 0.0;
		app.intensityOfSelection = 0.0;
		app.mapping = PayoffToFitnessMapping.LINEAR;
		app.continuationProbability = 2;
		app.mistakeProbability = 0.001;
		app.reward = 3.0;
		app.sucker = 1.0;
		app.temptation = 4.0;
		app.punishment = 2.0;
		app.outputFile = "lookupExamplePayoffSim.csv";
		app.populationSize = 100;
		app.timeStepsPerEstimate = 1000;
	}
}
