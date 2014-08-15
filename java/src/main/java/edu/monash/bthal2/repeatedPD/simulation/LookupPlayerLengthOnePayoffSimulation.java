package edu.monash.bthal2.repeatedPD.simulation;

import java.io.File;
import java.io.IOException;

import com.evolutionandgames.agentbased.AgentBasedSimulation;
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

import edu.monash.bthal2.repeatedPD.LookupTableRepresentation.LookupStrategyLengthOneFactory;
import edu.monash.bthal2.repeatedPD.LookupTableRepresentation.LookupStrategyMutator;

/**
 * Run LookupStrategy payoff simulation
 * 
 * @author Bradon Hall
 * 
 *         Modified from com.evolutionandgames.repeatedgames.fsa
 * 
 */
public class LookupPlayerLengthOnePayoffSimulation extends PayoffSimulation {

	// Most of variables and getters etc below can be in a parent
	// things that configure a simulation and are persited to a json file

	private double mutationProbability; // Bit flip

	// things to be build in the init method

	private void init() {

		this.factory = new RepeatedStrategyPopulationFactory(populationSize,
				LookupStrategyLengthOneFactory.allD());

		this.mutator = new LookupStrategyMutator(mutationProbability);

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

	private static LookupPlayerLengthOnePayoffSimulation loadFromFile(
			String string) throws IOException {
		File file = new File(string);
		Gson gson = new Gson();
		String json = Files.toString(file, Charsets.UTF_8);
		LookupPlayerLengthOnePayoffSimulation sim = gson.fromJson(json,
				LookupPlayerLengthOnePayoffSimulation.class);
		sim.init();
		return sim;
	}

	/**
	 * Runs payoff simulation, outputting to fule
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public static void runApp(String filename) throws IOException {
		LookupPlayerLengthOnePayoffSimulation app = LookupPlayerLengthOnePayoffSimulation
				.loadFromFile(filename);
		double totalPayoff = app.simulation.estimateTotalPayoff(
				app.burningTimePerEstimate, app.timeStepsPerEstimate,
				app.numberOfEstimates, app.reportEveryTimeSteps, app.seed,
				app.factory);
		double averageTotalPayoff = totalPayoff / (double) app.populationSize;
		File file = new File(app.outputFile);
		String json = new Gson().toJson(app);
		String result = json + "\n" + new Double(averageTotalPayoff).toString();
		Files.write(result, file, Charsets.UTF_8);
	}

	public double getMutationProbability() {
		return mutationProbability;
	}

	/**
	 * Run once and append result to file (csv of delta,r,payoff)
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public static void runWithSimpleOutput(String filename) throws IOException {
		LookupPlayerLengthOnePayoffSimulation app = LookupPlayerLengthOnePayoffSimulation
				.loadFromFile(filename);
		double totalPayoff = app.simulation.estimateTotalPayoff(
				app.burningTimePerEstimate, app.timeStepsPerEstimate,
				app.numberOfEstimates, app.reportEveryTimeSteps, app.seed,
				app.factory);
		double averageTotalPayoff = totalPayoff / (double) app.populationSize;
		File file = new File(app.outputFile);
		// Files.append(String.format("%.6f, %.6f, %.6f\r\n",
		// app.continuationProbability, app.m), file, Charsets.UTF_8);
		Files.append(
				Double.toString(app.continuationProbability) + ", "
						+ Double.toString(app.r) + ", "
						+ Double.toString(averageTotalPayoff) + "\r\n", file,
				Charsets.UTF_8);
	}

	public static String exampleJson() {
		LookupPlayerLengthOnePayoffSimulation app = new LookupPlayerLengthOnePayoffSimulation();
		prepareJson(app);
		app.mutationProbability = 0.00001;
		String json = new GsonBuilder().setPrettyPrinting().create()
				.toJson(app);
		return json;

	}
}
