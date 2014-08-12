package edu.monash.bthal2.repeatedPD.simulation;

import java.io.IOException;

import com.evolutionandgames.agentbased.AgentBasedPayoffCalculator;
import com.evolutionandgames.agentbased.AgentBasedPopulationFactory;
import com.evolutionandgames.agentbased.AgentBasedSimulation;
import com.evolutionandgames.agentbased.AgentMutator;
import com.evolutionandgames.agentbased.extensive.AgentBasedWrightFisherProcessWithAssortment;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulation;
import com.evolutionandgames.jevodyn.utils.PayoffToFitnessMapping;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGame;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGamePayoffCalculator;
import com.evolutionandgames.repeatedgames.utils.RepeatedStrategyPopulationFactory;

import edu.monash.bthal2.repeatedPD.PDARepresentation.PDAFactory;
import edu.monash.bthal2.repeatedPD.PDARepresentation.PDAMutatorImplementation;

public class PDATimeSeriesSimulation {
	// Set up with JSON
	protected int samplesPerEstimate;
	protected int reportEveryTimeSteps;
	protected Long seed;
	protected int burningTimePerEstimate;
	protected int numberOfEstimates;
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

	protected double mutationProbability;

	public void init() {
		// Refactor- some of this code can be generalized
		this.factory = new RepeatedStrategyPopulationFactory(populationSize,
				PDAFactory.ExampleStrategies.allD());

		// Will need mutation parameters
		this.mutator = new PDAMutatorImplementation(mutationProbability);

		this.population = (ExtensivePopulation) factory.createPopulation();
		this.repeatedGame = new RepeatedGame(this.reward, this.sucker,
				this.temptation, this.punishment, this.continuationProbability);

		this.payoffCalculator = new RepeatedGamePayoffCalculator(
				this.repeatedGame, this.mistakeProbability, false);

		this.process = new AgentBasedWrightFisherProcessWithAssortment(
				population, payoffCalculator, mapping, intensityOfSelection,
				mutator, r);
		this.simulation = new AgentBasedSimulation(this.process);
	}

	private static PDATimeSeriesSimulation loadFromFile(String filename) {
		return null;
	}

	public static void runApp(String filename) throws IOException {
		PDATimeSeriesSimulation app = PDATimeSeriesSimulation
				.loadFromFile(filename);
		app.simulation.simulateTimeSeries(app.numberOfTimeSteps,
				app.reportEveryTimeSteps, app.seed, filename);
	}

}
