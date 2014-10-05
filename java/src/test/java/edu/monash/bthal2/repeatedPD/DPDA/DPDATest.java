package edu.monash.bthal2.repeatedPD.DPDA;

import static org.junit.Assert.*;

import org.junit.Test;

import com.evolutionandgames.repeatedgames.evolution.Action;

public class DPDATest {

	@Test
	public void testDefaultConstructor() {
		DPDA test = new DPDA();
		assertTrue(test.currentAction() == Action.DEFECT);

	}
}
