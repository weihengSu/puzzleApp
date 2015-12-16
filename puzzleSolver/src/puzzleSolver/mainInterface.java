package puzzleSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import puzzleSolver.Parser;
import puzzleSolver.Tile;


public class mainInterface {

	
	public configure configure = null;

	private Extract extract = null;
	private Link link = null;
	private Search lookup = null;
	private Tile tilePanel = null;
	private List<Tile> tiles = null;


	private List<List<List<Integer>>> result = null;
	private List<int[][]> vLi = null;

	private boolean checkAsym = false;


	public mainInterface(Tile tile, List<Tile> list) {
		tilePanel = tile;
		tiles = list;
		configure = new configure();
		configure.tilesBoard = tile;
		configure.tileL = list;
		configure.infoExchange = false;
		configure.identifyReplicate(tiles);
	    Symmetry.configure = configure;
	}

	public mainInterface(String fp) {
		Parser parse = new Parser(fp);

		tiles = parse.extractPiece();
		tilePanel = tiles.get(0);
		tiles.remove(0);

		configure = new configure();
		configure.tilesBoard = tilePanel;
		configure.tileL = tiles;
		configure.infoExchange = false;
		configure.identifyReplicate(tiles);
	    Symmetry.configure = configure;
	}

	/**
	 * pre-check all things
	 */
	public void preCheck() {
		if (configure.infoExchange) {
			tilePanel.printTile();
			for (Tile t: tiles) t.printTile();
		}

		configure.selfReduceReplication();
		result = new ArrayList<List<List<Integer>>>();
		vLi = new ArrayList<int[][]>();

		extract = new Extract(tilePanel, tiles, configure);
		link = new Link(extract, configure);
		lookup = new Search(link, configure);
	}

	/**
	 *
	 * find next available solution and check
	 *
	 */
	public List<List<Integer>> findNext() {
		List<List<Integer>> solution = lookup.solveSingleSolution();
		if (solution != null) {
			if (configure.noSymmetry() && tilePanel.characterSetII.size() != 8 && !configure.reduceSymmetry()) {
				int a[][] = Symmetry.view(solution);
				if (vLi.size() == 0) {
					result.add(solution);
					vLi.add(a);
				} else {
	
					while (!Symmetry.asymList(a, vLi)) {
						solution = lookup.solveSingleSolution();
						if (solution != null)
							a = Symmetry.view(solution);
						else break;
					}
					if (solution != null) {
						result.add(solution);
						vLi.add(a);
					}
				}
			}
			else { 
				result.add(solution);
			}
		}
		return solution;
	}

	/**
	 * Solve with only a single step search.
	 * @return a partial solution
	 */
	public List<List<Integer>> nextStep() {
		checkAsym = false;
		List<List<Integer>> step = lookup.solveSingleStep();

		if (checkLastSolution()) {

			if (configure.noSymmetry() && tilePanel.characterSetII.size() != 8 && !configure.reduceSymmetry()) {
				int a[][] = Symmetry.view(step);
				if (vLi.size() == 0) {
					result.add(step);
					vLi.add(a);
				} else {
			
					if (Symmetry.asymList(a, vLi)) {
						result.add(step);
						vLi.add(a);
					} else {
						checkAsym = true;
					}
				}
			} else { 
				result.add(step);
			}
		}
		return step;
	}



	/**
	 * Reset the search
	 */
	public void reSearch() {
		lookup.reset();
		if (result != null) result.clear();
		configure.reset();
		if (configure.infoExchange) System.out.println("DLX search has been reset.");
	}

	/**
	 * Determine if the last single step search finds a complete solution.
	 * @return
	 */
	public boolean checkLastSolution() {
		return lookup.checkFinish() && !checkAsym;
	}



	public List<List<List<Integer>>> getSolutions(){
		return result;
	}
}

	

