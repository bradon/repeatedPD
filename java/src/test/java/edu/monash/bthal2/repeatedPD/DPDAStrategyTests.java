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
		// Initially C
		assertTrue(allc.currentAction() == Action.COOPERATE);
		// Response to C
		allc.readInput(Action.COOPERATE);
		assertTrue(allc.currentAction() == Action.COOPERATE);
		// Response to D
		allc.readInput(Action.DEFECT);
		assertTrue(allc.currentAction() == Action.COOPERATE);
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
				assertTrue(allc.currentAction() == Action.COOPERATE);
			}
		}
	}

	@Test
	public void allcCloneTest() throws MultipleTransitionException {
		DPDA original = DPDAFactory.ExampleStrategies.allC();
		DPDA clone = original.copy();
		DPDA allc = clone;
		// Initially C
		assertTrue(allc.currentAction() == Action.COOPERATE);
		// Response to C
		allc.readInput(Action.COOPERATE);
		assertTrue(allc.currentAction() == Action.COOPERATE);
		// Response to D
		allc.readInput(Action.DEFECT);
		assertTrue(allc.currentAction() == Action.COOPERATE);
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
				assertTrue(allc.currentAction() == Action.COOPERATE);
			}
		}
	}

	@Test
	public void alldCloneTest() throws MultipleTransitionException {
		DPDA original = DPDAFactory.ExampleStrategies.allD();
		DPDA clone = original.copy();
		DPDA alld = clone;
		// Initially C
		assertTrue(alld.currentAction() == Action.DEFECT);
		// Response to C
		alld.readInput(Action.COOPERATE);
		assertTrue(alld.currentAction() == Action.DEFECT);
		// Response to D
		alld.readInput(Action.DEFECT);
		assertTrue(alld.currentAction() == Action.DEFECT);
		alld.reset();
		// Response to random sequence
		Random.seed(System.nanoTime());
		for (int i = 0; i < 100; i++) {
			if (Random.nextBoolean()) {
				alld.readInput(Action.COOPERATE);
			} else {
				alld.readInput(Action.DEFECT);
			}
			if ((i + 1) % 10 == 0) {
				assertTrue(alld.currentAction() == Action.DEFECT);
			}
		}
	}

	@Test
	public void tftCloneTest() throws MultipleTransitionException {
		DPDA original = DPDAFactory.ExampleStrategies.tft();
		DPDA clone = original.copy();
		DPDA tft = clone;
		// Initially C
		assertTrue(tft.currentAction() == Action.COOPERATE);
		// Response to C
		tft.readInput(Action.COOPERATE);
		assertTrue(tft.currentAction() == Action.COOPERATE);
		// Response to D
		tft.readInput(Action.DEFECT);
		assertTrue(tft.currentAction() == Action.DEFECT);
		tft.reset();
		// Response to random sequence
		Random.seed(System.nanoTime());
		Action previousMove;
		for (int i = 0; i < 100; i++) {
			if (Random.nextBoolean()) {
				previousMove=tft.readInput(Action.COOPERATE);
			} else {
				previousMove=tft.readInput(Action.DEFECT);
			}
			if ((i + 1) % 10 == 0) {
				assertTrue(tft.currentAction() == previousMove);
			}
		}
	}

}
