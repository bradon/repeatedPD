package edu.monash.bthal2.repeatedPD.PDARepresentation;

import automata.pda.PushdownAutomaton;

import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.evolution.Action;

public class PDATest {
	public static void main(String[] args) {
		Random.seed(System.nanoTime());

		//PushdownAutomata testpda=
		for (int i = 0; i < 1; i++) {
			PDAStrategy pda = PDAFactory.ExampleStrategies.allD();
			PDAMutator mutator = new PDAMutator(0.01);
			PDAStrategy newpda = (PDAStrategy) mutator.mutate(pda);
			for (int j=0; j<50000; j++) {
				newpda = (PDAStrategy) mutator.mutate(newpda);
			}
			newpda.next(null, Action.COOPERATE);
			//newpda.next(null, Action.COOPERATE);
			if (newpda.currentAction()==Action.COOPERATE) {
				newpda.printStrategy();
				//System.out.println("Cooperator found");
			} else {
				newpda.printStrategy();
				//newpda.printStrategy();
				//System.out.print("N");
			}
			System.out.println(newpda.toString());
		}
	}
}
