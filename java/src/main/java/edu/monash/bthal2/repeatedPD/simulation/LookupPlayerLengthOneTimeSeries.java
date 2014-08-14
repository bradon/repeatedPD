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

public class LookupPlayerLengthOneTimeSeries {
	// setup with json
	private int populationSize;
	private double mutationProbability;
	private double reward;
	private double mistakeProbability;
	private PayoffToFitnessMapping mapping;
	private double temptation;
	private double punishment;
	private double sucker;
	private double continuationProbability;
	private double intensityOfSelection;
	private double r;
	private String outputFile;
	private int reportEveryTimeSteps;
	private int numberOfTimeSteps;
	private Long seed;

	// setup with init
	private RepeatedStrategyPopulationFactory factory;
	private LookupStrategyMutator mutator;
	private ExtensivePopulation population;
	private RepeatedGame repeatedGame;
	private RepeatedGamePayoffCalculator payoffCalculator;
	private AgentBasedWrightFisherProcessWithAssortment process;
	private AgentBasedSimulation simulation;

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

	private static LookupPlayerLengthOneTimeSeries loadFromFile(String string)
			throws IOException {
		File file = new File(string);
		Gson gson = new Gson();
		String json = Files.toString(file, Charsets.UTF_8);
		LookupPlayerLengthOneTimeSeries sim = gson.fromJson(json,
				LookupPlayerLengthOneTimeSeries.class);
		sim.init();
		return sim;
	}

	public void runApp(String filename) throws IOException {
		LookupPlayerLengthOneTimeSeries app = LookupPlayerLengthOneTimeSeries
				.loadFromFile(filename);
		app.simulation.simulateTimeSeries(app.numberOfTimeSteps,
				app.reportEveryTimeSteps, app.seed, app.outputFile);
	}

	public String exampleJson() {
		LookupPlayerLengthOneTimeSeries app = new LookupPlayerLengthOneTimeSeries();
		app.continuationProbability = 0.1;
		app.intensityOfSelection = 1.0;
		app.mapping = PayoffToFitnessMapping.LINEAR;
		app.mistakeProbability = 0.0;
		app.mutationProbability = 0.001;
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
		String json = new GsonBuilder().setPrettyPrinting().create()
				.toJson(app);
		return json;
	}
}
