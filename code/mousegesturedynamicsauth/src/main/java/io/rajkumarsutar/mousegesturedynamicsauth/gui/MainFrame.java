package io.rajkumarsutar.mousegesturedynamicsauth.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics.LVQ2;
import io.rajkumarsutar.mousegesturedynamicsauth.gui.gesture.creation.GestureCreator;

public class MainFrame extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = -8353174959097269098L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void startMainFrame() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setTitle("Biometric Authentication Using Mouse Gesture Dynamics");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1375, 765);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 1351, 708);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Biometric Authentication Using Mouse Gesture Dynamics");
		lblNewLabel.setFont(new Font("Century Gothic", Font.BOLD | Font.ITALIC, 30));
		lblNewLabel.setBounds(139, 10, 1090, 50);
		panel.add(lblNewLabel);

		JButton btnNewButton = new JButton("User Enrollment");
		btnNewButton.setBackground(new Color(255, 255, 255));
		btnNewButton.setForeground(new Color(192, 192, 192));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Enrollment.startEnrollment();
			}
		});
		btnNewButton.setBounds(64, 120, 321, 64);
		panel.add(btnNewButton);

		JButton btnPractice = new JButton("Practice");
		btnPractice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Practice.startGestureCreationPractice();
			}
		});
		btnPractice.setForeground(Color.LIGHT_GRAY);
		btnPractice.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnPractice.setBackground(Color.WHITE);
		btnPractice.setBounds(64, 207, 321, 64);
		panel.add(btnPractice);

		JButton btnDataAcquisition = new JButton("Data Acquisition");
		btnDataAcquisition.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GestureCreator.startGestureCreator();
			}
		});
		btnDataAcquisition.setForeground(Color.LIGHT_GRAY);
		btnDataAcquisition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnDataAcquisition.setBackground(Color.WHITE);
		btnDataAcquisition.setBounds(64, 294, 321, 64);
		panel.add(btnDataAcquisition);

		JButton btnDataPreparationAnd = new JButton("Data Preparation and Training");
		btnDataPreparationAnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LVQ2.heirarchicalTraining();
			}
		});
		btnDataPreparationAnd.setToolTipText("Data Preparation and Training");
		btnDataPreparationAnd.setForeground(Color.LIGHT_GRAY);
		btnDataPreparationAnd.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnDataPreparationAnd.setBackground(Color.WHITE);
		btnDataPreparationAnd.setBounds(64, 381, 321, 64);
		panel.add(btnDataPreparationAnd);

		JButton btnValidation = new JButton("Validation");
		btnValidation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				io.rajkumarsutar.mousegesturedynamicsauth.behaviomatrics.ValidationFrame.startGesture(true);
			}
		});
		btnValidation.setForeground(Color.LIGHT_GRAY);
		btnValidation.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnValidation.setBackground(Color.WHITE);
		btnValidation.setBounds(64, 472, 321, 64);
		panel.add(btnValidation);
	}
}
