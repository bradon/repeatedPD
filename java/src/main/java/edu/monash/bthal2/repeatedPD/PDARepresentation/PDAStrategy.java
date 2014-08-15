package edu.monash.bthal2.repeatedPD.PDARepresentation;

import automata.State;
import automata.Transition;
import automata.pda.PDATransition;
import automata.pda.PushdownAutomaton;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.repeatedgames.evolution.Action;
import com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy;

import edu.monash.bthal2.repeatedPD.PDASimulator;

public class PDAStrategy implements Agent, RepeatedStrategy {
	private int statesCount = 1;
	private static final String D = "D";
	private PDASimulator simulator;
	private PushdownAutomaton strategy;
	private String nextMove = "";

	public void incrementStates() {
		statesCount = statesCount + 1;
	}

	public int getStatesCount() {
		return statesCount;
	}

	public PDAStrategy(PushdownAutomaton newStrategy, int newStatesCount) {
		statesCount = newStatesCount;
		strategy = newStrategy;
		simulator = new PDASimulator(strategy);
	}

	public PDAStrategy(PushdownAutomaton newStrategy) {
		strategy = newStrategy;
		simulator = new PDASimulator(strategy);
	}

	public PDAStrategy() {
		// Default PDA
		// Move this over to factory soon
		strategy = PDAFactory.ExampleStrategies.allD().getPDA();
		// strategy = new PushdownAutomaton();
		// State initialState = strategy.createState(new Point(0, 0));
		// initialState.setLabel("q0");
		// strategy.setInitialState(initialState);
		// PDATransition initialCTransition = new PDATransition(initialState,
		// initialState, "C", "", "");
		// PDATransition initialDTransition = new PDATransition(initialState,
		// initialState, D, "", "");
		// strategy.addFinalState(initialState);
		// strategy.addTransition(initialDTransition);
		// strategy.addTransition(initialCTransition);
		simulator = new PDASimulator(strategy);
	}

	@Override
	public void reset() {
		simulator.resetInput();
	}

	public PushdownAutomaton getPDA() {
		return strategy;
	}

	@Override
	public Action currentAction() {
		// Run on input string
		if (simulator.simulateInput(nextMove)) {
			// Accepted
			return Action.COOPERATE;
		} else {
			return Action.DEFECT;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("S:");
		State[] states = strategy.getStates();
		State[] finalStates = strategy.getFinalStates();
		Transition[] transitions = strategy.getTransitions();
		for (int i = 0; i < states.length; i++) {
			if (i > 0) {
				builder.append("&");
			}
			builder.append(states[i].getLabel());
		}
		builder.append("F:");
		for (int i = 0; i < finalStates.length; i++) {
			if (i > 0) {
				builder.append("&");
			}
			builder.append(finalStates[i].getLabel());
		}
		builder.append("T:");
		for (int i = 0; i < transitions.length; i++) {
			PDATransition transition = (PDATransition) transitions[i];
			if (i > 0) {
				builder.append("&");
			}
			builder.append(transition.getFromState().getLabel());
			builder.append("->");
			builder.append(transition.getToState().getLabel());
			builder.append(":");
			builder.append(transition.getInputToRead());
			builder.append(",");
			builder.append(transition.getStringToPop());
			builder.append("->");
			builder.append(transition.getStringToPush());
		}
		return builder.toString();

	}

	public void printStrategy() {
		System.out.println("States:");
		State[] states = strategy.getStates();
		State[] finalStates = strategy.getFinalStates();
		Transition[] transitions = strategy.getTransitions();

		// System.out.println(states.length);
		for (int i = 0; i < states.length; i++) {
			System.out.print(states[i].getLabel() + ",");
		}
		System.out.println("\nFinal States:");
		for (int i = 0; i < finalStates.length; i++) {
			System.out.print(finalStates[i].getLabel() + ",");
		}
		System.out.println("\nTransitions:");
		for (int i = 0; i < transitions.length; i++) {
			PDATransition transition = (PDATransition) transitions[i];
			System.out.println(transition.getFromState().getLabel() + "->"
					+ transition.getToState().getLabel() + " by "
					+ transition.getInputToRead() + ","
					+ transition.getStringToPop() + "->"
					+ transition.getStringToPush());
		}

	}

	@Override
	public void next(Action actualPlayPlayer1, Action opponent) {
		// For now, be silly and convert action->string etc
		if (opponent == Action.COOPERATE)
			nextMove = "C";
		if (opponent == Action.DEFECT)
			nextMove = D;
	}

	@Override
	public boolean equals(Object obj) {

		// TODO: Problem possibly here-> comparison, duplicate strategies
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		// System.out.println(obj.toString());
		// System.out.println(this.toString());
		if (obj.toString().equals(this.toString())) {
			if (this.statesCount == ((PDAStrategy) obj).getStatesCount()) {
				return true;
				// should be return true, disabled for testing
			}
			return false;
		}
		return false;
	}

	// I am not a smart man.
	@Override
	public int hashCode() {
		
		return this.toString().hashCode();
	}
}
