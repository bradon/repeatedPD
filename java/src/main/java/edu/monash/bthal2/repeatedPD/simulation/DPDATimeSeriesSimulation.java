package edu.monash.bthal2.repeatedPD.simulation;

import java.io.File;
import java.io.IOException;

import com.evolutionandgames.agentbased.AgentBasedSimulation;
import com.evolutionandgames.agentbased.extensive.AgentBasedWrightFisherProcessWithAssortment;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulation;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGame;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGamePayoffCalculator;
import com.evolutionandgames.repeatedgames.utils.ExtraMeasuresProcessor;
import com.evolutionandgames.repeatedgames.utils.RepeatedStrategyPopulationFactory;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.monash.bthal2.repeatedPD.DPDA.DPDAFactory;
import edu.monash.bthal2.repeatedPD.DPDA.DPDAMutator;

public class DPDATimeSeriesSimulation extends TimeSeriesSimulation {

	protected double addStatesProbability;
	protected double removeStatesProbability;
	protected double addTransitionProbability;
	protected double removeTransitionProbability;
	protected double changeReadProbability;
	protected double changePopProbability;
	protected double changePushProbability;
	protected double changeDestinationProbability;
	protected double flipState;
	protected double mutationProbabilityPerState;
	private static boolean neutralPopulation = false;

	public void init() {
		// Refactor- some of this code can be generalized
		this.factory = new RepeatedStrategyPopulationFactory(populationSize,
				DPDAFactory.ExampleStrategies.allD());

		if (neutralPopulation) {
			((DPDAFactory) this.factory).setNeutralPopulation();
		}

		// Will need mutation parameters
		// mutationProbabilityPerState, addingStatesProbability,
		// removingStatesProbability, addTransitionProbability,
		// removeTransitionProbability, changingReadProbability,
		// changingPopProbability, changingPushProbability,
		// changingDestinationProbability, flipState
		this.mutator = new DPDAMutator(mutationProbabilityPerState,
				addStatesProbability, removeStatesProbability,
				addTransitionProbability, removeTransitionProbability,
				changeReadProbability, changePopProbability,
				changePushProbability, changeDestinationProbability, flipState);

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

	private static DPDATimeSeriesSimulation loadFromFile(String filename,
			boolean setNeutralPopulation) throws IOException {
		File file = new File(filename);
		Gson gson = new Gson();
		String json = Files.toString(file, Charsets.UTF_8);
		DPDATimeSeriesSimulation sim = gson.fromJson(json,
				DPDATimeSeriesSimulation.class);
		neutralPopulation=setNeutralPopulation;
		sim.init();
		return sim;
	}

	public static DPDATimeSeriesSimulation loadFromFile(String string)
			throws IOException {
		return loadFromFile(string, false);
	}

	public static void runApp(String filename) throws IOException {
		DPDATimeSeriesSimulation app = DPDATimeSeriesSimulation
				.loadFromFile(filename);
		app.simulation.simulateTimeSeries(app.numberOfTimeSteps,
				app.reportEveryTimeSteps, app.seed, app.outputFile);
		// ExtraMeasuresProcessor extraProcessor=new ExtraMeasuresProcessor();
		// ExtraMeasuresProcessor
		app.simulation.simulateTimeSeries(app.numberOfTimeSteps,
				app.reportEveryTimeSteps, app.seed, app.outputFile, null);
	}

	/**
	 * @return
	 */
	public static String exampleJson() {
		DPDATimeSeriesSimulation app = new DPDATimeSeriesSimulation();
		prepareJsonTimeSeries(app);
		app.mutationProbabilityPerState = 0.001;
		app.addStatesProbability = 0.01;
		app.removeStatesProbability = 0.05;
		app.addTransitionProbability = 0.05;
		app.removeTransitionProbability = 0.04;
		app.changeReadProbability = 0.05;
		app.changePopProbability = 0.05;
		app.changePushProbability = 0.05;
		app.flipState = 0.0;
		app.changeDestinationProbability = 0.70;

		String json = new GsonBuilder().setPrettyPrinting().create()
				.toJson(app);
		return json;
	}
}
