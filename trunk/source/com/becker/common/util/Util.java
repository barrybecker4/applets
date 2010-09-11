package com.becker.common.util;

import java.text.DecimalFormat;

/**
 * Miscelaneous commonly used static utility methods.
 */
public final class Util
{
    
    private static final DecimalFormat expFormat_ = new DecimalFormat("0.###E0");
    private static final DecimalFormat format_ = new DecimalFormat("###,###.##");
    private static final DecimalFormat intFormat_ = new DecimalFormat("#,###");

    private Util() {}


    /**
     * @param num the number to format.
     * @return a nicely formatted string representation of the number.
     */
    public static String formatNumber(double num)
    {
        double absnum = Math.abs(num);

        if (absnum == 0)  {
            return "0";
        }
        else if (absnum > 10000000.0 || absnum < 0.000000001) {
            return expFormat_.format(num);
        }
        else if (absnum > 1000.0) {
            format_.setMinimumFractionDigits(0);
            format_.setMaximumFractionDigits(0);
        }
        else if (absnum > 100.0) {
            format_.setMinimumFractionDigits(1);
            format_.setMaximumFractionDigits(1);
        }
        else if (absnum > 1.0) {
            format_.setMinimumFractionDigits(1);
            format_.setMaximumFractionDigits(3);
        }
        else if (absnum > 0.0001) {
            format_.setMinimumFractionDigits(2);
            format_.setMaximumFractionDigits(5);
        }
        else if (absnum>0.000001) {
            format_.setMinimumFractionDigits(3);
            format_.setMaximumFractionDigits(8);
        }
        else {
            format_.setMinimumFractionDigits(6);
            format_.setMaximumFractionDigits(11);
        }

        return format_.format(num);
    }

    /**
     * @param num the number to format.
     * @return a nicely formatted string representation of the number.
     */
    public static String formatNumber(long num)
    {
        return intFormat_.format(num);
    }

    /**
     * @param num the number to format.
     * @return a nicely formatted string representation of the number.
     */
    public static String formatNumber(int num)
    {
        return intFormat_.format(num);
    }


    public static void sleep(int millis) {
        if (millis > 0) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

