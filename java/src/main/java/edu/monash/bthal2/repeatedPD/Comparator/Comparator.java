package edu.monash.bthal2.repeatedPD.Comparator;

import java.util.ArrayList;

import com.evolutionandgames.repeatedgames.evolution.Action;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGame;
import com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy;

import edu.monash.bthal2.repeatedPD.DPDA.DPDA;
import edu.monash.bthal2.repeatedPD.DPDA.DPDAFactory;
import edu.monash.bthal2.repeatedPD.DPDA.State;
import edu.monash.bthal2.repeatedPD.DPDA.State.Transition;
import edu.monash.bthal2.repeatedPD.LookupTableRepresentation.LookupStrategyLengthOneFactory;

public class Comparator {

	public static DPDA parseStrategy(String strategyText) {
		// S#q0I&q1T#q0-q1vC.l->$&q1-q1vC.a->a&
		DPDA dpda = new DPDA();
		String sectionDelim = "T#";
		String[] sectionTokens = strategyText.split(sectionDelim);
		String statesToken = sectionTokens[0].replace("S#", "");
		String transitionsToken = sectionTokens[1];
		for (String stateString : statesToken.split("&")) {
			boolean initial = stateString.contains("I");
			boolean acceptor = stateString.contains("F");
			State newState = new State();
			dpda.addState(newState);
			if (acceptor) {
				newState.flip();
			}
			if (initial) {
				dpda.setInitialState(newState);
			}
		}
		// System.out.println(dpda.toString());
		for (String transitionString : transitionsToken.split("&")) {
			transitionString = transitionString.replace("q", "").replace(">",
					"");
			System.out.println(transitionString);
			String[] transitionSections = transitionString.split("[-,v,.]");
			int fromIndex = Integer.parseInt(transitionSections[0]);
			int toIndex = Integer.parseInt(transitionSections[1]);
			char readChar = transitionSections[2].charAt(0);
			char pop = transitionSections[3].charAt(0);
			char push = transitionSections[4].charAt(0);
			State sourceState = dpda.getStates().get(fromIndex);
			State destinationState = dpda.getStates().get(toIndex);
			Action read = null;
			if (readChar == 'l') {
				read = null;
			}
			if (readChar == 'C') {
				read = Action.COOPERATE;
			}
			if (readChar == 'D') {
				read = Action.DEFECT;
			}
			Transition newTransition = sourceState.new Transition(
					destinationState, read, push, pop);
			sourceState.addTransition(newTransition);
		}
		// System.out.println(dpda.toString());
		return dpda;
	}

	public static double[][] compare(RepeatedStrategy alpha,
			RepeatedStrategy beta) {
		double continuationProbability = 0.8;
		RepeatedGame pd = new RepeatedGame(3.0, 1.0, 4.0, 2.0,
				continuationProbability);
		RepeatedGameFixedLength pdfl = new RepeatedGameFixedLength();
		double[][] payoffs = new double[2][2];
		double[] comparison = pdfl.playOnceFixedLength(alpha, beta, 0.0, pd,
				100);
		double[] alphaSelf = pdfl.playOnceFixedLength(alpha, alpha, 0.0, pd,
				100);
		double[] betaSelf = pdfl.playOnceFixedLength(beta, beta, 0.0, pd, 100);
		payoffs[0][1] = comparison[0];
		payoffs[1][0] = comparison[1];
		payoffs[0][0] = alphaSelf[0];
		payoffs[1][1] = betaSelf[0];
		return payoffs;
	}

	public static double[][] compare(ArrayList<RepeatedStrategy> strategies) {
		RepeatedGame pd = new RepeatedGame(3.0, 1.0, 4.0, 2.0, 0.8);
		RepeatedGameFixedLength pdfl = new RepeatedGameFixedLength();
		double[][] payoffs = new double[strategies.size()][strategies.size()];

		for (int i = 0; i < strategies.size(); i++) {
			for (int j = 0; j < strategies.size(); j++) {
				double[] currentGame = pdfl.playOnceFixedLength(
						strategies.get(i), strategies.get(j), 0.0, pd, 100);
				payoffs[i][j] = currentGame[0];
//				/payoffs[j][i] = currentGame[1];
			}
		}
		return payoffs;

	}

	public static void main(String[] args) {
		// double[][]
		// result=compare(DPDAFactory.ExampleStrategies.tft(),LookupStrategyLengthOneFactory.stft());
		// System.out.print(result[0][0]+","+result[0][1]+"\n"+result[1][0]+","+result[1][1]);
		ArrayList<RepeatedStrategy> strategies = new ArrayList<RepeatedStrategy>();
		strategies.add(DPDAFactory.ExampleStrategies.allC());
		strategies.add(DPDAFactory.ExampleStrategies.allD());
		strategies.add(DPDAFactory.ExampleStrategies.tft());
		double[][] result = compare(strategies);
		for (int i = 0; i < result.length; i++) {
			System.out.print("\n");
			for (int j = 0; j < result.length; j++) {
				System.out.print(result[i][j]+",");
			}
		}
		// parseStrategy("S#q0I&q1FT#q0-q1vC.l->$&q1-q1vC.a->a&");
	}
}
