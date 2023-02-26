/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

import static io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics.BMApi.distanceFromOrigin;

import java.io.File;
import java.util.Arrays;

public class test {
    //public static final Rengine engine   = new Rengine(new String[] { "--no-save" }, false, null);

    public static void main(String args[]) {

        String d ="\\";
        File oFolder = new File(d);
        File aFiles[] = oFolder.listFiles();
        for(File  oUserFile : aFiles) {
        //File oUserFile = new File(s);
            String sUserName = oUserFile.getName();
        File oGestureFiles[] = oUserFile.listFiles();
        System.out.println(sUserName + " in progress");


        for(int i = 0; i<5; i++) {
            File oGestureFile = oGestureFiles[i];
            String UserFile = d + oUserFile.getName()+"\\" + oGestureFile.getName();
            System.out.println(UserFile);


            double aSmoothedData[][] = new double[100000][100000];// BMApi.smooth(engine, UserFile);

            String sGesture = oGestureFile.getName().substring(13, 14);
            extractFeatures(aSmoothedData, sUserName, sGesture, false, 1476003947364L);

            System.out.println(sGesture + " done");
        }
        }
    }



    public static String [] extractFeatures(double[][] aSmoothedData, String sUserName, String sGesture, boolean isValidation, long klong) {
            int m = isValidation == true
                    ? 1 : BMCostants.REPLICATIONS;
            String [] array;
            int n = BMApi.TEMPLATE_SIZE;
            String sFeatureFolderName = "./User/" + sUserName + "/Features/" + sGesture + "/";
            File ofile = new File(sFeatureFolderName);
            ofile.mkdirs();
            int size = BMCostants.REPLICATIONS * BMApi.TEMPLATE_SIZE;
                //System.out.println("Loop-");

            for (int i = 0; i < m; i++) {
                  //              System.out.println("Loop-" + i);

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
                        io.rajkumarsutar.mousegesturedynamicsauth.database.Database.storeFeatureSet(sUserName, sFeatureSet1, klong, "FS1", sGesture);
                        io.rajkumarsutar.mousegesturedynamicsauth.database.Database.storeFeatureSet(sUserName, sFeatureSet2, klong, "FS2", sGesture);
                        io.rajkumarsutar.mousegesturedynamicsauth.database.Database.storeFeatureSet(sUserName, sFeatureSet3, klong, "FS3", sGesture);
                        io.rajkumarsutar.mousegesturedynamicsauth.database.Database.storeFeatureSet(sUserName, sFeatureSet4, klong, "FS4", sGesture);
                    }

                    String array1[] = {sFeatureSet1, sFeatureSet2, sFeatureSet3, sFeatureSet4};
                    if(isValidation == true) {
                        return array1;
                    }
                    System.out.println("Replications: " + i + " Completed!");
            }
            return null;
    }


}
