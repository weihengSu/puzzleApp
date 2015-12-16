package puzzleSolver;

//Source used: website link: https://code.google.com/p/narorumo/wiki/SudokuDLX

import java.util.Arrays;


public class Matrix {

	private char character_set[][];

	
	//Matrix constructor. Initialize when it is called
	public Matrix(char d[][]) {
	
		character_set = new char[d.length][d[0].length];
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[0].length; j++)
				this.character_set[i][j] = d[i][j];
		}
	}

	/**
	 * rotate a tile by zero degree clockwise.
	 *
	 * 
	 */
	public char[][] rotateZero() {
		int length = character_set[0].length;
		int width = character_set.length;

		char data[][] = new char[width][length];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++)
				data[i][length - j - 1] = character_set[i][j];
		}
		return data;
	}

	/**
	 *  rotate a tile by 90 degrees clockwise 
	 *
	 * 
	 */
	public char[][] rotateHalfPi() {
		int length = character_set[0].length;
		int width = character_set.length;
		char data[][] = new char[length][width];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++)
				data[j][width - i - 1] = character_set[i][length - 1 - j];
		}
		return data;
	}

	/**
	 * rotate a tile by 180 degrees clockwise 
	 *
	 * 
	 */
	public char[][] rotatePi() {
		int length = character_set[0].length;
		int width = character_set.length;
	
		char data[][] = new char[width][length];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++)
				data[width - i - 1][length - j - 1] = character_set[i][length - 1 - j];
		}
		return data;
	}

	/**
	 * 	rotate a tile by 270 degrees clockwise 
	 *
	 * @return 
	 */
	public char[][] rotateOneAndHalfPi() {
		int w = character_set.length;
		int l = character_set[0].length;
		char result[][] = new char[l][w];

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < l; j++)
				result[l - 1 - j][i] = character_set[i][l - 1 - j];
		}
		return result;
	}

	/**
	 *rotate a tile by 0 degree anti-clockwise 
	 *
	 * @return 
	 */
	public char[][] rotateBackZero() {
		return character_set;
	}

	/**
	 *	rotate a tile by 90 degrees anti-clockwise 
	 *
	 * @return
	 */
	public char[][] rotateBackHalfPi() {
		int length = character_set[0].length;
		int width = character_set.length;
	
		char data[][] = new char[length][width];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++)
				data[j][width - i - 1] = character_set[i][j];
		}
		return data;
	}

	/**
	 * rotate a tile by 180 degrees anti-clockwise 
	 *
	 * @return 
	 */
	public char[][] rotateBackPi() {
		int length = character_set[0].length;
		int width = character_set.length;
		
		char data[][] = new char[width][length];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++)
				data[width - i - 1][length - j - 1] = character_set[i][j];
		}
		return data;
	}

	/**
	 * rotate a tile by 270 degrees anti-clockwise 
	 *
	 * @return 
	 */
	public char[][] rotateBackOneAndHalfPi() {
		int length = character_set[0].length;
		int width = character_set.length;
		
		char data[][] = new char[length][width];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++)
				data[length - 1 - j][i] = character_set[i][j];
		}
		return data;
	}

	/**
	 * 
	 * print out matrix 
	 * @param d
	 */
	public static void print(char result[][]) {
		System.out.println();
		for (int i = 0; i < result.length; i++)
			System.out.println(Arrays.toString(result[i]));
	}

}
