package edu.monash.bthal2.repeatedPD.DPDA;

import java.util.ArrayList;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentMutator;
import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.evolution.Action;

import edu.monash.bthal2.repeatedPD.DPDA.State.Transition;

public class DPDAMutator implements AgentMutator {
	// in json set this to 1/n
	private double mutationProbabilityPerState; // For testing its high

	// Uniform initially 'assumption free' <- arguable
	// Seems sensible though- high probability of a transition change
	// Low probability of a states change

	// TODO: Address bloat
	// Currently, addState can never fail, removeState can.
	// This means despit Pr(add)=Pr(remove), num states will increase
	// Maybe Pr(remove) proportional to states?
	int mutationOptions = 9;
	private double addingStatesProbability = 1.0 * 1 / mutationOptions;
	private double removingStatesProbability = 1.0 * 1 / mutationOptions;
	private double addTransitionProbability = 1.0 * 1 / mutationOptions;
	private double removeTransitionProbability = 1.0 * 1 / mutationOptions;
	private double changingReadProbability = 1.0 * 1 / mutationOptions;
	private double changingPopProbability = 1.0 * 1 / mutationOptions;
	private double changingPushProbability = 1.0 * 1 / mutationOptions;
	private double changingDestinationProbability = 1.0 * 1 / mutationOptions;
	private double flipState = 1.0 * 1 / mutationOptions;

	// TODO: Consider putting changes to transitions as a sub operation of
	// change
	public DPDAMutator() {
		super();
		mutationProbabilityPerState = 0.1; // Should be called only in testing
	}

	private double[] distributionOfEvents = { addingStatesProbability,
			removingStatesProbability, addTransitionProbability,
			removeTransitionProbability, changingReadProbability,
			changingPopProbability, changingPushProbability,
			changingDestinationProbability, flipState };

	private enum MutationEvent {
		ADDSTATE, REMOVESTATE, ADDTRANSITION, REMOVETRANSITION, CHANGEREAD, CHANGEPOP, CHANGEPUSH, CHANGEDESTINATION, FLIPSTATE
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

