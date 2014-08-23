package edu.monash.bthal2.repeatedPD.DPDA;

import static org.junit.Assert.*;

import org.junit.Test;

import com.evolutionandgames.jevodyn.utils.Random;

public class DPDAMutatorTest {

	@Test
	public void testAddState() {
		//Should always have an extra state after
		DPDA tft = DPDAFactory.ExampleStrategies.tft();

	}

	@Test
	public void testRandom() {
		Random.seed();
		DPDA victim = DPDAFactory.ExampleStrategies.tft();
		DPDAMutator mutator = new DPDAMutator();
		for (int i = 0; i < 10000; i++) {
			victim = (DPDA) mutator.mutate(victim);
		}
		System.out.println(victim.toString());
	}

}
