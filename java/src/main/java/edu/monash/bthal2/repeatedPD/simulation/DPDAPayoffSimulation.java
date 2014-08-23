package edu.monash.bthal2.repeatedPD.simulation;

import java.io.File;
import java.io.IOException;

import com.evolutionandgames.agentbased.AgentBasedSimulation;
import com.evolutionandgames.agentbased.extensive.AgentBasedWrightFisherProcessWithAssortment;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulation;
import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGame;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGamePayoffCalculator;
import com.evolutionandgames.repeatedgames.utils.RepeatedStrategyPopulationFactory;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.monash.bthal2.repeatedPD.DPDA.DPDA;
import edu.monash.bthal2.repeatedPD.DPDA.DPDAFactory;
import edu.monash.bthal2.repeatedPD.DPDA.DPDAMutator;

public class DPDAPayoffSimulation extends PayoffSimulation {
	protected double mutationProbabilityPerState;

	public double numericalRunOnce(String filename) throws IOException {
		DPDAPayoffSimulation app = DPDAPayoffSimulation.loadFromFile(filename);
		double totalPayoff = app.simulation.estimateTotalPayoff(
				app.burningTimePerEstimate, app.timeStepsPerEstimate,
				app.numberOfEstimates, app.reportEveryTimeSteps, app.seed,
				app.factory);

		double averageTotalPayoff = totalPayoff / (double) app.populationSize;

		for (int i = 0; i < 10; i++) {
			DPDA test = (DPDA) app.process.getPopulation()
					.getAgent(Random.nextInt(app.populationSize));
			test.printStrategy();
		}
		return averageTotalPayoff;
	}
	// RunOnce?
	public static void runOncePayoff(String filename) throws IOException {
		DPDAPayoffSimulation app = DPDAPayoffSimulation.loadFromFile(filename);
		double totalPayoff = app.simulation.estimateTotalPayoff(
				app.burningTimePerEstimate, app.timeStepsPerEstimate,
				app.numberOfEstimates, app.reportEveryTimeSteps, app.seed,
				app.factory);
		double averageTotalPayoff = totalPayoff / (double) app.populationSize;
		File file = new File(app.outputFile);
		// Payoff should match expected- first test to see if correct
		Files.append(
				Double.toString(app.continuationProbability) + ", "
						+ Double.toString(app.r) + ", "
						+ Double.toString(averageTotalPayoff) + "\r\n", file,
				Charsets.UTF_8);
	}

	public static DPDAPayoffSimulation loadFromFile(String string)
			throws IOException {
		File file = new File(string);
		Gson gson = new Gson();
		String json = Files.toString(file, Charsets.UTF_8);
		DPDAPayoffSimulation sim = gson.fromJson(json,
				DPDAPayoffSimulation.class);
		sim.init();
		return sim;

	}

	public static void generatesTimeSeries(String filename) throws IOException {
		DPDAPayoffSimulation app = DPDAPayoffSimulation.loadFromFile(filename);
		double totalPayoff = app.simulation.estimateTotalPayoff(
				app.burningTimePerEstimate, app.timeStepsPerEstimate,
				app.numberOfEstimates, app.reportEveryTimeSteps, app.seed,
				app.factory);
		double averageTotalPayoff = totalPayoff / (double) app.populationSize;
		File file = new File(app.outputFile);
		// Payoff should match expected- first test to see if correct
		Files.append(
				Double.toString(app.continuationProbability) + ", "
						+ Double.toString(app.r) + ", "
						+ Double.toString(averageTotalPayoff) + "\r\n", file,
				Charsets.UTF_8);
	}

	private void init() {

		// Refactor- some of this code can be generalized
		this.factory = new RepeatedStrategyPopulationFactory(populationSize,
				DPDAFactory.ExampleStrategies.allD());

		// Will need mutation parameters
		this.mutator = new DPDAMutator(mutationProbabilityPerState);

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

	public static String exampleJson() {
		DPDAPayoffSimulation app = new DPDAPayoffSimulation();
		prepareJson(app);
		app.mutationProbabilityPerState = 0.00001;
		String json = new GsonBuilder().setPrettyPrinting().create()
				.toJson(app);
		return json;

	}
}
