package Ficheros.AccesoADatos.Florida;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Modelo {
	private String fichero_lectura;
	private String fichero_escritura;

	public Modelo() {
		fichero_lectura = "Lorem.txt";
		fichero_escritura = "Lorem_Escrito.txt";
	}
	
	public ArrayList<String> contenidoFichero(String fichero){
		ArrayList<String> contenidoFichero = new ArrayList<String>();
		
		File file = new File(fichero);
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String linea = br.readLine();
			
			while(linea != null) {
				contenidoFichero.add(linea);
				linea = br.readLine();
			}
			br.close();
			fr.close();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return contenidoFichero;
	}
	
	public String ficheroEscritura() {
		return fichero_escritura;
	}
	
	public String ficheroLectura() {
		return fichero_lectura;
	}
	
	
}
