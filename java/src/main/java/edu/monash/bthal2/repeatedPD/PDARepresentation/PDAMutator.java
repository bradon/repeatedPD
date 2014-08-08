package edu.monash.bthal2.repeatedPD.PDARepresentation;

import automata.pda.PushdownAutomaton;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentMutator;
import com.evolutionandgames.jevodyn.utils.Random;

public class PDAMutator implements AgentMutator {
	double mutationProbability = 0.1;// should be JSON not hd

	@Override
	public Agent mutate(Agent agent) {
		// Return randomly chosen set PDA agent
		// INITIALLY TRY WITH RANDOM STRATEGY
		// TFT, ALLC, ALLD

		// Should allow for profiling

		if (Random.nextDouble() > mutationProbability) {
			return new PDAStrategy((PushdownAutomaton) ((PDAStrategy) agent).getPDA().clone());
		} else {
			double randomDouble = Random.nextDouble();
			if (randomDouble < 0.25) {
				return PDAFactory.ExampleStrategies.allC();
			}
			if (randomDouble < 0.5) {
				return PDAFactory.ExampleStrategies.allD();
			}
			if (randomDouble<0.75) {
			return PDAFactory.ExampleStrategies.TFT();
			}
			//Test pop/push functionality
			return PDAFactory.ExampleStrategies.bradonsGrudge();
		}
	}

}
