package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

/////////////////////////Dependencies///////////////////////////////////////////
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/////////////////////////Class Definition///////////////////////////////////////

/**
 * This class is used in creation on Gesture
 */
public class Behaviometrics{
    public static String sUserName       = "";
    public static String sUserFileName   = "";
    public static String sUserFolderName = "";
    public static int iReplicationNumber = 1;
    public static int iNextGesture       = 0;
    public static String sGesture        = "";
    public static long SessionID         = 0;
    public static boolean isValidation   = false;

   // public static final Rengine engine   = new Rengine(new String[] { "--no-save" }, false, null);

    public static void main(String[] args) {
        startGesture(false);
    }

    /**
     * Main method
     *
     * @param args
     */
    public static void startGesture(boolean bIsValidation){

        isValidation = bIsValidation;
        File oFile = new File(BMApi.LOG_FILE_NAME);
        try {
            PrintWriter oPrintWriter = new PrintWriter(oFile);
            oPrintWriter.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        String sTitle = isValidation
                ? "Behaviometrics :: Validation"
                : "Behaviometrics :: Gesture Creation";
        JFrame oFrame = new JFrame(sTitle);

        Container content = oFrame.getContentPane();
        content.setLayout(new BorderLayout());
        final GestureWindow oGestureWindow = new GestureWindow();
        content.add(oGestureWindow, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(50, 68));
        panel.setMinimumSize(new Dimension(32, 68));
        panel.setMaximumSize(new Dimension(32, 68));

        String sMessage = isValidation ? "Validate" : "Save" ;

        final JButton clearButton      = new JButton("Clear");
        final JButton createGestures   = new JButton("draw");
        final JButton saveButton       = new JButton(sMessage);
        final JButton createUserButton = new JButton("add user");
        final JLabel replication       = new JLabel("");

        clearButton.setBackground(Color.ORANGE);
        saveButton.setBackground(Color.CYAN);
        createUserButton.setBackground(Color.GREEN);
        replication.setBackground(Color.LIGHT_GRAY);

        createGestures.setEnabled(false);
        saveButton.setEnabled(false);

        panel.add(clearButton);
        panel.add(createGestures);
        if(!isValidation) {
            panel.add(createUserButton);

        }
        else {
            createGestures.setEnabled(true);
            saveButton.setEnabled(true);
            sUserFolderName = "./Validation/";

            sUserFolderName = "./User/" + sUserName;
            File oTempFile = new File(sUserFolderName);
            if(!oTempFile.exists()){
                oTempFile.mkdirs();
            }
        }
        panel.add(saveButton);
        panel.add(replication);

        clearButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                oGestureWindow.iPoints = 0;
                oGestureWindow.clear();
            }
        });

        saveButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){

                if(oGestureWindow.iPoints >= 100){
                clearButton.setEnabled(true);
                BMApi api = new BMApi();

                int aTrainingSet[][] = BMApi.readLogFile(oGestureWindow.iPoints, 3, BMApi.LOG_FILE_NAME);

                //step 1 - center normalization
                int aRowData[][] = oGestureWindow.centreNormalisation(aTrainingSet);

                //step 2 - Data pre-processing
                //aRowData = oGestureWindow.dataPreprocessing(aRowData, oGestureWindow.iPoints);

                //step 3 - size normalization
                int aSizeNormalisedGesture[][] = api.kMeans(aRowData, oGestureWindow.iPoints);

                replication.setText(iReplicationNumber + "");
                BMApi.writeLineToFile(sUserFileName, iReplicationNumber + "", iReplicationNumber);

                BMApi.writeArrayToFile(sUserFileName, aSizeNormalisedGesture, BMApi.TEMPLATE_SIZE);
                if(!isValidation) {
                if(iReplicationNumber == BMCostants.REPLICATIONS){

                    /*step 4 - Data Smoothing and outlier removal
                               (Outlier Removal using Percies criterion & Data Smoothing using Robust Weighted least Square Regression) */
                    double aSmoothedData[][] = null;
                    if(isValidation) {
                        aSmoothedData = Gesture.initializeGesture(sUserFileName);
                    } else {
                        System.out.println(sUserFileName);
                        //aSmoothedData = BMApi.smooth(engine, sUserFileName);
                    }

                    System.out.println(Arrays.deepToString(aSmoothedData));

                    //step 5 - feature extraction
                    BMApi.extractFeatures(aSmoothedData, sUserName, sGesture, false);

                    iReplicationNumber = 1;
                    if(iNextGesture < BMCostants.SAMPLE_GESTURES.length) {
                        JOptionPane.showMessageDialog(
                            null,
                            "Please draw Next Gesture : '" + BMCostants.SAMPLE_GESTURES[iNextGesture++] + "'",
                            "Behaviometrics" ,
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                    	JOptionPane.showMessageDialog(
                    		null,
                            "Now you will redirected to CLICK-PHASE",
                            "Behaviometrics" ,
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    	ClickDialog oClickDialog = new ClickDialog(sUserName, SessionID, isValidation);
                    }
                    replication.setText(iReplicationNumber + "");
                    sGesture = BMCostants.SAMPLE_GESTURES[iNextGesture-1];
                    sUserFileName = sUserFolderName + "/user_gesture_"+sGesture+".log";
                } else {
                    iReplicationNumber++;
                }
                }
                else {
                    double aSmoothedData[][] = null;
                    if(isValidation) {
                       aSmoothedData = Gesture.initializeGesture(sUserFileName);
                    } else {
                    //aSmoothedData = BMApi.smooth(engine, sUserFileName);
                    }
                    String array [] = BMApi.extractFeatures(aSmoothedData, sUserName, sGesture, true);

                    ClickDialog oClickDialog = new ClickDialog(sUserName, SessionID, isValidation);

                    String aClickFeatures[] = BMApi.extractClickFeaturesForValidation();
                    array[0] = array[0] + aClickFeatures[0];
                    array[1] = array[1] + aClickFeatures[1];

                    ClickDialog.aValidationdata = array;
                }
                }
                else{
                    oGestureWindow.clear();
                    JOptionPane.showMessageDialog(
                        null,
                        "Gesture Size is less than template size!",
                        "Behaviometrics" ,
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sUserName = JOptionPane.showInputDialog("Enter User ID (i.e. Mobile Number)");
                boolean bIsUniqueID = io.rajkumarsutar.mousegesturedynamicsauth.database.Database.checkValue("User", "Mobile", sUserName);
                if((!sUserName.equals("") || sUserName == null) && bIsUniqueID) {

                long lCurrentTime = BMApi.getSessionID();
                sUserFolderName = "./User/" + sUserName + "/" + lCurrentTime;
                File oFile = new File(sUserFolderName);
                if(!oFile.exists()){
                    oFile.mkdirs();
                    JOptionPane.showMessageDialog(
                    null,
                    "DIRECTORY FOR USER " + sUserName + " IS CREATED!" ,
                    "Behaviometrics" ,
                    JOptionPane.INFORMATION_MESSAGE
                    );
                    Behaviometrics.sUserName = sUserName;
                    Behaviometrics.SessionID = BMApi.getSessionID();
                    createGestures.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "DIRECTORY FOR USER " + sUserName + " IS ALREADY EXIST!" ,
                        "Behaviometrics" ,
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    Behaviometrics.sUserName = sUserName;
                    Behaviometrics.SessionID = BMApi.getSessionID();
                    createGestures.setEnabled(true);
                }
                }
                else {
                    JOptionPane.showMessageDialog(null
                    , "Incorrect User Name!!"
                    , "Behaviometrics"
                    , JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        createGestures.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random r = new Random();
                int g = r.nextInt(BMCostants.SAMPLE_GESTURES.length);
                String sMessage = !isValidation
                        ? "Please draw Next Gesture : '" + BMCostants.SAMPLE_GESTURES[iNextGesture++] + "'"
                        : "Please draw Next Gesture : '" + BMCostants.SAMPLE_GESTURES[g] + "'";

                if(iReplicationNumber == 1) {
                    JOptionPane.showMessageDialog(
                    null,
                    sMessage,
                    "Behaviometrics" ,
                    JOptionPane.INFORMATION_MESSAGE
                );
                }
                if(!isValidation) {
                sGesture = BMCostants.SAMPLE_GESTURES[iNextGesture-1];
                }
                else {
                 sGesture =    BMCostants.SAMPLE_GESTURES[g];
                }
                sUserFileName = !isValidation
                        ? sUserFolderName + "/user_gesture_"+sGesture+".log"
                        : sUserFolderName + "/" + System.currentTimeMillis() + ".log";
                oGestureWindow.setEnabled(true);
                saveButton.setEnabled(true);
                createUserButton.setEnabled(false);

            }
        });

        content.add(panel, BorderLayout.WEST);
        oFrame.setSize(1000,600);
        oFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        oFrame.setVisible(true);
    }
}

/////////////////////////Class Defination///////////////////////////////////////

/**
 * This class is used to create a canvas where user can draw fee hand sketches
 */
class GestureWindow extends JComponent{

    /////////////////////////Variables Declaration//////////////////////////////

    Image oImage;
    Graphics2D oGraphics2D;
    int iCurrentX, iCurrentY, iOldX, iOldY;
    long lMilliSecs;
    int iPoints = 0;
    int iMaxX, iMinX, iMaxY, iMinY;

    /////////////////////////Constructor Declaration////////////////////////////

    public GestureWindow(){
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter(){
        @Override
        public void mousePressed(MouseEvent e){

            iOldX = e.getX();
            iOldY = e.getY();
            iMaxX = iMinX = iOldX;
            iMaxY = iMinY = iOldY;
            lMilliSecs = System.currentTimeMillis();
            iPoints++;
            File oFile = new File(BMApi.LOG_FILE_NAME);
            try {
                FileWriter oFileWriter = new FileWriter(oFile.getName(), true);
                BufferedWriter oBufferedWriter = new BufferedWriter(oFileWriter);
                oBufferedWriter.write(iOldX+" "+ iOldY+" "+ 0+"\n");
                oBufferedWriter.close();
            }
            catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
        });
        addMouseMotionListener(new MouseMotionAdapter(){
        @Override
        public void mouseDragged(MouseEvent e){
            File oFile = new File(BMApi.LOG_FILE_NAME);
            iCurrentX = e.getX();
            iCurrentY = e.getY();

            if(iCurrentX > iMaxX)
                iMaxX = iCurrentX;
            if(iCurrentX < iMinX)
                iMinX = iCurrentX;
            if(iCurrentY > iMaxY)
                iMaxY = iCurrentY;
            if(iCurrentY < iMinY)
                iMinY = iCurrentY;

            iPoints++;

            try {
                FileWriter oFileWriter = new FileWriter(oFile.getName(), true);
                BufferedWriter oBufferedWriter = new BufferedWriter(oFileWriter);
                oBufferedWriter.write(
                        iCurrentX+" "+
                        iCurrentY+" "+
                        (System.currentTimeMillis()-lMilliSecs)+"\n"
                );
                oBufferedWriter.close();
            }
            catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
            if(oGraphics2D != null)
            oGraphics2D.drawLine(iOldX, iOldY, iCurrentX, iCurrentY);
            repaint();
            iOldX = iCurrentX;
            iOldY = iCurrentY;
        }
        });
    }

    /**
     * Overrides from JComponent
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g){
        if(oImage == null){
            oImage = createImage(getSize().width, getSize().height);
            oGraphics2D = (Graphics2D)oImage.getGraphics();
            oGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            clearScreen();
        }
        g.drawImage(oImage, 0, 0, null);
    }

    /**
     * This method is used to Create Screen
     *
     */
    public void clearScreen(){
        oGraphics2D.setPaint(Color.white);
        oGraphics2D.fillRect(0, 0, getSize().width, getSize().height);
        oGraphics2D.setPaint(Color.black);
        repaint();
    }

    /**
     * This method is used to clear the screen along with reset all variables
     *
     */
    public void clear(){
        clearScreen();
        File oFile = new File(BMApi.LOG_FILE_NAME);
        try {
            PrintWriter oPrintWriter = new PrintWriter(oFile);
            oPrintWriter.write("");
            oPrintWriter.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * This method is used to shift the gesture to the center of screen
     * @param aRowPointsData
     * @return int[][] 2-D integer array with
     */
    public int [][] centreNormalisation(int aRowPointsData[][]){
        int iCenterX = getSize().width/2;
        int iCenterY = getSize().height/2;
        int iDevX    = iCenterX - (iMinX + (iMaxX-iMinX)/2);
        int iDevY    = iCenterY - (iMinY + (iMaxY-iMinY)/2);

        int iLoopCounter = iPoints;
        clear();

        iOldX = aRowPointsData[0][0]+iDevX;
        iOldY = aRowPointsData[0][1]+iDevY;

        aRowPointsData[0][0] = iOldX;
        aRowPointsData[0][1] = iOldY;

        for(int i=1; i<iLoopCounter; i++){
            iCurrentX = aRowPointsData[i][0]+iDevX;
            iCurrentY = aRowPointsData[i][1]+iDevY;

            aRowPointsData[i][0] = iCurrentX;
            aRowPointsData[i][1] = iCurrentY;

            if(oGraphics2D != null)
            oGraphics2D.drawLine(iOldX, iOldY, iCurrentX, iCurrentY);
            repaint();

            iOldX = iCurrentX;
            iOldY = iCurrentY;
        }

        return aRowPointsData;
    }

    /**
     * This method is used to draw a gesture on screen
     *
     * @param aGestureData int[][]
     */
    public void drawGesture(int [][]aGestureData)
    {
    	clear();

        iOldX = aGestureData[0][0];
        iOldY = aGestureData[0][1];

        for(int i=1; i<100; i++){
            iCurrentX = aGestureData[i][0];
            iCurrentY = aGestureData[i][1];

            if(oGraphics2D != null)
            oGraphics2D.drawLine(iOldX, iOldY, iCurrentX, iCurrentY);
            repaint();

            iOldX = iCurrentX;
            iOldY = iCurrentY;
        }
    }

    /**
     * Removes data points with same time stamp
     *
     * @param aTrainingDataSet int[][]
     * @param iSize int
     * @return aPreprocessedDataset[][]
     */
    public int[][] dataPreprocessing(int[][] aTrainingDataSet, int iSize){
    	int iDataCount = 0;
    	for(int i=1; i<iSize; i++){
            if(aTrainingDataSet[i-1][2] == aTrainingDataSet[i][2]){
    		iDataCount++;
    		aTrainingDataSet[i-1][2] = 1000000000;
            }
    	}
    	iDataCount = iSize - iDataCount;

    	int aPreprocessedDataset[][] = new int[iDataCount][3];
    	int j=0;
    	for(int i=0; i<iSize; i++){

            if(aTrainingDataSet[i][2] != 1000000000){
                aPreprocessedDataset[j][0]   = aTrainingDataSet[i][0];
                aPreprocessedDataset[j][1]   = aTrainingDataSet[i][1];
                aPreprocessedDataset[j++][2] = aTrainingDataSet[i][2];
            }
    	}

    	return aPreprocessedDataset;
    }
}