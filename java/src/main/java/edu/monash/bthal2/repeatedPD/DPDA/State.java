package edu.monash.bthal2.repeatedPD.DPDA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import com.evolutionandgames.repeatedgames.evolution.Action;

import edu.monash.bthal2.repeatedPD.DPDA.Exception.CycleException;
import edu.monash.bthal2.repeatedPD.DPDA.Exception.MultipleTransitionException;
import edu.monash.bthal2.repeatedPD.DPDA.Exception.NoTransitionException;

public class State {

	// TODO: Getters/setters for everything (Before implementing copyable)
	private ArrayList<Transition> transitions = new ArrayList<Transition>();
	boolean isFinal = false;
	final int loopTolerance = 10; // How many states can be visited on a single
									// input before assuming it is in a cycle

	/**
	 * Possible transitions from the current state given an input and a stack.<br>
	 * 0 or 1 should exist
	 * 
	 * @param input
	 * @return
	 */
	public ArrayList<Transition> possibleTransitions(Action input, char popped) {
		ArrayList<Transition> possibleTransitions = new ArrayList<Transition>();
		for (Transition transition : transitions) {
			if (transition.pop == popped || transition.pop == DPDA.emptyChar) {
				// Stack state allows current transition
				if (transition.read == input || transition.read == null) {
					// Input read allows current transition
					possibleTransitions.add(transition);
				} else {
					// System.out.println("Fail: " + transition.read +
					// " is not "
					// + input + " or null");
				}
			} else {
				// System.out.println("Fail: " + transition.pop + " is not "
				// + popped + " or " + emptyChar);
			}
		}
		return possibleTransitions;

	}

	public void removeTransition(Transition transition) {
		transitions.remove(transition);
	}

	/**
	 * Adds transition<br>
	 * Returns true if added successfully
	 * 
	 * @param newTransition
	 * @return
	 */
	public boolean addTransition(Transition newTransition) {
		if (newTransition.checkDeterminism().isEmpty()) {
			// empty,empty to self is not allowed
			if (newTransition.isDoNothingTransition()) {
				if (newTransition.destination != this) {
					transitions.add(newTransition);
					return true;
				} else {
					// Was a do nothing transition, disallow
					return false;
				}
			} else {
				if (newTransition.getDestination() != null) {
					transitions.add(newTransition);
					return true;
				} else {
					System.out.println("ADD TRANSITION: ADDING NULL ATTEMPTED");
				}
				// System.out.println("wasn't do nothing");
			}
		} else {
			// System.out.println("wasn't determinisitic");
		}
		return false;
	}

	/**
	 * Takes input and current stack, and follows transitions
	 * 
	 * @param stack
	 * @param input
	 * @return
	 * @throws MultipleTransitionException
	 * @throws NoTransitionException
	 * @throws CycleException
	 */
	public State readInput(Stack<Character> stack, Action input)
			throws MultipleTransitionException, NoTransitionException,
			CycleException {
		return readInput(stack, input, 0);
	}

	/**
	 * Takes input and current stack, and follows transitions<br>
	 * DOES NOT FOLLOW EMPTY TRANSITIONS AFTER, SHOULD FOR COOPERATION CHECK
	 * 
	 * @param stack
	 * @param input
	 * @return
	 * @throws MultipleTransitionException
	 * @throws NoTransitionException
	 * @throws CycleException
	 */
	private State readInput(Stack<Character> stack, Action input,
			int statesVisitedThisInput) throws MultipleTransitionException,
			NoTransitionException, CycleException {
		if (loopTolerance + 1 > statesVisitedThisInput) {
			char topOfStack = stack.peek();

			ArrayList<Transition> possibleTransitions = possibleTransitions(
					input, topOfStack);
			if (possibleTransitions.isEmpty()) {
				// No transitions
				// System.out.println("There were " + transitions.size()
				// + " transitions to choose from");
				throw new NoTransitionException();
			}
			if (possibleTransitions.size() > 1) {
				throw new MultipleTransitionException();
			}
			Transition transition = possibleTransitions.get(0);
			// Call transition to actually operate on stack
			State newState = transition.follow(input, stack);
			// if the transition didn't do anything with the input, continue
			// until
			// one does
			if (transition.read == null) {
				// TODO: newstate can be null, why
				// Does this occur on running automaton with a null transition
				// at first
				if (newState == null) {
					// Null due to no transition to follow? (why does
					// possibleTransitions return nonzero size?)
					throw new NoTransitionException();
					// System.out.println("State was null");
				}
				return newState.readInput(stack, input,
						statesVisitedThisInput + 1);
			}
			return newState;
		} else {
			throw new CycleException();
		}
	}

