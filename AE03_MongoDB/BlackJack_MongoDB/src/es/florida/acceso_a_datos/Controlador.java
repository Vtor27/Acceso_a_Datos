package es.florida.acceso_a_datos;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.bson.Document;

/**
 * Clase `Controlador` que gestiona la interacción entre el modelo y las vistas.
 * 
 * Esta clase actúa como intermediario en el patrón Modelo-Vista-Controlador
 * (MVC), gestionando los eventos generados en las vistas y comunicándose con el
 * modelo para ejecutar la lógica. Se encarga de inicializar los controladores
 * de botones y de gestionar el flujo del juego.
 */
public class Controlador {

	private Modelo modelo;
	private Vista vista;
	private VistaLogin vistaLogin;
	private VistaHallOfFame vistaHallOfFame;
	private String user;
	private int puntosJugador = 0;
	private int puntosCrupier = 0;
	private List<Document> cartasJugador;
	private List<Document> cartasCrupier;
	private List<String> idCartas;
	private boolean jugadorSePlanta = false;
	private boolean crupierSePlanta = false;

	public Controlador(Modelo modelo, Vista vista, VistaLogin vistaLogin, VistaHallOfFame vistaHallOfFame) {
		this.modelo = modelo;
		this.vista = vista;
		this.vistaLogin = vistaLogin;
		this.vistaHallOfFame = vistaHallOfFame;

		controlBtnLoadCards();
		controlBtnRegister();
		controlBtnLogin();
		controlBtnLogin_Login();
		controlBtnRegistrarUser();
		controlBtnLogout();
		controlBtnStart();
		controlBtnSave();
		controlHallOfFame();

		vista.getBtnLogout().setEnabled(false);
		vista.getBtnStart().setEnabled(false);
		vista.getBtnSave().setEnabled(false);
		vista.getBtnHallOfFame().setEnabled(false);
		vista.getBtnNewCard().setEnabled(false);
		vista.getBtnStand().setEnabled(false);
		vista.getChoiceCards().setEnabled(false);
	}

