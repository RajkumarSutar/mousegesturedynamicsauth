package io.rajkumarsutar.mousegesturedynamicsauth.gui;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics.BMCostants;


/////////////////////////Class Definition///////////////////////////////////////

/**
 * This class is used in creation on Gesture
 */
public class Practice{
    public static int iReplicationNumber = 1;
    public static int iNextGesture       = 0;
    public static String sGesture        = "";

    public static void main(String[] args) {
        startGestureCreationPractice();
    }

    /**
     * Main method
     * 
     * @param args 
     */
    public static void startGestureCreationPractice(){


        String sTitle =  "Behaviometrics :: Gesture Creation Practice";
        JFrame oFrame = new JFrame(sTitle);

        Container content = oFrame.getContentPane();
        content.setLayout(new BorderLayout());
        final PracticeGestureWindow oPracticeGestureWindow = new PracticeGestureWindow();
        content.add(oPracticeGestureWindow, BorderLayout.CENTER);  
        
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(50, 68));
        panel.setMinimumSize(new Dimension(32, 68));
        panel.setMaximumSize(new Dimension(32, 68));
        
        final JButton clearButton      = new JButton("Clear");
        final JButton createGestures   = new JButton("draw");
        final JButton saveButton       = new JButton("Save");
        final JLabel replication       = new JLabel("");
        
        clearButton.setBackground(Color.ORANGE);
        saveButton.setBackground(Color.CYAN);
        replication.setBackground(Color.LIGHT_GRAY);
        
        panel.add(clearButton);
        panel.add(createGestures);
        panel.add(saveButton);
        panel.add(replication);
        
        clearButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                oPracticeGestureWindow.iPoints = 0;
                oPracticeGestureWindow.clear();
            }
        });
        
        saveButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){

                if(oPracticeGestureWindow.iPoints >= 100){
                clearButton.setEnabled(true);


 
                replication.setText(iReplicationNumber + "");

                if(iReplicationNumber == BMCostants.REPLICATIONS){

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
                    }
                    replication.setText(iReplicationNumber + "");
                    sGesture = BMCostants.SAMPLE_GESTURES[iNextGesture-1];
                } else {
                    iReplicationNumber++;
                }
                }
                else{
                    oPracticeGestureWindow.clear();
                    JOptionPane.showMessageDialog(
                        null, 
                        "Gesture Size is less than template size!",
                        "Behaviometrics" ,
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
        
        
        createGestures.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(iReplicationNumber == 1) {
                    JOptionPane.showMessageDialog(
                    null, 
                    "Please draw Next Gesture : '" + BMCostants.SAMPLE_GESTURES[iNextGesture++],
                    "Behaviometrics" ,
                    JOptionPane.INFORMATION_MESSAGE
                );
                }
                sGesture = BMCostants.SAMPLE_GESTURES[iNextGesture-1];
                oPracticeGestureWindow.setEnabled(true);
                saveButton.setEnabled(true);

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
class PracticeGestureWindow extends JComponent{

    /////////////////////////Variables Declaration//////////////////////////////
    
    Image oImage;
    Graphics2D oGraphics2D;
    int iCurrentX, iCurrentY, iOldX, iOldY;
    long lMilliSecs;
    int iPoints = 0;
    int iMaxX, iMinX, iMaxY, iMinY;
    
    /////////////////////////Constructor Declaration////////////////////////////
    
    public PracticeGestureWindow(){
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter(){
        @Override
        public void mousePressed(MouseEvent e){

            iOldX = e.getX();
            iOldY = e.getY();
            iMaxX = iMinX = iOldX;
            iMaxY = iMinY = iOldY;
            lMilliSecs = System.currentTimeMillis();
        }
        });
        addMouseMotionListener(new MouseMotionAdapter(){
        @Override
        public void mouseDragged(MouseEvent e){
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
    }

}