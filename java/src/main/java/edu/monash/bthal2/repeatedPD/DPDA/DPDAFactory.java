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

		public static DPDA allD() {
			DPDA alld = allC();
			alld.getStates().get(0).isFinal = false;
			return alld;
		}

		public static DPDA tft() {
			DPDA tft = new DPDA();
			State acceptState = new State();
			acceptState.isFinal = true;
			tft.addState(acceptState);
			tft.setInitialState(acceptState);
			State rejectState = new State();
			tft.addState(rejectState);
			System.out.println(tft.getStates().size());
			Transition selfCTransition = acceptState.new Transition(
					acceptState, Action.COOPERATE, emptyChar, emptyChar);
			Transition selfDTransition = rejectState.new Transition(
					rejectState, Action.DEFECT, emptyChar, emptyChar);
			Transition changeCTransition = rejectState.new Transition(
					acceptState, Action.COOPERATE, emptyChar, emptyChar);
			Transition changeDTransition = acceptState.new Transition(
					rejectState, Action.DEFECT, emptyChar, emptyChar);
			acceptState.addTransition(selfCTransition);
			acceptState.addTransition(changeDTransition);
			rejectState.addTransition(changeCTransition);
			rejectState.addTransition(selfDTransition);
			return tft;
		}
	}

}
