package io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics;

/////////////////////////Dependencies///////////////////////////////////////////
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/////////////////////////Class Definition///////////////////////////////////////

/**
 * This class is used in creation on Gesture
 */
public class ValidationFrame {
    public static String sUserName = "";
    public static String sUserFileName = "";
    public static String sUserFolderName = "";
    public static String sGesture = "";

    /**
     * Main method
     *
     * @param args
     */
    public static void startGesture(boolean bIsValidation) {
        JFrame oFrame = new JFrame("User Authentication using Mouse Gesture Dynamics :: Validation");

        Container content = oFrame.getContentPane();
        content.setLayout(new BorderLayout());
        final GestureWindow oGestureWindow = new GestureWindow();
        content.add(oGestureWindow, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(50, 68));
        panel.setMinimumSize(new Dimension(32, 68));
        panel.setMaximumSize(new Dimension(32, 68));

        final JButton clearButton = new JButton("Clear");
        final JButton createGestures = new JButton("draw");
        final JButton saveButton = new JButton("Validate");
        final JLabel replication = new JLabel("");

        clearButton.setBackground(Color.ORANGE);
        saveButton.setBackground(Color.CYAN);
        replication.setBackground(Color.LIGHT_GRAY);

        createGestures.setEnabled(false);
        saveButton.setEnabled(false);

        panel.add(clearButton);
        panel.add(createGestures);

        createGestures.setEnabled(true);
        saveButton.setEnabled(true);
        sUserFolderName = "./Validation/" + UUID.randomUUID().toString() + "/";
        File oTempFile = new File(sUserFolderName);
        if (!oTempFile.exists()) {
            oTempFile.mkdirs();
        }

        panel.add(saveButton);
        panel.add(replication);

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oGestureWindow.iPoints = 0;
                oGestureWindow.clear();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (oGestureWindow.iPoints >= 100) {
                    clearButton.setEnabled(true);
                    BMApi api = new BMApi();

                    int aTrainingSet[][] = BMApi.readLogFile(oGestureWindow.iPoints, 3, BMApi.LOG_FILE_NAME);

                    // step 1 - center normalization
                    int aRowData[][] = oGestureWindow.centreNormalisation(aTrainingSet);

                    // step 3 - size normalization
                    int aSizeNormalisedGesture[][] = api.kMeans(aRowData, oGestureWindow.iPoints);
                    BMApi.writeLineToFile(sUserFileName, 1 + "", 1);

                    BMApi.writeArrayToFile(sUserFileName, aSizeNormalisedGesture, BMApi.TEMPLATE_SIZE);

                    double[][] aSmoothedData = Gesture.initializeGesture(sUserFileName);
                    String array[] = BMApi.extractFeatures(aSmoothedData, sUserName, sGesture, true);

                    int i = LVQ2.validation(array, sGesture);

                    System.out.println("User Detected: " + i);

                    String sMessage = i == -1 ? "User not detected!!"
                            : "User detected:\n"
                                    + io.rajkumarsutar.mousegesturedynamicsauth.database.Database.getUserByCategory(i);

                    JOptionPane.showMessageDialog(null, sMessage, "Behaviometrics", JOptionPane.INFORMATION_MESSAGE);

                }
            }
        });

        createGestures.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random r = new Random();
                int g = 1;//r.nextInt(BMCostants.SAMPLE_GESTURES.length);

                JOptionPane.showMessageDialog(null,
                        "Please draw Next Gesture : '" + BMCostants.SAMPLE_GESTURE_NAMES[g] + "'", "Behaviometrics",
                        JOptionPane.INFORMATION_MESSAGE);

                sGesture = BMCostants.SAMPLE_GESTURES[g];

                sUserFileName = sUserFolderName + "/" + System.currentTimeMillis() + ".log";
                oGestureWindow.setEnabled(true);
                saveButton.setEnabled(true);
            }
        });

        content.add(panel, BorderLayout.WEST);
        oFrame.setSize(1200, 563);
        oFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        oFrame.setVisible(true);
    }
}
