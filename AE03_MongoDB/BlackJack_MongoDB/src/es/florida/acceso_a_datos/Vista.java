package es.florida.acceso_a_datos;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Choice;

/**
 * Clase Vista que representa la interfaz gráfica principal de la aplicación
 * Blackjack.
 * 
 * Esta clase proporciona los elementos visuales y componentes necesarios para
 * que el usuario pueda interactuar con el juego. Incluye botones, etiquetas, y
 * áreas para mostrar las cartas y las puntuaciones del jugador y el crupier.
 */
public class Vista {

	private JFrame frame;
	private JButton btnLoadCards;
	private JButton btnRegister;
	private JButton btnLogin;
	private JButton btnStart;
	private JButton btnSave;
	private JButton btnHallOfFame;
	private JButton btnLogout;
	private JButton btnNewCard;
	private JButton btnStand;
	private JLabel lblTotalScoreCrupier;
	private JLabel lblScoreHistoryCrupier;
	private JLabel lblTotalScorePlayer;
	private JLabel lblScoreHistoryPlayer;
	private JLabel lblMuestraCartaCrupier;
	private JLabel lblMuestraCartaPlayer;
	private JLabel lblPlayer;
	private Choice choiceCards;

	/**
	 * Constructor de la clase Vista.
	 * 
	 * Inicializa todos los componentes gráficos de la interfaz y configura su
	 * disposición dentro del marco principal.
	 */
	public Vista() {
		initialize();
	}

	/**
	 * Método que inicializa los componentes de la interfaz gráfica.
	 * 
	 * Configura la ventana principal y añade todos los botones, etiquetas y
	 * componentes.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1102, 705);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblSelectCards = new JLabel("Cards suit:");
		lblSelectCards.setFont(new Font("Arial", Font.PLAIN, 13));
		lblSelectCards.setBounds(426, 25, 69, 32);
		frame.getContentPane().add(lblSelectCards);

		choiceCards = new Choice();
		choiceCards.setBounds(501, 30, 46, 32);
		frame.getContentPane().add(choiceCards);
		choiceCards.add("ES");
		choiceCards.add("FR");

		btnLoadCards = new JButton("Load Cards");
		btnLoadCards.setFont(new Font("Arial", Font.PLAIN, 13));
		btnLoadCards.setBounds(10, 25, 112, 32);
		frame.getContentPane().add(btnLoadCards);

		btnRegister = new JButton("Register");
		btnRegister.setFont(new Font("Arial", Font.PLAIN, 13));
		btnRegister.setBounds(140, 25, 112, 32);
		frame.getContentPane().add(btnRegister);

		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Arial", Font.PLAIN, 13));
		btnLogin.setBounds(273, 25, 112, 32);
		frame.getContentPane().add(btnLogin);

		btnStart = new JButton("Start");
		btnStart.setFont(new Font("Arial", Font.BOLD, 14));
		btnStart.setBounds(566, 24, 112, 32);
		frame.getContentPane().add(btnStart);

		btnSave = new JButton("Save");
		btnSave.setFont(new Font("Arial", Font.PLAIN, 13));
		btnSave.setBounds(697, 24, 112, 32);
		frame.getContentPane().add(btnSave);

		btnHallOfFame = new JButton("Hall of fame");
		btnHallOfFame.setFont(new Font("Arial", Font.PLAIN, 13));
		btnHallOfFame.setBounds(830, 24, 112, 32);
		frame.getContentPane().add(btnHallOfFame);

		btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Arial", Font.PLAIN, 13));
		btnLogout.setBounds(964, 24, 112, 32);
		frame.getContentPane().add(btnLogout);

		JLabel lblCrupier = new JLabel("CRUPIER");
		lblCrupier.setFont(new Font("Arial", Font.BOLD, 15));
		lblCrupier.setBounds(56, 89, 98, 32);
		frame.getContentPane().add(lblCrupier);

		lblPlayer = new JLabel("PLAYER");
		lblPlayer.setFont(new Font("Arial", Font.BOLD, 15));
		lblPlayer.setBounds(668, 89, 98, 32);
		frame.getContentPane().add(lblPlayer);

		btnNewCard = new JButton("New Card");
		btnNewCard.setFont(new Font("Arial", Font.PLAIN, 13));
		btnNewCard.setBounds(370, 623, 112, 32);
		frame.getContentPane().add(btnNewCard);

		btnStand = new JButton("Stand");
		btnStand.setFont(new Font("Arial", Font.PLAIN, 13));
		btnStand.setBounds(528, 623, 112, 32);
		frame.getContentPane().add(btnStand);

		lblMuestraCartaCrupier = new JLabel("");
		lblMuestraCartaCrupier.setBackground(Color.LIGHT_GRAY);
		lblMuestraCartaCrupier.setBounds(66, 117, 290, 437);
		lblMuestraCartaCrupier.setOpaque(true);
		frame.getContentPane().add(lblMuestraCartaCrupier);

		lblMuestraCartaPlayer = new JLabel("");
		lblMuestraCartaPlayer.setBackground(Color.LIGHT_GRAY);
		lblMuestraCartaPlayer.setBounds(668, 117, 290, 437);
		lblMuestraCartaPlayer.setOpaque(true);
		frame.getContentPane().add(lblMuestraCartaPlayer);

		lblTotalScoreCrupier = new JLabel("TOTAL SCORE: ");
		lblTotalScoreCrupier.setFont(new Font("Arial", Font.PLAIN, 13));
		lblTotalScoreCrupier.setBounds(56, 565, 216, 20);
		frame.getContentPane().add(lblTotalScoreCrupier);

		lblTotalScorePlayer = new JLabel("TOTAL SCORE: ");
		lblTotalScorePlayer.setFont(new Font("Arial", Font.PLAIN, 13));
		lblTotalScorePlayer.setBounds(668, 565, 216, 20);
		frame.getContentPane().add(lblTotalScorePlayer);

		lblScoreHistoryCrupier = new JLabel("Score history:");
		lblScoreHistoryCrupier.setFont(new Font("Arial", Font.PLAIN, 13));
		lblScoreHistoryCrupier.setBounds(56, 593, 216, 20);
		frame.getContentPane().add(lblScoreHistoryCrupier);

		lblScoreHistoryPlayer = new JLabel("Score history:");
		lblScoreHistoryPlayer.setFont(new Font("Arial", Font.PLAIN, 13));
		lblScoreHistoryPlayer.setBounds(668, 596, 216, 20);
		frame.getContentPane().add(lblScoreHistoryPlayer);

		this.frame.setVisible(true);

	}

	public Choice getChoiceCards() {
		return choiceCards;
	}

	public JLabel getLblPlayer() {
		return lblPlayer;
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

	public JButton getBtnLogout() {
		return btnLogout;
	}

	public JButton getBtnNewCard() {
		return btnNewCard;
	}

	public JButton getBtnStand() {
		return btnStand;
	}

	public JLabel getLblTotalScorePlayer() {
		return lblTotalScorePlayer;
	}

	public JLabel getLblTotalScoreCrupier() {
		return lblTotalScoreCrupier;
	}

	public JLabel getLblScoreHistoryPlayer() {
		return lblScoreHistoryPlayer;
	}

	public JLabel getLblScoreHistoryCrupier() {
		return lblScoreHistoryCrupier;
	}

	public JLabel getLblMuestraCartaCrupier() {
		return lblMuestraCartaCrupier;
	}

	public JLabel getLblMuestraCartaPlayer() {
		return lblMuestraCartaPlayer;
	}
}