package edu.monash.bthal2.repeatedPD.DPDA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.evolutionandgames.repeatedgames.evolution.Action;

import edu.monash.bthal2.repeatedPD.DPDA.State.Transition;
import edu.monash.bthal2.repeatedPD.DPDA.Exception.CycleException;
import edu.monash.bthal2.repeatedPD.DPDA.Exception.MultipleTransitionException;
import edu.monash.bthal2.repeatedPD.DPDA.Exception.NoTransitionException;

/**
 * @author Bradon Hall
 * 
 */
public class DPDA {
	public static final char emptyChar = 'l';
	public static final char stackMarker = '$';
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

	// If a transition has failed, anything with the current prefix moves is not
	// in the language
	protected boolean prefixInLanguage = true;

	public DPDA() {
		// Initialize to ALLD

		// Set current state
		currentState = initialState;

		// Set up stack marker
		stack.add('$');
	}

	public DPDA copy() {
		// Copy can ignore the stack
		// Must create new states. Transitions need a way to know what the new
		// state is based on the old state
		HashMap<State, State> map = new HashMap<State, State>();
		// fill map first
		for (State state : states) {
			map.put(state, new State());
		}
		DPDA newDPDA = new DPDA();
		newDPDA.setInitialState(map.get(this.initialState));
		// newDPDA.setInitialState(map.get(this.initialState));
		for (State state : states) {
			newDPDA.addState(map.get(state));
			map.get(state).isFinal = state.isFinal;
			State newState = map.get(state);
			// Set state id?
			for (Transition transition : state.getTransitions()) {
				// map old transition to new
				State newDestination = map.get(transition.getDestination());
				Transition newTransition = newState.new Transition(
						newDestination, transition.getRead(),
						transition.getPop(), transition.getPush());
				map.get(state).addTransition(newTransition);
			}
		}

		return newDPDA;

	}

	public void addState(State state) {
		states.add(state);
	}

	public void setInitialState(State state) {
		initialState = state;
		reset();
	}

	public State getInitialState() {
		return initialState;
	}

	public Action currentAction() {
		if (currentState.isFinal && prefixInLanguage) {
			return Action.COOPERATE;
		} else {
			return Action.DEFECT;
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
		return false;
	}

	public Action readInput(Action input) throws MultipleTransitionException {
		if (prefixInLanguage) {
			try {
				currentState = currentState.readInput(stack, input);
				if (currentState.isFinal) {
					return Action.COOPERATE;
				} else {
					return Action.DEFECT;
				}
			} catch (MultipleTransitionException e) {
				// Handle here or throw?
				System.out.println("A PDA was non-deterministic");
				throw e;

			} catch (NoTransitionException e) {
				System.out.println("No transition found");
				prefixInLanguage = false;
				return defaultAction;
			} catch (CycleException e) {
				// Non-fatal
				System.out.println("A PDA appeared to have a cycle");
				prefixInLanguage = false;
				return defaultAction;
			}
		}
		return defaultAction;
	}

	public ArrayList<State> getStates() {
		return states;
	}
}
