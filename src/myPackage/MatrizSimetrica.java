package myPackage;

public class MatrizSimetrica {

	private boolean[] matrizSimetrica;
	private int ordenMatriz;
	private int dimensionVector;

	public MatrizSimetrica(int n) { // Recibe el orden de la matriz
		ordenMatriz = n;
		dimensionVector = (n * n - n) / 2;
		matrizSimetrica = new boolean[dimensionVector];

		for (int i = 0; i < n - 1; i++)
			matrizSimetrica[i] = false;
	}

	public void ponerArista(int pos) {
		matrizSimetrica[pos] = true;
	}

	public boolean hayArista(int pos) {
		return matrizSimetrica[pos];
	}

	public int getIndice(int fil, int col) {
		return calcularIndice(fil, col);
	}

	public void setIndice(int fil, int col) {
		matrizSimetrica[calcularIndice(fil, col)] = true;
	}

	private int calcularIndice(int fil, int col) {
		return fil * this.ordenMatriz + col - (fil * fil + 3 * fil + 2) / 2;
	}

	public void mostrar() {
		for (int i = 0; i < dimensionVector; i++)
			System.out.print((matrizSimetrica[i] ? 1 : 0) + " ");
	}

	public int getOrdenMatriz() {
		return ordenMatriz;
	}

	public int getDimension() {
		return dimensionVector;
	}
}
