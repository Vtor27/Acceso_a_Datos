package es.florida.ae03.main;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import es.florida.ae03.controlador.Controlador;
import es.florida.ae03.modelo.Modelo;
import es.florida.ae03.vista.Vista;

/**
 * Clase Principal
 */
public class Principal {

	/**
	 * Clase principal ejecutable.
	 * @param args No se necesitan argumentos de entrada.
	 */
	public static void main(String[] args) {
		
		Vista vista = new Vista();
		Modelo modelo = new Modelo();
	    Controlador controlador = new Controlador(modelo, vista);
		
	}

}
