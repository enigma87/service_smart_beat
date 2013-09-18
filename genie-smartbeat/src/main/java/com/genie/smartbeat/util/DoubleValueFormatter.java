package com.genie.smartbeat.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author dhasarathy
 **/

public class DoubleValueFormatter {

	public static double format3Dot2(double unformattedValue){			
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat formatter3Dot2 = new DecimalFormat("###.##",symbols);
		return Double.valueOf(formatter3Dot2.format(unformattedValue));
	}
	
	public static double format3dot4(double unformattedValue){
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat formatter3Dot2 = new DecimalFormat("###.####",symbols);
		return Double.valueOf(formatter3Dot2.format(unformattedValue));

	}
}

