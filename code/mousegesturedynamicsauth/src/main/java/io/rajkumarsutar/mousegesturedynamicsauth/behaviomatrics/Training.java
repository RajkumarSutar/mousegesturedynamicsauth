package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

import java.io.File;
import java.util.ArrayList;

public class Training {



    public static ArrayList<String> scanUserDirectory() {
        ArrayList<String> oUserList = listFolders(new File("User"), true);
        return oUserList;
    }

    public static ArrayList<String> listFolders(File file, boolean bIsRecursive){
        ArrayList<String> oFolders = new ArrayList<>();

        File [] aFiles = file.listFiles();

        for(File oFile: aFiles){

            if(oFile.isDirectory()){
                if(bIsRecursive) {
                    oFolders.addAll(listFolders(oFile, bIsRecursive));
                }
                oFolders.add(oFile.getName());
                System.out.println(oFile.getName());
            }
        }
        return oFolders;
    }

    public static double[] stringToDoubleArray(String sFeatureData) {
        String aStringData[] = sFeatureData.split(" ");
        double aFeatureData[] = new double[aStringData.length];

        for(int i = 0; i < aStringData.length; i++) {
            aFeatureData[i] = Double.parseDouble(aStringData[i]);
        }

        return aFeatureData;
    }
/*
    public static void main(String[] args) {
        //String s[]= userList();
        //System.out.println(Arrays.toString(s));
        double d = 100 + NaN;
        System.out.println(d);
        String sk = "10.12 10.2 101E12 10.1 Infinity NaN";
        double l[] = stringToDoubleArray(sk);
        //String l[] = sk.split(" ");

        System.out.println(Arrays.toString(l));

    }
*/
}
