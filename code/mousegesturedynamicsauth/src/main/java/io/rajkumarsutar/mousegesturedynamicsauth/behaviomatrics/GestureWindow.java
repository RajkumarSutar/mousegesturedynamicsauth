package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JComponent;


/**
 * This class is used to create a canvas where user can draw fee hand sketches
 */
public class GestureWindow extends JComponent {

    /////////////////////////Variables Declaration//////////////////////////////

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
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

	public Image getoImage() {
		return oImage;
	}

	public void setoImage(Image oImage) {
		this.oImage = oImage;
	}

	public Graphics2D getoGraphics2D() {
		return oGraphics2D;
	}

	public void setoGraphics2D(Graphics2D oGraphics2D) {
		this.oGraphics2D = oGraphics2D;
	}

	public int getiCurrentX() {
		return iCurrentX;
	}

	public void setiCurrentX(int iCurrentX) {
		this.iCurrentX = iCurrentX;
	}

	public int getiCurrentY() {
		return iCurrentY;
	}

	public void setiCurrentY(int iCurrentY) {
		this.iCurrentY = iCurrentY;
	}

	public int getiOldX() {
		return iOldX;
	}

	public void setiOldX(int iOldX) {
		this.iOldX = iOldX;
	}

	public int getiOldY() {
		return iOldY;
	}

	public void setiOldY(int iOldY) {
		this.iOldY = iOldY;
	}

	public long getlMilliSecs() {
		return lMilliSecs;
	}

	public void setlMilliSecs(long lMilliSecs) {
		this.lMilliSecs = lMilliSecs;
	}

	public int getiPoints() {
		return iPoints;
	}

	public void setiPoints(int iPoints) {
		this.iPoints = iPoints;
	}

	public int getiMaxX() {
		return iMaxX;
	}

	public void setiMaxX(int iMaxX) {
		this.iMaxX = iMaxX;
	}

	public int getiMinX() {
		return iMinX;
	}

	public void setiMinX(int iMinX) {
		this.iMinX = iMinX;
	}

	public int getiMaxY() {
		return iMaxY;
	}

	public void setiMaxY(int iMaxY) {
		this.iMaxY = iMaxY;
	}

	public int getiMinY() {
		return iMinY;
	}

	public void setiMinY(int iMinY) {
		this.iMinY = iMinY;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}