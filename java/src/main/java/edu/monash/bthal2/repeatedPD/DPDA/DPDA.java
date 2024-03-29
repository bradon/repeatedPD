package edu.monash.bthal2.repeatedPD.DPDA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.repeatedgames.evolution.Action;
import com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy;

import edu.monash.bthal2.repeatedPD.DPDA.DPDAMutator.MutationEvent;
import edu.monash.bthal2.repeatedPD.DPDA.State.Transition;
import edu.monash.bthal2.repeatedPD.DPDA.Exception.CycleException;
import edu.monash.bthal2.repeatedPD.DPDA.Exception.MultipleTransitionException;
import edu.monash.bthal2.repeatedPD.DPDA.Exception.NoTransitionException;

/**
 * Deterministic Pushdown Automata that play the half-memory Prisoner's Dilemma
 * 
 * @author Bradon Hall
 * 
 */

public class DPDA implements Agent, RepeatedStrategy {
	public MutationEvent lastMutation = null;
	// TODO: Pop stack marker should always push stack marker?
	public static final char emptyChar = 'l';
	public static final char stackMarker = '$';
	// TODO: Different Alphabets
	// Stack marker should be tail
	public static final char[] stackAlphabet = { 'l', 'a', stackMarker };
	public static final Action[] inputAlphabet = { null, Action.COOPERATE,
			Action.DEFECT };
	// Notes: -Testing for determinism is easiest to do from perspective of
	// transitions from a state
	// Treating Transitions as link between states, which belongs in the state
	// of origin, as opposed to a separate object should simplify this aspect.
	// -This changes the mutations that will likely be used (no changeFrom at
	// first)
	// -This allows for me to use some methods similar to JG's FSA package
	private Stack<Character> stack = new Stack<Character>();
	private State currentState;
	private State initialState;
	Action defaultAction = Action.DEFECT;
	private ArrayList<State> states = new ArrayList<State>();

	// Accept if in Language vs Accept if not in Language
	public boolean flipResult = false;

	// If a transition has failed, anything with the current prefix moves is not
	// in the language
	protected boolean prefixInLanguage = true;

	/**
	 * Constructor<br>
	 * Creates single-state ALLD automaton by default
	 */
	public DPDA() {
		State initialState = new State();
		// Set current state
		currentState = initialState;

		// Set up stack marker
		stack.add(stackMarker);
	}

	/**
	 * Create a copy of the current automaton
	 * 
	 * @return Copy of Automaton
	 */
	public DPDA copy() {
		// Copy can ignore the stack
		// Must create new states. Transitions need a way to know what the new
		// state is based on the old state
		HashMap<State, State> map = new HashMap<State, State>();
		// Map old to new states
		for (State state : states) {
			map.put(state, new State());
		}
		DPDA newDPDA = new DPDA();
		newDPDA.setInitialState(map.get(this.initialState));
		for (State state : states) {
			newDPDA.addState(map.get(state));
			map.get(state).isFinal = state.isFinal;

			State newState = map.get(state);
			// Set state id if implemented
			for (Transition transition : state.getTransitions()) {
				// map old transition to new
				State newDestination = map.get(transition.getDestination());
				if (newDestination == null) {
					System.out.println("COPY HAD A NULL STATE");
					printStrategy();
				} else {
					Transition newTransition = newState.new Transition(
							newDestination, transition.getRead(),
							transition.getPop(), transition.getPush());
					map.get(state).addTransition(newTransition);
				}
			}
		}
		newDPDA.flipResult = flipResult;
		return newDPDA;

	}

	/**
	 * Add state to DPDA
	 * 
	 * @param state
	 */
	public void addState(State state) {
		states.add(state);
	}

	/**
	 * Set initial state and reset automaton
	 * 
	 * @param state
	 */
	public void setInitialState(State state) {
		initialState = state;
		reset();
	}

