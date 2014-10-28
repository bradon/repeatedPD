package edu.monash.bthal2.repeatedPD.DPDA;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.agentbased.AgentBasedPopulationFactory;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulationImpl;
import com.evolutionandgames.repeatedgames.evolution.Action;

import edu.monash.bthal2.repeatedPD.DPDA.State.Transition;
import edu.monash.bthal2.repeatedPD.DPDA.State;

/**
 * Population Factory for DPDA Players
 * 
 * @author Bradon Hall
 * 
 */
public class DPDAFactory implements AgentBasedPopulationFactory {
	private int popSize = 100;
	boolean neutralPopulation = false; // Mix C and D for neutrality test

	/**
	 * Create population of ALLD DPDA players
	 * 
	 * @return
	 */
	@Override
	public AgentBasedPopulation createPopulation() {
		Agent[] agents = new DPDA[popSize];
		for (int i = 0; i < popSize; i++) {
			if (neutralPopulation) {
				if (i % 2 == 0) {
					agents[i] = ExampleStrategies.allC();
				} else {
					agents[i] = ExampleStrategies.allD();
				}
			} else {
				agents[i] = ExampleStrategies.allD();
			}
		}
		return new ExtensivePopulationImpl(agents);
	}

	public DPDAFactory() {

	}

	public DPDAFactory(int popSize) {
		this.popSize = popSize;
	}

	public DPDAFactory(int popSize, boolean neutralPopulation) {
		this.popSize = popSize;
		this.neutralPopulation = neutralPopulation;
	}

	/**
	 * Basic Common Strategies
	 * 
	 * @author Bradon Hall
	 * 
	 */
	public static class ExampleStrategies {
		public static DPDA allC() {
			DPDA allc = new DPDA();
			State singleState = new State();
			allc.addState(singleState);
			allc.setInitialState(singleState);
			singleState.isFinal = true;
			Transition selfCTransition = singleState.new Transition(
					singleState, Action.COOPERATE, DPDA.emptyChar,
					DPDA.emptyChar);
			Transition selfDTransition = singleState.new Transition(
					singleState, Action.DEFECT, DPDA.emptyChar, DPDA.emptyChar);
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
//			/System.out.println(tft.getStates().size());
			Transition selfCTransition = acceptState.new Transition(
					acceptState, Action.COOPERATE, DPDA.emptyChar,
					DPDA.emptyChar);
			Transition selfDTransition = rejectState.new Transition(
					rejectState, Action.DEFECT, DPDA.emptyChar, DPDA.emptyChar);
			Transition changeCTransition = rejectState.new Transition(
					acceptState, Action.COOPERATE, DPDA.emptyChar,
					DPDA.emptyChar);
			Transition changeDTransition = acceptState.new Transition(
					rejectState, Action.DEFECT, DPDA.emptyChar, DPDA.emptyChar);
			acceptState.addTransition(selfCTransition);
			acceptState.addTransition(changeDTransition);
			rejectState.addTransition(changeCTransition);
			rejectState.addTransition(selfDTransition);
			return tft;
		}
	}

	public void setNeutralPopulation() {
		neutralPopulation = true;

	}

}
