package io.rajkumarsutar.mousegesturedynamicsauth;

import java.util.Arrays;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCallerOptions;
import com.github.rcaller.rstuff.RCode;

public class App {

	public static void main(String[] args) {
		double aX[] = {1, 2};
		double aY[] = {1, 2};

		//double aSmoothedArray[][] = new double[aX.length][2];

        String sXVector = "c(";
        String sYVector = "c(";

        for (int i = 0; i < aX.length; i++) {
                sXVector = sXVector + aX[i] + ",";
                sYVector = sYVector + aY[i] + ",";
        }

        sXVector = sXVector.substring(0, sXVector.length() - 1) + ")";
        sYVector = sYVector.substring(0, sYVector.length() - 1) + ")";
        RCode rcode = RCode.create();
        
        String code = ""
        		+ "xVector=" + sXVector + "\n"
        		+ "yVector=" + sYVector + "\n"
        		+ "aSmoothedArray=lowess(xVector, yVector, 0.8, 3, 0.06)";
        rcode.addRCode(code);
        
        RCaller caller = RCaller.create(rcode, RCallerOptions.create());
        caller.runAndReturnResult("aSmoothedArray");
        //caller.runAndReturnResult("x");
        //String d = caller.getParser().to);
        //System.out.println(Arrays.toString(caller.getParser().getAsIntArray("x")));
        /*
        engine.eval("xVector=" + sXVector);
        engine.eval("yVector=" + sYVector);
        engine.eval("aSmoothedArray=lowess(xVector, yVector, 0.8, 3, 0.06)");
        
        org.rosuda.JRI.REXP dXval = new org.rosuda.JRI.REXP();
        org.rosuda.JRI.REXP dYval = new org.rosuda.JRI.REXP();
        org.rosuda.JRI.RVector vSmoothedData = (RVector) engine.eval("aSmoothedArray").getContent();
        dXval = (org.rosuda.JRI.REXP) vSmoothedData.get(0);
        dYval = (org.rosuda.JRI.REXP) vSmoothedData.get(1);
		
        double aSX[] = dXval.asDoubleArray();
        double aSY[] = dYval.asDoubleArray();
        for (int i = 0; i < aX.length; i++) {
                aSmoothedArray[i][0] = aSX[i];
                aSmoothedArray[i][1] = aSY[i];
        }
        */
        double aSmoothedArray[][] = caller.getParser().getAsDoubleMatrix("aSmoothedArray");
        
        System.out.println(Arrays.toString(aSmoothedArray));
	}

}
