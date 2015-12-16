package puzzleSolver;

//external source used: website link: https://code.google.com/p/narorumo/wiki/SudokuDLX
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Tile implements Comparable<Tile> {

	public char[][] characterSet;

	public List<char[][]> characterSetI = new ArrayList<char[][]>();

	
	public List<char[][]> characterSetII = new ArrayList<char[][]>();

	public int width;
	public int length;

	public int plane;

	private int number = -1;
	private int replicate = -1;
	private int replicateI= -1;
	private int replicateII = -1;
	public void setNumber(int a) {
		number = a;
		}
	public void setReplicate(int a) { 
		replicate = a;
		}
	public void setReplicateI(int a) { 
		replicateI = a;
		}
	public void setReplicateII(int a) { 
		replicateII = a;
		}
	public int getNumber() { 
		return number; 
		}
	public int getReplicate() { 
		return replicate; 
		}
	public int getReplicateI() {
		return replicateI; 
		}
	public int getReplicateII() {
		return replicateII; 
		}
/*
 * 
 * Tile constructor. Invoked if it is called
 * 
 */
	public Tile(char[][] tile) {
		width = tile.length;
		length = tile[0].length;
		characterSet = new char[width][length];
		plane = 0;

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < width; j++) {
				characterSet[j][i] = tile[j][i];
				if (tile[j][i] != ' ')
					plane++;
			}
		}
		Matrix matrix = new Matrix(characterSet);
	
		List<char[][]> patterns = new ArrayList<char[][]>();
		patterns.add(matrix.rotateBackZero());
		patterns.add(matrix.rotateBackHalfPi());
		patterns.add(matrix.rotateBackPi());
		patterns.add(matrix.rotateBackOneAndHalfPi());
		patterns.add(matrix.rotateZero());
		patterns.add(matrix.rotateHalfPi());
		patterns.add(matrix.rotatePi());
		patterns.add(matrix.rotateOneAndHalfPi());
		int x = 0;
		characterSetI.add(patterns.get(0));
		for (int i = 1; i < 4; i++) {
			boolean e = true;
			for (int j = 0; j <= x; j++) {
	
				if (equal(characterSetI.get(j), patterns.get(i))) {
					e = false;
					break;
				}
			}
			if (e) {
				characterSetI.add(patterns.get(i));
				x++;
			}
		}

	
		x = 0;
		characterSetII.add(patterns.get(0));
		for (int i = 1; i < 8; i++) {
			boolean e = true;
			for (int j = 0; j <= x; j++) {

				if (equal(characterSetII.get(j), patterns.get(i))) {
					e = false;
					break;
				}
			}
			if (e) {
				characterSetII.add(patterns.get(i));
				x++;
			}
		}
	}
/**
 * 
 * print tiles
 * 
 */
   public void printTile() {
		for (int i = 0; i < width; i++)
			System.out.println(Arrays.toString(characterSet[i]));
		System.out.println();
	} 

	@Override
	/**
	 * sort all tiles
	 */
	public int compareTo(Tile tile) {
	
		return tile.plane - plane;
	}

	/**
	 * check whether 2 char arrays are equal

	 * @return
	 */
	public boolean equal(char[][] x, char[][] y) {
		if (x.length != y.length || x[0].length != y[0].length)
			return false;
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				if (x[i][j] != y[i][j])
					return false;
			}
		}
		return true;
	}

	/**
	 * check whether a tile is equal to current tile (without rotation or reflection

	 */
	public boolean equal(Tile a) {
		if (plane != a.plane) return false;
		return equal(characterSet, a.characterSet);
	}

	/**
	 * check whether a tile is equal to current tile when rotations exist

	 */
	public boolean equalI(Tile a) {
		if (plane != a.plane) return false;
		if (characterSetI.size() != a.characterSetI.size()) return false;
		for (int i = 0; i < characterSetI.size(); i++) {
			if (equal(characterSetI.get(i), a.characterSetI.get(0)))
				return true;
		}
		return false;
	}

	/**
	 *check whether a tile is equal to current tile when rotations and reflection exist
	 */
	public boolean equalII(Tile a) {
		if (plane != a.plane) return false;
		if (characterSetII.size() != a.characterSetII.size()){
			return false;
		}
		for (int i = 0; i < characterSetII.size(); i++) {
			if (equal(characterSetII.get(i), a.characterSetII.get(0)))
				return true;
		}
		return false;
	}
