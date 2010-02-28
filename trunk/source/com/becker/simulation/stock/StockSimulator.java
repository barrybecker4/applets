package com.becker.simulation.stock;

import com.becker.common.format.CurrencyFormatter;
import com.becker.common.format.INumberFormatter;
import com.becker.common.math.function.Function;
import com.becker.common.math.function.LinearFunction;
import com.becker.common.math.function.LogFunction;
import com.becker.simulation.common.DistributionSimulator;
import com.becker.simulation.common.SimulatorOptionsDialog;
import com.becker.ui.HistogramRenderer;

/**
 * Simluates the N stocks over M time periods (and other options).
 * Graphs the resulting distribution of values for the sample.
 *
 * @author Barry Becker
 */
public class StockSimulator extends DistributionSimulator {

    private static final int LABEL_WIDTH = 70;

    private StockSampleOptions opts_ = new StockSampleOptions();
    private Function xFunction_;



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

        xFunction_ =
                opts_.useLogScale ? new LogFunction(xLogScale, 10.0, true) : new LinearFunction(1/xScale);

        int maxX = (int)xFunction_.getFunctionValue(max);
        data_ = new int[maxX + 1];

        histogram_ = new HistogramRenderer(data_);
        histogram_.setXFormatter(new CurrencyFormatter());
        histogram_.setXFunction(xFunction_);
        histogram_.setMaxLabelWidth(LABEL_WIDTH);
    }

    @Override
    protected SimulatorOptionsDialog createOptionsDialog() {
        return new StockOptionsDialog( frame_, this );
    }

    @Override
    protected int getXPositionToIncrement() {
        double sample = createSample();
        int normalizedSample = (int)xFunction_.getFunctionValue(sample);
        //System.out.println("sample=" + sample + " normalizedSample=" + normalizedSample);
        return normalizedSample;
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

    @Override
    protected String getFileNameBase()
    {
        return "stock";
    }

    public static void main( String[] args )
    {
        final StockSimulator sim = new StockSimulator();
        runSimulation(sim);
    }
}