	/**
	 * Get initial state
	 * 
	 * @return
	 */
	public State getInitialState() {
		return initialState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy#currentAction
	 * ()
	 */
	public Action currentAction() {
		if (currentState == null) {
			System.out.println("Current state is null");
			// printStrategy();
			if (flipResult) {
				if (defaultAction == Action.COOPERATE) {
					return Action.DEFECT;
				} else {
					return Action.COOPERATE;
				}
			} else {
				return defaultAction;
			}
		}
		if (currentState.isFinal && prefixInLanguage) {
			if (flipResult) {
				return Action.DEFECT;
			} else {
				return Action.COOPERATE;
			}
		} else {
			if (flipResult) {
				return Action.COOPERATE;
			} else {
				return Action.DEFECT;
			}
		}
	}

	/**
	 * Reset to initial state (before input)
	 */
	public void reset() {
		// Set current state
		currentState = initialState;
		stack = new Stack<Character>();
		stack.add('$'); // Stack marker
		prefixInLanguage = true;
	}

	/**
	 * Cycle detection
	 * 
	 * @param transitions
	 *            in a DPDA
	 * @return true if a cycle is found
	 */
	public static boolean containsCycle(ArrayList<State> states) {
		// TODO: Cycle detection
		return false;
	}

	/**
	 * Follow the DPDA according to an input
	 * 
	 * @param input
	 * @return
	 * @throws MultipleTransitionException
	 */
	public Action readInput(Action input) throws MultipleTransitionException {
		if (prefixInLanguage) {
			try {
				if (currentState == null) {
					System.out
							.println("Current state was null before following transition");
					prefixInLanguage = false;
					return defaultAction;
				}
				currentState = currentState.readInput(stack, input);
				if (currentState == null) {
					System.out
							.println("Current state was null after following transition");
					prefixInLanguage = false;
					return defaultAction;
				}
				if (currentState.isFinal) {
					return Action.COOPERATE;
				} else {
					return Action.DEFECT;
				}
			} catch (MultipleTransitionException e) {
				// If non-determinisim is appearing, serious problem, break
				System.out.println("A PDA was non-deterministic");
				printStrategy();
				throw e;

			} catch (NoTransitionException e) {
				// System.out.println("No transition found");
				prefixInLanguage = false;
				return defaultAction;
			} catch (CycleException e) {
				// Non-fatal
				System.out.println("A DPDA appeared to have a cycle");
				printStrategy();
				prefixInLanguage = false;
				return defaultAction;
			}
		}
		return defaultAction;
	}

	/**
	 * Get states in the DPDA
	 * 
	 * @return
	 */
	public ArrayList<State> getStates() {
		return states;
	}

	@Override
	public void next(Action focal, Action opponent) {
		try {
			readInput(opponent);
		} catch (MultipleTransitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object obj) {

		// TODO: Replace with more sensible method, esp for tostring comparison
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		// System.out.println(obj.toString());
		// System.out.println(this.toString());
		if (obj.toString().equals(this.toString())) {
			if (this.states.size() == ((DPDA) obj).getStates().size()) {
				return true;
				// should be return true, disabled for testing
			}
			return false;
		}
		return false;
	}

	@Override
	public int hashCode() {
		// TODO: Replace with something more sensible
		return this.toString().hashCode();
	}

	/**
	 * Remove a state from the DPDA
	 * 
	 * @param toRemove
	 */
	public void removeState(State toRemove) {
		// Possibly put checks here
		states.remove(toRemove);
	}

	@Override
	public String toString() {
		// I am likely to add IDs to states
		// Start by printing each state, indexed by position in states arraylist
		// Postfix F if final
		StringBuilder builder = new StringBuilder();
		if (flipResult) {
			builder.append("S!#");
		} else {
			builder.append("S#");
		}
		for (int i = 0; i < states.size(); i++) {
			if (i > 0) {
				builder.append("&");
			}
			builder.append("q" + i);
			if (states.get(i).isFinal) {
				builder.append("F");
			}
		}
		builder.append("T#");
		// This is terrible, revisit asap
		// No need to search for states each time
		for (int i = 0; i < states.size(); i++) {
			for (Transition transition : states.get(i).getTransitions()) {
				builder.append("q" + i);
				builder.append("-");
				builder.append("q"
						+ states.indexOf(transition.getDestination()));
				builder.append("v");
				if (transition.getRead() == null) {
					builder.append(emptyChar);
				} else if (transition.getRead() == Action.COOPERATE) {
					builder.append("C");
				} else {
					builder.append("D");
				}
				builder.append("." + transition.getPop() + "->"
						+ transition.getPush());
				builder.append("&");
			}
		}
		return builder.toString();
	}

	/**
	 * ToString to System.out
	 */
	public void printStrategy() {
		System.out.println(this.toString());
	}
}
