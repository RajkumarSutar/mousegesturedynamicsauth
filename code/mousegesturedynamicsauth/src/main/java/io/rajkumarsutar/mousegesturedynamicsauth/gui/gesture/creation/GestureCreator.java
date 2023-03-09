package io.rajkumarsutar.mousegesturedynamicsauth.gui.gesture.creation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics.BMApi;
import io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics.BMCostants;
import io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics.GestureWindow;
import io.rajkumarsutar.mousegesturedynamicsauth.database.Database;
import io.rajkumarsutar.mousegesturedynamicsauth.database.User;

public class GestureCreator extends JFrame {

	private static final long serialVersionUID = -1844257866567799099L;

	private JPanel contentPane;
	private JTextField textField;
	private JPanel panel_1;
	private JPanel panel_2;
	private JButton practiceButton;
	private JButton startButton;
	private JButton saveButton;
	private GestureWindow gestureWindow;
	private Map<Integer, Integer> replication;

	private User user;

	private JButton sShapeButton;
	private JButton oShapeButton;
	private JButton cShapeButton;
	private JButton MShapeButton;
	private JButton nShapeButton;
	private JButton zShapeButton;
	private JButton eightShapeButton;
	private JButton lShapeButton;
	private JButton wShapeButton;
	private JButton sixShapeButton;

	private int gestureID;
	private String gestureName;
	private String gestureFileName;
	private int replicationNumber;

	JLabel lblNewLabel_2;

	private String sessionID;


