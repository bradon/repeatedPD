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

	protected double mutationProbability;

	public void init() {
		// Refactor- some of this code can be generalized
		this.factory = new RepeatedStrategyPopulationFactory(populationSize,
				DPDAFactory.ExampleStrategies.allD());

		// Will need mutation parameters
		this.mutator = new DPDAMutator(mutationProbability);

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

	private static DPDATimeSeriesSimulation loadFromFile(String filename)
			throws IOException {
		File file = new File(filename);
		Gson gson = new Gson();
		String json = Files.toString(file, Charsets.UTF_8);
		DPDATimeSeriesSimulation sim = gson.fromJson(json,
				DPDATimeSeriesSimulation.class);
		sim.init();
		return sim;
	}

	public static void runApp(String filename) throws IOException {
		DPDATimeSeriesSimulation app = DPDATimeSeriesSimulation
				.loadFromFile(filename);
		app.simulation.simulateTimeSeries(app.numberOfTimeSteps,
				app.reportEveryTimeSteps, app.seed, app.outputFile);
		//ExtraMeasuresProcessor extraProcessor=new ExtraMeasuresProcessor();
		//ExtraMeasuresProcessor
		app.simulation.simulateTimeSeries(app.numberOfTimeSteps,
				app.reportEveryTimeSteps, app.seed, app.outputFile, null);
	}

	public static String exampleJson() {
		DPDATimeSeriesSimulation app = new DPDATimeSeriesSimulation();
		prepareJson(app);
		app.mutationProbability = 0.001;

		String json = new GsonBuilder().setPrettyPrinting().create()
				.toJson(app);
		return json;
	}
}
