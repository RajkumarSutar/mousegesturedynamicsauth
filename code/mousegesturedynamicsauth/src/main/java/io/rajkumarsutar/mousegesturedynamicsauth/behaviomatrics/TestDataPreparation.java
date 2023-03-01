package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import io.rajkumarsutar.mousegesturedynamicsauth.database.Database;

public class TestDataPreparation {
    public static void main(String[] args) throws IOException {
        String dir = "F:\\Projects\\RajkumarProject\\mousegesturedynamicsauth\\code\\mousegesturedynamicsauth\\TrainingDataSet\\";
        Map<String, Map<String, List<String>>> data = new HashMap<>();
        List<String> users = new ArrayList<>();
        Files.list(Path.of(dir)).forEach(path -> {
            data.put(path.getFileName().toString(), new HashMap<>());
            users.add(path.getFileName().toString());
        });

        for (String user : users) {
            List<String> sessions = new ArrayList<>();
            String temp = dir + user;
            Files.list(Path.of(temp)).forEach(path -> {
                sessions.add(path.getFileName().toString());
            });

            List<Path> paths = new ArrayList<>();
            for (String session : sessions) {
                String tempSession = dir + user + "\\" + session;
                Files.list(Path.of(tempSession)).forEach(path -> {
                    paths.add(path);
                });
            }

            paths.forEach(path -> {

                int gesture = Integer.parseInt(path.getFileName().toString());

                try {
                    Files.list(path).forEach(path1 -> {
                        int replication = Integer.parseInt(path1.getFileName().toString());

                        try {
                            Path pp = Paths.get(path1.toString(), "data.log");

                            List<String> lines = Files.readAllLines(pp).stream().filter(p -> !"0 0 0".equals(p))
                                    .collect(Collectors.toList());
                            if (!lines.isEmpty()) {

                                int[][] rawData = new int[lines.size()][3];

                                int i = 0;
                                for (String line : lines) {
                                    String[] parts = line.split(" ");
                                    rawData[i][0] = Integer.parseInt(parts[0]);
                                    rawData[i][1] = Integer.parseInt(parts[1]);
                                    rawData[i][2] = Integer.parseInt(parts[2]);
                                    i++;
                                }

                                int[][] centreNormalizedData = centreNormalisation(rawData);

                                int[][] sizeNormalizedData = kMeans(centreNormalizedData, centreNormalizedData.length);

                                if (!Database.insertUserReplication(user, gesture, sizeNormalizedData)) {
                                    System.out
                                            .println(user + " --- " + gesture + " --- " + replication + pp.toString());
                                }
                            } else {
                                System.out.println(pp.toString());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }

    }

    /**
     * This method is used to shift the gesture to the center of screen
     *
     * @param aRowPointsData
     * @return int[][] 2-D integer array with //1150, 563
     */
    public static int[][] centreNormalisation(int aRowPointsData[][]) {
        int iCenterX = 1150 / 2;
        int iCenterY = 563 / 2;

        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        for (int[] row : aRowPointsData) {
            x.add(row[0]);
            y.add(row[1]);
        }

        int iMinX = Collections.min(x);
        int iMinY = Collections.min(y);
        int iMaxX = Collections.max(x);
        int iMaxY = Collections.max(y);

        int iOldX = aRowPointsData[0][0];
        int iOldY = aRowPointsData[0][0];

        int iCurrentX = 0;
        int iCurrentY = 0;

        int iDevX = iCenterX - (iMinX + (iMaxX - iMinX) / 2);
        int iDevY = iCenterY - (iMinY + (iMaxY - iMinY) / 2);

        int iLoopCounter = aRowPointsData.length;

        iOldX = aRowPointsData[0][0] + iDevX;
        iOldY = aRowPointsData[0][1] + iDevY;

        aRowPointsData[0][0] = iOldX;
        aRowPointsData[0][1] = iOldY;

        for (int i = 1; i < iLoopCounter; i++) {
            iCurrentX = aRowPointsData[i][0] + iDevX;
            iCurrentY = aRowPointsData[i][1] + iDevY;

            aRowPointsData[i][0] = iCurrentX;
            aRowPointsData[i][1] = iCurrentY;

            iOldX = iCurrentX;
            iOldY = iCurrentY;
        }

        return aRowPointsData;
    }

    /**
     * Convert row data into 100 points is points are more than 100
     *
     * @param aRowData
     * @param n
     * @return
     */
    public static int[][] kMeans(int aRowData[][], int n) {

        // template size
        int iTotalClusters = 64;
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
                    double dDistance = Long.MAX_VALUE;
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
     * This Function returns the distance between to Points on Graph using Euclidean
     * Distance formula *
     *
     * @param x1 int X-Cordinate of 1st Point
     * @param y1 int Y-Cordinate of 1st Point
     * @param x2 int X-Cordinate of 2nd Point
     * @param y2 int Y-Cordinate of 2nd Point
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
     * @param sCluster String space separated clusters numbers
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
}
