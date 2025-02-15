package es.florida.ae03.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Vista {

	private JFrame frmBlackjack;
	private JButton btnLoadCards, btnRegister, btnLogin, btnStart, btnSave, btnHallOfFame, btnCardCrupier01,
	btnCardPlayer01, btnNewCard, btnStand, btnLogout;
	private JComboBox<String> comboBoxSuit;
	JLabel lblCrupierScore, lblPlayerScore, lblCrupierHistory, lblPlayerHistory;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Vista window = new Vista();
//					window.frmBlackjack.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public Vista() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBlackjack = new JFrame();
		frmBlackjack.setTitle("21 Blackjack - DAM 24/25 Edition");
		frmBlackjack.setBounds(100, 100, 924, 670);
		frmBlackjack.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBlackjack.getContentPane().setLayout(null);
		
		btnLoadCards = new JButton("Load cards");
		btnLoadCards.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLoadCards.setBounds(21, 22, 100, 21);
		frmBlackjack.getContentPane().add(btnLoadCards);
		
		btnRegister = new JButton("Register");
		btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRegister.setBounds(131, 22, 100, 21);
		frmBlackjack.getContentPane().add(btnRegister);
		
		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLogin.setBounds(241, 22, 100, 21);
		frmBlackjack.getContentPane().add(btnLogin);
		
		JLabel lblNewLabel = new JLabel("Cards suit:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(351, 26, 68, 13);
		frmBlackjack.getContentPane().add(lblNewLabel);
		
		comboBoxSuit = new JComboBox<String>();
		comboBoxSuit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxSuit.setModel(new DefaultComboBoxModel<String>(new String[] {"ES", "FR"}));
		comboBoxSuit.setBounds(416, 22, 41, 21);
		frmBlackjack.getContentPane().add(comboBoxSuit);
		
		btnStart = new JButton("Start");
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnStart.setBounds(467, 22, 100, 21);
		frmBlackjack.getContentPane().add(btnStart);
		
		btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSave.setBounds(577, 22, 100, 21);
		frmBlackjack.getContentPane().add(btnSave);
		
		btnHallOfFame = new JButton("Hall of fame");
		btnHallOfFame.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnHallOfFame.setBounds(687, 22, 100, 21);
		frmBlackjack.getContentPane().add(btnHallOfFame);
		
		btnCardCrupier01 = new JButton("");
		btnCardCrupier01.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCardCrupier01.setBounds(21, 90, 300, 450);
		frmBlackjack.getContentPane().add(btnCardCrupier01);
		
		JLabel lblCrupier = new JLabel("CRUPIER");
		lblCrupier.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCrupier.setBounds(21, 67, 68, 13);
		frmBlackjack.getContentPane().add(lblCrupier);
		
		lblCrupierScore = new JLabel("TOTAL SCORE:");
		lblCrupierScore.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCrupierScore.setBounds(21, 550, 300, 13);
		frmBlackjack.getContentPane().add(lblCrupierScore);
		
		JLabel lblPlayer = new JLabel("PLAYER");
		lblPlayer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPlayer.setBounds(521, 67, 68, 13);
		frmBlackjack.getContentPane().add(lblPlayer);
		
		btnCardPlayer01 = new JButton("");
		btnCardPlayer01.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCardPlayer01.setBounds(521, 90, 300, 450);
		frmBlackjack.getContentPane().add(btnCardPlayer01);
		
		lblPlayerScore = new JLabel("TOTAL SCORE:");
		lblPlayerScore.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPlayerScore.setBounds(521, 550, 300, 13);
		frmBlackjack.getContentPane().add(lblPlayerScore);
		
		btnNewCard = new JButton("New card");
		btnNewCard.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewCard.setBounds(564, 601, 113, 21);
		frmBlackjack.getContentPane().add(btnNewCard);
		
		btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLogout.setBounds(797, 23, 100, 21);
		frmBlackjack.getContentPane().add(btnLogout);
		
		lblCrupierHistory = new JLabel("Score history:");
		lblCrupierHistory.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCrupierHistory.setBounds(21, 570, 300, 13);
		frmBlackjack.getContentPane().add(lblCrupierHistory);
		
		lblPlayerHistory = new JLabel("Score history:");
		lblPlayerHistory.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPlayerHistory.setBounds(521, 571, 300, 13);
		frmBlackjack.getContentPane().add(lblPlayerHistory);
		
		btnStand = new JButton("Stand");
		btnStand.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnStand.setBounds(687, 601, 113, 21);
		frmBlackjack.getContentPane().add(btnStand);
		
		frmBlackjack.setVisible(true);
	}

	public JFrame getFrmBlackjack() {
		return frmBlackjack;
	}

	public JButton getBtnLoadCards() {
		return btnLoadCards;
	}

	public JButton getBtnRegister() {
		return btnRegister;
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}

	public JButton getBtnStart() {
		return btnStart;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public JButton getBtnHallOfFame() {
		return btnHallOfFame;
	}

	public JButton getBtnCardCrupier01() {
		return btnCardCrupier01;
	}

	public JButton getBtnCardPlayer01() {
		return btnCardPlayer01;
	}

	public JButton getBtnNewCard() {
		return btnNewCard;
	}
	
	public JButton getBtnLogout() {
		return btnLogout;
	}
	
	public JButton getBtnStand() {
		return btnStand;
	}

	public JComboBox<String> getComboBoxSuit() {
		return comboBoxSuit;
	}

	public JLabel getLblCrupierScore() {
		return lblCrupierScore;
	}

	public JLabel getLblPlayerScore() {
		return lblPlayerScore;
	}
	
	public JLabel getLblCrupierHistory() {
		return lblCrupierHistory;
	}

	public JLabel getLblPlayerHistory() {
		return lblPlayerHistory;
	}
}
