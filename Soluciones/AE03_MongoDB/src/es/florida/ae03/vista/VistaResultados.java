package es.florida.ae03.vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

/**
 * Clase VistaResultados (muestra los records)
 */
public class VistaResultados extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					VistaResultados frame = new VistaResultados();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Constructor VistaResultados
	 */
	public VistaResultados(String resultados) {
		setTitle("21 Blackjack Hall of Fame");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  //DISPOSE_ON_CLOSE para que solo se cierre esta ventana y no la aplicación
		setBounds(100, 100, 330, 514);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 300, 464);
		contentPane.add(scrollPane);
		
		JTextArea textArea_1 = new JTextArea();
		scrollPane.setViewportView(textArea_1);
		textArea_1.setText(resultados);
		
		setVisible(true);
	}
}