	public ArrayList<Transition> getTransitions() {
		return transitions;
	}

	public class Transition {
		private State destination;
		private Action read;
		private char pop;
		private char push;

		public Transition(State destination, Action read, char pop, char push) {
			this.destination = destination;
			this.pop = pop;
			this.push = push;
			this.read = read;
		}

		public void setRead(Action read) {
			this.read = read;
		}

		public void setPop(char pop) {
			this.pop = pop;
		}

		public char getPop() {
			return pop;
		}

		public void setPush(char push) {
			this.push = push;
		}

		public char getPush() {
			return push;
		}

		public Action getRead() {
			return read;
		}

		public void changeDestination(State destination) {
			this.destination = destination;
		}

		public State getDestination() {
			return destination;
		}

		/**
		 * Follow a transition
		 * 
		 * @param stack
		 * @return State after move
		 * @throws NoTransitionException
		 * @throws MultipleTransitionException
		 */
		public State follow(Action input, Stack<Character> stack)
				throws NoTransitionException, MultipleTransitionException {
			// Pop, Pull, return state
			if (pop != DPDA.emptyChar) {
				stack.pop();
				if (stack.size() == 0) {
					// Prevent an empty stack, readd stack marker
					// TODO: Is this the best solution? Force pop $, pull $
					// instead?
					stack.push('$');
				}
			}
			if (push != DPDA.emptyChar) {
				stack.push(push);
			}
			return destination;
		}

		/**
		 * Check determinism of newly created transition on the current state<br>
		 * It is assumed State is currently deterministic
		 * 
		 * @return null array if deterministic <br>
		 *         or int array of index of non-deterministic transitions<br>
		 * 
		 */
		public ArrayList<Integer> checkDeterminism() {
			ArrayList<Integer> indexOfNonDeterministicPartners = new ArrayList<Integer>();
			int index = 0;
			for (Transition transition : transitions) {
				// If there is an empty, empty -> x transition, there can be no
				// other transitions
				// debatable if empty, empty-> x should be allowed?
				if (isDoNothingTransition()) {
					indexOfNonDeterministicPartners.add(index);
					// conflict with all (if any exist), don't bother checking
					// other conditions

					// Check combinations with empty
				} else if ((transition.pop == DPDA.emptyChar && transition.read == this.read)
						|| (transition.pop == transition.pop && transition.read == null)
						|| (pop == DPDA.emptyChar && transition.read == this.read)
						|| (transition.pop == transition.pop && read == null)) {
					indexOfNonDeterministicPartners.add(index);

					// Check identical combinations
				} else if (transition.pop == pop && transition.read == read) {
					indexOfNonDeterministicPartners.add(index);
				}
			}

			return indexOfNonDeterministicPartners;
		}

		/**
		 * Do nothing to self disallowed.
		 * 
		 * To self checked externally to this
		 * 
		 * @return
		 */
		public boolean isDoNothingTransition() {
			if (pop == DPDA.emptyChar && read == null) {
				return true;
			}
			if (read == null && pop == push) {
				return true;
			}
			return false;
		}
	}

	public void flip() {
		isFinal = !isFinal;
	}

	public Transition copyTransition(Transition transition) {
		Transition newTransition = this.new Transition(
				transition.getDestination(), transition.getRead(),
				transition.getPop(), transition.getPush());
		return newTransition;
	}
}
