package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ClickDialog extends JFrame {
	static String sUserName;
	static long iSessionID;
        static boolean isValidation;
        static String aValidationdata[];
        

	ClickDialog(String sUserName, long iSessionID, boolean isValidation) {
            this.sUserName = sUserName;
            this.iSessionID = iSessionID;
            this.isValidation = isValidation;
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Container content = getContentPane();
            content.setLayout(new BorderLayout());
            final ClickGestureWindow oGestureWindow = new ClickGestureWindow();
            content.add(oGestureWindow, BorderLayout.CENTER);  

            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(50, 68));
            panel.setMinimumSize(new Dimension(32, 68));
            panel.setMaximumSize(new Dimension(32, 68));

            final JButton clearButton      = new JButton("Clear");
            final JButton createGestures   = new JButton("click");
            
            String sLabel = isValidation ? "Validate" : "Save";
            final JButton saveButton       = new JButton(sLabel);
            final JLabel replication       = new JLabel("");

            clearButton.setBackground(Color.ORANGE);
            saveButton.setBackground(Color.CYAN);
            replication.setBackground(Color.LIGHT_GRAY);
            saveButton.setEnabled(false);

            panel.add(clearButton);
            panel.add(createGestures);

            panel.add(saveButton);
            panel.add(replication);


            clearButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    oGestureWindow.iPoints = 0;
                    oGestureWindow.clear();
                }
            });

            saveButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(!ClickDialog.isValidation) {
                        boolean bIsSaved = BMApi.extractClickFeatures(ClickDialog.sUserName, ClickDialog.iSessionID);
                        String sMessage = bIsSaved ? "Saved succesfully!" : "Not saved successfully, please check SQL Error logs!";
                        JOptionPane.showMessageDialog(
                                    null,
                                    sMessage,
                                    "Behaviometrics",
                                    JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        int i = LVQ2.validation(aValidationdata, Behaviometrics.sGesture);
                    
                        String sMessage = i == -1 ? "User not detected!!"
                            : "User detected:\n" + io.rajkumarsutar.mousegesturedynamicsauth.database.Database.getUserByCategory(i);

                        
                        
                        JOptionPane.showMessageDialog(null, sMessage, "Behaviometrics", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            createGestures.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                    oGestureWindow.drawGesture(1);
                                    oGestureWindow.setEnabled(true);
                                    saveButton.setEnabled(true);
                            }
                    });

            content.add(panel, BorderLayout.WEST);
            setTitle("Behaviometrics::Click Phase");
            setSize(1000,600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setVisible(true);
        }
	
        public void closeWindow(){
            this.setVisible(false);
        }
        
	public static void main(String args[]) {
            new ClickDialog("TEST", 1l, false);
	}
}
