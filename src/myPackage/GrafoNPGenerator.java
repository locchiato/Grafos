package myPackage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GrafoNPGenerator {

	public static void aleatorioConProbabilidad(int cantNodos, double probabilidad) throws IOException {
		ArrayList<Arista> array = new ArrayList<Arista>();
		Random rand = new Random();
		int cantMaximaAristas = ((cantNodos * (cantNodos - 1)) / 2);
		
		for (int i = 0; i < cantNodos - 1; i++)
			for (int j = i + 1; j < cantNodos; j++)
				if (rand.nextFloat() < probabilidad)
					array.add(new Arista(i, j));
		
		int cantAristas = array.size();
		
		/// Arista grados = new Arista(gradoMaximo, gradoMinimo);
		Arista grados = calcularGrado(array, cantNodos);
		double porcentajeAdyacencia = (double) cantAristas / cantMaximaAristas;
		String path = "ALEATORIO_PROB_" + cantNodos + "_" + probabilidad + ".txt";

		escribirGrafoEnArchivo(path, array, cantNodos, cantAristas, porcentajeAdyacencia, grados.getNodo1(),
				grados.getNodo2());
	}

	public static void aleatorioConPorcentajeAdyacencia(int cantNodos, double porcentajeAdyacencia) throws IOException {
		ArrayList<Arista> array = new ArrayList<Arista>();
		ArrayList<RandomParDeNodos> random = new ArrayList<RandomParDeNodos>();
		Random rand = new Random();
		int cantMaximaAristas = ((cantNodos * (cantNodos - 1)) / 2);
		
		for (int i = 0; i < cantNodos - 1; i++)
			for (int j = i + 1; j < cantNodos; j++) 
				random.add(new RandomParDeNodos(new Arista(i, j), rand.nextDouble()));

		Collections.sort(random);

		int cantAristas = (int) (cantMaximaAristas * porcentajeAdyacencia);
		
		for (int i = 0; i < cantAristas; i++)
			array.add(random.get(i).getNodos());

		Arista grados = calcularGrado(array, cantNodos);
		String path = "ALEATORIO_PTAJE_" + cantNodos + "_" + String.format("%.2f", porcentajeAdyacencia) + ".txt";

		escribirGrafoEnArchivo(path, array, cantNodos, cantAristas, porcentajeAdyacencia, grados.getNodo1(), grados.getNodo2());
	}

	public static void regularConGrado(int cantNodos, int grado) throws IOException {
		ArrayList<Arista> array = new ArrayList<Arista>();
		int cantAristas = 0;
		int gradoActual = 2;
		int cantMaximaAristas = (cantNodos * (cantNodos - 1)) / 2;
		int salto = 0;
		int j = 0;
		int des;
		int tam;

		if (grado % 2 != 0 && cantNodos % 2 != 0) {
			System.out.println("No se puede generar un grafo regular de grado impar con N impar");
			return;
		}
		if (grado >= cantNodos) {
			System.out.println("El grado no puede ser igual, o mayor a la cantidad de nodos");
			return;
		}

		if (grado % 2 != 0) { // si el grado es impar, genero la cruz
			tam = cantNodos / 2;
			for (int i = 0; i < tam; i++)
				array.add(new Arista(i, i+tam));
			cantAristas += tam;
		}

		if (grado > 1) {
			while (gradoActual < grado) {
				salto = gradoActual / 2;
				j = 0;
				for (int i = 0; i < cantNodos; i++) {
					des = (i + salto) < cantNodos ? i+salto : j++;
					array.add(new Arista(i, des));
				}
				cantAristas += cantNodos;
				gradoActual += 2;
			}
		}
		
		Arista grados = calcularGrado(array, cantNodos);
		double porcentajeAdyacencia = (double) cantAristas / cantMaximaAristas;
		String path = "REGULAR_" + cantNodos + "_" + String.format("%.2f", porcentajeAdyacencia) + ".txt";

		escribirGrafoEnArchivo(path, array, cantNodos, cantAristas, porcentajeAdyacencia, grados.getNodo1(),
				grados.getNodo2());
	}

	public static void regularConPorcentajeAdyacencia(int cantNodos, double porcentajeAdyacencia) throws IOException {
		int cantMaximaAristas = (cantNodos * (cantNodos - 1)) / 2;
		double minimo = (double) (cantNodos / 2) / cantMaximaAristas;
		int grado = (int) Math.ceil(porcentajeAdyacencia / minimo);

		if (porcentajeAdyacencia < minimo) {
			System.out.println(
					"El porcentaje de adyacencia ingresado es demasiado bajo para generar un grafo regular (el minimo es: "
							+ String.format("%1.3f", minimo) + ")");
			System.exit(1);
		}
		if (porcentajeAdyacencia > 1) {
			System.out.println("El porcentaje de adyacencia ingresado es superior al 100%");
			System.exit(1);
		}

		regularConGrado(cantNodos, grado);
	}

	public static void nPartito(int cantNodos, int cantConjuntos) throws IOException {
		ArrayList<Arista> array = new ArrayList<Arista>();
		ArrayList<Integer> conjuntos = new ArrayList<Integer>();
		Random rand = new Random();
		int random;
		int cantMaximaAristas = (cantNodos * (cantNodos - 1)) / 2;

		if (cantConjuntos > cantNodos) {
			System.out.println("La cantidad de conjuntos no puede ser mayor a la cantidad de nodos");
			System.exit(1);
		}

		for (int i = 0; i < cantNodos; i++) {
			random = rand.nextInt(cantConjuntos);
			conjuntos.add(random);
			System.out.println(random);
		}

		for (int i = 0; i < cantNodos - 1; i++)
			for (int j = i + 1; j < cantNodos; j++)
				if (conjuntos.get(i) != conjuntos.get(j))   // Si el nodo1 y el
															// nodo2 están en
															// conjuntos
															// diferentes
					array.add(new Arista(i, j));

		int cantAristas = array.size();
		Arista grados = calcularGrado(array, cantNodos);
		double porcentajeAdyacencia = (double) cantAristas / cantMaximaAristas;
		String path = cantConjuntos + "_PARTITO" + ".txt";

		escribirGrafoEnArchivo(path, array, cantNodos, cantAristas, porcentajeAdyacencia, grados.getNodo1(),
				grados.getNodo2());
	}

	private static Arista calcularGrado(ArrayList<Arista> array, int cantNodos) {
		int[] grados = new int[cantNodos];
		int gradoMaximo = 0;
		int gradoMinimo = Integer.MAX_VALUE;
		int grado = 0;

		for (int i = 0; i < cantNodos; i++)
			grados[i] = 0;

		for (int i = 0; i < array.size(); i++) {
			grados[array.get(i).getNodo1()] += 1;
			grados[array.get(i).getNodo2()] += 1;
		}

		for (int i = 0; i < cantNodos; i++) {
			grado = grados[i];
			if (grado > gradoMaximo)
				gradoMaximo = grado;
			if (grado < gradoMinimo)
				gradoMinimo = grado;
		}

		return new Arista(gradoMaximo, gradoMinimo);
	}

	private static void escribirGrafoEnArchivo(String path, ArrayList<Arista> array, int cantNodos, int cantAristas,
			double porcentajeAdyacencia, int gradoMaximo, int gradoMinimo) throws IOException {
		FileWriter file = new FileWriter(path);
		BufferedWriter buffer = new BufferedWriter(file);
		System.out.println();
		buffer.write(String.valueOf(cantNodos));
		buffer.write(" ");
		buffer.write(String.valueOf(cantAristas));
		buffer.write(" ");
		buffer.write(String.format("%1.2f", porcentajeAdyacencia));

		buffer.write(" ");
		buffer.write(String.valueOf(gradoMaximo));
		buffer.write(" ");
		buffer.write(String.valueOf(gradoMinimo));
		buffer.newLine();

		for (int i = 0; i < array.size(); i++) {
			buffer.write(String.valueOf(array.get(i).getNodo1()));
			buffer.write(" ");
			buffer.write(String.valueOf(array.get(i).getNodo2()));
			buffer.newLine();
		}

		buffer.close();

		if (gradoMinimo == 0) {
			System.out.println("El grafo generado tiene 0 como grado mínimo, genere un nuevo grafo...");
			System.exit(0);
		}

	}

	private static class RandomParDeNodos implements Comparable<RandomParDeNodos> {
		private Arista arista;
		private double probabilidad;

		public RandomParDeNodos(Arista arista, double probabilidad) {
			this.arista = arista;
			this.probabilidad = probabilidad;
		}

		public Arista getNodos() {
			return this.arista;
		}

		@SuppressWarnings("unused")
		public double getProbabilidad() {
			return this.probabilidad;
		}

		@SuppressWarnings("unused")
		public void mostrar() {
			System.out.println(this.arista.getNodo1() + " " + this.arista.getNodo2() + " " + this.probabilidad);
		}

		@Override
		public int compareTo(RandomParDeNodos o) {
			if (this.probabilidad > o.probabilidad)
				return -1;
			if (this.probabilidad < o.probabilidad)
				return 1;
			return 0;
		}
	}

}
