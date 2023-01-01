/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.rajkumarsutar.mousegesturedynamicsauth.gui;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BehaviourBiometrics extends JFrame{
	private static final long serialVersionUID = 2244316959263395810L;
	static JPanel jp = new JPanel();
    static Menu menu;
    static Container oContainer;
    
    private static BehaviourBiometrics INSTANCE;

    private BehaviourBiometrics() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Biometric Authentication using Mouse Gesture Dynamics");
        menu = new Menu();
        jp.add(menu);
        oContainer = getContentPane();
        oContainer.add(jp);
        this.setSize(800, 600);
        this.setVisible(true);
    }

    public static void start() {
        if(INSTANCE == null) {
        	INSTANCE = new BehaviourBiometrics();
        }
        INSTANCE.setVisible(true);
    }
    
    public static BehaviourBiometrics get() {
    	if(INSTANCE == null) {
        	INSTANCE = new BehaviourBiometrics();
        }
        return INSTANCE;
    }
}
