package puzzleSolver;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import puzzleSolver.Tile;

public class Extract {

	public int numberOfTiles = 0;
	public int numberOfSpace = 0;
	public int numberOfCol = 0;
	public int numberOfRow = 0;
	public int[][] extract = null;

	private configure conf = null;

	private Tile tileboard;

	private List<Tile> tiles;

	private int[][] resultPlane = null;


/**
 * 
 * Extract constructor. Initialize when it is invoked
 * @param tile
 * @param tileList
 * @param config
 */
	public Extract(Tile tile, List<Tile> tileList, configure config) {
		tileboard = tile;
		tiles = tileList;
		conf = config;

		numberOfTiles = tiles.size();
		numberOfSpace = tileboard.plane;
		numberOfCol = numberOfTiles + numberOfSpace;

		resultPlane = buildBoard(tileboard);

		extract = buildArray(tileboard, tiles);

		numberOfRow = extract.length;
		int total_area = 0;
		for (Tile i: tiles) {
			total_area += i.plane;
		}
		if (tileboard.plane > total_area ) {
			conf.setNoSpace(true);
			if (conf.infoExchange)
				System.out.println("No Sufficient tiles.");
		} else {
			conf.setNoSpace(false);
		}
	}


	/**
	 * Build the board
	 *
	 * @param board
	 */
	private int[][] buildBoard(Tile a) {
		int[][] buildBoard= new int[a.characterSet.length][a.characterSet[0].length];
		int b = 0;
		for (int i = 0; i < a.characterSet.length; i++) {
			for (int c = 0; c < a.characterSet[0].length; c++) {
				if (a.characterSet[i][c] != conf.space) {
					buildBoard[i][c] = b;
					b++;
				} else {
					buildBoard[i][c] = -1;
				}
			}
		}

		int z = 0;
		for (int i = 0; i < tiles.size(); i++)
			z = z + tiles.get(i).plane;

		if (b < z) {
			conf.setExcessFinding(true);
		}

		return buildBoard;
	}

	
	 /* 
	 *check if it is allowed to place a tile in a specific position of the board
	 * @return
	 */
	private boolean isPlaceable(char[][] a, char[][] b, int e, int d) {
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				if (b[i][j] != a[e + i][d + j] && b[i][j] != conf.space )
					return false;
			}
		}
		return true;
	}

	/** build the rows of the board
	 
	 * @return
	 */
	private int[] buildRow(int[][] a, char[][] b, int x, int y, int number) {
		
		
		int[] result = new int[numberOfCol];
		result[number] = 1;
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				if (b[i][j] != conf.space) {
					result[numberOfTiles + a[x + i][y + j]] = 1;
				}
			}
		}
		return result;
	}

	/**
	 * Build board array
	 *
	 * @return
	 */
	private int[][] buildArray(Tile plane, List<Tile> tList) {
		List<int[]> build = new ArrayList<int[]>();
		for (int i = 0; i < tList.size(); i++) {
			Tile result = tList.get(i);
	
			int x = 0;
			if (conf.startFlipping())
				x = result.characterSetII.size();
			else if (conf.startRotation())
				x = result.characterSetI.size();
			else
				x = 1;

			for (int j = 0; j < x; j++) {
				char[][] t;
				if (conf.startFlipping())
					t = result.characterSetII.get(j);
				else if (conf.startRotation())
					t = result.characterSetI.get(j);
				else
					t = result.characterSet;

				for (int r = 0; r < plane.characterSet.length - t.length + 1; r++) {
					for (int c = 0; c < plane.characterSet[0].length - t[0].length + 1; c++) {
						if (isPlaceable(plane.characterSet, t, r, c)) {
							int[] row = buildRow(resultPlane, t, r, c, i);
							build.add(row);
						}
					}
				}
			}
		}

		int[][] extract = new int[build.size()][numberOfCol];
		for (int i = 0; i < build.size(); i++) {
			for (int j = 0; j < numberOfCol; j++) {
				if (j < build.get(i).length) {
					extract[i][j] = build.get(i)[j];
				}
			}
		}

		return extract;
	}
}





