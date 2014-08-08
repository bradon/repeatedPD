package edu.monash.bthal2.repeatedPD.JFLAPoverride;

import java.util.ArrayList;
import java.util.Iterator;

import automata.Automaton;
import automata.Configuration;
import automata.pda.PDAConfiguration;
import automata.pda.PDAStepByStateSimulator;

/**
 * @author bradon
 * 
 *         Replaces calls for user input when many configurations are found to
 *         an assumption that the machine does not accept the input
 */
public class PDAStepByStateWithoutUI extends PDAStepByStateSimulator {

	public PDAStepByStateWithoutUI(Automaton automaton) {
		super(automaton);
	}

	/* (non-Javadoc)
	 * @see automata.pda.PDAStepByStateSimulator#simulateInput(java.lang.String)
	 * Override the call to user input
	 */
	@Override
	public boolean simulateInput(String input) {
		//Code from JFLAP 7.0 with minor change
		/** clear the configurations to begin new simulation. */
		myConfigurations.clear();
		Configuration[] initialConfigs = getInitialConfigurations(input);
		for (int k = 0; k < initialConfigs.length; k++) {
			PDAConfiguration initialConfiguration = (PDAConfiguration) initialConfigs[k];
			myConfigurations.add(initialConfiguration);
		}
		int count = 0;
		while (!myConfigurations.isEmpty()) {
			if (isAccepted())
				return true;
			ArrayList configurationsToAdd = new ArrayList();
			Iterator it = myConfigurations.iterator();
			while (it.hasNext()) {
				PDAConfiguration configuration = (PDAConfiguration) it.next();
				ArrayList configsToAdd = stepConfiguration(configuration);
				configurationsToAdd.addAll(configsToAdd);
				it.remove();
                count++;
                if(count > 10000){
                	//Change: Halt on many configurations
                        return false;
                }
			}
			myConfigurations.addAll(configurationsToAdd);
		}
		return false;
	}

}
