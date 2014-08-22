package edu.monash.bthal2.repeatedPD.DPDA;

import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.repeatedgames.evolution.Action;

import edu.monash.bthal2.repeatedPD.DPDA.State.Transition;
import edu.monash.bthal2.repeatedPD.DPDA.State;

public class DPDAFactory {
	static char emptyChar = 'l';

	public static class ExampleStrategies {
		public static DPDA allC() {
			DPDA allc = new DPDA();
			State singleState = new State();
			allc.addState(singleState);
			allc.setInitialState(singleState);
			singleState.isFinal = true;
			Transition selfCTransition = singleState.new Transition(
					singleState, Action.COOPERATE, emptyChar, emptyChar);
			Transition selfDTransition = singleState.new Transition(
					singleState, Action.DEFECT, emptyChar, emptyChar);
			singleState.addTransition(selfCTransition);
			singleState.addTransition(selfDTransition);
			return allc;
		}
	}

}
