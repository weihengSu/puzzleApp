package puzzleSolver;

//External source used: website link: https://code.google.com/p/narorumo/wiki/SudokuDLX

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Parser {
	

	private String path = " ";
	public Parser(String n) {
		path = n;
	}

	/**
	 * Read file and parse a file into app. 
	 *
	 */
	private String[] readFile() {
		File file = new File(path);
		FileReader file_reader;
		try {
			file_reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			
			return new String[0];
		}
		BufferedReader buffer_reader = new BufferedReader(file_reader);
		List<String> lines = new ArrayList<String>();
		String line;
		try {
			line = buffer_reader.readLine();
			while (line != null) {
				lines.add(line);
				line = buffer_reader.readLine();
			}
		} catch (IOException e) {
			
			System.out.println("Parsing error. Please re-try");
		}
		try {
			buffer_reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines.toArray(new String[lines.size()]);
	}

	/**
	 * Extract puzzle tiles
	 *
	 */
	public List<Tile> extractPiece() {

		String[] li = readFile();
		List<Tile> tList = new ArrayList<Tile>();

		int ro = li.length + 2;
		int co = 0;
		for (int row = 0; row < li.length; row++) {
			if (li[row].length() > co)
				co = li[row].length();
		}
		co = co +  2;
		char[][] charArrayI = new char[ro][co]; 
		char[][] charArrayII = new char[ro][co]; 
		for (int row = 0; row < ro; row++) {
			for (int col = 0; col < co; col++) {
				charArrayI[row][col] = ' ';
				charArrayII[row][col] = ' ';
			}
		}
		for (int row = 0; row < li.length; row++) {
			for (int col = 0; col < li[row].length(); col++) {
				charArrayI[row + 1][col + 1] = li[row].charAt(col);
			}
		}

		for (int row = 1; row < ro - 1; row++) {
			for (int col = 1; col < co - 1; col++) {
				if (charArrayI[row][col] != ' ') {
					backUpTile(charArrayI, charArrayII, row, col, row);
					Tile tile = createTileArray(charArrayII);
					tList.add(tile);
				}
			}
		}
		Tile candidates[] = new Tile[tList.size()];
		for (int i = 0; i < tList.size(); i++)
			candidates[i] = tList.get(i);

		Arrays.sort(candidates);
		tList = new ArrayList<Tile>(Arrays.asList(candidates));

		return tList;
	}


	/**
	 * Print out array
	 *
	 * @param buf
	 */
	public void printArray(char[][] result) {
		for (int i = 0; i < result[0].length + 2; i++) {
			System.out.print("-");
		}
		System.out.println();
		for (int i = 0; i < result.length; i++) {
			System.out.print("|");
			for (int j = 0; j < result[0].length; j++) {
				System.out.print(result[i][j]);
			}
			System.out.println("|");
		}
		for (int i = 0; i < result[0].length + 2; i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	/**
	 * re-copy all files
	 *
	
	 * 
	 */
	private void backUpTile(char[][] a, char[][] b, int c, int d, int ros) {

		if (a[c][d] != ' ') {
			b[c - ros][d] = a[c][d];
			a[c][d] = ' ';
		}
		if (a[c][d + 1] != ' ')
			backUpTile(a, b, c, d + 1, ros);
		if (a[c][d - 1] != ' ')
			backUpTile(a, b, c, d - 1, ros);
		if (a[c + 1][d] != ' ')
			backUpTile(a, b, c + 1, d, ros);
		if (a[c - 1][d] != ' ')
			backUpTile(a, b, c - 1, d, ros);
	}

	/**
	 * Create a 2d tile array
	 *
	 * @param buf_p
	 * @return
	 */
	private Tile createTileArray(char[][] data) {
		int offset_col = -1;
		for (int col = 0; col < data[0].length; col++) {
			for (int row = 0; row < data.length; row++) {
				if (data[row][col] != ' ') {
					if (offset_col < 0)
						offset_col = col;
					if (offset_col > 0) {
						data[row][col - offset_col] = data[row][col];
						data[row][col] = ' ';
					}
				}
			}
		}
		int heightSize = 0;
		for (int row = data.length - 1; row >= 0; row--) {
			for (int col = data[0].length - 1; col >= 0; col--) {
				if (data[row][col] != ' ') {
					heightSize = row + 1;
					break;
				}
			}
			if (heightSize > 0)
				break;
		}
		int width_size= 0;
		for (int col = data[0].length - 1; col >= 0; col--) {
			for (int row = data.length - 1; row >= 0; row--) {
				if (data[row][col] != ' ') {
					width_size = col + 1;
					break;
				}
			}
			if (width_size > 0)
				break;
		}

		
		char[][] d = new char[heightSize][width_size];
		for (int row = 0; row < heightSize; row++) {
			for (int col = 0; col < width_size; col++) {
				d[row][col] = data[row][col];
				data[row][col] = ' ';
			}
		}

		return new Tile(d);
	}



}