	/**
	 * Configura el botón "Load Cards" para cargar las barajas en la base de datos.
	 * 
	 * Este método asocia un `ActionListener` al botón "Load Cards" de la vista.
	 * Cuando se pulsa el botón, se invoca el método estático `cargarBaraja` de la
	 * clase `Modelo`, que carga las barajas española y francesa desde las rutas
	 * especificadas y las almacena en la base de datos.
	 */
	public void controlBtnLoadCards() {
		vista.getBtnLoadCards().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Modelo.cargarBaraja("./cards_es", "./cards_fr");

			}
		});
	}

	/**
	 * Configura el botón "Register" para mostrar la interfaz de registro de
	 * usuarios.
	 * 
	 * Este método asocia un `ActionListener` al botón "Register" de la vista
	 * principal. Al pulsar el botón, se ajustan los elementos de la vista de login
	 * para habilitar la funcionalidad de registro de nuevos usuarios.
	 * 
	 */
	public void controlBtnRegister() {
		vista.getBtnRegister().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				vistaLogin.resetearVistaLogin();
				vistaLogin.mostrarVistaLogin();
				vistaLogin.getBtnRegistrar_Login().setVisible(true);
				vistaLogin.getPasswordField_RepitePasword().setVisible(true);
				vistaLogin.getLblRepiteContraseña().setVisible(true);

				vistaLogin.getBtnLogin_Login().setVisible(false);
				vistaLogin.getLblTitulo().setText("Registrar User");
			}
		});
	}

	/**
	 * Controla la acción del botón "Login" en la vista principal.
	 * 
	 * Este método se activa al presionar el botón "Login" en la interfaz principal
	 * y muestra la ventana de vista login para que el usuario pueda ingresar sus
	 * credenciales.
	 */
	public void controlBtnLogin() {
		vista.getBtnLogin().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				vistaLogin.mostrarVistaLogin();
			}
		});
	}

	/**
	 * Configura el botón "Logout" para gestionar el cierre de sesión del usuario.
	 * 
	 * Este método asocia un `ActionListener` al botón "Logout" en la vista
	 * principal y devuelve el color por defecto al boton de login.
	 */
	public void controlBtnLogout() {
		vista.getBtnLogout().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Sesión cerrada.");

				vistaLogin.resetearVistaLogin();

				Modelo.cerrarConexion();

				vista.getBtnStart().setEnabled(false);
				vista.getBtnSave().setEnabled(false);
				vista.getBtnHallOfFame().setEnabled(false);
				vista.getBtnNewCard().setEnabled(false);
				vista.getBtnStand().setEnabled(false);
				vista.getChoiceCards().setEnabled(false);
				vista.getBtnLogout().setEnabled(false);

				vista.getBtnLogin().setBackground(UIManager.getColor("Button.background"));
				vista.getBtnLogin().setEnabled(true);

				vista.getLblPlayer().setText("PLAYER");
			}
		});
	}

	/**
	 * Actualiza el estado de la interfaz en función de la conexión del usuario.
	 * 
	 * Este método ajusta el botón "Login" y el label del jugador según el estado de
	 * conexión del usuario. Si el usuario se conecta el boton de login pasa a ser
	 * verde y se desactiva.
	 * 
	 * @param conectado Un valor booleano que indica si el usuario está conectado o
	 *                  no: - `true` para usuario conectado. - `false` para usuario
	 *                  desconectado.
	 */
	public void controlUserConectado(boolean conectado) {
		if (conectado == true) {
			vista.getBtnLogin().setEnabled(false);
			vista.getBtnLogin().setBackground(Color.GREEN);
			vista.getLblPlayer().setText(user);
		} else {
			vista.getBtnLogin().setEnabled(true);
			vista.getBtnLogin().setBackground(UIManager.getColor("Button.background"));
		}
	}

	/**
	 * Controla la acción del botón "Login" en la vista de inicio de sesión.
	 * 
	 * Este método gestiona el proceso de autenticación del usuario, valida que los
	 * campos de usuario y contraseña no estén vacios y comprueba si hay una
	 * conexión abierta. Si las credenciales son correctas se activarán los botones
	 * relacionados al juego y el botón de "Logout", una vez hecho esto limpia los
	 * campos. Si las credenciales no son correctas cerrará la conexión con la base
	 * de datos.
	 * 
	 */
	public void controlBtnLogin_Login() {
		vistaLogin.getBtnLogin_Login().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userInput = vistaLogin.getTextField_User().getText();
				String password = new String(vistaLogin.getPasswordField_Password().getPassword());

				if (userInput.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Para poder loguearse el Usuario y la Contraseña no pueden estar vacios.");
					return;
				}

				Modelo.conectarConDataBase();

				if (modelo.validarUsuario(userInput, password)) {
					user = userInput;
					JOptionPane.showMessageDialog(null, "Bienvenido " + user);
					vista.getLblPlayer().setText(user);

					vista.getBtnRegister().setEnabled(true);
					vista.getBtnStart().setEnabled(true);
					vista.getBtnHallOfFame().setEnabled(true);
					vista.getChoiceCards().setEnabled(true);

					if (Modelo.compruebaConexion == true) {
						controlUserConectado(true);
					} else {
						controlUserConectado(false);
					}
					vistaLogin.getTextField_User().setText("");
					vistaLogin.getPasswordField_Password().setText("");

					vistaLogin.cerrarVentanaLogin();
					vista.getBtnLogout().setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					Modelo.cerrarConexion();
				}
			}
		});
	}

	/**
	 * Configura el botón "Registrar" para registrar un nuevo usuario en la base de
	 * datos.
	 * 
	 * Este método asocia un `ActionListener` al botón "Registrar" de la vista de
	 * login. Cuando se pulsa el botón, se validan los datos introducidos por el
	 * usuario en otra vista a parte y, si son válidos, se registra un nuevo usuario
	 * en la base de datos utilizando el método `registrarUsuario` de la clase
	 * `Modelo`. Si el registro es exitoso, se muestra un mensaje de confirmación y
	 * se resetea el formulario de login.
	 *
	 */
	public void controlBtnRegistrarUser() {

		vistaLogin.getBtnRegistrar_Login().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String user = vistaLogin.getTextField_User().getText();
				String password = new String(vistaLogin.getPasswordField_Password().getPassword());
				String confirmPassword = new String(vistaLogin.getPasswordField_RepitePasword().getPassword());

				if (user.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Todos los campos són obligatorios.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!password.equals(confirmPassword)) {
					JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				boolean userInsertado = modelo.registrarUsuario(user, password);
				if (userInsertado == true) {
					JOptionPane.showMessageDialog(null,
							"Usuario " + user + " registrado en la base de datos con exito.");
					vistaLogin.resetearVistaLogin();
				} else {
					JOptionPane.showMessageDialog(null, "ERROR al insertar el nuevo usuario.", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * Controla la acción del botón "Start" para iniciar una nueva partida.
	 * 
	 * Este método registra un `ActionListener` en el botón "Start" de la vista
	 * principal. Cuando el botón es presionado, se invoca el método
	 * `iniciarPartida()` para configurar e iniciar una nueva partida de Blackjack.
	 */
	public void controlBtnStart() {
		vista.getBtnStart().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				iniciarPartida();
			}
		});
	}

	/**
	 * Inicia una nueva partida de Blackjack.
	 * 
	 * Este método reinicia los valores de la partida, selecciona la baraja deseada
	 * por el usuario, baraja las cartas obtenidas de la base de datos, y solicita
	 * al usuario que elija quién comienza el turno (jugador o crupier).
	 * 
	 */
	public void iniciarPartida() {
		reiniciarPartida();

		String barajaSeleccionada = vista.getChoiceCards().getSelectedItem();
		System.out.println("Baraja seleccionada: " + barajaSeleccionada);

		idCartas = modelo.extraerIDsCartasDeDB(barajaSeleccionada);
		Collections.shuffle(idCartas);

		String[] opciones = { "CRUPIER", "JUGADOR" };
		int seleccion = JOptionPane.showOptionDialog(null, "¿Quién comienza la partida?", "Inicio del Juego",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		if (seleccion == -1) {
			JOptionPane.showMessageDialog(null, "Tienes que elegir quien empieza a jugar.");
			return;
		}

		boolean seleccionTurno = false;
		if (seleccion == 0) {
			seleccionTurno = false;
		} else if (seleccion == 1) {
			seleccionTurno = true;
		}
		iniciarJuego(barajaSeleccionada, seleccionTurno);
	}

	/**
	 * Reinicia las variables y el estado de la interfaz para comenzar una nueva
	 * partida.
	 * 
	 * Este método restablece las puntuaciones, los estados del jugador y el
	 * crupier, limpia las etiquetas de la interfaz relacionadas con el historial de
	 * puntuaciones y las cartas mostradas, y habilita los botones necesarios para
	 * que el jugador pueda jugar de nuevo.
	 */
	public void reiniciarPartida() {

		puntosJugador = 0;
		puntosCrupier = 0;

		jugadorSePlanta = false;
		crupierSePlanta = false;

		vista.getLblTotalScorePlayer().setText("TOTAL SCORE: ");
		vista.getLblScoreHistoryPlayer().setText("Score history: ");
		vista.getLblTotalScoreCrupier().setText("TOTAL SCORE: ");
		vista.getLblScoreHistoryCrupier().setText("Score history: ");

		vista.getBtnNewCard().setEnabled(true);
		vista.getBtnStand().setEnabled(true);

		vista.getLblMuestraCartaPlayer().setIcon(null);
		vista.getLblMuestraCartaCrupier().setIcon(null);
	}

	/**
	 * Inicializa una nueva partida de Blackjack configurando las variables
	 * iniciales y habilitando los controles necesarios.
	 * 
	 * Este método obtiene las cartas desde la base de datos según la baraja
	 * seleccionada, las mezcla y reinicia las puntuaciones. También habilita los
	 * botones de la interfaz gráfica para que los usuarios puedan jugar.
	 * Finalmente, determina quién comienza el turno basado en la selección inicial.
	 * 
	 * @param baraja         El tipo de baraja seleccionada para el juego ("ES" para
	 *                       baraja española o "FR" para baraja francesa).
	 * @param seleccionTurno Indica si el jugador o el crupier comienza la partida.
	 *                       `true` para el jugador, `false` para el crupier.
	 */
	public void iniciarJuego(String baraja, boolean seleccionTurno) {
		idCartas = modelo.extraerIDsCartasDeDB(baraja);

		Collections.shuffle(idCartas);

		puntosJugador = 0;
		puntosCrupier = 0;
		cartasJugador = new ArrayList<>();
		cartasCrupier = new ArrayList<>();

		vista.getBtnNewCard().setEnabled(true);
		vista.getBtnStand().setEnabled(true);

		for (ActionListener al : vista.getBtnNewCard().getActionListeners()) {
			vista.getBtnNewCard().removeActionListener(al);
		}
		for (ActionListener al : vista.getBtnStand().getActionListeners()) {
			vista.getBtnStand().removeActionListener(al);
		}

		if (seleccionTurno == true) {
			JOptionPane.showMessageDialog(null, "El jugador comienza la partida.");
			turnoJugador(baraja);
		} else {
			JOptionPane.showMessageDialog(null, "El crupier comienza la partida.");
			turnoCrupier(baraja);
		}
	}

	/**
	 * Gestiona el turno del jugador en el juego de Blackjack.
	 * 
	 * Este método permite al jugador interactuar con la interfaz durante su turno.
	 * El jugador puede pedir una carta o plantarse, al pedir una carta el método
	 * obtiene la carta que esté en la primera posición de la lista a través de su
	 * ID, la muestra en la interfaz sumando su puntuación y se comprueba que la
	 * puntuación sea menor a 21 para poder seguir jugando o terminar la partida
	 * 
	 * Si el jugador se planta o termina su turno, pasa el control al crupier.
	 * 
	 * @param mazoBarajado El nombre de la baraja seleccionada ("cards_es" o
	 *                     "cards_fr").
	 */
	public void turnoJugador(String mazoBarajado) {
		JOptionPane.showMessageDialog(null, "Es el turno del jugador.");

		vista.getBtnNewCard().setEnabled(true);
		vista.getBtnStand().setEnabled(true);

		for (ActionListener al : vista.getBtnNewCard().getActionListeners()) {
			vista.getBtnNewCard().removeActionListener(al);
		}
		for (ActionListener al : vista.getBtnStand().getActionListeners()) {
			vista.getBtnStand().removeActionListener(al);
		}

		vista.getBtnNewCard().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String idCarta = idCartas.remove(0);
				Document carta = modelo.obtenerCartaPorID(mazoBarajado, idCarta);

				cartasJugador.add(carta);

				int puntosCarta = carta.getInteger("points");

				ImageIcon iconCarta = modelo.obtenerImagen(carta);
				vista.getLblMuestraCartaPlayer().setIcon(iconCarta);

				if (puntosCarta == 1) {
					String[] opciones = { "1", "11" };
					int seleccion = JOptionPane.showOptionDialog(null, "Has sacado un As. ¿Qué valor deseas usar?",
							"Elige el valor del As", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
							opciones, opciones[1]);
					if (seleccion == 1) {
						puntosCarta = 11;
					} else if (seleccion == 0) {
						puntosCarta = 1;
					}
				}

				puntosJugador += puntosCarta;
				actualizarPuntuacionJugador(carta, puntosJugador, puntosCarta);

				if (puntosJugador == 21 || puntosJugador > 21) {
					comprobarPartida();
					return;
				}

				if (crupierSePlanta != true) {
					turnoCrupier(mazoBarajado);
				}
			}
		});

		vista.getBtnStand().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Botón 'Stand' pulsado");
				if (crupierSePlanta == true) {
					comprobarPartida();
				} else {
					jugadorSePlanta = true;
					turnoCrupier(mazoBarajado);
				}
			}
		});
	}

	/**
	 * Gestiona el turno del crupier en el juego de Blackjack.
	 * 
	 * Este método realiza acciones dependiendo de las reglas y condiciones del
	 * juego. Se basa en las puntuaciones actuales del juego y depende de si el
	 * jugador se planta o no para jugar sus turnos de manera secuencial o todos de
	 * golpe. El crupier toma cartas hasta alcanzar un mínimo de 17 puntos o hasta
	 * que la partida termine.
	 * 
	 * @param mazoBarajado El nombre de la baraja seleccionada ("cards_es" o
	 *                     "cards_fr").
	 */
	public void turnoCrupier(String mazoBarajado) {
		JOptionPane.showMessageDialog(null, "Es el turno del crupier.");

		if (jugadorSePlanta && crupierSePlanta) {
			comprobarPartida();
			return;
		}

		if (jugadorSePlanta) {
			while (puntosCrupier < 17) {
				String idCarta = idCartas.remove(0);
				Document carta = modelo.obtenerCartaPorID(mazoBarajado, idCarta);

				cartasCrupier.add(carta);

				int puntosCarta = carta.getInteger("points");

				ImageIcon iconCarta = modelo.obtenerImagen(carta);
				vista.getLblMuestraCartaCrupier().setIcon(iconCarta);

				if (puntosCarta == 1) {
					if (puntosCrupier + 11 <= 21) {
						puntosCarta = 11;
					} else {
						puntosCarta = 1;
					}
				}
				puntosCrupier += puntosCarta;
				actualizarPuntuacionCrupier(carta, puntosCrupier, puntosCarta);

				if (puntosCrupier > 21) {
					JOptionPane.showMessageDialog(null, "El crupier se ha pasado de 21. Tú ganas.");
					comprobarPartida();
					return;
				}

				if (puntosCrupier >= 17 && puntosCrupier <= 21) {
					JOptionPane.showMessageDialog(null, "El crupier se planta.");
					crupierSePlanta = true;

					if (jugadorSePlanta == true) {
						comprobarPartida();
						vista.getBtnNewCard().setEnabled(false);
						vista.getBtnStand().setEnabled(false);
						vista.getBtnSave().setEnabled(true);
					}
					return;
				}
			}
		} else {
			String idCarta = idCartas.remove(0);
			Document carta = modelo.obtenerCartaPorID(mazoBarajado, idCarta);
			cartasCrupier.add(carta);

			int puntosCarta = carta.getInteger("points");

			ImageIcon iconCarta = modelo.obtenerImagen(carta);
			vista.getLblMuestraCartaCrupier().setIcon(iconCarta);

			if (puntosCarta == 1) {
				if (puntosCrupier + 11 <= 21) {
					puntosCarta = 11;
				} else {
					puntosCarta = 1;
				}
			}
			puntosCrupier += puntosCarta;
			actualizarPuntuacionCrupier(carta, puntosCrupier, puntosCarta);

			if (puntosCrupier > 21) {
				JOptionPane.showMessageDialog(null, "El crupier se ha pasado de 21. Tú ganas.");
				comprobarPartida();
				return;
			}

			if (puntosCrupier >= 17 && puntosCrupier <= 21) {
				JOptionPane.showMessageDialog(null, "El crupier se planta.");
				crupierSePlanta = true;

				if (jugadorSePlanta == true) {
					comprobarPartida();
				}
			} else {
				turnoJugador(mazoBarajado);
			}
		}
	}

	/**
	 * Evalúa el estado actual del juego y determina el resultado final de la
	 * partida.
	 * 
	 * Este método analiza las puntuaciones del jugador y del crupier para
	 * determinar quién gana o si hay un empate. También actualiza la interfaz
	 * gráfica deshabilitando los botones de juego y habilitando el botón para
	 * guardar el resultado de la partida.
	 * 
	 * Finalmente, muestra el resultado en un cuadro de diálogo y actualiza el
	 * estado de los botones de la interfaz.
	 */
	public void comprobarPartida() {
		String resultado;

		if (puntosCrupier == 21) {
			resultado = "El crupier gana por tener BlackJack.";
		} else if (puntosJugador == 21) {
			resultado = "El jugador gana por tener BlackJack.";
		} else if (puntosJugador > 21) {
			resultado = "El JUGADOR pierde la partida";
		} else if (puntosCrupier > 21) {
			resultado = "El CRUPIER pierde la partida";
		} else if (puntosJugador > puntosCrupier) {
			resultado = "El jugador gana con " + puntosJugador + " puntos contra " + puntosCrupier + ".";
		} else if (puntosCrupier > puntosJugador) {
			resultado = "El crupier gana con " + puntosCrupier + " puntos contra " + puntosJugador + ".";
		} else {
			resultado = "Empate con " + puntosJugador + " puntos.";
		}

		JOptionPane.showMessageDialog(null, resultado, "Resultado final", JOptionPane.INFORMATION_MESSAGE);

		vista.getBtnNewCard().setEnabled(false);
		vista.getBtnStand().setEnabled(false);
		vista.getBtnSave().setEnabled(true);
	}

	/**
	 * Actualiza las etiquetas de la interfaz gráfica para mostrar la puntuación
	 * actual del crupier.
	 * 
	 * Este método toma la puntuación acumulada del crupier, el valor de la carta
	 * recién obtenida, y actualiza las etiquetas correspondientes en la vista. La
	 * etiqueta "Score history" muestra un historial de los puntos obtenidos por
	 * cada carta, mientras que "TOTAL SCORE" refleja la puntuación acumulada actual
	 * del crupier.
	 * 
	 * @param carta         El documento que representa la carta obtenida por el
	 *                      crupier.
	 * @param puntosCrupier La puntuación acumulada del crupier después de obtener
	 *                      la carta.
	 * @param puntosCarta   El valor de la carta que el crupier acaba de obtener.
	 */
	public void actualizarPuntuacionCrupier(Document carta, int puntosCrupier, int puntosCarta) {
		String puntuacionActual = vista.getLblScoreHistoryCrupier().getText();
		vista.getLblScoreHistoryCrupier().setText(puntuacionActual + " " + puntosCarta);
		vista.getLblTotalScoreCrupier().setText("TOTAL SCORE: " + puntosCrupier);
	}

	/**
	 * Actualiza las etiquetas de la interfaz gráfica para mostrar la puntuación
	 * actual del jugador.
	 * 
	 * Este método toma la puntuación acumulada del jugador, el valor de la carta
	 * recién obtenida, y actualiza las etiquetas correspondientes en la vista. La
	 * etiqueta "Score history" muestra un historial de los puntos obtenidos por
	 * cada carta, mientras que "TOTAL SCORE" refleja la puntuación acumulada
	 * actual.
	 * 
	 * @param carta         El documento que representa la carta obtenida por el
	 *                      jugador.
	 * @param puntosJugador La puntuación acumulada del jugador después de obtener
	 *                      la carta.
	 * @param puntosCarta   El valor de la carta que el jugador acaba de obtener.
	 */
	public void actualizarPuntuacionJugador(Document carta, int puntosJugador, int puntosCarta) {
		String puntuacionActual = vista.getLblScoreHistoryPlayer().getText();
		vista.getLblScoreHistoryPlayer().setText(puntuacionActual + " " + puntosCarta);
		vista.getLblTotalScorePlayer().setText("TOTAL SCORE: " + puntosJugador);
	}

	/**
	 * Controla la acción del botón "Hall of Fame" para mostrar los resultados de
	 * las partidas.
	 * 
	 * Este método obtiene los resultados almacenados en la base de datos, los
	 * organiza en un formato legible y los muestra en la ventana del Hall of Fame.
	 * Los datos incluyen el nombre del usuario, los puntos obtenidos, el tipo de
	 * baraja utilizada y el timestamp de cada partida.
	 */
	public void controlHallOfFame() {
		vista.getBtnHallOfFame().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<Document> hallOfFame = modelo.obtenerHallOfFame();

				StringBuilder sb = new StringBuilder();
				sb.append("SCORES: \n\n");
				for (Document game : hallOfFame) {
					sb.append("Usuario : ").append(game.getString("user")).append("\n")
							.append("Points: " + game.getInteger("points")).append("\n")
							.append("Suits: " + game.getString("suit")).append("\n").append(game.getString("timestamp"))
							.append("\n").append("-------------------\n");
				}
				vistaHallOfFame.getTextArea().setText(sb.toString());
				vistaHallOfFame.frame.setVisible(true);
			}
		});
	}

	/**
	 * Controla la acción del botón "Guardar" para almacenar el resultado de la
	 * partida en la base de datos.
	 * 
	 * Este método registra el resultado de la partida actual en la colección
	 * "scores" de la base de datos, incluyendo el nombre del usuario, el tipo de
	 * baraja utilizada, los puntos obtenidos y el timestamp de la partida. Si el
	 * resultado se guarda correctamente, el botón se desactiva para evitar que se
	 * guarde la misma partida mas de una vez.
	 */
	public void controlBtnSave() {
		vista.getBtnSave().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
				String suit = vista.getChoiceCards().getSelectedItem();

				boolean saved = modelo.guardarResultadoEnDB(user, suit, puntosJugador, timeStamp);

				if (saved == true) {
					JOptionPane.showMessageDialog(null, "¡Resultado guardado con exito!");
					vista.getBtnSave().setEnabled(false);
				} else {
					JOptionPane.showConfirmDialog(null, "Error al guardar el resultado.");
				}

			}
		});
	}
}