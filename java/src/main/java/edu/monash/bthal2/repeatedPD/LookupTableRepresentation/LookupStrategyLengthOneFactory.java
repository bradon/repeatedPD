package edu.monash.bthal2.repeatedPD.LookupTableRepresentation;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.agentbased.AgentBasedPopulationFactory;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulationImpl;

public class LookupStrategyLengthOneFactory implements
		AgentBasedPopulationFactory {
	int popSize = 100;

	public LookupStrategyLengthOneFactory(int popSize) {
		this.popSize = popSize;
	}

	public LookupStrategyLengthOneFactory() {

	}

	public static LookupStrategyLengthOne allD() {
		return new LookupStrategyLengthOne();
	}

	public AgentBasedPopulation createPopulation() {
		Agent[] agents = new LookupStrategyLengthOne[popSize];
		for (int i = 0; i < popSize; i++) {
			agents[i] = new LookupStrategyLengthOne();
		}
		return new ExtensivePopulationImpl(agents);
	}
}
