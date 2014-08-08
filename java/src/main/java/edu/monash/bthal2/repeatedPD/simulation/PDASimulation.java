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

import edu.monash.bthal2.repeatedPD.PDARepresentation.PDAFactory;
import edu.monash.bthal2.repeatedPD.PDARepresentation.PDAMutatorImplementation;
import edu.monash.bthal2.repeatedPD.PDARepresentation.PDAStrategy;

public class PDASimulation extends PayoffSimulation {
	protected double mutationProbability;
	protected double addStateProbability;
	protected double addTransitionProbability;
	protected double changeTransitionDestinationProbability;
	protected double changeTransitionPopProbability;
	protected double changeTransitionPushProbability;
	protected double changeTransitionSourceProbability;
	protected double changeFinalStateProbability;
	protected double deleteStateProbability;

	public double numericalRunOnce(String filename) throws IOException {
		PDASimulation app = PDASimulation.loadFromFile(filename);
		double totalPayoff = app.simulation.estimateTotalPayoff(
				app.burningTimePerEstimate, app.timeStepsPerEstimate,
				app.numberOfEstimates, app.reportEveryTimeSteps, app.seed,
				app.factory);

		double averageTotalPayoff = totalPayoff / (double) app.populationSize;
		
		for (int i = 0; i < 10; i++) {
			PDAStrategy test = (PDAStrategy) app.process.getPopulation().getAgent(Random.nextInt(app.populationSize));
			test.printStrategy();
		}
		return averageTotalPayoff;
	}
	
	// RunOnce?
	public static void runOncePayoff(String filename) throws IOException {
		PDASimulation app = PDASimulation.loadFromFile(filename);
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

	public static PDASimulation loadFromFile(String string)
			throws IOException {
		File file = new File(string);
		Gson gson = new Gson();
		String json = Files.toString(file, Charsets.UTF_8);
		PDASimulation sim = gson
				.fromJson(json, PDASimulation.class);
		sim.init();
		return sim;

	}
	
	
	public static void generatesTimeSeries(String filename) throws IOException {
		PDASimulation app = PDASimulation.loadFromFile(filename);
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

}
