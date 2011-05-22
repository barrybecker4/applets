package com.becker.optimization.optimizees;

import com.becker.common.util.FileUtil;
import com.becker.optimization.Optimizer;
import com.becker.optimization.parameter.IntegerParameter;
import com.becker.optimization.parameter.NumericParameterArray;
import com.becker.optimization.parameter.Parameter;
import com.becker.optimization.parameter.ParameterArray;
import com.becker.optimization.strategy.OptimizationStrategyType;

/**
 * This is a simple search space to test the optimization package.
 * The problem we will try to solve is :
 *
 *   p1 + p2 + p3 + p4  = 711
 *   p1 * p2 * p3 * p4  = 711000000
 *
 * Which corresponds to the problem of someone going into a 7-11 and buying 4 things
 * whose sum and product equal $7.11.
 * This problem can be solved analytically by finding the prime factors of 711 and
 * eliminating combinations until you are left with:
 *   316, 125, 120, 150
 * as being the only solution.
 * Our choice of evaluation function to maximize is somewhat arbitrary.
 * When this function evaluates to 0, we have a solution.
 *
 * @see AnalyticFunctionTestProblem for an easier optimization example.
 *
 * @author Barry Becker
 */
public class SevenElevenTestProblem extends OptimizeeTestProblem {

  private static final Parameter[] INITIAL_GUESS_PARAMS =  {
             new IntegerParameter(100, 0, 708, "p1"),
             new IntegerParameter(200, 0, 708, "p2"),
             new IntegerParameter(200, 0, 708, "p3"),
             new IntegerParameter(200, 0, 708, "p4")};

    private static final int  P1 = 316;
    private static final int  P2 = 125;
    private static final int  P3 = 120;
    private static final int  P4 = 150;
    private static final Parameter[] EXACT_SOLUTION_PARAMS =  {
             new IntegerParameter(P1, 0, 708, "p1"),
             new IntegerParameter(P2, 0, 708, "p2"),
             new IntegerParameter(P3, 0, 708, "p3"),
             new IntegerParameter(P4, 0, 708, "p4")};

    private static final ParameterArray INITIAL_GUESS = new NumericParameterArray(INITIAL_GUESS_PARAMS);
    private static final ParameterArray EXACT_SOLUTION = new NumericParameterArray(EXACT_SOLUTION_PARAMS);

    // @@ exp errors.
    private static final double FITNESS_RANGE = 5000000.0;

    /** constructor */
    public SevenElevenTestProblem() {
    }

    /**
     * we evaluate directly not by comparing with a different trial.
     */
    public boolean evaluateByComparison() {
        return false;
    }

    // not used
    public double compareFitness(ParameterArray a, ParameterArray b) {
        return 0.0;
    }

    public String getName() {
        return "Zeven Eleven Problem";
    }
    /**
     *  The choice of fitness function here is somewhat arbitrary.
     *  I chose to use:
     *    -abs( p1 + p2 + p3 + p4 - 711)^3  - abs(711000000 - p1 * p2 * p3 * p4)
     *    or
     *    -abs(711 - sum) - abs(711000000 - product)/1000000
     * @param a the position in the search space given values of p1, p2, p4, p4.
     * @return fitness value
     */
    public double evaluateFitness(ParameterArray a) {

        double sum = a.get(0).getValue() + a.get(1).getValue() + a.get(2).getValue() + a.get(3).getValue();
        double product = a.get(0).getValue() * a.get(1).getValue() * a.get(2).getValue() * a.get(3).getValue();

        return -Math.abs(711.0 - sum) - Math.abs(711000000.0 - product) / 1000000.0;
    }


    @Override
    public ParameterArray getExactSolution() {
        return EXACT_SOLUTION;
    }

    @Override
    public ParameterArray getInitialGuess() {
        return INITIAL_GUESS;
    }

    @Override
    public double getFitnessRange() {
        return FITNESS_RANGE;
    }
    /**
     * This finds the solution for the above optimization problem.
     */
    public static void main(String[] args)
    {
        OptimizeeTestProblem problem = new SevenElevenTestProblem();
        Optimizer optimizer =
                new Optimizer(problem, FileUtil.PROJECT_HOME + "performance/test_optimizer/seven11_optimization.txt");

        ParameterArray initialGuess = problem.getInitialGuess();

        ParameterArray solution = optimizer.doOptimization(OptimizationStrategyType.GLOBAL_SAMPLING, initialGuess, FITNESS_RANGE);

        System.out.println( "\n************************************************************************" );
        System.out.println( "The solution to the 7-11 Test Problem is :\n"+solution );
        System.out.println( "Which evaluates to: "+ problem.evaluateFitness(solution));
        System.out.println( "We expected to get exactly 711000000:  p1-4 = {316, 125, 120, 150} " );

    }
}
