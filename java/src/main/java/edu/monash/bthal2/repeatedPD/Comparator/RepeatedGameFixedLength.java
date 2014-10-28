package edu.monash.bthal2.repeatedPD.Comparator;

import java.util.ArrayList;

import com.evolutionandgames.repeatedgames.evolution.Action;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGame;
import com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy;

public class RepeatedGameFixedLength {

	/**
	 * Payoff with a lot of noise, just one interaction takes place.
	 * 
	 * @param playerOne
	 * @param playerTwo
	 * @param mistakeProbability
	 * @return
	 */
	public double[] playOnceFixedLength(RepeatedStrategy playerOne,
			RepeatedStrategy playerTwo, double mistakeProbability,
			RepeatedGame rg, int rounds) {
		// reset the machines
		playerOne.reset();
		playerTwo.reset();
		double[][] payoffArray=new double[2][2];
		payoffArray[0][0]=rg.getReward();
		payoffArray[1][1]=rg.getSucker();
		payoffArray[0][1]=rg.getTemptation();
		payoffArray[1][0]=rg.getSucker();

		double payoffOne = 0.0;
		double payoffTwo = 0.0;
		// We start with empty histories
		ArrayList<Action> actionsPlayerOne = new ArrayList<Action>();
		ArrayList<Action> actionsPlayerTwo = new ArrayList<Action>();

		for (int i = 0; i < rounds; i++) {
			double[] stageResult = oneRound(playerOne, playerTwo,
					actionsPlayerOne, actionsPlayerTwo, payoffArray);
			payoffOne = payoffOne + stageResult[0];
			payoffTwo = payoffTwo + stageResult[1];
		}

		double[] ans = new double[2];
		ans[0] = payoffOne;
		ans[1] = payoffTwo;
		return ans;
	}

	private double[] oneRound(RepeatedStrategy playerOne,
			RepeatedStrategy playerTwo, ArrayList<Action> actionsPlayerOne,
			ArrayList<Action> actionsPlayerTwo, double[][] payoffArray) {
		// history tracking
		Action playerOneAction=playerOne.currentAction();
		Action playerTwoAction=playerTwo.currentAction();
		actionsPlayerOne.add(playerOneAction);
		actionsPlayerTwo.add(playerTwoAction);
		// move automata
		playerOne.next(playerOneAction, playerTwoAction);
		playerTwo.next(playerTwoAction, playerOneAction);
		//System.out.println(playerOneAction);
		//System.out.println(playerTwoAction);
		// payoff tracking
		int playerOneMove=0;
		switch (playerOneAction) {
		case COOPERATE:
			playerOneMove=0;
			break;
		case DEFECT:
			playerOneMove=1;
			break;
		default:
			break;
		}
		int playerTwoMove=0;
		switch (playerTwoAction) {
		case COOPERATE:
			playerTwoMove=0;
			break;
		case DEFECT:
			playerTwoMove=1;
			break;
		default:
			break;
		}
		double[] ans = new double[2];
		ans[1]=payoffArray[playerTwoMove][playerOneMove];
		ans[0]=payoffArray[playerOneMove][playerTwoMove];
		return ans;
	}

}
