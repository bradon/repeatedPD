package edu.monash.bthal2.repeatedPD;

import automata.Automaton;
import automata.pda.PDAStepByStateSimulator;

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