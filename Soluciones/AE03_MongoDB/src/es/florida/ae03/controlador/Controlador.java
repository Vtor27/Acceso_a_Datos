package es.florida.ae03.controlador;

import java.awt.Color;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.codec.binary.Base64;

import es.florida.ae03.modelo.Carta;
import es.florida.ae03.modelo.Modelo;
import es.florida.ae03.vista.Vista;
import es.florida.ae03.vista.VistaResultados;

/**
 * Clase Controlador
 */
public class Controlador {

	Modelo modelo;
	Vista vista;
	ActionListener actionListener;
	int puntos, totalCrupier, totalUsuario;
	boolean usuarioAutorizado, crupierSePlanta, usuarioSePlanta;
	String usuario, suit, turno, ganador, strPuntosCrupier, strPuntosUsuario;

	public Controlador(Modelo modelo, Vista vista) {

		this.modelo = modelo;
		this.vista = vista;
		this.usuarioAutorizado = false;
		this.usuario = "";
//		String[] options = new String[] { "Local", "Remote (Atlas)" };
//		int response = JOptionPane.showOptionDialog(null, "Select server...", "Memory",
//				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
//		if (response == 0) {
//			modelo.conexion("local");
//		} else {
//			modelo.conexion("remote");
//		}
		modelo.conexion("local");
		configurarBotones();
	}

	void inicializarVariables() {
		totalCrupier = totalUsuario = 0;
		crupierSePlanta = usuarioSePlanta = false;
		ganador = strPuntosCrupier = strPuntosUsuario = "";
		vista.getBtnCardCrupier01().setIcon(null);
		vista.getBtnCardPlayer01().setIcon(null);
		vista.getLblCrupierScore().setText("TOTAL SCORE: ");
		vista.getLblPlayerScore().setText("TOTAL SCORE: ");
		vista.getLblCrupierHistory().setText("Score history: ");
		vista.getLblPlayerHistory().setText("Score history: ");
		vista.getBtnNewCard().setEnabled(true);
		vista.getBtnStand().setEnabled(true);
		vista.getFrmBlackjack().setTitle("21 Blackjack - DAM 24/25 Edition");
	}
	
