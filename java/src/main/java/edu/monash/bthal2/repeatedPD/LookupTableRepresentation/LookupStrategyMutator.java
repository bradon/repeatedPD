package edu.monash.bthal2.repeatedPD.LookupTableRepresentation;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentMutator;
import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.evolution.Action;

public class LookupStrategyMutator implements AgentMutator {

	@Override
	public Agent mutate(Agent agent) {
		if (Random.nextDouble() > mutationProbability) {
			// No mutation
			return new LookupStrategyLengthOne(
					((LookupStrategyLengthOne) agent).getStrategyVector());
		} else {
			Action[] strategy = ((LookupStrategyLengthOne) agent)
					.getStrategyVector();
			int randomBit = Random.nextInt(strategy.length);
			// Bit flip mutation
			if (strategy[randomBit] == Action.COOPERATE) {
				strategy[randomBit] = Action.DEFECT;
			} else {
				strategy[randomBit] = Action.COOPERATE;
			}
			return new LookupStrategyLengthOne(strategy);
		}
	}

	private double mutationProbability = 0.1;

	/**
	 * Constructor- use default mutation Probability
	 */
	public LookupStrategyMutator() {

	}

	/**
	 * Constructor- specify mutation Probability
	 * 
	 * @param mutationProbability
	 */
	public LookupStrategyMutator(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

}
