package edu.monash.bthal2.repeatedPD.DPDA;
import org.junit.Test;


public class DPDACopyTest {
	@Test
	public void copyBasicStrategies() {
		DPDA original = DPDAFactory.ExampleStrategies.allD();
		DPDA copy=original.copy();
	}
}
