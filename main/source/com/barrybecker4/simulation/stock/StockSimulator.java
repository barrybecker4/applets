/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.simulation.stock;



import com.barrybecker4.common.format.CurrencyFormatter;
import com.barrybecker4.common.math.function.InvertibleFunction;
import com.barrybecker4.common.math.function.LinearFunction;
import com.barrybecker4.common.math.function.LogFunction;
import com.barrybecker4.simulation.common.ui.DistributionSimulator;
import com.barrybecker4.simulation.common.ui.SimulatorOptionsDialog;
import com.barrybecker4.ui.renderers.HistogramRenderer;

/**
 * Simulates the N stocks over M time periods (and other options).
 * Graphs the resulting distribution of values for the sample.
 *
 * @author Barry Becker
 */
public class StockSimulator extends DistributionSimulator {

    private static final int LABEL_WIDTH = 70;

    private StockSampleOptions opts_ = new StockSampleOptions();


    public StockSimulator() {
        super("Stock Market Simulation");
        initHistogram();
    }

    public void setSampleOptions(StockSampleOptions stockSampleOptions) {
        opts_ = stockSampleOptions;
        initHistogram();
    }

    @Override
    protected void initHistogram() {

        double max = opts_.getTheoreticalMaximum();
        double xScale = Math.pow(10, Math.max(0, Math.log10(max) - opts_.xResolution));
        double xLogScale = 3 * opts_.xResolution * opts_.xResolution;

        InvertibleFunction xFunction =
                opts_.useLogScale ? new LogFunction(xLogScale, 10.0, true) : new LinearFunction(1/xScale);

        int maxX = (int)xFunction.getValue(max);
        data_ = new int[maxX + 1];

        histogram_ = new HistogramRenderer(data_, xFunction);
        histogram_.setXFormatter(new CurrencyFormatter());
        histogram_.setMaxLabelWidth(LABEL_WIDTH);
    }

    @Override
    protected SimulatorOptionsDialog createOptionsDialog() {
        return new StockOptionsDialog( frame_, this );
    }

    @Override
    protected double getXPositionToIncrement() {
        return createSample();
    }

    /**
     * @return value of a set of numStocks after numTimePeriods.
     */
    private double createSample() {

        double total = 0;
        for (int j = 0; j < opts_.numStocks; j++) {
            total += calculateFinalStockPrice();
        }
        return total / opts_.numStocks;
    }

    /**
     * @return final stock price for a single stock after numTimePeriods.
     */
    private double calculateFinalStockPrice() {

        double stockPrice = opts_.startingValue;
        for (int i = 0; i < opts_.numTimePeriods; i++) {
            double percentChange =
                    Math.random() > 0.5 ? opts_.percentIncrease : -opts_.percentDecrease;
            if (opts_.useRandomChange)
                stockPrice *= (1.0 + Math.random() * percentChange);
            else
                stockPrice *= (1.0 + percentChange);
        }
        return stockPrice;
    }

    public static void main( String[] args )
    {
        final StockSimulator sim = new StockSimulator();
        runSimulation(sim);
    }
}