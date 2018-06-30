package myPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Grafo {
	
	protected File file;
	protected Scanner scan;
	
	protected int cantNodos;
	protected int cantAristas;
	protected double ptajeAdyacencia;
	protected int gradoMax;
	protected int gradoMin;

	protected int colorMax;
	protected ArrayList<Nodo> nodos;
	protected int[] nodosColoreados; // el indice coincide con el numero de nodo,
									// y el valor que guarda es el color
	protected int[] gradosNodos;

	protected int mejorColor;
	protected int[] solucion;
	protected int[] mejoresColores;
	
	public Grafo(String path) throws FileNotFoundException {
		file = new File(path);
		scan = new Scanner(file);
		cantNodos = scan.nextInt();
		cantAristas = scan.nextInt();
		ptajeAdyacencia = scan.nextDouble();
		gradoMax = scan.nextInt();
		gradoMin = scan.nextInt();

		nodos = new ArrayList<Nodo>();
		mejorColor = cantNodos;
		nodosColoreados = new int[cantNodos];
		gradosNodos = new int[cantNodos];
		solucion = new int[cantNodos];
	}

	public abstract void coloreoSecuencial(int corridas) throws IOException;
	public abstract void coloreoMatula(int corridas) throws IOException;
	public abstract void coloreoWelshPowell(int corridas) throws IOException;
	
}
