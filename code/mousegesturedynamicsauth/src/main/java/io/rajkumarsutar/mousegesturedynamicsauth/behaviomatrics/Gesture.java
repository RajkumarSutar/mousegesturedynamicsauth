package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import io.rajkumarsutar.mousegesturedynamicsauth.database.Database;

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

    public static void initialize(int gestureID, String userID) {
    	int n = BMCostants.REPLICATIONS * BMApi.TEMPLATE_SIZE;
        G = new int[n][3];

        List<int[][]> replications = Database.fetchUserReplications(userID, gestureID);

        for(int[][] replica : replications) {
        	for(int j = 0; j<BMApi.TEMPLATE_SIZE; j++) {
                G[j][0] = replica[j][0];
                G[j][1] = replica[j][1];
                G[j][2] = replica[j][2];
            }
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
