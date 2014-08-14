package edu.monash.bthal2.repeatedPD.LookupTableRepresentation;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.repeatedgames.evolution.Action;
import com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy;

/**
 * Agents that can play the prisoners dilemma, which base move only on last
 * opponents move.
 * 
 * @author Bradon Hall
 * 
 */
public class LookupStrategyLengthOne implements Agent, RepeatedStrategy {

	private Action opponentsLastMove;
	// strategy=[initial, respond to c, respond to d]
	private Action[] strategy = new Action[3];

	@Override
	public void reset() {
		// Reset so the PD can be played from start again
		opponentsLastMove = null;
	}

	/**
	 * Default constructor- default to no cooperation
	 */
	public LookupStrategyLengthOne() {
		// Default ALLD
		strategy[0] = Action.DEFECT;
		strategy[1] = Action.DEFECT;
		strategy[2] = Action.DEFECT;
	}

	/**
	 * Constructor for general strategies
	 * 
	 * @param newStrategy
	 */
	public LookupStrategyLengthOne(Action[] newStrategy) {
		strategy = newStrategy.clone();
	}

	/**
	 * Create Unique ID for each type of strategy<br>
	 * 000-> ALLC, 000 to decimal is 0<br>
	 * 101-> STFT, 101 to decimal is 5
	 * 
	 * @return id
	 */
	public int strategyIdentifier() {
		int id = 0;
		for (int i = 0; i < 3; i++) {
			if (strategy[i] == Action.DEFECT) {
				id = id + (int) Math.pow(2, 2 - i);
			}
		}
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 * Returns name of strategy
	 */
	@Override
	public String toString() {
		switch (strategyIdentifier()) {
		case 0:
			return "ALLC";
		case 1:
			return "TFT";
		case 2:
			return "TFT-1";
		case 3:
			return "GALLD"; // Generous ALLD
		case 4:
			return "SALLC"; // Suspicious ALLC
		case 5:
			return "STFT";
		case 6:
			return "STFT-1";
		case 7:
			return "ALLD";

		default:
			return Integer.toString(strategyIdentifier());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 * 
	 * Unique ID for each strategy
	 */
	@Override
	public int hashCode() {
		return strategyIdentifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LookupStrategyLengthOne other = (LookupStrategyLengthOne) obj;
		for (int i = 0; i < 3; i++) {
			if (other.getStrategyVector()[i] != this.getStrategyVector()[i])
				return false;
		}
		return true;
	}

	public Action[] getStrategyVector() {
		return strategy.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy#currentAction
	 * ()
	 * 
	 * Action to perform based on moves supplied so far
	 */
	@Override
	public Action currentAction() {
		// [Initial, OnC, OnD]
		if (opponentsLastMove == null) {
			return strategy[0];
		}
		if (opponentsLastMove == Action.COOPERATE) {
			return strategy[1];
		}
		if (opponentsLastMove == Action.DEFECT) {
			return strategy[2];
		}
		// Unreachable
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy#next(com
	 * .evolutionandgames.repeatedgames.evolution.Action,
	 * com.evolutionandgames.repeatedgames.evolution.Action)
	 * 
	 * Specify set of moves performed immediately previously
	 */
	@Override
	public void next(Action focal, Action opponent) {
		opponentsLastMove = opponent;
	}

}