	public static void startGestureCreator() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GestureCreator frame = new GestureCreator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GestureCreator() {
		setTitle("Gesture Creation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sessionID = UUID.randomUUID().toString();
		setBounds(100, 100, 1253, 769);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 107, 69, 615);
		contentPane.add(panel);
		panel.setLayout(null);

		sShapeButton = new JButton("S");
		sShapeButton.setBackground(new Color(192, 192, 192));
		sShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGestureDetails(1, "S");
			}
		});
		sShapeButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		sShapeButton.setBounds(10, 47, 45, 45);
		panel.add(sShapeButton);

		oShapeButton = new JButton("O");
		oShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGestureDetails(2, "O");
			}
		});
		oShapeButton.setBackground(new Color(192, 192, 192));
		oShapeButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		oShapeButton.setBounds(10, 102, 45, 45);
		panel.add(oShapeButton);

		cShapeButton = new JButton("C");
		cShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGestureDetails(3, "C");
			}
		});
		cShapeButton.setBackground(new Color(192, 192, 192));
		cShapeButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cShapeButton.setBounds(10, 157, 45, 45);
		panel.add(cShapeButton);

		MShapeButton = new JButton("M");
		MShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGestureDetails(4, "M");
			}
		});
		MShapeButton.setBackground(new Color(192, 192, 192));
		MShapeButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		MShapeButton.setBounds(10, 212, 45, 45);
		panel.add(MShapeButton);

		nShapeButton = new JButton("N");
		nShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGestureDetails(5, "N");
			}
		});
		nShapeButton.setBackground(new Color(192, 192, 192));
		nShapeButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		nShapeButton.setBounds(10, 267, 45, 45);
		panel.add(nShapeButton);

		zShapeButton = new JButton("Z");
		zShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGestureDetails(6, "Z");
			}
		});
		zShapeButton.setBackground(new Color(192, 192, 192));
		zShapeButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		zShapeButton.setBounds(10, 322, 45, 45);
		panel.add(zShapeButton);

		eightShapeButton = new JButton("8");
		eightShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGestureDetails(7, "8");
			}
		});
		eightShapeButton.setBackground(new Color(192, 192, 192));
		eightShapeButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		eightShapeButton.setBounds(10, 377, 45, 45);
		panel.add(eightShapeButton);

		lShapeButton = new JButton("L");
		lShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGestureDetails(8, "L");
			}
		});
		lShapeButton.setBackground(new Color(192, 192, 192));
		lShapeButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lShapeButton.setBounds(10, 432, 45, 45);
		panel.add(lShapeButton);

		wShapeButton = new JButton("W");
		wShapeButton.setBackground(new Color(192, 192, 192));
		wShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGestureDetails(9, "W");
			}
		});
		wShapeButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		wShapeButton.setBounds(10, 487, 45, 45);
		panel.add(wShapeButton);

		sixShapeButton = new JButton("6");
		sixShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGestureDetails(10, "6");
			}
		});
		sixShapeButton.setBackground(new Color(192, 192, 192));
		sixShapeButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		sixShapeButton.setBounds(10, 542, 45, 45);
		panel.add(sixShapeButton);

		JLabel lblNewLabel_1 = new JLabel("Gestures");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 8));
		lblNewLabel_1.setBounds(10, 10, 49, 27);
		panel.add(lblNewLabel_1);

		panel_1 = new JPanel();
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_1.setBounds(10, 10, 1229, 91);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		textField = new JTextField();
		textField.setBounds(189, 26, 332, 33);
		panel_1.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Enter Mobile Number");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(40, 24, 155, 33);
		panel_1.add(lblNewLabel);

		JButton btnNewButton = new JButton("Login");
		btnNewButton.setBackground(new Color(192, 192, 192));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionPerform();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.setBounds(545, 20, 85, 40);
		panel_1.add(btnNewButton);

		panel_2 = new JPanel();
		panel_2.setBounds(89, 107, 1150, 563);
		panel_2.setLayout(new BorderLayout(0, 0));
		gestureWindow = new GestureWindow();
		//gestureWindow.setEnabled(false);
		panel_2.add(gestureWindow);
		contentPane.add(panel_2);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(89, 680, 1150, 42);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		practiceButton = new JButton("Practice");
		practiceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startPractice();
			}
		});
		practiceButton.setBackground(new Color(192, 192, 192));
		practiceButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		practiceButton.setBounds(748, 0, 108, 32);
		panel_3.add(practiceButton);

		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGestureGestureCreation();
			}
		});
		startButton.setBackground(new Color(192, 192, 192));
		startButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		startButton.setBounds(892, 0, 108, 32);
		panel_3.add(startButton);

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveGesture();
			}
		});
		saveButton.setBackground(new Color(192, 192, 192));
		saveButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		saveButton.setBounds(1032, 0, 108, 32);
		panel_3.add(saveButton);

		lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_2.setBounds(10, 11, 679, 21);
		panel_3.add(lblNewLabel_2);
	}

	private void saveGesture() {
		BMApi api = new BMApi();

        int trainingSet[][] = BMApi.readLogFile(gestureWindow.getiPoints(), 3, BMApi.LOG_FILE_NAME);

        BMApi.writeArrayToFile(getTrainingDataSetFileName(), trainingSet, trainingSet.length);

        //step 1 - center normalization
        int aRowData[][] = gestureWindow.centreNormalisation(trainingSet);

        //step 2 - size normalization
        int aSizeNormalisedGesture[][] = api.kMeans(aRowData, gestureWindow.getiPoints());

        System.out.println(Arrays.deepToString(aSizeNormalisedGesture));

        BMApi.writeLineToFile(gestureFileName, replicationNumber + "", replicationNumber);
        BMApi.writeArrayToFile(gestureFileName, aSizeNormalisedGesture, BMApi.TEMPLATE_SIZE);

        if(Database.insertUserReplication(user.getMobile(), gestureID, aSizeNormalisedGesture)) {
        	replication.put(gestureID, replicationNumber);
            replicationNumber++;
            lblNewLabel_2.setText("Current Gesture: " + this.gestureName + " | Total Replications: "
    				+ (replication.containsKey(this.gestureID) ? replication.get(this.gestureID) : 0));

            try {
    			Thread.sleep(3000l);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
        }
        gestureWindow.clear();
        practiceButton.setEnabled(false);
        if(replicationNumber < BMCostants.REPLICATIONS) {
        	JOptionPane.showMessageDialog(
                this,
                "Draw Next Replication " + replicationNumber,
                "Gesture Creation" ,
                JOptionPane.INFORMATION_MESSAGE);
        } else {
        	this.repaint();
        }
	}

	private String getTrainingDataSetFileName() {

		String fileName =  "user" + File.separator + "TrainingDataSet" + File.separator
				+ user.getMobile() + File.separator + sessionID + File.separator + gestureID + File.separator
				+ replicationNumber + File.separator;

		try {
			Files.createDirectories(Path.of(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		fileName += "data.log";

		return fileName;
	}

	private void actionPerform() {
		user = Database.isMobileNumberExists(textField.getText());
		if(Objects.isNull(user)) {
			JOptionPane.showMessageDialog(this,
					"Invalid Mobile Number '"+textField.getText()+"'! Please complete enrollment process before creating gestures");
		} else {
			panel_1.removeAll();
			panel_1.repaint();
			JLabel lblNewLabel = new JLabel("User Name: " + user.getUserName() + " | Mobile: " + user.getMobile() + " | Profession: " + user.getProfession());
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblNewLabel.setBounds(40, 24, 1000, 33);
			panel_1.add(lblNewLabel);

			replication = Database.getUserGestureReplicationCount(user.getMobile());

			System.out.println(user);
			System.out.println(replication);

			for(Map.Entry<Integer, Integer> entry : replication.entrySet()) {
				switch(entry.getKey()) {
					case 1:
						disableIfReplicationsPresent(sShapeButton, entry.getValue());
						break;
					case 2:
						disableIfReplicationsPresent(oShapeButton, entry.getValue());
						break;
					case 3:
						disableIfReplicationsPresent(cShapeButton, entry.getValue());
						break;
					case 4:
						disableIfReplicationsPresent(MShapeButton, entry.getValue());
						break;
					case 5:
						disableIfReplicationsPresent(nShapeButton, entry.getValue());
						break;
					case 6:
						disableIfReplicationsPresent(zShapeButton, entry.getValue());
						break;
					case 7:
						disableIfReplicationsPresent(eightShapeButton, entry.getValue());
						break;
					case 8:
						disableIfReplicationsPresent(lShapeButton, entry.getValue());
						break;
					case 9:
						disableIfReplicationsPresent(wShapeButton, entry.getValue());
						break;
					case 10:
						disableIfReplicationsPresent(sixShapeButton, entry.getValue());
						break;
				}
			}
		}
	}

	private void disableIfReplicationsPresent(JButton button, int value) {
		System.out.println(":::::" + value);
		if(BMCostants.REPLICATIONS == value) {
			button.setEnabled(false);
		}
	}

	private void startPractice() {
		if("Practice".equals(practiceButton.getText())) {
			//gestureWindow.setEnabled(true);
			practiceButton.setText("Clear");
			startButton.setEnabled(false);
			saveButton.setEnabled(false);
		} else {
			gestureWindow.clearScreen();
			//gestureWindow.setEnabled(false);
			practiceButton.setText("Practice");
			startButton.setEnabled(true);
			saveButton.setEnabled(true);
		}
	}

	private void startGestureGestureCreation() {
		if("Start".equalsIgnoreCase(startButton.getText())) {
			JOptionPane.showMessageDialog(
                    this,
                    "Draw Next Gesture '" + gestureName + "'",
                    "Gesture Creation" ,
                    JOptionPane.INFORMATION_MESSAGE);
			//gestureWindow.setEnabled(true);
			//startButton.setText("Clear");
			practiceButton.setEnabled(false);
			this.replicationNumber = replication.containsKey(this.gestureID) ? replication.get(this.gestureID) + 1 : 1;
			this.gestureFileName = "user" + File.separator + user.getMobile() + File.separator + sessionID
					+ File.separator + "user_gesture_" + gestureName + "_" + this.replicationNumber +".log";

			File oFile = new File("user" + File.separator + user.getMobile() + File.separator + sessionID);
            if(!oFile.exists()){
                oFile.mkdirs();
                JOptionPane.showMessageDialog(
	                this,
	                "DIRECTORY FOR USER " + user.getUserName() + " IS CREATED!" ,
	                "Behaviometrics" ,
	                JOptionPane.INFORMATION_MESSAGE
	                );

            }
		}
	}

	private void updateGestureDetails(int gestureID, String gestureName) {
		this.gestureID = gestureID;
		this.gestureName = gestureName;

		this.repaint();
		lblNewLabel_2.setText("Current Gesture: " + this.gestureName + " | Total Replications: "
				+ (replication.containsKey(this.gestureID) ? replication.get(this.gestureID) : 0));
	}

	public int getGestureID() {
		return gestureID;
	}

	public void setGestureID(int gestureID) {
		this.gestureID = gestureID;
	}

	public String getGestureName() {
		return gestureName;
	}

	public void setGestureName(String gestureName) {
		this.gestureName = gestureName;
	}
}