	void configurarBotones() {

		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modelo.cargarCartas();
			}
		};
		vista.getBtnLoadCards().addActionListener(actionListener);

		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(3, 2));
				JLabel userLabel = new JLabel("User: ");
				JLabel passLabel = new JLabel("Password: ");
				JLabel passLabel2 = new JLabel("Repeat password: ");
				JTextField userField = new JTextField();
				JPasswordField passField = new JPasswordField();
				JPasswordField passField2 = new JPasswordField();
				panel.add(userLabel);
				panel.add(userField);
				panel.add(passLabel);
				panel.add(passField);
				panel.add(passLabel2);
				panel.add(passField2);
				String[] options = new String[] { "Ok", "Cancel" };
				int option = JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
				if (option == 0) {
					char[] password = passField.getPassword();
					char[] password2 = passField2.getPassword();
					if (!new String(password).equals(new String(password2))) {
						JOptionPane.showMessageDialog(null, "Passwords do not match", "ERROR",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (!modelo.registro(userField.getText(), password)) {
						JOptionPane.showMessageDialog(null, "Error creating user", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};
		vista.getBtnRegister().addActionListener(actionListener);

		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(2, 2));
				JLabel userLabel = new JLabel("User: ");
				JLabel passLabel = new JLabel("Password: ");
				JTextField userField = new JTextField();
				JPasswordField passField = new JPasswordField();
				panel.add(userLabel);
				panel.add(userField);
				panel.add(passLabel);
				panel.add(passField);
				String[] options = new String[] { "Ok", "Cancel" };
				int option = JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
				if (option == 0) {
					char[] password = passField.getPassword();
					boolean respuesta = modelo.login(userField.getText(), password);
					if (respuesta) {
						usuarioAutorizado = true;
						usuario = userField.getText();
						vista.getBtnLogin().setBackground(Color.GREEN);
						vista.getFrmBlackjack().setTitle("21 Blackjack - DAM 24/25 Edition - Logged in as " + usuario);
					} else {
						usuarioAutorizado = false;
					}
				}
			}
		};
		vista.getBtnLogin().addActionListener(actionListener);

		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (usuarioAutorizado) {
					inicializarVariables();
					String suit = vista.getComboBoxSuit().getSelectedItem().toString();
					modelo.barajar(suit);
					String[] options = new String[] { "Crupier (A.I.)", "User (human)" };
					int option = JOptionPane.showOptionDialog(null, null, "Who starts?", JOptionPane.NO_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
					if (option == 0) {
						JOptionPane.showMessageDialog(null, "Crupier starts!", "Info", JOptionPane.INFORMATION_MESSAGE);
						turno = "c";
						tiradaCrupier();
					} else {
						JOptionPane.showMessageDialog(null, "User starts!", "Info", JOptionPane.INFORMATION_MESSAGE);
						turno = "u";
					}
				} else {
					JOptionPane.showMessageDialog(null, "Login to play", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		vista.getBtnStart().addActionListener(actionListener);

		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (usuarioAutorizado) {
					if (turno.equals("u")) {
						tiradaUser();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Login to play", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		vista.getBtnNewCard().addActionListener(actionListener);
		
		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (usuarioAutorizado) {
					usuarioSePlanta = true;
					vista.getBtnNewCard().setEnabled(false);
					JOptionPane.showMessageDialog(null, "User stands", "Info", JOptionPane.INFORMATION_MESSAGE);
					turno = "c";
					tiradaCrupier();
				} else {
					JOptionPane.showMessageDialog(null, "Login to play", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		vista.getBtnStand().addActionListener(actionListener);

		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (usuarioAutorizado) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm");
					String timeStamp = sdf.format(new Date());
					suit = "Suit " + vista.getComboBoxSuit().getSelectedItem().toString();
					boolean insertado = modelo.guardarResultado(usuario, suit, totalUsuario, timeStamp);
					if (insertado) {
						JOptionPane.showMessageDialog(null, "Result saved", "Info", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Login to play", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		vista.getBtnSave().addActionListener(actionListener);

		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (usuarioAutorizado) {
					ArrayList<String> scores = modelo.obtenerResultados();
					String strScores = "SCORES\n\n";
					for (String str : scores) {
						strScores += str + "\n";
					}
					VistaResultados vr = new VistaResultados(strScores);
				} else {
					JOptionPane.showMessageDialog(null, "Login to play", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		vista.getBtnHallOfFame().addActionListener(actionListener);
		
		actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (usuarioAutorizado) {
					usuarioAutorizado = false;
					vista.getBtnLogin().setBackground(null);
					inicializarVariables();
				} else {
					JOptionPane.showMessageDialog(null, "Login to play", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		vista.getBtnLogout().addActionListener(actionListener);
	}

	public void tiradaCrupier() {
		try {
			if (totalCrupier <= 16) {
				Carta c = modelo.getBaraja().get(0);
				byte[] btDataFile = Base64.decodeBase64(c.getBase64());
				BufferedImage imagen = ImageIO.read(new ByteArrayInputStream(btDataFile));
				ImageIcon imagenBoton = new ImageIcon(imagen.getScaledInstance(-1, 450, java.awt.Image.SCALE_SMOOTH));
				vista.getBtnCardCrupier01().setIcon(imagenBoton);
				int puntosCarta = c.getPoints();
				if ((puntosCarta == 1) && (totalCrupier <= 10)) {
					puntosCarta = 11;
				} 
				totalCrupier += puntosCarta;
				strPuntosCrupier = vista.getLblCrupierHistory().getText() + " " + puntosCarta;
				vista.getLblCrupierHistory().setText(strPuntosCrupier);
				vista.getLblCrupierScore().setText("TOTAL SCORE: " + totalCrupier);
				modelo.getBaraja().remove(0);
			} else {
				crupierSePlanta = true;
				JOptionPane.showMessageDialog(null, "Crupier stands", "Info", JOptionPane.INFORMATION_MESSAGE);
				turno = "u";
			}
			if (!comprobarFinJuego()) {
				JOptionPane.showMessageDialog(null, "User's turn", "Info", JOptionPane.INFORMATION_MESSAGE);
				turno = "u";
			} else {
				finJuego();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void tiradaUser() {
		try {
			Carta c = modelo.getBaraja().get(0);
			byte[] btDataFile = Base64.decodeBase64(c.getBase64());
			BufferedImage imagen = ImageIO.read(new ByteArrayInputStream(btDataFile));
			ImageIcon imagenBoton = new ImageIcon(imagen.getScaledInstance(-1, 450, java.awt.Image.SCALE_SMOOTH));
			vista.getBtnCardPlayer01().setIcon(imagenBoton);
			int puntosCarta = c.getPoints();
			if (puntosCarta == 1) {
				String[] options = new String[] { "1", "11" };
				int option = JOptionPane.showOptionDialog(null, null, "Choose your points", JOptionPane.NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
				if (option == 1) {
					puntosCarta = 11;
				}
			}
			totalUsuario += puntosCarta;
			strPuntosUsuario = vista.getLblPlayerHistory().getText() + " " + puntosCarta;
			vista.getLblPlayerHistory().setText(strPuntosUsuario);
			vista.getLblPlayerScore().setText("TOTAL SCORE: " + totalUsuario);
			modelo.getBaraja().remove(0);
			if (!comprobarFinJuego()) {
				JOptionPane.showMessageDialog(null, "Crupier's turn", "Info", JOptionPane.INFORMATION_MESSAGE);
				turno = "c";
				tiradaCrupier();
			} else {
				finJuego();
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void finJuego() {
		vista.getBtnNewCard().setEnabled(false);
		vista.getBtnStand().setEnabled(false);
		String mensaje = "Game over. The winner is " + ganador;
		JOptionPane.showMessageDialog(null, mensaje, "Info", JOptionPane.INFORMATION_MESSAGE);
	}


	public boolean comprobarFinJuego() {
		boolean fin = false;
		if (turno.equals("c")) {
			if (totalCrupier == 21) {
				fin = true;
				ganador = "Crupier";
			} else if (totalCrupier > 21) {
				fin = true;
				ganador = "User";
			}
		} 
		if (turno.equals("u")){
			if (totalUsuario == 21) {
				fin = true;
				ganador = "User";
			} else if (totalUsuario > 21) {
				fin = true;
				ganador = "Crupier";
			}
		}
		if (crupierSePlanta && usuarioSePlanta) {
			fin = true;
			if (totalCrupier > totalUsuario) {
				ganador = "Crupier";
			} else if (totalCrupier < totalUsuario) {
				ganador = "User";
			} else {
				ganador = "YOU DRAW";
			}
		}
		return fin;
	}

}
