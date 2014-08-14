package edu.monash.bthal2.repeatedPD;

import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import edu.monash.bthal2.repeatedPD.simulation.LookupPlayerLengthOnePayoffSimulation;
import edu.monash.bthal2.repeatedPD.simulation.LookupPlayerLengthOneTimeSeries;
import edu.monash.bthal2.repeatedPD.simulation.PDAPayoffSimulation;
import edu.monash.bthal2.repeatedPD.simulation.PDATimeSeriesSimulation;

public class App {
	@Parameter(names = { "-file", "-f" }, description = "Name of the json file")
	private String file;

	enum SimulationType {
		LOOKUPPAYOFF, LOOKUPTIMESERIES, PAYOFF, PDAPAYOFF, TIMESERIES, PDATIMESERIES
	}

	@Parameter(names = { "-type", "-t" }, description = "Type of simulation", required = true)
	private SimulationType type;

	@Parameter(names = { "-json", "-j" }, description = "Show example JSON file", required = false)
	private static boolean showJson = false;

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
		case LOOKUPPAYOFF:
			if (showJson) {
				System.out.println(LookupPlayerLengthOnePayoffSimulation
						.exampleJson());
			} else {
				LookupPlayerLengthOnePayoffSimulation
						.runWithSimpleOutput(app.file);
			}
			break;
		case LOOKUPTIMESERIES:
			if (showJson) {
				System.out.println(LookupPlayerLengthOneTimeSeries
						.exampleJson());
			} else {
				LookupPlayerLengthOneTimeSeries.runApp(app.file);
			}
			break;
		case PAYOFF:
		case PDAPAYOFF:
			if (showJson) {
				System.out.println("unimplemented");
			} else {
				PDAPayoffSimulation.runOncePayoff(app.file);
			}
			break;
		case TIMESERIES:
		case PDATIMESERIES:
			if (showJson) {
				System.out.println("unimplemented");
			} else {
				PDATimeSeriesSimulation.runApp(app.file);
			}
			break;
		default:
			System.out.println("Simulation not implemented");
			break;

		}
	}
}
