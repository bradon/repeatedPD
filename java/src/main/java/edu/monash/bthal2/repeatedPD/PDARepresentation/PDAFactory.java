package edu.monash.bthal2.repeatedPD.PDARepresentation;

import java.awt.Point;

import automata.State;
import automata.pda.PDATransition;
import automata.pda.PushdownAutomaton;

import com.evolutionandgames.agentbased.Agent;
import com.evolutionandgames.agentbased.AgentBasedPopulation;
import com.evolutionandgames.agentbased.AgentBasedPopulationFactory;
import com.evolutionandgames.agentbased.extensive.ExtensivePopulationImpl;


public class PDAFactory implements AgentBasedPopulationFactory {
	int popSize = 100;

	public PDAFactory(int popSize) {
		this.popSize = popSize;
	}

	public PDAFactory() {

	}

	@Override
	public AgentBasedPopulation createPopulation() {
		Agent[] agents = new PDAStrategy[popSize];
		for (int i = 0; i < popSize; i++) {
			agents[i] = new PDAStrategy();
		}
		return new ExtensivePopulationImpl(agents);
	}

	public static class ExampleStrategies {
		public static PDAStrategy allC() {
			PushdownAutomaton strategy = new PushdownAutomaton();
			State initialState = strategy.createState(new Point(0, 0));
			strategy.setInitialState(initialState);
			initialState.setLabel("q0");
			PDATransition initialCTransition = new PDATransition(initialState,
					initialState, "C", "", "");
			PDATransition initialDTransition = new PDATransition(initialState,
					initialState, "D", "", "");
			strategy.addTransition(initialDTransition);
			strategy.addTransition(initialCTransition);
			strategy.addFinalState(initialState);
			return new PDAStrategy(strategy);
		}

		public static PDAStrategy allD() {
			PushdownAutomaton strategy = new PushdownAutomaton();
			State initialState = strategy.createState(new Point(0, 0));
			initialState.setLabel("q0");
			strategy.setInitialState(initialState);
			
			PDATransition initialCTransition = new PDATransition(initialState,
					initialState, "C", "", "");
			PDATransition initialDTransition = new PDATransition(initialState,
					initialState, "D", "", "");
			strategy.addTransition(initialDTransition);
			strategy.addTransition(initialCTransition);
			return new PDAStrategy(strategy);
		}

		public static PDAStrategy TFT() {
			PushdownAutomaton strategy = new PushdownAutomaton();
			State initialState = strategy.createState(new Point(0, 0));
			initialState.setLabel("q0");
			strategy.setInitialState(initialState);
			State rejectState = strategy.createState(new Point(0, 0));
			rejectState.setLabel("q1");
			PDATransition selfCTransition = new PDATransition(initialState,
					initialState, "C", "", "");
			PDATransition transferDTransition = new PDATransition(initialState,
					rejectState, "D", "", "");
			PDATransition selfDTransition = new PDATransition(rejectState,
					rejectState, "D", "", "");
			PDATransition transferCTransition = new PDATransition(rejectState,
					initialState, "C", "", "");
			strategy.addTransition(transferDTransition);
			strategy.addTransition(selfCTransition);
			strategy.addTransition(selfDTransition);
			strategy.addTransition(transferCTransition);
			strategy.addFinalState(initialState);
			return new PDAStrategy(strategy);
		}

		public static PDAStrategy STFT() {
			PushdownAutomaton strategy = new PushdownAutomaton();
			State initialState = strategy.createState(new Point(0, 0));
			initialState.setLabel("q0");
			strategy.setInitialState(initialState);
			State acceptState = strategy.createState(new Point(0, 0));
			acceptState.setLabel("q1");
			PDATransition selfDTransition = new PDATransition(initialState,
					initialState, "D", "", "");
			PDATransition transferCTransition = new PDATransition(initialState,
					acceptState, "C", "", "");
			PDATransition selfCTransition = new PDATransition(acceptState,
					acceptState, "C", "", "");
			PDATransition transferDTransition = new PDATransition(acceptState,
					initialState, "D", "", "");
			strategy.addTransition(transferDTransition);
			strategy.addTransition(selfCTransition);
			strategy.addTransition(selfDTransition);
			strategy.addTransition(transferCTransition);
			strategy.addFinalState(acceptState);
			return new PDAStrategy(strategy);
		}

		// Strategy from my presentation
		public static PDAStrategy bradonsGrudge() {
			PushdownAutomaton strategy = new PushdownAutomaton();
			State initialState = strategy.createState(new Point(0, 0));
			strategy.setInitialState(initialState);
			initialState.setLabel("q0");
			State acceptState=strategy.createState(new Point(0,0));
			strategy.addFinalState(acceptState);
			acceptState.setLabel("q1");
			PDATransition finalTransition=new PDATransition(initialState,acceptState,"","Z","");
			PDATransition selfCTransition=new PDATransition(initialState,initialState,"C","","");
			PDATransition selfDTransition=new PDATransition(initialState,initialState,"D","","D");
			//read, pop, push
			PDATransition selfCPop=new PDATransition(initialState,initialState,"C","D","");
			strategy.addTransition(selfDTransition);
			strategy.addTransition(selfCTransition);
			strategy.addTransition(finalTransition);
			strategy.addTransition(selfCPop);
			return new PDAStrategy(strategy);
		}

		public static PDAStrategy ratioTFT() {
			return null;
		}
		
		public static PDAStrategy noTransitions() {
			PushdownAutomaton strategy = new PushdownAutomaton();
			State initialState = strategy.createState(new Point(0, 0));
			strategy.setInitialState(initialState);
			initialState.setLabel("q0");
			strategy.addFinalState(initialState);
			return new PDAStrategy(strategy);
		}
	}

}
