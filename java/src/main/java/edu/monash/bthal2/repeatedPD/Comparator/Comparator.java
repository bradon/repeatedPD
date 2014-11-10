package edu.monash.bthal2.repeatedPD.Comparator;

import java.util.ArrayList;

import com.evolutionandgames.jevodyn.utils.Random;
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
			// System.out.println(transitionString);
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
					destinationState, read, pop, push);
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
		RepeatedGame pd = new RepeatedGame(2.0, 0.0, 3.0, 1.0, 0.8);
		RepeatedGameFixedLength pdfl = new RepeatedGameFixedLength();
		double[][] payoffs = new double[strategies.size()][strategies.size()];

		for (int i = 0; i < strategies.size(); i++) {
			for (int j = 0; j < strategies.size(); j++) {
				double[] currentGame = pdfl.playOnceFixedLength(
						strategies.get(i), strategies.get(j), 0.0, pd, 100);
				payoffs[i][j] = currentGame[0];
				// /payoffs[j][i] = currentGame[1];
			}
		}
		return payoffs;

	}

	public static double[][] compare3(ArrayList<RepeatedStrategy> strategies,
			double continuity,double assortment) {
		Random.seed(System.nanoTime());
		RepeatedGame pd = new RepeatedGame(3.0, 0.0, 4.0, 1.0, 0.8);
		RepeatedGameFixedLength pdfl = new RepeatedGameFixedLength();
		double[][] payoffs = new double[strategies.size()][strategies.size()];
		int rounds=Random.simulateGeometricDistribution(1.0-continuity)+1;
		for (int i = 0; i < strategies.size(); i++) {
			for (int j = 0; j < strategies.size(); j++) {
				double[] currentGame = pdfl.playOnceFixedLength(
						strategies.get(i), strategies.get(j), 0.0, pd, rounds);
				if (i==j) {
					payoffs[i][j] = (1+assortment)*currentGame[0];
				} else {
					payoffs[i][j] = currentGame[0];
				}
				payoffs[i][j] =payoffs[i][j] /rounds;
				
				// /payoffs[j][i] = currentGame[1];
			}
		}
		return payoffs;

	}

	public static void main(String[] args) {
		// double[][]
		// result=compare(DPDAFactory.ExampleStrategies.tft(),LookupStrategyLengthOneFactory.stft());
		// System.out.print(result[0][0]+","+result[0][1]+"\n"+result[1][0]+","+result[1][1]);
		// ArrayList<RepeatedStrategy> strategies = new
		// ArrayList<RepeatedStrategy>();
		// strategies.add(DPDAFactory.ExampleStrategies.allC());
		// strategies.add(DPDAFactory.ExampleStrategies.allD());
		// strategies.add(DPDAFactory.ExampleStrategies.tft());
		// strategies.add(LookupStrategyLengthOneFactory.stft());
		// double[][] result = compare(strategies);
		// for (int i = 0; i < result.length; i++) {
		// System.out.print("\n");
		// for (int j = 0; j < result.length; j++) {
		// System.out.print(result[i][j]+",");
		// }
		// }
		// parseStrategy("S#q0I&q1FT#q0-q1vC.l->$&q1-q1vC.a->a&");

		// DPDA
		// alpha=parseStrategy("S#q0I&q1F&q2FT#q0-q0vC.l->l&q0-q1vD.l->l&q1-q2vD.l->l&q1-q0vC.l->l&");
		// DPDA
		// beta=parseStrategy("S#q0I&q1F&q2FT#q0-q0vC.l->l&q0-q1vD.l->l&q1-q2vD.l->l&q1-q0vC.l->a&");
		// DPDA
		// gamma=parseStrategy("S#q0I&q1FT#q0-q0vC.l->l&q0-q1vD.l->l&q1-q0vC.$->$&");
		DPDA alpha = parseStrategy("S#q0I&q1F&q2FT#q0-q1vD.l->l&q0-q0vC.l->l&q1-q2vD.l->l&q1-q0vC.l->l&");
		DPDA beta = parseStrategy("S#q0I&q1F&q2FT#q0-q0vC.l->l&q0-q1vD.l->l&q1-q2vD.l->l&q1-q0vC.l->l&");
		DPDA gamma = parseStrategy("S#q0I&q1F&q2T#q0-q0vC.l->l&q0-q1vD.l->l&q1-q0vC.l->l&q1-q2vD.l->a&q2-q0vC.a->a&");
		DPDA grudge = parseStrategy("S#q0FIT#q0-q0vC.l->a&q0-q0vD.a->l&");
		RepeatedStrategy alld = LookupStrategyLengthOneFactory.allD();
		RepeatedStrategy tft = DPDAFactory.ExampleStrategies.tft();
		RepeatedStrategy allc = DPDAFactory.ExampleStrategies.allC();
		RepeatedStrategy stft = LookupStrategyLengthOneFactory.stft();
		RepeatedStrategy one = parseStrategy("S#q0IT#q0-q0vD.$->$&");
		// DDGRIM
		RepeatedStrategy three = parseStrategy("S#q0I&q1&q2FT#q0-q1vD.$->$&q1-q2vD.$->l&q1-q2vC.a->a&q2-q2vC.$->$&q2-q2vD.a->a&");
		// DDALLC
		RepeatedStrategy two = parseStrategy("S#q0I&q1&q2FT#q0-q1vD.$->$&q1-q2vD.$->l&q1-q2vC.a->a&q2-q2vD.$->$&q2-q2vC.$->$&");
		ArrayList<RepeatedStrategy> strategies = new ArrayList<RepeatedStrategy>();
		//strategies.add(one);
		//strategies.add(two);
		//strategies.add(three);

		strategies.add(tft);
		strategies.add(allc);
		strategies.add(alld);
		//strategies.add(grudge);
		//strategies.add(parseStrategy("S#q0FI&q1T#q0-q1vC.l->a&q0-q0vD.a->l&q1-q0vC.a->l&"));
		// strategies.add(parseStrategy("S#q0IT#q0-q0vC.l->l&"));
		// strategies.add(parseStrategy("S#q0I&q1FT#q0-q1vC.l->l&q1-q1vD.$->$&"));
		// strategies.add(parseStrategy("S#q0FIT#q0-q0vC.l->l&"));
		RepeatedStrategy grim=parseStrategy("S#q0FIT#q0-q0vC.$->$&");
		//strategies.add(grim);
		// strategies.add(tft);
		
		
		//0.7,0.3
		//strategies.add(parseStrategy("S#q0I&q1&q2FT#q0-q1vD.l->l&q0-q0vC.a->l&q0-q0vC.$->$&q1-q2vC.$->a&q1-q2vC.a->l&q1-q2vD.l->l&q2-q2vC.l->l&"));
		//strategies.add(parseStrategy("S#q0I&q1&q2F&q3T#q0-q0vC.a->l&q0-q0vC.$->$&q0-q1vD.l->l&q1-q2vC.$->a&q1-q3vC.a->l&q1-q2vD.l->l&q2-q2vC.l->l&"));
		//strategies.add(alld);
		DPDA rtft = new DPDA();
		State initialState = new State();
		rtft.addState(initialState);
		rtft.setInitialState(initialState);
		initialState.flip(); // Accepting
		Transition initialSelfC = initialState.new Transition(initialState,
				Action.COOPERATE, DPDA.emptyChar, 'a');
		initialState.addTransition(initialSelfC);
		State rejectState = new State();
		rtft.addState(rejectState);
		Transition initialToReject = initialState.new Transition(rejectState,
				Action.DEFECT, DPDA.stackMarker, DPDA.stackMarker);
		rejectState.addTransition(initialToReject);
		Transition rejectSelf = initialState.new Transition(rejectState,
				Action.DEFECT, DPDA.emptyChar, DPDA.emptyChar);
		rejectState.addTransition(rejectSelf);

		// System.out.print(rtft);

		// strategies.add(alld);
		// strategies.add(parseStrategy("S#q0FI&q1T#q0-q1vC.l->a&q0-q0vD.a->l&q1-q0vC.a->l&"));
		// strategies.add(two);
		// strategies.add(one);

		// strategies.add(three);

		Random.seed(System.nanoTime());
		double continuity=0.666;
		double assortment=0.0;
		double[][] result = compare3(strategies,continuity, assortment);
		for (int i = 0; i < 999; i++) {
			double[][] newResult = compare3(strategies,continuity, assortment);
			for (int j = 0; j < result.length; j++) {
				for (int k = 0; k < result.length; k++) {
					result[j][k] = result[j][k] + newResult[j][k];
				}
			}
		}
		System.out.print("{");
		for (int i = 0; i < result.length; i++) {
			System.out.print("{");
			for (int j = 0; j < result.length; j++) {
				System.out.print(result[j][i] / 1000);
				if (j != result.length - 1) {
					System.out.print(",");
				} else {

				}
			}
			System.out.print("}");
			if (i != result.length - 1) {
				System.out.print(",");
			}
		}
		System.out.print("}");
	}
}
