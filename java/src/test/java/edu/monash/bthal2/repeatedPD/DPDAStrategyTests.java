package edu.monash.bthal2.repeatedPD;

import static org.junit.Assert.*;

import org.junit.Test;

import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.evolution.Action;

import edu.monash.bthal2.repeatedPD.DPDA.DPDA;
import edu.monash.bthal2.repeatedPD.DPDA.DPDAFactory;
import edu.monash.bthal2.repeatedPD.DPDA.Exception.MultipleTransitionException;

public class DPDAStrategyTests {

	@Test
	public void allcTest() throws MultipleTransitionException {
		DPDA allc = DPDAFactory.ExampleStrategies.allC();
		//Initially C
		assertTrue(allc.currentAction()==Action.COOPERATE);
		// Response to C
		allc.readInput(Action.COOPERATE);
		assertTrue(allc.currentAction()==Action.COOPERATE);
		// Response to D
		allc.readInput(Action.DEFECT);
		assertTrue(allc.currentAction()==Action.COOPERATE);
		allc.reset();
		// Response to random sequence
		Random.seed(System.nanoTime());
		for (int i = 0; i < 100; i++) {
			if (Random.nextBoolean()) {
				allc.readInput(Action.COOPERATE);
			} else {
				allc.readInput(Action.DEFECT);
			}
			if ((i + 1) % 10 == 0) {
				assertTrue(allc.currentAction()==Action.COOPERATE);
			}
		}
	}

}
