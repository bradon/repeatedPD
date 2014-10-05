package edu.monash.bthal2.repeatedPD.DPDA;

import static org.junit.Assert.*;

import org.junit.Test;

import com.evolutionandgames.repeatedgames.evolution.Action;

import edu.monash.bthal2.repeatedPD.DPDA.Exception.MultipleTransitionException;

public class DPDATest {

	@Test
	public void testDefaultConstructor() throws MultipleTransitionException {
		DPDA test = new DPDA();
		assertTrue(test.currentAction() == Action.DEFECT);
		test.readInput(Action.DEFECT);
		assertTrue(test.currentAction() == Action.DEFECT);
		test.readInput(Action.COOPERATE);
		assertTrue(test.currentAction() == Action.DEFECT);
	}
	
	@Test
	public void testFlipResultBasic() throws MultipleTransitionException {
		DPDA test = new DPDA();
		test.flipResult=true;
		assertTrue(test.currentAction() == Action.COOPERATE);
		test.readInput(Action.DEFECT);
		assertTrue(test.currentAction() == Action.COOPERATE);
		test.readInput(Action.COOPERATE);
		assertTrue(test.currentAction() == Action.COOPERATE);
	}
	
}
