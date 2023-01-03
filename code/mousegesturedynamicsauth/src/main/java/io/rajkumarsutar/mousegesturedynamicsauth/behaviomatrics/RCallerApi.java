package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;

public class RCallerApi {

	public static double[][] wlsr(double[] x, double[] y) {
		RCaller rcaller = RCaller.create();
		RCode rcode = RCode.create();
		rcode.addDoubleArray("x", x);
		rcode.addDoubleArray("y", y);
		rcode.addRCode("result <- lowess(x, y, 0.8, 3, 0.06)");
		rcaller.setRCode(rcode);
		rcaller.runAndReturnResult("result");
		double[] smoothedX = rcaller.getParser().getAsDoubleArray("x");
		double[] smoothedY = rcaller.getParser().getAsDoubleArray("y");

		double[][] result = new double[x.length][2];
		
		for(int i = 0; i < x.length; i++) {
			result[i][0] = smoothedX[i];
			result[i][1] = smoothedY[i];
		}
		
		return result;
	}
}
