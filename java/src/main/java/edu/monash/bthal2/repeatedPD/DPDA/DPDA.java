package edu.monash.bthal2.repeatedPD.DPDA;

import java.util.ArrayList;
import java.util.Stack;

import com.evolutionandgames.repeatedgames.evolution.Action;

import edu.monash.bthal2.repeatedPD.DPDA.Exception.CycleException;
import edu.monash.bthal2.repeatedPD.DPDA.Exception.MultipleTransitionException;
import edu.monash.bthal2.repeatedPD.DPDA.Exception.NoTransitionException;
import edu.monash.bthal2.repeatedPD.DPDA.State.Transition;

/**
 * @author Bradon Hall
 * 
 */
public class DPDA {
	// Notes: -Testing for determinism is easiest to do from perspective of
	// transitions from a state
	// Treating Transitions as link between states, which belongs in the state
	// of origin, as opposed to a separate object should simplify this aspect.
	// -This changes the mutations that will likely be used (no changeFrom at
	// first)
	// -This allows for me to use some methods similar to JG's FSA package
	Stack<Character> stack = new Stack<Character>();
	State currentState;
	State initialState;
	Action defaultAction = Action.DEFECT;
	ArrayList<State> states = new ArrayList<State>();

	// If a transition has failed, anything with the current prefix moves is not
	// in the language
	boolean prefixInLanguage = true;

	public DPDA() {
		// Initialize to ALLD

		// Set current state
		currentState = initialState;

		// Set up stack marker
		stack.add('$');
	}

	public void addState(State state) {
		states.add(state);
	}

	public void setInitialState(State state) {
		initialState = state;
		reset();
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

	public static void main(String[] args) {

	}
}
