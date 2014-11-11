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

public class CompareStrategies {
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

	public static double getFitness(RepeatedStrategy focal,
			RepeatedStrategy opponent, double continuityProbability,
			double assortment, double mistakeProbability) {
		RepeatedGame rpd = new RepeatedGame(3.0, 0.0, 4.0, 1.0,
				continuityProbability);
		double[] payoff;
		if (Random.nextDouble() < assortment) {
			// Play self
			payoff = rpd.playOnce(focal, focal, mistakeProbability);
		} else {
			// Play opponent
			payoff = rpd.playOnce(focal, opponent, mistakeProbability);
		}
		return payoff[0];

	}

	public static double[][] compareStrategies(
			ArrayList<RepeatedStrategy> strategies,
			double continuityProbability, double assortment,
			double mistakeProbability) {
		double[][] payoffs = new double[strategies.size()][strategies.size()];
		for (int i = 0; i < strategies.size(); i++) {

			for (int j = 0; j < strategies.size(); j++) {
				strategies.get(i).reset();
				strategies.get(j).reset();
				double currentGame = getFitness(strategies.get(i),
						strategies.get(j), continuityProbability, assortment,
						mistakeProbability);

				payoffs[i][j] = currentGame;
			}
		}
		return payoffs;

	}

	public static void printComparison(ArrayList<RepeatedStrategy> strategies,
			double continuity, double assortment, double mistakeProbability) {
		//Random.seed(System.nanoTime());
		double[][] result = compareStrategies(strategies, continuity,
				assortment, mistakeProbability);
		for (int i = 0; i < 99999; i++) {
			double[][] newResult = compareStrategies(strategies, continuity,
					assortment, mistakeProbability);
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
				System.out.print(result[i][j] / 100000);
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
		System.out.print("}\n");
	}

	public static void main(String[] args) {
		ArrayList<RepeatedStrategy> strategies = new ArrayList<RepeatedStrategy>();
		RepeatedStrategy alld = LookupStrategyLengthOneFactory.allD();
		RepeatedStrategy tft = DPDAFactory.ExampleStrategies.tft();
		RepeatedStrategy allc = DPDAFactory.ExampleStrategies.allC();
		strategies.add(tft);
		strategies.add(allc);
		//strategies.add(alld);
		RepeatedStrategy alternate=parseStrategy("S#q0FI&q1T#q0-q1vC.$->$&q0-q0vC.a->a&q1-q0vD.$->$&");
		DPDA grudge = parseStrategy("S#q0FIT#q0-q0vC.l->a&q0-q0vD.a->l&");
		strategies.add(grudge);
		//strategies
			//	.add(parseStrategy("S#q0FI&q1T#q0-q1vC.l->a&q0-q0vD.a->l&q1-q0vC.a->l&"));
		RepeatedStrategy grim = parseStrategy("S#q0FIT#q0-q0vC.$->$&");
		//strategies.add(grim);
		//strategies.add(alternate);
		//strategies.add(allc);
		//strategies.add(alld);
		//printComparison(strategies, 0.99, 0.0, 0.0);
		testGrudge(89320234);
	}
	
	public static void testGrudge(long seed) {
		Random.seed(seed);
		ArrayList<RepeatedStrategy> strategies = new ArrayList<RepeatedStrategy>();
		DPDA grudge = parseStrategy("S#q0FIT#q0-q0vC.l->a&q0-q0vD.a->l&");
		RepeatedStrategy tft = DPDAFactory.ExampleStrategies.tft();
		RepeatedStrategy allc = DPDAFactory.ExampleStrategies.allC();
		strategies.add(grudge);
		strategies.add(tft);
		strategies.add(allc);
		printComparison(strategies,0.99,0.0,0.0);
		printComparison(strategies,0.99,0.0,0.01);
		printComparison(strategies,0.99,0.0,0.05);
		printComparison(strategies,0.99,0.3,0.01);
		
		
		
	}
}
