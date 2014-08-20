package edu.monash.bthal2.repeatedPD.DPDA;

import java.util.ArrayList;
import java.util.Stack;

import com.evolutionandgames.repeatedgames.evolution.Action;

public class State {
	final static char emptyChar = 'l'; // move elsewhere?
	ArrayList<Transition> transitions = new ArrayList<Transition>();

	public boolean addTransition(Transition newTransition) {
		if (newTransition.checkDeterminism() == -1) {
			// empty,empty to self is not allowed
			if (newTransition.destination != this) {
				transitions.add(newTransition);
				return true;
			}
		}
		return false;
	}


	public class Transition {
		State destination;
		Action read;
		char pop;
		char push;

		public Transition(State destination, Action read, char pop, char push) {
			this.destination = destination;
			this.pop = pop;
			this.push = push;
			this.read = read;
		}

		public void changeDestination(State destination) {
			this.destination = destination;
		}

		/**
		 * Follow a transition
		 */
		public State follow(Stack<Character> stack) {
			// Pop, Pull, return state
			stack.pop();

			if (push != emptyChar) {
				stack.push(push);
			}
			return destination;
		}

		/**
		 * @return null array if deterministic <br>
		 *         or int array of index of non-deterministic transitions<br>
		 * 
		 */
		public int checkDeterminism() {
			// It is assumed transitions already added are deterministic
			// Compare existing to new, not existing to existing

			// If there is an empty, empty -> x transition, there can be no
			// other transitions
			if (isDoNothingTransition(this)) {
				// conflict with all (if any exist)
			}

			// if either read or pop is empty
			// empty, X->x means A, X ->? cannot exist
			// X, empty->x means X, A ->? cannot exist
			// Ie empty cannot be in same position as non-empty

			// At most once combination of A, B can exist

			return 0;
		}

		private boolean isDoNothingTransition(Transition transition) {
			if (transition.pop == emptyChar && transition.read == null) {
				return true;
			}
			return false;
		}
	}
}
