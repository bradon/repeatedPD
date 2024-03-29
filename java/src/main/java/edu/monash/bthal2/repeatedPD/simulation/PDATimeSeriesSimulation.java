package edu.monash.bthal2.repeatedPD.simulation;

import java.io.File;
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
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.monash.bthal2.repeatedPD.PDARepresentation.PDAFactory;
import edu.monash.bthal2.repeatedPD.PDARepresentation.PDAMutator;

public class PDATimeSeriesSimulation extends TimeSeriesSimulation {
	// Set up with JSON

	protected double mutationProbability;

	public void init() {
		// Refactor- some of this code can be generalized
		this.factory = new RepeatedStrategyPopulationFactory(populationSize,
				PDAFactory.ExampleStrategies.allD());

		// Will need mutation parameters
		this.mutator = new PDAMutator(mutationProbability);

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

	private static PDATimeSeriesSimulation loadFromFile(String filename)
			throws IOException {
		File file = new File(filename);
		Gson gson = new Gson();
		String json = Files.toString(file, Charsets.UTF_8);
		PDATimeSeriesSimulation sim = gson.fromJson(json,
				PDATimeSeriesSimulation.class);
		sim.init();
		return sim;
	}

	public static void runApp(String filename) throws IOException {
		PDATimeSeriesSimulation app = PDATimeSeriesSimulation
				.loadFromFile(filename);
		app.simulation.simulateTimeSeries(app.numberOfTimeSteps,
				app.reportEveryTimeSteps, app.seed, app.outputFile);
	}

	public static String exampleJson() {
		PDATimeSeriesSimulation app = new PDATimeSeriesSimulation();
		prepareJson(app);
		app.mutationProbability = 0.001;

		String json = new GsonBuilder().setPrettyPrinting().create()
				.toJson(app);
		return json;
	}

}
