package edu.monash.bthal2.repeatedPD.DPDA;

import com.evolutionandgames.repeatedgames.evolution.Action;

import edu.monash.bthal2.repeatedPD.DPDA.State.Transition;

public class Testing {
	public static void main(String[] args) throws MultipleTransitionException {
		DPDA dpda = new DPDA();
		State newState = new State();
		dpda.addState(newState);
		// Transition newTransition=newState.new.
		Transition newTransition = newState.new Transition(newState,
				Action.COOPERATE, 'l', 'l');
		newState.addTransition(newTransition);
		dpda.setInitialState(newState);
		newState.isFinal=true;
		System.out.println(dpda.currentAction());
		dpda.readInput(Action.COOPERATE);
		dpda.readInput(Action.COOPERATE);
		dpda.readInput(Action.COOPERATE);
		dpda.readInput(Action.DEFECT);
		dpda.readInput(Action.DEFECT);
		System.out.println(dpda.currentAction());
	}
}
