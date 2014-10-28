package edu.monash.bthal2.repeatedPD.LookupTableRepresentation;

import java.lang.reflect.Array;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.agentbased.AgentBasedPopulationFactory;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulationImpl;
import com.evolutionandgames.repeatedgames.evolution.Action;

/**
 * Factory for Agents with memory 1/2 , length 1
 * 
 * @author Bradon Hall
 * 
 */
public class LookupStrategyLengthOneFactory implements
		AgentBasedPopulationFactory {
	int popSize = 100;

	public LookupStrategyLengthOneFactory(int popSize) {
		this.popSize = popSize;
	}

	public LookupStrategyLengthOneFactory() {

	}

	/**
	 * Default allD strategy
	 * 
	 * @return
	 */
	public static LookupStrategyLengthOne allD() {
		return new LookupStrategyLengthOne();
	}
	
	public static LookupStrategyLengthOne stft() {
		Action[] stft ={Action.DEFECT,Action.COOPERATE,Action.DEFECT};
		return new LookupStrategyLengthOne(stft);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.evolutionandgames.agentbased.AgentBasedPopulationFactory#createPopulation
	 * ()
	 * 
	 * Create population of possibly specified size, all simple ALLD
	 */
	public AgentBasedPopulation createPopulation() {
		Agent[] agents = new LookupStrategyLengthOne[popSize];
		for (int i = 0; i < popSize; i++) {
			agents[i] = new LookupStrategyLengthOne();
		}
		return new ExtensivePopulationImpl(agents);
	}
}
