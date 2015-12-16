package puzzleSolver;

//source used: website link: https://code.google.com/p/narorumo/wiki/SudokuDLX

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Symmetry {


	public static configure configure;





	/**
	 * check if there is a solution if we rotate the tile by zero degree
	 *
	 * 
	 */
	private static boolean rotateByZero(int list[][], int a[][]) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		int li = list.length;
		int liII= list[0].length;

		for (int i = 0; i < li; i++) {
			for (int j = 0; j < liII; j++) {
				int t1 = a[i][liII - j - 1];
				int t2 = list[i][j];
				if (!checkMatch(t1, t2, map)){ return false;}
			}
		}
		return true;
	}

	/**
	 * check whether there is a solution if we rotate the tile by 90 degrees
	 *
	 * 
	 */
	private static boolean rotateByHalfPi(int list[][], int a[][]) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		int li = list.length;
		int liII = list[0].length;

		for (int i = 0; i < li; i++) {
			for (int j = 0; j < liII; j++) {
				int t1 = a[liII - j - 1][li - i - 1];
				int t2 = list[i][j];
				if (!checkMatch(t1, t2, map)){ return false;}
			}
		}
		return true;
	}

	/**
	 * check whether there is a solution if we rotate the tile by 180 degrees
	 *
	 * 
	 */
	private static boolean rotateByPi(int list[][], int a[][]) {
		
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		int li = list.length;
		int liII = list[0].length;

		for (int i = 0; i < li; i++) {
			for (int j = 0; j < liII; j++) {
				int t1 = a[li - i - 1][j];
				int t2 = list[i][j];
				if (!checkMatch(t1, t2, map)){ return false;}
			}
		}
		return true;
	}

	/**
	 * check whether there is a solution if we rotate the tile by 180 degrees
	 *
	 * 
	 */
	private static boolean rotateByOneAndHalfPi(int list[][], int a[][]) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		int li = list.length;
		int liII = list[0].length;

		for (int i = 0; i < li; i++) {
			for (int j = 0; j < liII; j++) {
				int t1 = a[j][i];
				int t2 = list[i][j];
				if (!checkMatch(t1, t2, map)){ return false;}
			}
		}
		return true;
	}

	/**
	 * check whether there is a solution if we rotate the tile anti-clockwise by 0 degree
	 *
	 * 
	 */
	private static boolean rotateBackByZero(int list[][], int a[][]) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		int li = list.length;
		int liII = list[0].length;

		for (int i = 0; i < li; i++) {
			for (int j = 0; j < liII; j++) {
				int t1 = a[i][j];
				int t2 = list[i][j];
				if (!checkMatch(t1, t2, map)){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * check whether there is a solution if we rotate the tile anti-clockwise by 90 degrees
	 *
	 * 
	 */
	private static boolean rotateBackByHalfPi(int list[][], int a[][]) {

		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		int li = list.length;
		int liII = list[0].length;

		for (int i = 0; i < li; i++) {
			for (int j = 0; j < liII; j++) {
				int t1 = a[j][li - i - 1];
				int t2 = list[i][j];
				if (!checkMatch(t1, t2, map)) {
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * check whether there is a solution if we rotate the tile anti-clockwise by 180 degrees
	 *
	 * 
	 */
	private static boolean rotateBackByPi(int list[][], int a[][]) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		int li = list.length;
		int liII = list[0].length;

		for (int i = 0; i < li; i++) {
			for (int j = 0; j < liII; j++){
				int t1 = a[li - i - 1][liII - j - 1];
				int t2 = list[i][j];
				if (!checkMatch(t1, t2, map)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * check whether there is a solution if we rotate the tile anti-clockwise by 270 degrees
	 *
	 * 
	 */
	private static boolean rotateBackByOneAndHalfPi(int list[][], int a[][]) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		int li = list.length;
		int liII = list[0].length;

		for (int i = 0; i < li; i++) {
			for (int j = 0; j < liII; j++) {
				int t1 = a[liII - j - 1][i];
				int t2 = list[i][j];
				if (!checkMatch(t1, t2, map)) {
					return false;
				}
			}
		}
		return true;
	}
	

	/**
	 * check if there are two asymmetric solutions

	 */
	public static boolean isAsym(int list[][], int a[][]) {

		
		if (list.length == list[0].length) {
			if (rotateByZero(list, a)||rotateBackByZero(list, a)||rotateByHalfPi(list, a)||rotateBackByHalfPi(list, a)||rotateByPi(list, a)||rotateBackByPi(list, a)||rotateByOneAndHalfPi(list, a)||rotateBackByOneAndHalfPi(list, a)){
				return false;
			}
			else{
				return true;
		}
		}

		else {
			if (rotateBackByZero(list, a)||rotateByZero(list, a)||rotateBackByPi(list, a)||rotateByPi(list, a))
			{		return false;}
			else{
				return true;}
		}

	}

	/**
	 * check if there are two symmetric solutions in a list(file)
	 * @return
	 */
	public static boolean asymList(int list[][], List<int[][]> a) {
		for (int i = 0; i < a.size(); i++) {
			if (!isAsym(list, a.get(i))) {
				if (configure.infoExchange) {System.out.println("It is symmetric .");}
				return false;
			}
		}
		return true;
	}

	
	

	/**
	 * the method is used to transform 2 dimensional matrix to 2d plane
	 * @param solution
	 * @return
	 */
	public static int[][] view(List<List<Integer>> s) {
		int[] location = new int[configure.tilesBoard.plane];
		for (int i = 0; i < configure.tilesBoard.plane; i++) {
			location[i] = -1;
		}
		for (int i = 0; i < s.size(); i++) {
			int index = s.get(i).get(0);
			for (int j = 1; j < s.get(i).size(); j++) {
				location[s.get(i).get(j)] = index;
			}
		}

		int a = configure.tilesBoard.characterSet.length;
		int b = configure.tilesBoard.characterSet[0].length;
		int[][] c = new int[a][b];
		int d = 0;
		for (int i = 0; i < a; i++) {
			for (int j = 0; j < b; j++) {
				if (configure.tilesBoard.characterSet[i][j] != ' ') {
					c[i][j] = location[d];
					d++;
				} else {
					c[i][j] = -2;
				}
			}
		}

		return c;
	}
	
	/**
	 * check whether two solutions are matched or not
	 *
	 * 
	 */
	private static boolean checkMatch(int a, int b, HashMap<Integer, Integer> map) {
		if (configure.reduceReplication() && a >= 0) {
			if (configure.replication()[a] == a) { 
				if (a != b) {
					return false;
				}
			} 
			else { 
				if (map.containsKey(a)) {
					if (map.get(a) != b) {
						return false;
					}
				} else {
					boolean like = false;
					if (a == b) { 
						like = true;
						map.put(a, b);
					} 
					else {
						for (int k = configure.replication()[a]; k != a; k = configure.replication()[k]) {
						if (k == b) {
							like = true;
							map.put(a, b);
							break;
						}
					}
					if (!like) return false;
				}
			}
			}
		} else { 
			if (a != b) return false;
		}
		return true;
	}

	
	
	
	
/**
 * 
 * print out the solutions to the board
 * @param d
 */
	public static void output(int d[][]) {
		System.out.println();
		for (int i = 0; i < d.length; i++)
			System.out.println(Arrays.toString(d[i]));
	}

}
