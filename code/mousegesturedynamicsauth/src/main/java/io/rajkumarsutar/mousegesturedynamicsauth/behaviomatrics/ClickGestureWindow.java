package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * This class is used to create a canvas where user can draw fee hand sketches
 *
 * @author
 */
class ClickGestureWindow extends JComponent{

    /////////////////////////Variables Declaration//////////////////////////////

	private static final long serialVersionUID = 6450945343564413543L;
	Image oImage;
    Graphics2D oGraphics2D;
    int iCurrentX, iCurrentY;
    long lMilliSecs;
    int iPoints = 0;
    int startX, startY;
    int endX, endY;

    static boolean SINGLE_CLICK_MODE_ON;
    static boolean DOUBLE_CLICK_MODE_ON;
    static int CLICK_MODE = 0;
    public static long START;
    public static long END;
    /////////////////////////Constructor Declaration////////////////////////////

    public ClickGestureWindow(){
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter(){
        	@Override
			public void mouseReleased(MouseEvent e)
        	{
        		endX = e.getX();
        		endY = e.getY();
        		endTimer();
        	}

        	@Override
			public void mousePressed(MouseEvent e)
        	{
        		startX = e.getX();
        		startY = e.getY();
        		startTimer();
        	}

        	@Override
			public void mouseClicked(MouseEvent e)
        	{
        		if(CLICK_MODE == e.getClickCount()  && iPoints < 10) {
        			iCurrentX = e.getX();
        			iCurrentY = e.getY();
        			File oFile = new File(BMApi.CLICK_LOG_FILE_NAME);
        			try {
                        FileWriter oFileWriter = new FileWriter(oFile.getName(), true);
                        BufferedWriter oBufferedWriter = new BufferedWriter(oFileWriter);
                        oBufferedWriter.write(
                                iCurrentX+" "+
                                iCurrentY+" "+
                                (System.currentTimeMillis()-lMilliSecs)+" "+
                                (END-START)+" "+
                                //startX+" "+
                                //startY+" "+
                                //endX+" "+
                                //endY+
                                "\n"
                        );
                        oBufferedWriter.close();
                    }
                    catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    }

        			int x = BMCostants.CLICK_POINTS[++iPoints][0];
        	    	int y = BMCostants.CLICK_POINTS[iPoints][1];
        			clearScreen();
        			showNextPoint(x, y);
        		} else {
        			clearScreen();

                                String sMessage = ClickDialog.isValidation
                                        ? "Please click on Validate!"
                                        : "Your session is Completed! Please save and close the Window!";

        			JOptionPane.showMessageDialog(
        					null,
        					sMessage,
        					"Behaviometrics",
        					JOptionPane.INFORMATION_MESSAGE
        			);
        		}
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
        File oFile = new File(BMApi.CLICK_LOG_FILE_NAME);
        try {
            PrintWriter oPrintWriter = new PrintWriter(oFile);
            oPrintWriter.write("");
            oPrintWriter.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }


    /**
     * This method is used to draw a gesture on screen
     *
     * @param aGestureData int[][]
     */
    public void drawGesture(int i)
    {
    	CLICK_MODE = i;
    	lMilliSecs    = System.currentTimeMillis();
    	clear();

    	int x = BMCostants.CLICK_POINTS[0][0];
    	int y = BMCostants.CLICK_POINTS[0][1];

    	showNextPoint(x, y);

    }

    public void showNextPoint(int x, int y) {
    	Color aColors[] = {Color.BLACK, Color.RED, Color.BLUE, Color.green, Color.YELLOW, Color.CYAN, Color.PINK};
    	if(oGraphics2D != null) {
    		Random oRandom = new Random();
    		int iColorIndex = oRandom.nextInt(7);
    		oGraphics2D.setColor(aColors[iColorIndex]);
    		oGraphics2D.fillOval(x, y, 5, 5);
    		repaint();
    	}

    }

    public void startTimer()
    {
    	START = System.currentTimeMillis();
    }

    public void endTimer()
    {
    	END = System.currentTimeMillis();
    }

}
