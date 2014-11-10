package edu.monash.bthal2.repeatedPD.Comparator;

import com.evolutionandgames.jevodyn.utils.Random;
import com.evolutionandgames.repeatedgames.evolution.Action;
import com.evolutionandgames.repeatedgames.evolution.RepeatedGame;
import com.evolutionandgames.repeatedgames.evolution.RepeatedStrategy;

import edu.monash.bthal2.repeatedPD.DPDA.DPDAFactory;
import edu.monash.bthal2.repeatedPD.LookupTableRepresentation.LookupStrategyLengthOne;
import edu.monash.bthal2.repeatedPD.LookupTableRepresentation.LookupStrategyLengthOneFactory;

public class cOMPARE {
	public static double[][] compare(double R, double S, double T, double P,
			double r, double delta, int samples, RepeatedStrategy[] strategies) {
		RepeatedGame rpd = new RepeatedGame(R, S, T, P, delta);
		double[][] estimatedMatrix = new double[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				double payoffEstimate = 0;
				for (int sample = 0; sample < samples; sample++) {
					payoffEstimate = payoffEstimate
							+ rpd.playOnce(strategies[i], strategies[j])[0];
				}

				estimatedMatrix[i][j] = payoffEstimate / (double) samples;
			}
		}

		return estimatedMatrix;
	}

	public static void main(String[] args) {
		double[][] game = { { 3, 3, 2 / 3 }, { 3, 3, 0 }, { 2, 4, 1 } };
		double r = 0;
		double delta = 2.0/3.0;
		int samples = 10000;
		Random.seed(System.nanoTime());
		RepeatedStrategy[] strategies = new RepeatedStrategy[3];
		RepeatedStrategy alld = LookupStrategyLengthOneFactory.allD();
		//RepeatedStrategy tft = DPDAFactory.ExampleStrategies.tft();
		Action[] tftactions=new Action[3];
		tftactions[0]=Action.COOPERATE;
		tftactions[1]=Action.COOPERATE;
		tftactions[2]=Action.DEFECT;
		Action[] allcActions=new Action[3];
		allcActions[0]=Action.COOPERATE;
		allcActions[1]=Action.COOPERATE;
		allcActions[2]=Action.COOPERATE;
		RepeatedStrategy tft=new LookupStrategyLengthOne(tftactions);
		RepeatedStrategy allc = new LookupStrategyLengthOne(allcActions);
		strategies[2]=alld;
		strategies[1]=allc;
		strategies[0]=tft;
		double[][] result = compare(3.0, 0.0, 4.0, 1.0, r, delta, samples,
				strategies);

		System.out.print("{");
		for (int i = 0; i < result.length; i++) {
			System.out.print("{");
			for (int j = 0; j < result.length; j++) {
				System.out.print(result[j][i]);
				if (j != result.length - 1) {
					System.out.print(",");
				} else {

				}
			}
			System.out.print("}\n");
			if (i != result.length - 1) {
				System.out.print(",");
			}
		}
		System.out.print("}");
	}
}