/**
 * 
 * 
 * check whether a tile list is able to be shown/packed in the tile panel
 * @param resultList
 * @param a
 * @param b
 * @param c
 * @return
 */
	private static boolean isPackable(List<List<Integer>> resultList, Tile a, int b, int c) {
		int tile_length = a.characterSet.length;
		int tile_width = a.characterSet[0].length;
	
		for (int i = 0; i < tile_length; i++) {
			List<Integer> l = resultList.get(b + i);
			if ((l.get(c) != 0) || (l.get(c + tile_width - 1) != 0)|| (l.get(c + tile_width) != 0 && l.get(c + tile_width) != -2))
				return false;
		}
		for (int i = 0; i < tile_width; i++) {
			int z = b + tile_length;
			if ((resultList.get(b).get(c + i) != 0)|| (resultList.get(z - 1).get(c + i) != 0)|| (resultList.get(z).get(c + i) != 0 && resultList.get(z).get(c + i) != -2)){
				return false;
			}
		}
		int z = b + tile_length;
		if (resultList.get(z).get(c + tile_width) != 0 && resultList.get(z).get(c + tile_width) != -1){
			return false;
		}
		return true;
	}
	
	/*
	 * 
	 * pack a single tile in the tile panel
	 * 
	 */
	private static boolean packSingleTile(List<List<Integer>> resultList, Tile tile, int a) {
		int size = resultList.size();
		int sizeI = resultList.get(0).size();
		int tile_length = tile.characterSet.length;
		int tile_width = tile.characterSet[0].length;

		for (int i = 0; i < size - tile_length; i++) {
			for (int j = 0; j < sizeI - tile_width; j++) {
				if (isPackable(resultList, tile, i, j)) {
	
					for (int m = 0; m < tile_length; m++) {
						for (int n = 0; n < tile_width; n++) {
							resultList.get(i + m).set(j + n, a + 1);
						}
					}
					
					for (int m = i - 1; m <= i + tile_length; m++) {
						resultList.get(m).set(j - 1, -2);
						resultList.get(m).set(j + tile_width, -2);
					}
					for (int m = j - 1; m <= j + tile_width; m++) {
						resultList.get(i - 1).set(m, -2);
						resultList.get(i + tile_length).set(m, -2);
					}
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * 
	 * increase Tile array sizes
	 */
	
	private static void increaseSize(List<List<Integer>> resultList, double rat) {
		int size = resultList.size();
		int sizeI = resultList.get(0).size();

		if ((double)size / sizeI <= rat) { 
			resultList.add(new ArrayList<Integer>());
			resultList.get(size).add(-2);
			for (int i = 1; i < sizeI; i++) resultList.get(size).add(0);
		} else { 
			resultList.get(0).add(-2);
			for (int i = 1; i < size; i++) resultList.get(i).add(0);
		}
	
	}

	private static int[][] packToArray(List<List<Integer>> p) {
		int h = p.size();
		int w = p.get(0).size();
		int[][] a = new int[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				a[i][j] = p.get(i).get(j);
			}
		}
		return a;
	}

	/**
	 * show tiles in the board and change the size of tiles 
	 * @return
	 */
	public static int[][] packTiles(List<Tile> a, double rat) {
		List<List<Integer>> resultList = new ArrayList<List<Integer>>();
		resultList.add(new ArrayList<Integer>());

		for (int i = 0; i < a.size(); i++) {
			while (!packSingleTile(resultList, a.get(i), i))
				increaseSize(resultList, rat);
			
		}

	
		resultList.remove(0);
		resultList.remove(resultList.size() - 1);
		for (int i = 0; i < resultList.size(); i++) {
			resultList.get(i).remove(0);
			resultList.get(i).remove(resultList.get(i).size() - 1);
		}
		for (int i = resultList.size() - 1; i >= 0; i--) {
			boolean bool = true;
			for (int j = 0; j < resultList.get(0).size(); j++) {
				if (resultList.get(i).get(j) != 0 && resultList.get(i).get(j) != -2) {
					bool = false;
					break;
				}
			}
			if (bool) resultList.remove(i);
			else break;
		}
		for (int i = resultList.get(0).size() - 1; i >= 0; i--) {
			boolean boolI = true;
			for (int j = 0; j < resultList.size(); j++) {
				if (resultList.get(j).get(i) != 0 && resultList.get(j).get(i) != -2) {
					boolI = false;
					break;
				}
			}
			if (boolI) {
				for (int j = 0; j < resultList.size(); j++)
					resultList.get(j).remove(resultList.get(j).size() - 1);
			}
			else break;
		}

		for (int i = 0; i < resultList.size(); i++) {
			for (int j = 0; j < resultList.get(i).size(); j++) {
				int k = resultList.get(i).get(j);
				if (k <= 0) resultList.get(i).set(j, -1);
				else resultList.get(i).set(j, k - 1);
			}
		}

		return packToArray(resultList);
	}




}
