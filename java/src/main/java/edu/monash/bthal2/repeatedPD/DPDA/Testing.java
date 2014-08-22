package edu.monash.bthal2.repeatedPD.DPDA;

import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.evolution.Action;

import edu.monash.bthal2.repeatedPD.DPDA.Exception.MultipleTransitionException;
import edu.monash.bthal2.repeatedPD.DPDA.State.Transition;

public class Testing {
	public static void main(String[] args) throws MultipleTransitionException {
		DPDA dpda = new DPDA();
		State newState = new State();
		dpda.addState(newState);
		// Transition newTransition=newState.new.
		Transition newTransition = newState.new Transition(newState,
				Action.COOPERATE, 'l', 'l');
		if (newState.addTransition(newTransition)) {

		} else {
			System.out.println("Rejected transition");
		}
		dpda.setInitialState(newState);
		newState.isFinal = true;
		System.out.println(dpda.currentAction());
		dpda.readInput(Action.COOPERATE);
		System.out.println(dpda.currentAction());
		dpda.readInput(Action.COOPERATE);
		dpda.readInput(Action.COOPERATE);
		dpda.readInput(Action.DEFECT);
		dpda.readInput(Action.DEFECT);
		System.out.println(dpda.currentAction());

	}

}
