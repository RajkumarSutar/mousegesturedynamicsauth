package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

import java.io.BufferedWriter;

/////////////////////////////Dependencies///////////////////////////////////////

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;

import io.rajkumarsutar.mousegesturedynamicsauth.database.Database;

/////////////////////////CLASS DEFINITION///////////////////////////////////////

/**
 * This class is a common API class for Behaviometrics
 *
 * @author Prashant Sutar
 */
public class BMApi {

    ///////////////////////// Variables
    ///////////////////////// Declaration//////////////////////////////

    public static final String LOG_FILE_NAME = "BehaviometricsLog.txt";
    public static final String CLICK_LOG_FILE_NAME = "ClickLogs.txt";
    public static final int TEMPLATE_SIZE = 64;

    ///////////////////////// Constructor
    ///////////////////////// Declaration////////////////////////////

    public BMApi() {
    }

    ///////////////////////// Public
    ///////////////////////// Methods/////////////////////////////////////

    /**
     * This method read the gesture information from the log file
     *
     * @param n
     *            integer Size of gesture
     * @return int[][] gesture coordinates and time
     */
    public static int[][] readLogFile(int row, int colum, String sFileName) {
            int aTempRowData[][] = new int[row][colum];
            try {
                    Scanner oScanner = new Scanner(new File(sFileName));

                    for (int i = 0; i < row; i++) {
                            for(int j=0; j<colum; j++) {
                                    aTempRowData[i][j] = oScanner.nextInt();
                            }
                    }
            } catch (Exception oException) {
                    // oException.printStackTrace();
            }

            return aTempRowData;
    }

    /**
     * Convert row data into 100 points is points are more than 100
     *
     * @param aRowData
     * @param n
     * @return
     */
    public int[][] kMeans(int aRowData[][], int n) {

            // template size
            int iTotalClusters = TEMPLATE_SIZE;
            try {

                    int iCopyIndex = 0;
                    String aClusters[] = new String[iTotalClusters];
                    int iClusterSize = Math.round(n / (float) iTotalClusters);
                    iTotalClusters = iTotalClusters - 1;

                    // index contains data point postion in aRowData and value contains
                    // cluster number
                    int aPointClusterMapping[] = new int[n];

                    for (int i = 0; i < iTotalClusters; i++) {
                            int iUI = iClusterSize + iCopyIndex - 1;
                            aClusters[i] = "";

                            for (int j = iCopyIndex; j < iUI; j++) {
                                    aClusters[i] = aClusters[i] + " " + j;
                                    iCopyIndex = j;
                                    // System.out.println("value of j= "+j);
                                    aPointClusterMapping[j] = i;
                            }
                            iCopyIndex += 1;
                    }

                    aClusters[iTotalClusters] = "";

                    for (int j = iCopyIndex + 1; j < n; j++) {
                            aClusters[iTotalClusters] = aClusters[iTotalClusters] + " " + j;

                            aPointClusterMapping[j] = iTotalClusters;
                    }

                    boolean bContinueFlag = true;

                    while (bContinueFlag) {

                            bContinueFlag = false;
                            int aCentriods[][] = new int[iTotalClusters + 1][3];

                            for (int i = 0; i <= iTotalClusters; i++) {
                                    int a[] = centroid(aRowData, aClusters[i]);
                                    aCentriods[i][0] = a[0];
                                    aCentriods[i][1] = a[1];
                                    aCentriods[i][2] = a[2];
                            }

                            for (int j = 0; j < n; j++) {
                                    int iX1 = aRowData[j][0];
                                    int iY1 = aRowData[j][1];
                                    int iClusterNumber = 0;
                                    double dDistance = 999999999999999999999999999999999999999d;
                                    for (int i = 0; i <= iTotalClusters; i++) {
                                            int iX2 = aCentriods[i][0];
                                            int iY2 = aCentriods[i][1];

                                            double dTempDist = BMApi.euclideanDistance(iX1, iY1, iX2, iY2);

                                            if (dTempDist < dDistance) {
                                                    dDistance = dTempDist;
                                                    iClusterNumber = i;
                                            }
                                    }
                                    int iOldCluster = aPointClusterMapping[j];
                                    if (iOldCluster != iClusterNumber) {
                                            bContinueFlag = true;

                                            aClusters[iOldCluster] = aClusters[iOldCluster].replaceAll(" " + j + " ", " ");
                                            aClusters[iClusterNumber] = aClusters[iClusterNumber] + " " + j;
                                            aPointClusterMapping[j] = iClusterNumber;
                                    }
                            }
                    }

                    int aCentriods[][] = new int[iTotalClusters + 1][3];

                    for (int i = 0; i <= iTotalClusters; i++) {
                            int a[] = centroid(aRowData, aClusters[i]);
                            aCentriods[i][0] = a[0];
                            aCentriods[i][1] = a[1];
                            aCentriods[i][2] = a[2];

                            // System.out.println(i+">>
                            // ["+aCentriods[i][0]+":"+aCentriods[i][1]+":"+aCentriods[i][2]+"]");
                    }
                    // System.out.println(Arrays.deepToString(aCentriods));
                    return aCentriods;
            } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    // ex.printStackTrace();
            }