	/**
	 * Mutate an automaton
	 * 
	 * Expects to receive a clone- don't give it the original
	 * 
	 * @param automaton
	 * @param mutationEvent
	 * @return
	 */
	private DPDA applyMutation(DPDA automaton, MutationEvent mutationEvent) {
		switch (mutationEvent) {
		case ADDSTATE:
			State newState = new State();
			automaton.addState(newState);
			break;
		case ADDTRANSITION:
			ArrayList<State> atstates = automaton.getStates();
			State atEminatingStates = atstates.get(Random.nextInt(atstates
					.size()));
			// Generate a new transition
			Action newRead = DPDA.inputAlphabet[Random
					.nextInt(DPDA.inputAlphabet.length)];
			// Pop, Push- bias towards null?
			char newPop = DPDA.stackAlphabet[Random
					.nextInt(DPDA.stackAlphabet.length)];
			char newPush = DPDA.stackAlphabet[Random
					.nextInt(DPDA.stackAlphabet.length)];
			State newDestination = atstates
					.get(Random.nextInt(atstates.size()));
			Transition newTransition = atEminatingStates.new Transition(
					newDestination, newRead, newPop, newPush);
			if (atEminatingStates.addTransition(newTransition)) {

			} else {
				// How to respond to transition addition failure
			}
			break;
		case CHANGEDESTINATION:
			ArrayList<State> cdstates = automaton.getStates();
			if (cdstates.size() > 1) {
				State cdeminatingState = cdstates.get(Random.nextInt(cdstates
						.size()));
				ArrayList<Transition> transitions = cdeminatingState
						.getTransitions();
				if (transitions.size() > 0) {
					Transition transition = transitions.get(Random
							.nextInt(transitions.size()));
					// Change: Base probability of destination partly on
					// 'distance' from current state?
					transition.changeDestination(cdstates.get(Random
							.nextInt(cdstates.size())));
				} else {
					// Switch to add transition?
				}
			} else {
				// Switch to add state?
			}
			break;
		case CHANGEPOP:
			ArrayList<State> cpstates = automaton.getStates();
			if (cpstates.size() > 1) {
				State cdeminatingState = cpstates.get(Random.nextInt(cpstates
						.size()));
				ArrayList<Transition> transitions = cdeminatingState
						.getTransitions();
				if (transitions.size() > 0) {
					Transition transition = transitions.get(Random
							.nextInt(transitions.size()));
					// Change: Base probability of destination partly on
					// 'distance' from current state?
					transition.setPop((DPDA.stackAlphabet[Random
							.nextInt(DPDA.stackAlphabet.length)]));
				} else {
					// Switch to add transition?
				}
			} else {
				// Switch to add state?
			}
			break;
		case CHANGEPUSH:
			ArrayList<State> cpustates = automaton.getStates();
			if (cpustates.size() > 1) {
				State cdeminatingState = cpustates.get(Random.nextInt(cpustates
						.size()));
				ArrayList<Transition> transitions = cdeminatingState
						.getTransitions();
				if (transitions.size() > 0) {
					Transition transition = transitions.get(Random
							.nextInt(transitions.size()));
					// Change: Base probability of destination partly on
					// 'distance' from current state?
					transition.setPush((DPDA.stackAlphabet[Random
							.nextInt(DPDA.stackAlphabet.length)]));
				} else {
					// Switch to add transition?
				}
			} else {
				// Switch to add state?
			}
			break;
		case CHANGEREAD:
			ArrayList<State> crstates = automaton.getStates();
			if (crstates.size() > 1) {
				State cdeminatingState = crstates.get(Random.nextInt(crstates
						.size()));
				ArrayList<Transition> transitions = cdeminatingState
						.getTransitions();
				if (transitions.size() > 0) {
					Transition transition = transitions.get(Random
							.nextInt(transitions.size()));
					// Change: Base probability of destination partly on
					// 'distance' from current state?
					transition.setRead((DPDA.inputAlphabet[Random
							.nextInt(DPDA.inputAlphabet.length)]));
				} else {
					// Switch to add transition?
				}
			} else {
				// Switch to add state?
			}
			break;
		case REMOVESTATE:
			// Going to be a bit complex
			// If 1 state, do nothing
			// if >1, delete state and redirect transitions
			// redirect transitions will require stepping through all
			// transitions
			ArrayList<State> rsstates = automaton.getStates();

			if (rsstates.size() > 1) {
				State rmState;
				rmState = rsstates.get(Random.nextInt(rsstates.size()));
				automaton.removeState(rmState);
				// Iterate through remaining states
				rsstates = automaton.getStates();
				for (State state : rsstates) {
					for (Transition transition : state.getTransitions()) {
						// Change destination can create loops, can't remove
						// determinism
						transition.changeDestination(rsstates.get(Random
								.nextInt(rsstates.size())));
					}
				}

			} else {
				// Cant remove action?
			}
			break;
		case REMOVETRANSITION:
			ArrayList<State> rtstates = automaton.getStates();
			State rtState = rtstates.get(Random.nextInt(rtstates.size()));
			ArrayList<Transition> rttransitions = rtState.getTransitions();
			if (rttransitions.size() > 0) {
				Transition rttransition = rttransitions.get(Random
						.nextInt(rttransitions.size()));
				rtState.removeTransition(rttransition);
			}
			break;
		case FLIPSTATE:
			ArrayList<State> fsstates = automaton.getStates();
			fsstates.get(Random.nextInt(fsstates.size())).flip();
			break;
		}

		return automaton;

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
					mutationEvents.add(MutationEvent.ADDSTATE);
					break;
				case 1:
					mutationEvents.add(MutationEvent.REMOVESTATE);
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
				case 8:
					mutationEvents.add(MutationEvent.FLIPSTATE);
					break;
				}
			}
		}
		return mutationEvents;
	}
}
