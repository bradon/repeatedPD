package edu.monash.bthal2.repeatedPD.DPDA;

import java.util.Stack;

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

	/**
	 * Reset to initial state (before input)
	 */
	public void reset() {
		// Set current state
		currentState = initialState;
		stack = new Stack<Character>();
		stack.add('$');
	}

	public static void main(String[] args) {
		State state = new State();
		Transition trans = state.new Transition(null, null, 'l', 'l');
	}
}
