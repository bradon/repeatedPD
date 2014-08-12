package edu.monash.bthal2.repeatedPD;


import org.junit.Test;

import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.evolution.Action;

import edu.monash.bthal2.repeatedPD.PDARepresentation.PDAFactory;
import edu.monash.bthal2.repeatedPD.PDARepresentation.PDAStrategy;
import junit.framework.TestCase;

public class StrategyTests extends TestCase {
	// Generalise to test TFT, STFT, etc for any repeated games strategy
	// representation
	
	/**
	 * ALLC should always C
	 */
	@Test
	public void testAllC() {
		Random.seed(System.nanoTime());
		// Initial Move
		PDAStrategy allc = PDAFactory.ExampleStrategies.allC();
		assertTrue(allc.currentAction() == Action.COOPERATE);
		// Randomly move a random number of times
		if (Random.nextBoolean()) {
			allc.next(null, Action.COOPERATE);
		} else {
			allc.next(null, Action.DEFECT);
		}
		assertTrue(allc.currentAction() == Action.COOPERATE);
		while (Random.nextDouble() < 0.9) {
			if (Random.nextBoolean()) {
				allc.next(null, Action.COOPERATE);
			} else {
				allc.next(null, Action.DEFECT);
			}
		}
		assertTrue(allc.currentAction() == Action.COOPERATE);
	}

	/**
	 * ALLD should always D
	 */
	@Test
	public void testAllD() {
		Random.seed(System.nanoTime());
		// Initial Move
		PDAStrategy allc = PDAFactory.ExampleStrategies.allD();
		assertTrue(allc.currentAction() == Action.DEFECT);
		// Randomly move a random number of times
		if (Random.nextBoolean()) {
			allc.next(null, Action.COOPERATE);
		} else {
			allc.next(null, Action.DEFECT);
		}
		assertTrue(allc.currentAction() == Action.DEFECT);
		while (Random.nextDouble() < 0.9) {
			if (Random.nextBoolean()) {
				allc.next(null, Action.COOPERATE);
			} else {
				allc.next(null, Action.DEFECT);
			}
		}
		assertTrue(allc.currentAction() == Action.DEFECT);
	}

	/**
	 * TFT should only care about last move, and initally C
	 */
	@Test
	public void testTFT() {
		Random.seed(System.nanoTime());
		// Initial Move
		PDAStrategy allc = PDAFactory.ExampleStrategies.TFT();
		assertTrue(allc.currentAction() == Action.COOPERATE);
		// Randomly move a random number of times
		if (Random.nextBoolean()) {
			allc.next(null, Action.COOPERATE);
		} else {
			allc.next(null, Action.DEFECT);
		}

		while (Random.nextDouble() < 0.9) {
			if (Random.nextBoolean()) {
				allc.next(null, Action.COOPERATE);
				assertTrue(allc.currentAction() == Action.COOPERATE);
			} else {
				allc.next(null, Action.DEFECT);
				assertTrue(allc.currentAction() == Action.DEFECT);
			}
		}
	}

	/**
	 * STFT- start D, then reciprocate
	 */
	@Test
	public void testSTFT() {
		Random.seed(System.nanoTime());
		// Initial Move
		PDAStrategy allc = PDAFactory.ExampleStrategies.STFT();
		assertTrue(allc.currentAction() == Action.DEFECT);
		// Randomly move a random number of times
		if (Random.nextBoolean()) {
			allc.next(null, Action.COOPERATE);
		} else {
			allc.next(null, Action.DEFECT);
		}

		while (Random.nextDouble() < 0.9) {
			if (Random.nextBoolean()) {
				allc.next(null, Action.COOPERATE);
				assertTrue(allc.currentAction() == Action.COOPERATE);
			} else {
				allc.next(null, Action.DEFECT);
				assertTrue(allc.currentAction() == Action.DEFECT);
			}
		}
	}
	@Test
	public void testNoTransitions() {
		Random.seed(System.nanoTime());
		// Initial Move
		PDAStrategy noTransition = PDAFactory.ExampleStrategies.noTransitions();
		assertTrue(noTransition.currentAction() == Action.COOPERATE);
		noTransition.next(null, Action.COOPERATE);
		assertTrue(noTransition.currentAction() == Action.DEFECT);
	}
}
