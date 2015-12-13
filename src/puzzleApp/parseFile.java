package puzzleApp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * 
 * Sources: readFile method came from Professor Aarron Bloomfield's c++ slides: https://github.com/aaronbloomfield/pdr
 * 
 * 
 * 
 * @author ws2ta
 *
 */
public class parseFile {
	//private static char space= ' ';
	private String path = " ";

	public parseFile(String file_path){
		path = file_path;
	}

	/**
	 * Read puzzle file and return a String array.
	 *
	 * @param puzzle_file
	 * @return String array represents the tiles and a board.
	 */
	private String[] readFile() {
		File newFile = new File(path);
		FileReader fileReader;
		try {
			fileReader = new FileReader(newFile);
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist!");
			return new String[0];
		}
		BufferedReader bufferReader = new BufferedReader(fileReader);
		List<String> readLine = new ArrayList<String>();
		String l;
		try {
			l = bufferReader.readLine();
			while (l != null) {
				readLine.add(l);
				l = bufferReader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Can't read you file. Please try again!");
		}
		try {
			bufferReader.close();
		} catch (IOException e) {
			System.out.println("Can't read you file. Please re-try!");
		}
		return readLine.toArray(new String[readLine.size()]);
	}

  /**
   * 
   * print characters 
   * 
   * 
   */
	public void printFile(char[][] charFile) {
		for (int i = 0; i < charFile[0].length + 2; i++) {
			System.out.print("-");
		}
		System.out.println();
		for (int i = 0; i < charFile.length; i++) {
			System.out.print("|");
			for (int j = 0; j < charFile[0].length; j++) {
				System.out.print(charFile[i][j]);
			}
			System.out.println("|");
		}
		for (int i = 0; i < charFile[0].length + 2; i++) {
			System.out.print("-");
		}
		System.out.println();
	}
/**
 * 
 * make a copy of Tile
 * 
 * 
 */

	private void tileReplicate(char[][] rep, char[][] rel_a, int a, int b,
			int c) {

		if (rep[a][b] != ' ') {
			rel_a[a - c][b] = rep[a][b];
			rep[a][b] = ' ';
		}
		else if (rep[a][b + 1] != ' ') {
			tileReplicate(rep, rel_a, a, b + 1, c);
			}
		else if (rep[a][b - 1] != ' ') {
			tileReplicate(rep, rel_a, a, b - 1, c);
		}
		else if (rep[a + 1][b] != ' ') {
		   tileReplicate(rep, rel_a, a + 1, b, c);
		}
		else if (rep[a - 1][b] != ' '){
			
			tileReplicate(rep, rel_a, a - 1, b, c);
	 }
		else{
			
		}
	}

	/**
	 * 
	 *
	 * create an array
	 * @return
	 */
	private Tile cropTile(char[][] charFile) {
		/* move to leftmost */
		int c = -1;
		for (int i = 0; i < charFile[0].length; i++) {
			for (int j = 0; j < charFile.length; j++) {
				if (charFile[j][i] != ' ') {
					if (c < 0)
						c = i;
					if (c > 0) {
					
						charFile[i][j - c] = charFile[j][i];
						charFile[j][i] = ' ';
					}
				}
			}
		}
		/* calculate tile size */
		int piece_h = 0;
		for (int row = charFile.length - 1; row >= 0; row--) {
			for (int col = charFile[0].length - 1; col >= 0; col--) {
				if (charFile[row][col] != ' ') {
					piece_h = row + 1;
					break;
				}
			}
			if (piece_h > 0)
				break;
		}
		int piece_w = 0;
		for (int col = charFile[0].length - 1; col >= 0; col--) {
			for (int row = charFile.length - 1; row >= 0; row--) {
				if (charFile[row][col] != ' ') {
					piece_w = col + 1;
					break;
				}
			}
			if (piece_w > 0)
				break;
		}

		/* create 2d array for a tile */
		char[][] data = new char[piece_h][piece_w];
		for (int row = 0; row < piece_h; row++) {
			for (int col = 0; col < piece_w; col++) {
				data[row][col] = charFile[row][col];
				charFile[row][col] = ' ';
			}
		}

		return new Tile(data);
	}

	/**
	 * Extract puzzle pieces from the input String array.
	 *
	 * @param lines
	 *            Lines contain characters.
	 * @return A list includes all tiles.
	 */
	public List<Tile> ExtractTiles() {

		String[] lines = readFile();
		/* Output tile list which includes all tiles and the board. */
		List<Tile> tiles = new ArrayList<Tile>();

		/* convert string array to 2-D char array with margin. */
		int buf_rows = lines.length + 2;
		int buf_cols = 0;
		for (int row = 0; row < lines.length; row++) {
			if (lines[row].length() > buf_cols)
				buf_cols = lines[row].length();
		}
		buf_cols += 2;
		char[][] buf = new char[buf_rows][buf_cols]; 
		char[][] buf_p = new char[buf_rows][buf_cols]; 
		for (int row = 0; row < buf_rows; row++) {
			for (int col = 0; col < buf_cols; col++) {
				buf[row][col] = ' ';
				buf_p[row][col] = ' ';
			}
		}
		for (int row = 0; row < lines.length; row++) {
			for (int col = 0; col < lines[row].length(); col++) {
				buf[row + 1][col + 1] = lines[row].charAt(col);
			}
		}

		/* Find and add tiles. */
		for (int row = 1; row < buf_rows - 1; row++) {
			for (int col = 1; col < buf_cols - 1; col++) {
				if (buf[row][col] != ' ') {
					tileReplicate(buf, buf_p, row, col, row);
					Tile tile = cropTile(buf_p);
					tiles.add(tile);
				}
			}
		}
		Tile candidates[] = new Tile[tiles.size()];
		for (int i = 0; i < tiles.size(); i++)
			candidates[i] = tiles.get(i);

		Arrays.sort(candidates);
		tiles = new ArrayList<Tile>(Arrays.asList(candidates));

		return tiles;
	}

}
