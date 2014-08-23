package edu.monash.bthal2.repeatedPD.DPDA;

import java.util.ArrayList;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentMutator;
import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.fsa.FiniteStateAutomatonMutator;

public class DPDAMutator implements AgentMutator {

	private double mutationProbabilityPerState;

	// Uniform initially 'assumption free' <- arguable
	// Seems sensible though- high probability of a transition change
	// Low probability of a states change
	int mutationOptions = 8;
	private double addingStatesProbability = 1.0 * 1 / mutationOptions;
	private double removingStatesProbability = 1.0 * 1 / mutationOptions;
	private double addTransitionProbability = 1.0 * 1 / mutationOptions;
	private double removeTransitionProbability = 1.0 * 1 / mutationOptions;
	private double changingReadProbability = 1.0 * 1 / mutationOptions;
	private double changingPopProbability = 1.0 * 1 / mutationOptions;
	private double changingPushProbability = 1.0 * 1 / mutationOptions;
	private double changingDestinationProbability = 1.0 * 1 / mutationOptions;

	private double[] distributionOfEvents = { addingStatesProbability,
			removingStatesProbability, addTransitionProbability,
			removeTransitionProbability, changingReadProbability,
			changingPopProbability, changingPushProbability,
			changingDestinationProbability };

	private enum MutationEvent {
		ADD, REMOVE, ADDTRANSITION, REMOVETRANSITION, CHANGEREAD, CHANGEPOP, CHANGEPUSH, CHANGEDESTINATION
	}

	@Override
	public Agent mutate(Agent agent) {
		DPDA newDPDA = ((DPDA) agent).copy();
		ArrayList<MutationEvent> mutationEvents = buildMutationChain(newDPDA
				.getStates().size());
		for (MutationEvent mutationEvent : mutationEvents) {
			applyMutation(newDPDA, mutationEvent);
		}
		return newDPDA;
	}

	private DPDA applyMutation(DPDA automaton, MutationEvent mutationEvent) {
		switch (mutationEvent) {

		}

		return null;

	}

	/**
	 * Build a chain of mutation events to be applied to the automata. One event
	 * is added per state with probability mutationProbabilityPerState.
	 * Therefore the expected size of a chain is size of the automata times the
	 * probability of mutation per state.
	 * 
	 * @param size
	 * @return
	 */
	private ArrayList<MutationEvent> buildMutationChain(int size) {
		ArrayList<MutationEvent> mutationEvents = new ArrayList<DPDAMutator.MutationEvent>();
		for (int i = 0; i < size; i++) {
			if (Random.bernoulliTrial(mutationProbabilityPerState)) {
				int eventType = Random
						.simulateDiscreteDistribution(distributionOfEvents);
				switch (eventType) {
				case 0:
					mutationEvents.add(MutationEvent.ADD);
					break;
				case 1:
					mutationEvents.add(MutationEvent.REMOVE);
					break;
				case 2:
					mutationEvents.add(MutationEvent.ADDTRANSITION);
					break;
				case 3:
					mutationEvents.add(MutationEvent.REMOVETRANSITION);
					break;
				case 4:
					mutationEvents.add(MutationEvent.CHANGEREAD);
					break;
				case 5:
					mutationEvents.add(MutationEvent.CHANGEPOP);
					break;
				case 6:
					mutationEvents.add(MutationEvent.CHANGEPUSH);
					break;
				case 7:
					mutationEvents.add(MutationEvent.CHANGEDESTINATION);
					break;
				}
			}
		}
		return mutationEvents;
	}
}
