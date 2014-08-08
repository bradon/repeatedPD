package edu.monash.bthal2.repeatedPD;

import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import edu.monash.bthal2.repeatedPD.simulation.LookupPlayerLengthOnePayoffSimulation;
import edu.monash.bthal2.repeatedPD.simulation.PDASimulation;

public class App {
	@Parameter(names = { "-file", "-f" }, description = "Name of the json file")
	private String file;

	enum SimulationType {
		PAYOFFLOOKUP, PDAPAYOFF, FSAPAYOFF
	}

	@Parameter(names = { "-type", "-t" }, description = "Type of simulation PAYOFF", required = true)
	private SimulationType type;

	public static void main(String[] args) throws IOException {

		App app = new App();
		// Parsing
		JCommander commander = new JCommander(app);
		try {
			commander.parse(args);
		} catch (ParameterException e) {
			System.out.println(e.getMessage());
			commander.usage();
			return;
		}

		switch (app.type) {
		case PAYOFFLOOKUP:
			LookupPlayerLengthOnePayoffSimulation.runOnce(app.file);
			break;
		case PDAPAYOFF:
			PDASimulation.runOncePayoff(app.file);
			break;
		case FSAPAYOFF:
			System.out.println("Simulation not implemented");
			break;
		default:
			System.out.println("Simulation not implemented");
			break;

		}
	}
}
