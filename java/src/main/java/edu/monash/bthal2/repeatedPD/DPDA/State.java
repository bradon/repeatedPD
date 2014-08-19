package edu.monash.bthal2.repeatedPD.DPDA;

import java.util.ArrayList;
import java.util.Stack;

import com.evolutionandgames.repeatedgames.evolution.Action;

public class State {
	ArrayList<Transition> transitions = new ArrayList<Transition>();

	public class Transition {
		State destination;
		Action read;
		char pop;
		char push;

		public Transition(State destination, Action read, char pop, char push) {

		}

		/**
		 * Follow a transition
		 */
		public State follow(Stack<Character> stack) {
			// Pop, Pull, return state
			return null;
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

			// empty,empty to self is not allowed

			// if either read or pop is empty
			// empty, X->x means A, X ->? cannot exist
			// X, empty->x means X, A ->? cannot exist
			// Ie empty cannot be in same position as non-empty

			// At most once combination of A, B can exist

			return 0;
		}
	}
}
