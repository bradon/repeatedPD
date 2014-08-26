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

	public DPDAMutator(double mutationProbabilityPerState) {
		super();
		this.mutationProbabilityPerState = mutationProbabilityPerState;
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
		// TODO: most of these are complicated enough to justify seperating to
		// new functions
		// Some change transition events share code too
		switch (mutationEvent) {
		case ADDSTATE:
			addState(automaton);
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
		case CHANGEPOP:
		case CHANGEPUSH:
		case CHANGEREAD:
			changeTransition(automaton, mutationEvent);
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
				// +1 to prevent 0 (initial state) being selected
				rmState = rsstates.get(1 + Random.nextInt(rsstates.size() - 1));
				automaton.removeState(rmState);
				// Iterate through remaining states
				rsstates = automaton.getStates();
				for (State state : rsstates) {
					for (Transition transition : state.getTransitions()) {
						// Change destination can create loops, can't remove
						// determinism

						// TODO: Check if a loop is created
						State randomState = rsstates.get(Random
								.nextInt(rsstates.size()));
						// Basic self-loop check, identifies empty, $ or
						// empty->? to self
						if (!(randomState == state && transition.getRead() == null)
								&& (transition.getPop() == DPDA.stackMarker || transition
										.getPop() == DPDA.emptyChar)) {
							transition.changeDestination(randomState);
						}
					}
				}
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

	/**
	 * Add a state to an automaton
	 * 
	 * @param dpda
	 */
	public void addState(DPDA dpda) {
		boolean addOutwardsTransitionsWithState = true;
		boolean addInwardsTransitionWithState = true;
		// Options:
		// add state, do nothing
		// add state, add transition to
		// add state, add transitions from
		// add state, add transitions to+from
		// last compatible with pruning

		State newState = new State();
		dpda.addState(newState);
		// 50/50 what the state is.....
		if (Random.nextBoolean()) {
			newState.flip();
		}

		if (addOutwardsTransitionsWithState) {
			// Increase chance of state being useful?
			// Allow pruning function to do its thing
			newState.addTransition(newState.new Transition(dpda.getStates()
					.get(Random.nextInt(dpda.getStates().size())),
					Action.COOPERATE, DPDA.emptyChar, DPDA.emptyChar));
			newState.addTransition(newState.new Transition(dpda.getStates()
					.get(Random.nextInt(dpda.getStates().size())),
					Action.DEFECT, DPDA.emptyChar, DPDA.emptyChar));
		}
		if (addInwardsTransitionWithState) {

		}
	}

	/**
	 * Random prune
	 * 
	 * @param dpda
	 */
	public void prune(DPDA dpda) {
		ArrayList<State> states = dpda.getStates();
		// Pick a random part of the dpda except head
		State state = states.get(1 + Random.nextInt(states.size() - 1));
		for (State sourceState : states) {
			for (Transition transition : sourceState.getTransitions()) {
				if (transition.getDestination() == state)
					// If state selected is reachable
					return;
			}
		}
		// State unreachable
		dpda.removeState(state);
	}

	/**
	 * Change A Random Transition Function
	 * 
	 * @param dpda
	 * @param mutationEvent
	 */
	public void changeTransition(DPDA dpda, MutationEvent mutationEvent) {
		ArrayList<State> cdstates = dpda.getStates();
		if (cdstates.size() > 1) {
			State state = cdstates.get(Random.nextInt(cdstates.size()));
			ArrayList<Transition> transitions = state.getTransitions();
			if (transitions.size() > 0) {
				Transition transition = transitions.get(Random
						.nextInt(transitions.size()));
				Transition newTransition = state.copyTransition(transition);
				state.removeTransition(transition);
				// Change: Base probability of destination partly on
				// 'distance' from current state?
				switch (mutationEvent) {
				case CHANGEDESTINATION:
					newTransition.changeDestination(cdstates.get(Random
							.nextInt(cdstates.size())));
					break;
				case CHANGEPOP:
					newTransition.setPop((DPDA.stackAlphabet[Random
							.nextInt(DPDA.stackAlphabet.length)]));
					break;
				case CHANGEPUSH:
					newTransition.setPush((DPDA.stackAlphabet[Random
							.nextInt(DPDA.stackAlphabet.length)]));
					break;
				case CHANGEREAD:
					newTransition.setRead((DPDA.inputAlphabet[Random
							.nextInt(DPDA.inputAlphabet.length)]));
					break;
				default:
					System.out.println("Unreachable code reached");
					break;
				}
				if (!state.addTransition(newTransition)) {
					// if addition fails
					// revert to original! (with transition moved->
					// problem?)
					state.addTransition(transition);
				}
			}
		}
	}

	public void addTransition(DPDA dpda) {

	}
}
