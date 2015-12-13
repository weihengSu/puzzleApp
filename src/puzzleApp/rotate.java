package puzzleApp;

import java.util.Arrays;


public class rotate {

	private char data[][];

	public rotate (char d[][]) {
		data = new char[d.length][d[0].length];
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[0].length; j++)
				this.data[i][j] = d[i][j];
		}
	}

	/**
	 * Flip the matrix horizontally, rotate clockwise by 0.
	 *
	 * @return result
	 */
	public char[][] frotateC0() {
		int w = data.length;
		int l = data[0].length;
		char result[][] = new char[w][l];

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < l; j++)
				result[i][l - j - 1] = data[i][j];
		}
		return result;
	}

	/**
	 * Flip the matrix horizontally, rotate clockwise by pi/2.
	 *
	 * @return result
	 */
	public char[][] frotateC1() {
		int w = data.length;
		int l = data[0].length;
		char result[][] = new char[l][w];

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < l; j++)
				result[j][w - i - 1] = data[i][l - 1 - j];
		}
		return result;
	}

	/**
	 * Flip the matrix horizontally, rotate clockwise by pi.
	 *
	 * @return result
	 */
	public char[][] frotateC2() {
		int w = data.length;
		int l = data[0].length;
		char result[][] = new char[w][l];

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < l; j++)
				result[w - i - 1][l - j - 1] = data[i][l - 1 - j];
		}
		return result;
	}

	/**
	 * Flip the matrix horizontally, rotate clockwise by 3*pi/2.
	 *
	 * @return result
	 */
	public char[][] frotateC3() {
		int w = data.length;
		int l = data[0].length;
		char result[][] = new char[l][w];

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < l; j++)
				result[l - 1 - j][i] = data[i][l - 1 - j];
		}
		return result;
	}

	/**
	 * Rotate the matrix clockwise by 0.
	 *
	 * @return result
	 */
	public char[][] rotateC0() {
		return data;
	}

	/**
	 * Rotate the matrix clockwise by pi/2.
	 *
	 * @return result
	 */
	public char[][] rotateC1() {
		int w = data.length;
		int l = data[0].length;
		char result[][] = new char[l][w];

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < l; j++)
				result[j][w - i - 1] = data[i][j];
		}
		return result;
	}

	/**
	 * Rotate the matrix clockwise by pi.
	 *
	 * @return result
	 */
	public char[][] rotateC2() {
		int w = data.length;
		int l = data[0].length;
		char result[][] = new char[w][l];

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < l; j++)
				result[w - i - 1][l - j - 1] = data[i][j];
		}
		return result;
	}

	/**
	 * Rotate the matrix clockwise by 3*pi/2.
	 *
	 * @return result
	 */
	public char[][] rotateC3() {
		int w = data.length;
		int l = data[0].length;
		char result[][] = new char[l][w];

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < l; j++)
				result[l - 1 - j][i] = data[i][j];
		}
		return result;
	}

	public static void printMatrix(char d[][]) {
		System.out.println();
		for (int i = 0; i < d.length; i++)
			System.out.println(Arrays.toString(d[i]));
	}

}
