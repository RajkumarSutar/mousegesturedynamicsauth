package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

import java.io.File;
import java.util.Scanner;

/**
 * @author 
 */
public class Gesture {
    public static int G[][];

    public static void initialize(String sFileName) {
    	int n = BMCostants.REPLICATIONS * BMApi.TEMPLATE_SIZE;
        G = new int[n][3];
        try{
        Scanner oScanner = new Scanner(new File(sFileName));
            for(int i=0; i<BMCostants.REPLICATIONS; i++){
                oScanner.nextInt();
                for(int j = i*BMApi.TEMPLATE_SIZE; j<BMApi.TEMPLATE_SIZE*(i+1); j++) {
	                G[j][0] = oScanner.nextInt();
	                G[j][1] = oScanner.nextInt();
	                G[j][2] = oScanner.nextInt();
                }
            }
        }
        catch(Exception oException){
            oException.printStackTrace();
        }
    }

    public static double[][] initializeGesture(String sFileName) {
    	int n = 1 * BMApi.TEMPLATE_SIZE;
        double Gesture[][] = new double[n][3];
        try{
        Scanner oScanner = new Scanner(new File(sFileName));
            for(int i=0; i<1; i++){
                oScanner.nextInt();
                for(int j = i*BMApi.TEMPLATE_SIZE; j<BMApi.TEMPLATE_SIZE*(i+1); j++) {
	            Gesture[j][0] = oScanner.nextInt();
	            Gesture[j][1] = oScanner.nextInt();
	            Gesture[j][2] = oScanner.nextInt();
                }
            }
        }
        catch(Exception oException){
            oException.printStackTrace();
        }

        return Gesture;
    }    
}
