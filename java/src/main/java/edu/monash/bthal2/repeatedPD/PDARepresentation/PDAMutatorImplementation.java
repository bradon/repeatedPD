package edu.monash.bthal2.repeatedPD.PDARepresentation;

import java.awt.Point;

import automata.State;
import automata.Transition;
import automata.pda.PushdownAutomaton;
import automata.pda.PDATransition;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentMutator;
import com.evolutionandgames.jevodyn.utils.Random;

public class PDAMutatorImplementation implements AgentMutator {
	double mutationProbability = 0.1;// move to json

	// Mutation Type Parameters
	// Sum to 1, mutationProbability determines chance of selecting from these

	// move to json ofc
	double uniformShare = 1.0 * 1 / 8;
	double addStateProbability = uniformShare;
	double addTransitionProbability = uniformShare;
	double changeTransitionDestinationProbability = uniformShare;
	double changeTransitionPopProbability = uniformShare;
	double changeTransitionPushProbability = uniformShare;
	double changeTransitionSourceProbability = uniformShare;
	double changeFinalStateProbability = uniformShare;
	double deleteStateProbability = uniformShare;

	String[] readAlphabet = { "C", "D" };
	String[] stackAlphabet = { "0", "" }; // Modifiable for each
											// PDA?

	public PDAMutatorImplementation(double mutationProbability) {
		super();
		this.mutationProbability = mutationProbability;
	}

	private void addState(PushdownAutomaton pda, int statesCount) {
		State newState = pda.createState(new Point(0, 0));
		newState.setLabel("q" + statesCount);
	}

	private void addTransition(PushdownAutomaton pda) {
		// Add transition
		State[] states = pda.getStates();
		State sourceState = states[Random.nextInt(states.length)];
		State destinationState = states[Random.nextInt(states.length)];
		// Bias towards null pop/push?
		// First method- just have a lot of nulls in alphabet

		String readString = readAlphabet[Random.nextInt(readAlphabet.length)];
		String popString = stackAlphabet[Random.nextInt(stackAlphabet.length)];
		String pushString = stackAlphabet[Random.nextInt(stackAlphabet.length)];
		PDATransition newTransition = new PDATransition(sourceState,
				destinationState, readString, popString, pushString);
		pda.addTransition(newTransition);
	}

	public void changeDestination(PushdownAutomaton pda) {
		State[] states = pda.getStates();
		Transition[] transitions = pda.getTransitions();
		PDATransition toChange = (PDATransition) transitions[Random
				.nextInt(transitions.length)];
		PDATransition newTransition = (PDATransition) toChange.clone();
		newTransition.setToState(states[Random.nextInt(states.length)]);
		pda.replaceTransition(toChange, newTransition);
	}

	public void changeTransition(PushdownAutomaton pda) {
		Transition[] transitions = pda.getTransitions();
		PDATransition toChange = (PDATransition) transitions[Random
				.nextInt(transitions.length)];
		String newPop = stackAlphabet[Random.nextInt(stackAlphabet.length)];
		PDATransition newTransition = new PDATransition(
				toChange.getFromState(), toChange.getToState(),
				toChange.getInputToRead(), newPop, toChange.getStringToPush());
		pda.replaceTransition(toChange, newTransition);
	}

	public void changePush(PushdownAutomaton pda) {
		Transition[] transitions = pda.getTransitions();
		PDATransition toChange = (PDATransition) transitions[Random
				.nextInt(transitions.length)];
		String newPush = stackAlphabet[Random.nextInt(stackAlphabet.length)];
		PDATransition newTransition = new PDATransition(
				toChange.getFromState(), toChange.getToState(),
				toChange.getInputToRead(), toChange.getStringToPop(), newPush);
		pda.replaceTransition(toChange, newTransition);
	}

	public void changeSource(PushdownAutomaton pda) {
		State[] states = pda.getStates();
		Transition[] transitions = pda.getTransitions();
		PDATransition toChange = (PDATransition) transitions[Random
				.nextInt(transitions.length)];
		PDATransition newTransition = (PDATransition) toChange.clone();
		newTransition.setFromState(states[Random.nextInt(states.length)]);
		pda.replaceTransition(toChange, newTransition);
	}

	public void changeFinal(PushdownAutomaton pda) {
		State[] states = pda.getStates();
		State stateToFlip = states[Random.nextInt(states.length)];
		if (pda.isFinalState(stateToFlip)) {
			pda.removeFinalState(stateToFlip);
		} else {
			pda.addFinalState(stateToFlip);
		}
	}

