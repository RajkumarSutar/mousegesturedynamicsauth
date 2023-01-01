package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

/**
 *
 */
public class Feature {

    private String sUserID;
    private String sGesture;
    private String sFeatureData;
    private String sModuleName;
    private String sFeatureName;
    private String sFeatureTime;
    private int iFeatureID;


    public String getsModuleName() {
        return sModuleName;
    }


    public void setsModuleName(String sModuleName) {
        this.sModuleName = sModuleName;
    }
    

    public Feature()
    {    
    }


    public String getsUserID() {
        return sUserID;
    }


    public void setsUserID(String sUserID) {
        this.sUserID = sUserID;
    }


    public String getsGesture() {
        return sGesture;
    }


    public void setsGesture(String sGesture) {
        this.sGesture = sGesture;
    }


    public String getsFeatureData() {
        return sFeatureData;
    }

    public void setsFeatureData(String sFeatureData) {
        this.sFeatureData = sFeatureData;
    }


    public String getsFeatureName() {
        return sFeatureName;
    }


    public void setsFeatureName(String sFeatureName) {
        this.sFeatureName = sFeatureName;
    }


    public String getsFeatureTime() {
        return sFeatureTime;
    }


    public void setsFeatureTime(String sFeatureTime) {
        this.sFeatureTime = sFeatureTime;
    }


    public int getiFeatureID() {
        return iFeatureID;
    }


    public void setiFeatureID(int iFeatureID) {
        this.iFeatureID = iFeatureID;
    }
}
