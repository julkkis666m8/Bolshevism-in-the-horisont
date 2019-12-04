package constants;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Functions {

	public static double divideArrays(double[] arrayT, double[] arrayN) {
		
		
		double fin = 0;
		double totalT = 0;
		double totalN = 0;
		for(double d : arrayT) {
			totalT += d;
		}
		for(double d : arrayN) {
			totalN += d;
		}
		try {
			 fin = (totalN - totalT) / totalN;	
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		return fin;
	}
	
	private static String[] suffix = new String[]{"","k", "m", "b", "t"};
	private static int MAX_LENGTH = 8;

	public static String formatNum(double number) {
		if(number < 1) {
			return myFormat(number);
		}
	    String r = new DecimalFormat("##0E0").format(number);
	    r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
	    while(r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")){
	        r = r.substring(0, r.length()-2) + r.substring(r.length() - 1);
	    }
	    return r;
	}

	private static String myFormat(double number) {
		DecimalFormat df = new DecimalFormat("0.0000000");
		String numb = df.format(number);

		return numb;
	}

}