	// This operation changes a lot of the automata
	// Large 'edit distance jump'
	public void deleteState(PushdownAutomaton pda) {
		State[] states = pda.getStates();
		// Select state except initial state, if there is such a state
		if (states.length < 2) {
			return;
		}
		State stateToDelete = states[Random.nextInt(states.length - 1) + 1];
		// Get transitions that transition from this state
		Transition[] transitionsOut = pda
				.getTransitionsFromState(stateToDelete);
		// Get transitions that transition to this state
		Transition[] transitionsIn = pda.getTransitionsToState(stateToDelete);
		// For each transition out, replace it
		for (int i = 0; i < transitionsOut.length; i++) {
			PDATransition toChange = (PDATransition) transitionsOut[i];
			PDATransition newTransition = (PDATransition) toChange.clone();
			newTransition.setFromState(states[Random.nextInt(states.length)]);
			pda.addTransition(newTransition);
		}
		// For each transition in
		for (int i = 0; i < transitionsIn.length; i++) {
			PDATransition toChange = (PDATransition) transitionsIn[i];
			PDATransition newTransition = (PDATransition) toChange.clone();
			newTransition.setToState(states[Random.nextInt(states.length)]);
			pda.addTransition(newTransition);
		}
		// May remove states we just added
		pda.removeState(stateToDelete);
	}

	@Override
	public Agent mutate(Agent agent) {
		if (Random.nextDouble() > mutationProbability) {
			return new PDAStrategy((PushdownAutomaton) ((PDAStrategy) agent)
					.getPDA().clone(),((PDAStrategy) agent).getStatesCount());
		} else {
			// mutate
			PushdownAutomaton pda = (PushdownAutomaton) ((PDAStrategy) agent)
					.getPDA().clone();
			double mutationSelector = Random.nextDouble();
			// System.out.println(mutationSelector);
			if (mutationSelector < addStateProbability) {
				// Add state
				// Initially has no actual impact
				// System.out.println("Adding state");
				addState(pda, ((PDAStrategy) agent).getStatesCount());
				return new PDAStrategy(pda,
						((PDAStrategy) agent).getStatesCount()+1);
			} else if (mutationSelector < addStateProbability
					+ addTransitionProbability) {
				// System.out.println("Adding transition");
				addTransition(pda);
				return new PDAStrategy(pda,((PDAStrategy) agent).getStatesCount());
			} else if (mutationSelector < addStateProbability
					+ addTransitionProbability
					+ changeTransitionDestinationProbability) {
				// System.out.println("Changing destination");
				changeDestination(pda);
				return new PDAStrategy(pda,((PDAStrategy) agent).getStatesCount());
				// Change transition
			} else if (mutationSelector < addStateProbability
					+ addTransitionProbability
					+ changeTransitionDestinationProbability
					+ changeTransitionPopProbability) {
				// System.out.println("Changing pop");
				// Change Pop
				changeTransition(pda);
				return new PDAStrategy(pda,((PDAStrategy) agent).getStatesCount());
			} else if (mutationSelector < addStateProbability
					+ addTransitionProbability
					+ changeTransitionDestinationProbability
					+ changeTransitionPopProbability
					+ changeTransitionPushProbability) {
				// System.out.println("Changing push");
				// Change push
				changePush(pda);
				return new PDAStrategy(pda,((PDAStrategy) agent).getStatesCount());
			} else if (mutationSelector < addStateProbability
					+ addTransitionProbability
					+ changeTransitionDestinationProbability
					+ changeTransitionPopProbability
					+ changeTransitionPushProbability
					+ changeTransitionSourceProbability) {
				// System.out.println("Changing source");
				// Change source
				changeSource(pda);
				return new PDAStrategy(pda,((PDAStrategy) agent).getStatesCount());
			} else if (mutationSelector < addStateProbability
					+ addTransitionProbability
					+ changeTransitionDestinationProbability
					+ changeTransitionPopProbability
					+ changeTransitionPushProbability
					+ changeTransitionSourceProbability
					+ changeFinalStateProbability) {
				// System.out.println("Changing final");
				// Change final states
				changeFinal(pda);
				return new PDAStrategy(pda,((PDAStrategy) agent).getStatesCount());
			} else {
				deleteState(pda);
				return new PDAStrategy(pda,((PDAStrategy) agent).getStatesCount());
				// Delete state
			}
			// return null;
		}

	}

}
