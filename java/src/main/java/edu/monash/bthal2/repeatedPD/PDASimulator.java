package edu.monash.bthal2.repeatedPD;

import edu.monash.bthal2.repeatedPD.JFLAPoverride.PDAStepByStateSimulator;
import automata.Automaton;



/**
 * @author Bradon Hall
 *
 */
public class PDASimulator extends PDAStepByStateSimulator {

	String inputString="";
	
	public PDASimulator(Automaton automaton) {
		super(automaton);
	}

	public void resetInput() {
		inputString = "";
	}

	public boolean simulateInput(String input) {
		inputString = inputString.concat(input);
		return super.simulateInput(inputString);
	}
}
