package edu.monash.bthal2.repeatedPD.simulation;

import com.evolutionandgames.agentbased.AgentBasedPayoffCalculator;
import com.evolutionandgames.agentbased.AgentBasedPopulationFactory;
import com.evolutionandgames.agentbased.AgentBasedSimulation;
import com.evolutionandgames.agentbased.AgentMutator;
import com.evolutionandgames.agentbased.extensive.AgentBasedWrightFisherProcessWithAssortment;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulation;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGame;

public abstract class TimeSeriesSimulation {
	// JSON stuff
	protected int reportEveryTimeSteps;
	protected Long seed;

	protected int numberOfTimeSteps;

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

	// Set up by init
	public transient AgentBasedPopulationFactory factory;
	protected transient AgentMutator mutator;
	protected transient RepeatedGame repeatedGame;
	protected transient AgentBasedPayoffCalculator payoffCalculator;
	protected transient AgentBasedWrightFisherProcessWithAssortment process;
	protected transient AgentBasedSimulation simulation;
	protected transient ExtensivePopulation population;

	protected static void prepareJsonTimeSeries(TimeSeriesSimulation app) {
		app.continuationProbability = 0.1;
		app.intensityOfSelection = 1.0;
		app.mapping = PayoffToFitnessMapping.LINEAR;
		app.mistakeProbability = 0.0;
		app.numberOfTimeSteps = 1000;
		app.outputFile = "exampleLookupTimeSeries.out";
		app.populationSize = 1000;
		app.reward = 3.0;
		app.sucker = 1.0;
		app.temptation = 4.0;
		app.punishment = 2.0;
		app.r = 0.5;
		app.reportEveryTimeSteps = 10;
		app.seed = (long) 123456;
	}
}