            return null;
    }

    ///////////////////////// Private
    ///////////////////////// Methods////////////////////////////////////

    /**
     * This Function returns the distance between to Points on Graph using
     * Euclidean Distance formula *
     *
     * @param x1
     *            int X-Cordinate of 1st Point
     * @param y1
     *            int Y-Cordinate of 1st Point
     * @param x2
     *            int X-Cordinate of 2nd Point
     * @param y2
     *            int Y-Cordinate of 2nd Point
     *
     * @return double the distance between two points
     */
    public static double euclideanDistance(int x1, int y1, int x2, int y2) {
            double fDistance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

            return fDistance;
    }

    /**
     * This function gives the centroid of a graph
     *
     * @param sCluster
     *            String space separated clusters numbers
     *
     * @return int[] x- coordinate, y- coordinate, time
     */
    private static int[] centroid(int aRowData[][], String sCluster) {
    	try (Scanner oScanner = new Scanner(sCluster)) {
            String t = sCluster;
            int idatapoints = t.replaceAll("[^\\s-]", "").length();
            int iX = 0;
            int iY = 0;
            int iT = 0;

            if (idatapoints > 0) {

                    for (int i = 0; i < idatapoints; i++) {

                            int x = oScanner.nextInt();
                            iX = iX + aRowData[x][0];
                            iY = iY + aRowData[x][1];
                            iT = iT + aRowData[x][2];
                    }

                    iX = iX / idatapoints;
                    iY = iY / idatapoints;
                    iT = iT / idatapoints;
                    int aT[] = { iX, iY, iT };

                    return aT;
            } else {
                    int aT[] = { 0, 0, 0 };

                    return aT;
            }
    	}
    }

    /**
     * This method is used to Remove outliers from the data
     *
     * @param aDataSet
     *            double[]
     * @return
     */
    public static double[] peircesCriterion(double aDataSet[]) {
            double iSum = 0;
            int idatapoints = aDataSet.length;
            for (int i = 0; i < idatapoints; i++) {
                    iSum = aDataSet[i] + iSum;
            }

            double dStandardDeviation = 0.0;
            double dSampleMean = iSum / idatapoints;

            for (int i = 0; i < idatapoints; i++) {
                    double dDiff = (aDataSet[i] - dSampleMean);
                    dStandardDeviation = dStandardDeviation + (dDiff * dDiff);
            }

            double dNforSampleSD = (double) idatapoints - 1;
            dStandardDeviation = Math.sqrt(dStandardDeviation / dNforSampleSD);

            int ix = 0;
            double dR = 0;

            double dMaxAllowableDeviation = 0.0;
            int iDoubtfullObservations = 0;
            boolean bIsModeDoubtfullObservations = true;

            while (bIsModeDoubtfullObservations) {
                    double dMaxDeviation = 0.0;
                    for (int i = 0; i < idatapoints; i++) {
                            if (aDataSet[i] != 99999999999d) {
                                    double dTemAllowMaxDev = Math.abs(aDataSet[i] - dSampleMean);
                                    if (dTemAllowMaxDev > dMaxDeviation) {
                                            dMaxDeviation = dTemAllowMaxDev;
                                            ix = i;
                                    }
                            }
                    }
                    dR = BMCostants.R[idatapoints][iDoubtfullObservations];

                    dMaxAllowableDeviation = dR * dStandardDeviation;

                    if (Math.abs(aDataSet[ix] - dSampleMean) > dMaxAllowableDeviation) {
                            aDataSet[ix] = 99999999999d;
                            iDoubtfullObservations++;
                    } else {
                            bIsModeDoubtfullObservations = false;
                    }
            }

            return aDataSet;
    }

    /**
     * This function is used to Print a Array to a file
     *
     * @param sFileName
     * @param dData
     * @param iDataCount
     */
    public static void printArrayToFile(String sFileName, double dData[][], int iDataCount) {
            try {
                    File oFile = new File(sFileName);
                    FileWriter oFileWriter = new FileWriter(oFile.getName(), true);
                    BufferedWriter oBufferedWriter = new BufferedWriter(oFileWriter);
                    for (int i = 0; i < iDataCount; i++) {
                            oBufferedWriter.write(dData[i][0] + " " + dData[i][1] + " " + dData[i][2] + "\n");
                    }
                    oBufferedWriter.close();
            } catch (Exception oException) {
                    oException.printStackTrace();
            }
    }

    /**
     * This function is used to write the data from array to a file
     *
     * @param sFileName
     * @param aDataSet
     * @param iRowCount
     */
    public static void writeArrayToFile(String sFileName, int aDataSet[][], int iRowCount) {
            try {
                    FileWriter oFileWriter = new FileWriter(sFileName, true);
                    BufferedWriter oBufferedWriter = new BufferedWriter(oFileWriter);
                    for (int i = 0; i < iRowCount; i++) {
                            oBufferedWriter.write(aDataSet[i][0] + " " + aDataSet[i][1] + " " + aDataSet[i][2] + "\n");
                    }
                    oBufferedWriter.close();
            } catch (IOException ex) {
                    System.err.println(ex.getMessage());
            }
    }

    /**
     *
     * @param sFileName
     * @param sMessage
     * @param iRowCount
     */
    public static void writeLineToFile(String sFileName, String sMessage, int iRowCount) {

            File oFile = new File(sFileName);
            if (!oFile.exists()) {
                    try {
                            oFile.createNewFile();
                    } catch (Exception e) {
                            e.printStackTrace();
                    }
            }
            try {
                    FileWriter oFileWriter = new FileWriter(sFileName, true);
                    BufferedWriter oBufferedWriter = new BufferedWriter(oFileWriter);
                    oBufferedWriter.write(sMessage + "\n");
                    oBufferedWriter.close();
            } catch (IOException ex) {
                    System.err.println(ex.getMessage());
            }
    }

    /**
     *
     * @param sFileName
     * @param sMessage
     */
    public static void writeFeatureToFile(String sFileName, String sMessage) {

            File oFile = new File(sFileName);
            if (!oFile.exists()) {
                    try {
                            oFile.createNewFile();
                    } catch (Exception e) {
                            e.printStackTrace();
                    }
            }
            try {
                    FileWriter oFileWriter = new FileWriter(sFileName, true);
                    BufferedWriter oBufferedWriter = new BufferedWriter(oFileWriter);
                    oBufferedWriter.write(sMessage + "\n");
                    oBufferedWriter.close();
            } catch (IOException ex) {
                    System.err.println(ex.getMessage());
            }
    }

    /**
     *
     * @param engine
     * @param sGestureFileName
     * @return
     */
    public static double[][] smooth(String sGestureFileName) {
            int m = BMCostants.REPLICATIONS;
            int n = BMApi.TEMPLATE_SIZE;

            int size = BMCostants.REPLICATIONS * BMApi.TEMPLATE_SIZE;

            Gesture.initialize(sGestureFileName);
            double _G_[][] = new double[size][3];
            for (int i = 0; i < n; i++) {
                    double aX[] = new double[m];
                    double aY[] = new double[m];
                    double aT[] = new double[m];
                    for (int j = i, k = 0; j < size; j += n, k++) {
                            aX[k] = Gesture.G[j][0];
                            aY[k] = Gesture.G[j][1];
                            aT[k] = Gesture.G[j][2];
                    }

                    double[][] aSmoothData = BMApi.WLSR(aX, aY);
                    int j = 0;

                    for (int l = i; l < size; l += n) {
                            _G_[l][0] = aX[j];
                            _G_[l][1] = getY(aX[j], aSmoothData);
                            _G_[l][2] = aT[j];
                            j++;
                    }
            }

            return _G_;
    }

    public static double[][] smooth(String userID, int gestureID) {
        int m = BMCostants.REPLICATIONS;
        int n = BMApi.TEMPLATE_SIZE;

        int size = BMCostants.REPLICATIONS * BMApi.TEMPLATE_SIZE;

        Gesture.initialize(gestureID, userID);
        double _G_[][] = new double[size][3];
        for (int i = 0; i < n; i++) {
                double aX[] = new double[m];
                double aY[] = new double[m];
                double aT[] = new double[m];
                for (int j = i, k = 0; j < size; j += n, k++) {
                        aX[k] = Gesture.G[j][0];
                        aY[k] = Gesture.G[j][1];
                        aT[k] = Gesture.G[j][2];
                }

                double[][] aSmoothData = BMApi.WLSR(aX, aY);
                int j = 0;

                for (int l = i; l < size; l += n) {
                        _G_[l][0] = aX[j];
                        _G_[l][1] = getY(aX[j], aSmoothData);
                        _G_[l][2] = aT[j];
                        j++;
                }
        }

        return _G_;
}


    public static double getY(double X, double array[][]) {
            double d = 0;
            for (int i = 0; i < BMCostants.REPLICATIONS; i++) {
                    if (array[i][0] == X) {
                            return array[i][1];
                    }
            }
            return d;
    }

    /**
     * Extract Y-coordinates from the 2d matrix
     *
     * @param data
     * @return
     */
    public static double[] getXVal(int[][] data) {
            double xval[] = new double[TEMPLATE_SIZE];
            for (int i = 0; i < TEMPLATE_SIZE; i++) {
                    xval[i] = data[i][0];
            }

            return xval;
    }

    /**
     * Extract X-coordinates from the 2d matrix
     *
     * @param data
     * @return
     */
    public static double[] getYVal(int[][] data) {
            double yval[] = new double[TEMPLATE_SIZE];
            for (int i = 0; i < TEMPLATE_SIZE; i++) {
                    yval[i] = data[i][1];
            }

            return yval;
    }

    /**
     * This method is used to Smooth the data this method uses the R language to
     * implement Robust Weighted least Square Regression
     *
     * @param aX
     *            double[] x coordinates
     * @param aY
     *            double[] y coordinates
     *
     * @return double[][] smoothed X-Y coordinates
     */
    public static double[][] WLSR(double aX[], double aY[]) {
		return RCallerApi.wlsr(aX, aY);
    }


    /*
    public static double[][] WLSR(Rengine engine, double aX[], double aY[]) {
            double aSmoothedArray[][] = new double[aX.length][2];

            String sXVector = "c(";
            String sYVector = "c(";

            for (int i = 0; i < aX.length; i++) {
                    sXVector = sXVector + aX[i] + ",";
                    sYVector = sYVector + aY[i] + ",";
            }

            sXVector = sXVector.substring(0, sXVector.length() - 1) + ")";
            sYVector = sYVector.substring(0, sYVector.length() - 1) + ")";

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

            return aSmoothedArray;
    }
*/


    /**
     * This method extracts features from specified input. These features will be used in LVQ training.
     *
     * @param aSmoothedData Smoothed User input
     * @param sUserName     User name
     * @param sGesture      Gesture Name
     * @param isValidation  Validation or training
     *
     * @return Extracted features for all users and specified gestures
     */
    public static String [] extractFeatures(double[][] aSmoothedData, String sUserName, String sGesture, boolean isValidation) {
            int m = isValidation == true
                    ? 1 : BMCostants.REPLICATIONS;
            int n = BMApi.TEMPLATE_SIZE;
            String sFeatureFolderName = "./User/" + sUserName + "/Features/" + sGesture + "/";
            File ofile = new File(sFeatureFolderName);
            ofile.mkdirs();
            //int size = BMCostants.REPLICATIONS * BMApi.TEMPLATE_SIZE;

            for (int i = 0; i < m; i++) {
                String sFeatureFileName = sFeatureFolderName + (i + 1) + ".txt";
                    double aX[] = new double[n];
                    double aY[] = new double[n];
                    double aT[] = new double[n];
                    for (int j = (i * n), k = 0; j < (i + 1) * n; j++, k++) {
                            aX[k] = aSmoothedData[j][0];
                            aY[k] = aSmoothedData[j][1];
                            aT[k] = aSmoothedData[j][2];
                    }

                    // hv, vv, tv, tj, ta, theta, distance, c
                    double horizontalVelocity[] = new double[BMApi.TEMPLATE_SIZE];
                    double verticalVelocity[] = new double[BMApi.TEMPLATE_SIZE];
                    double tangentialVelocity[] = new double[BMApi.TEMPLATE_SIZE];
                    double slopeAngleOfTangent[] = new double[BMApi.TEMPLATE_SIZE];
                    double distanceFromOrigin[] = new double[BMApi.TEMPLATE_SIZE];
                    double c[] = new double[BMApi.TEMPLATE_SIZE];
                    for (int l = 1; l < n; l++) {
                            int e = l;
                            int s = l - 1;
                            double t = (aT[e] - aT[s]);
                            double hv = (aX[e] - aX[s]) / t;
                            double vv = (aY[e] - aY[s]) / t;
                            horizontalVelocity[l] = hv;
                            verticalVelocity[l] = vv;
                            tangentialVelocity[l] = Math.sqrt(hv * hv + vv * vv);
                            slopeAngleOfTangent[l] = Math.atan(aY[l] / aX[l]);
                            distanceFromOrigin[l] = distanceFromOrigin(aY[l], aX[l]);
                            c[l] = slopeAngleOfTangent[l] / distanceFromOrigin[l];
                    }
                    BMApi.writeFeatureToFile(sFeatureFileName, "X-Coordinates:" + Arrays.toString(aX));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature(BMCostants.X, BMApi.arrayToString(aX), "FS1", sUserName, sGesture);

                    BMApi.writeFeatureToFile(sFeatureFileName, "Y-Coordinates:" + Arrays.toString(aY));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature(BMCostants.Y, BMApi.arrayToString(aY), "FS1", sUserName, sGesture);

                    BMApi.writeFeatureToFile(sFeatureFileName, "Time:" + Arrays.toString(aT));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature(BMCostants.T, BMApi.arrayToString(aT), "FS1", sUserName, sGesture);

                    BMApi.writeFeatureToFile(sFeatureFileName, "HorizontalVelocity:" + Arrays.toString(horizontalVelocity));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature("HorizontalVelocity", BMApi.arrayToString(horizontalVelocity), "FS3",
                                    sUserName, sGesture);

                    BMApi.writeFeatureToFile(sFeatureFileName, "VerticalVelocity:" + Arrays.toString(verticalVelocity));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature("VerticalVelocity", BMApi.arrayToString(verticalVelocity), "FS3", sUserName,
                                    sGesture);

                    BMApi.writeFeatureToFile(sFeatureFileName, "TangentialVelocity:" + Arrays.toString(tangentialVelocity));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature("TangentialVelocity", BMApi.arrayToString(tangentialVelocity), "FS3",
                                    sUserName, sGesture);

                    BMApi.writeFeatureToFile(sFeatureFileName, "SlopeAngleOfTangent:" + Arrays.toString(slopeAngleOfTangent));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature("SlopeAngleOfTangent", BMApi.arrayToString(slopeAngleOfTangent), "FS4",
                                    sUserName, sGesture);

                    BMApi.writeFeatureToFile(sFeatureFileName, "DistanceFromOrigin:" + Arrays.toString(distanceFromOrigin));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature("DistanceFromOrigin", BMApi.arrayToString(distanceFromOrigin), "FS4",
                                    sUserName, sGesture);
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature("DistanceFromOrigin", BMApi.arrayToString(distanceFromOrigin), "FS2",
                                    sUserName, sGesture);

                    BMApi.writeFeatureToFile(sFeatureFileName, "Curvature:" + Arrays.toString(c));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature("Curvature", BMApi.arrayToString(c), "FS2", sUserName, sGesture);

                    // tangential accelaration
                    double tangentialAccelaration[] = new double[BMApi.TEMPLATE_SIZE];
                    for (int l = 1; l < n; l++) {
                            int e = l;
                            int s = l - 1;
                            double t = (aT[e] - aT[s]);
                            tangentialAccelaration[l] = (tangentialVelocity[e] - tangentialVelocity[s]) / t;
                    }

                    BMApi.writeFeatureToFile(sFeatureFileName,
                                    "TangentialAccelaration:" + Arrays.toString(tangentialAccelaration));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature("TangentialAccelaration", BMApi.arrayToString(tangentialAccelaration), "FS3",
                                    sUserName, sGesture);

                    // tangential jerk
                    double tangentialJerk[] = new double[BMApi.TEMPLATE_SIZE];
                    for (int l = 1; l < n; l++) {
                            int e = l;
                            int s = l - 1;
                            double t = (aT[e] - aT[s]);
                            tangentialJerk[l] = (tangentialAccelaration[e] - tangentialAccelaration[s]) / t;
                    }

                    BMApi.writeFeatureToFile(sFeatureFileName, "TangentialJerk:" + Arrays.toString(tangentialJerk));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature("TangentialJerk", BMApi.arrayToString(tangentialJerk), "FS3", sUserName,
                                    sGesture);

                    // curvature rate of change, delta Distance from origin, delta slope
                    // angle of the tangent
                    double dTheta[] = new double[BMApi.TEMPLATE_SIZE];
                    double dL[] = new double[BMApi.TEMPLATE_SIZE];
                    double curv_rate_change[] = new double[BMApi.TEMPLATE_SIZE];
                    for (int l = 1; l < n; l++) {
                            int e = l;
                            int s = l - 1;
                            dTheta[l] = (slopeAngleOfTangent[e] - slopeAngleOfTangent[s]);
                            dL[l] = (distanceFromOrigin[e] - distanceFromOrigin[s]);
                            curv_rate_change[l] = dTheta[l] / dL[l];
                    }

                    BMApi.writeFeatureToFile(sFeatureFileName, "CurvatureRateChange:" + Arrays.toString(curv_rate_change));
                    io.rajkumarsutar.mousegesturedynamicsauth.database.Database.saveFeature("CurvatureRateChange", BMApi.arrayToString(curv_rate_change), "FS2",
                                    sUserName, sGesture);

                    String tempFeature1 = BMApi.arrayToString(aX);
                    String tempFeature2 = BMApi.arrayToString(aY);
                    String tempFeature3 = BMApi.arrayToString(aT);
                    String sFeatureSet1 = tempFeature1 + tempFeature2 + tempFeature3;

                    tempFeature1 = BMApi.arrayToString(c);
                    tempFeature2 = BMApi.arrayToString(distanceFromOrigin);
                    tempFeature3 = BMApi.arrayToString(curv_rate_change);
                    String sFeatureSet2 = tempFeature1 + tempFeature2 + tempFeature3;

                    tempFeature1 = BMApi.arrayToString(horizontalVelocity);
                    tempFeature2 = BMApi.arrayToString(verticalVelocity);
                    tempFeature3 = BMApi.arrayToString(tangentialAccelaration);
                    String tempFeature4 = BMApi.arrayToString(tangentialVelocity);
                    String tempFeature5 = BMApi.arrayToString(tangentialJerk);
                    String sFeatureSet3 = tempFeature1 + tempFeature2 + tempFeature3 + tempFeature4 + tempFeature5;

                    tempFeature1 = BMApi.arrayToString(distanceFromOrigin);
                    tempFeature2 = BMApi.arrayToString(slopeAngleOfTangent);
                    String sFeatureSet4 = tempFeature1 + tempFeature2;

                    if(isValidation == false) {
                        io.rajkumarsutar.mousegesturedynamicsauth.database.Database.storeFeatureSet(sUserName, sFeatureSet1, Behaviometrics.SessionID, "FS1", sGesture);
                        io.rajkumarsutar.mousegesturedynamicsauth.database.Database.storeFeatureSet(sUserName, sFeatureSet2, Behaviometrics.SessionID, "FS2", sGesture);
                        io.rajkumarsutar.mousegesturedynamicsauth.database.Database.storeFeatureSet(sUserName, sFeatureSet3, Behaviometrics.SessionID, "FS3", sGesture);
                        io.rajkumarsutar.mousegesturedynamicsauth.database.Database.storeFeatureSet(sUserName, sFeatureSet4, Behaviometrics.SessionID, "FS4", sGesture);
                    }

                    String array1[] = {sFeatureSet1, sFeatureSet2, sFeatureSet3, sFeatureSet4};
                    if(isValidation == true) {
                        return array1;
                    }
            }
            return null;
    }

    public static double distanceFromOrigin(double x, double y) {
            return Math.sqrt(x * x + y * y);
    }

    public static double[] getFeatureVector(String sAllFeatures, String sFeatureName, int iSpace) {
            double dFeatureVector[] = new double[iSpace];
            int iFeatureIndex = sAllFeatures.indexOf(sFeatureName) + sFeatureName.length() + 2;
            int iEndIndex = sAllFeatures.indexOf(']', iFeatureIndex);
            String sFeature = sAllFeatures.substring(iFeatureIndex, iEndIndex);
            sFeature = sFeature.replaceAll(",", "  ");
            try (Scanner oStringScanner = new Scanner(sFeature)) {
	            int i = 0;
	            while (oStringScanner.hasNextDouble()) {
	                    dFeatureVector[i++] = oStringScanner.nextDouble();
	            }
            }
            return dFeatureVector;
    }

    /**
     * This method used to log the SQL errors
     *
     * @param Time
     *            Time of error
     * @param oException
     *            Exception thrown
     */
    public static void logError(String Time, Exception oException) {
            try {
                    Writer w = new FileWriter("sqlLogs.log", true);
                    PrintWriter pw = new PrintWriter(w);
                    pw.write(Time + ":");
                    pw.write(oException.getMessage());
                    oException.printStackTrace(pw);
                    pw.write("\n\n");
                    pw.close();
            } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Behaviometrics", JOptionPane.ERROR_MESSAGE);
            }
    }

    public static String arrayToString(double[] dArrayFeatures) {
            String sFeatureString = "";
            for (int i = 0; i < dArrayFeatures.length; i++) {
                    sFeatureString += dArrayFeatures[i] + " ";
            }

            return sFeatureString;
    }

    /**
     *
     * @param aFeatures
     *
     * @return
     */
    public static double[] averageArray(double[]... aFeatures) {
            double average[] = null;
            int count = 0;
            for (double feature[] : aFeatures) {
                    int length = feature.length;
                    if (average == null) {
                            average = new double[length];
                    }

                    for (int i = 0; i < feature.length; i++) {
                            average[i] += feature[i];
                    }
                    count++;
            }
            if (average != null) {
                    for (int i = 0; i < average.length; i++) {
                            average[i] /= count;
                    }
            }

            return average;
    }

    public static double[] stringToArray(String s) {
        double d[];
        String ss[] = s.split(" ");
        d = new double[ss.length];
        int i = 0;
        for(String as : ss) {
            d[i++] = Double.parseDouble(as);
        }

        return d;
    }

    public static long getSessionID() {
            return (new Date()).getTime();
    }

    public static boolean extractClickFeatures(String sUserName, long SessionID) {

        int aClickData[][]   = readLogFile(10, 4, BMApi.CLICK_LOG_FILE_NAME);
        String x = "";
        String y = "";
        String t = "";
        String click_duration = "";

        for(int j = 0; j < 10; j++) {
                x = x + aClickData[j][0] + " ";
                y = y + aClickData[j][1] + " ";
                t = t + aClickData[j][2] + " ";
                click_duration = click_duration + aClickData[j][3] + " ";
        }
        boolean isF1Saved = Database.storeClickFeatureSet(sUserName, x + y + t, SessionID, "FS1");
        boolean isF2Saved  = Database.storeClickFeatureSet(sUserName, click_duration, SessionID, "FS2");

        if(isF1Saved && isF2Saved)
        {
            String sUpdateQuery = "UPDATE featureset f, click c "
                    + "SET f.FeatureSetData = CONCAT(f.FeatureSetData, c.FeatureData) "
                    + "WHERE c.SessionID = "+SessionID+" AND f.SessionID = "+SessionID+" "
                    + "AND f.FeatureSetName = c.FeatureName "
                    + "AND f.UserID = c.UserID;";

            int iUpdatedRows = Database.executeUpdateQuery(sUpdateQuery);

            System.out.println("Rows updated: " + iUpdatedRows);
        }

        return isF1Saved && isF2Saved;
    }

    public static String[] extractClickFeaturesForValidation() {

        int aClickData[][]   = readLogFile(10, 4, BMApi.CLICK_LOG_FILE_NAME);
        String x = "";
        String y = "";
        String t = "";
        String click_duration = "";

        for(int j = 0; j < 10; j++) {
                x = x + aClickData[j][0] + " ";
                y = y + aClickData[j][1] + " ";
                t = t + aClickData[j][2] + " ";
                click_duration = click_duration + aClickData[j][3] + " ";
        }

        String features[] = {(x + y + t), click_duration};
        return features;
    }

    public static void smoothDataAndExtractFeatures() {
    	String[] users = Database.userList();

    	for(String userID : users) {
    		for(int i = 1; i <= 10; i++) {
    			//Fetch replication for user and gesture
    			double[][] smoothedData = BMApi.smooth(userID, i);
    			String features [] = BMApi.extractFeatures(smoothedData, userID, i+"", true);
    			System.out.println(features);
    		}
    	}
    }
}
