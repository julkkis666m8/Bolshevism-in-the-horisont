package constants;

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
			 fin = totalT / totalN;	
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		
		return fin;
	}

}
