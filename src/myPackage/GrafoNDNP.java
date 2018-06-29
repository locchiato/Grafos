package myPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class GrafoNDNP extends Grafo {

	private MatrizSimetrica grafo;

	public GrafoNDNP(String path) throws FileNotFoundException {
		super(path);

		int fil;
		int col;
		int indice;

		grafo = new MatrizSimetrica(cantNodos);
		mejoresColores = new int[cantNodos];

		for (int i = 0; i < cantNodos; i++)
			gradosNodos[i] = 0;

		for (int i = 0; i < cantNodos; i++)
			mejoresColores[i] = 0;

		for (int i = 0; i < cantAristas; i++) {
			fil = scan.nextInt();
			col = scan.nextInt();
			gradosNodos[fil]++;
			gradosNodos[col]++;
			indice = grafo.getIndice(fil, col);
			grafo.ponerArista(indice);
		}

		for (int i = 0; i < cantNodos; i++) {
			Nodo nodo = new Nodo(i);
			nodo.setGrado(gradosNodos[i]);
			nodos.add(nodo);
		}

		scan.close();
	}

	private void colorear() {
		
		int color = 1;
		int nodo;
		int indice;

		colorMax = 1;
		for (int i = 0; i < cantNodos; i++)
			nodosColoreados[i] = 0;

		nodosColoreados[nodos.get(0).getNroNodo()] = color; // coloreo el
															// primer
															// nodo de
															// la lista

		for (int i = 1; i < cantNodos; i++) {
			nodo = nodos.get(i).getNroNodo();
			nodosColoreados[nodo] = color;
			
			for(int j = 0; j < cantNodos; j++) {
				if (nodo != j) {
					if (nodo < j)
						indice = grafo.getIndice(nodo, j);
					else
						indice = grafo.getIndice(j, nodo);

					if (grafo.hayArista(indice) && nodosColoreados[j] != color) {
						if (color > colorMax)
							colorMax = color;
						nodosColoreados[nodo] = color;
					}
				}
			}
			color = 1;
		}
	}

	public void coloreoSecuencial(int corridas) throws IOException {
		int nroCorrida = 0;
		for (int i = 0; i < corridas; i++) {
			Collections.shuffle(nodos);
			colorear();

			if (colorMax < mejorColor || mejorColor == 0) {
				mejorColor = colorMax;
				nroCorrida = i + 1;
				solucion = nodosColoreados.clone(); // copio la mejor
													// solucion
													// hasta el
													// momento
			}

			if (colorMax < cantNodos)
				mejoresColores[colorMax]++;
		}

		escribirSolucion("SECUENCIAL");
		escribirVectorEstadistico("SECUENCIAL");
		System.out.print("SECUENCIAL: ");
		System.out.println("Menor cantidad de colores: " + mejorColor + ", en numero de iteracion: " + nroCorrida);

		mejorColor = 0;
		for (int i = 0; i < cantNodos; i++) // me aseguro de limpiar el
											// vector estadistico
			mejoresColores[i] = 0;
	}

	public void coloreoWelshPowell(int corridas) throws IOException {
		int nroCorrida = 0;
		for (int i = 0; i < corridas; i++) {
			Collections.shuffle(nodos);
			Collections.sort(nodos, new Comparator<Nodo>() {
				@Override
				public int compare(Nodo nodo1, Nodo nodo2) {
					return nodo2.getGrado() - nodo1.getGrado(); // ordeno por
																// grado
																// decreciente
				}
			});
			colorear();

			if (colorMax < mejorColor || mejorColor == 0) {
				mejorColor = colorMax;
				nroCorrida = i + 1;
				solucion = nodosColoreados.clone(); // copio la mejor
													// solucion
													// hasta el
													// momento
			}

			if (colorMax < cantNodos)
				mejoresColores[colorMax]++;
		}

		escribirSolucion("WELSH-POWELL");
		escribirVectorEstadistico("WELSH-POWELL");
		System.out.print("WELSH-POWELL: ");
		System.out.println("Menor cantidad de colores: " + mejorColor + ", en numero de iteracion: " + nroCorrida);

		mejorColor = 0;
		for (int i = 0; i < cantNodos; i++) // me aseguro de limpiar el
											// vector estadistico
			mejoresColores[i] = 0;
	}

	public void coloreoMatula(int corridas) throws IOException {
		int nroCorrida = 0;
		for (int i = 0; i < corridas; i++) {
			Collections.shuffle(nodos);
			Collections.sort(nodos, new Comparator<Nodo>() {
				@Override
				public int compare(Nodo nodo1, Nodo nodo2) {
					return nodo1.getGrado() - nodo2.getGrado(); // ordeno por
																// grado
																// creciente
				}
			});
			colorear();

			if (colorMax < mejorColor || mejorColor == 0) {
				mejorColor = colorMax;
				nroCorrida = i + 1;
				solucion = nodosColoreados.clone(); // copio la mejor
													// solucion
													// hasta el
													// momento
			}

			if (colorMax < cantNodos)
				mejoresColores[colorMax]++;
		}

		escribirSolucion("MATULA");
		escribirVectorEstadistico("MATULA");
		System.out.print("MATULA: ");
		System.out.println("Menor cantidad de colores: " + mejorColor + ", en numero de iteracion: " + nroCorrida);

		mejorColor = 0;
		for (int i = 0; i < cantNodos; i++) // me aseguro de limpiar el
											// vector estadistico
			mejoresColores[i] = 0;
	}

	public void mostrar() {
		grafo.mostrar();
	}

	public void mostrarNodos() {
		for (int i = 0; i < cantNodos; i++)
			System.out.println("n: " + nodos.get(i).getNroNodo() + " g: " + nodos.get(i).getGrado());
	}

	public void mostrarGrados() {
		for (int i = 0; i < cantNodos; i++)
			System.out.println("n: " + i + " g: " + gradosNodos[i]);
	}

	private void escribirSolucion(String algoritmo) throws IOException {
		FileWriter file = new FileWriter(
				"COLOREO" + "_" + algoritmo + "_" + cantNodos + "_" + String.format("%.2f", ptajeAdyacencia) + ".out");
		BufferedWriter buffer = new BufferedWriter(file);

		buffer.write(String.valueOf(cantNodos));
		buffer.write(" ");
		buffer.write(String.valueOf(colorMax));
		buffer.write(" ");
		buffer.write(String.valueOf(cantAristas));
		buffer.write(" ");
		buffer.write(String.valueOf(NumberFormat.getInstance().format(ptajeAdyacencia)));
		buffer.write(" ");
		buffer.write(String.valueOf(gradoMax));
		buffer.write(" ");
		buffer.write(String.valueOf(gradoMin));
		buffer.newLine();

		for (int i = 0; i < solucion.length; i++) {
			buffer.write(String.valueOf(i));
			buffer.write(" ");
			buffer.write(String.valueOf(solucion[i]));
			buffer.newLine();
		}

		buffer.close();
	}

	private void escribirVectorEstadistico(String algoritmo) throws IOException {
		FileWriter file = new FileWriter(
				"VECTOR" + "_" + algoritmo + "_" + cantNodos + "_" + String.format("%.2f", ptajeAdyacencia) + ".out");
		BufferedWriter buffer = new BufferedWriter(file);

		for (int i = 0; i < cantNodos; i++) {
			buffer.write(String.valueOf(i + 1));
			buffer.write(" ");
			buffer.write(String.valueOf(mejoresColores[i]));
			buffer.newLine();
		}

		buffer.close();
	}

	public int getCantNodos() {
		return cantNodos;
	}

	public int getCantAristas() {
		return cantAristas;
	}

	public double getPtajeAdyacencia() {
		return ptajeAdyacencia;
	}

	public int getGradoMax() {
		return gradoMax;
	}

	public int getGradoMin() {
		return gradoMin;
	}

	public MatrizSimetrica getGrafo() {
		return grafo;
	}

}
