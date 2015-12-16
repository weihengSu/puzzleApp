package puzzleSolver;
//external source used: website link: https://code.google.com/p/narorumo/wiki/SudokuDLX
import java.util.ArrayList;
import java.util.List;


public class Search {


	private Trail stack = null;
	private Trail Solution = null;
	private configure conf = null;


	private Link link = null;





	/**
	 * search constructor. Invoked when it is called
	 */
	public Search(Link l, configure c) {
		link = l;
		conf = c;
		stack = new Trail();
		Solution = new Trail();
	}

	/**
	 * find all solutions
	 * 
	 */
	public List<List<List<Integer>>> solve() {
		conf.setOneSearch(false);
		conf.setOneSolSearch(false);

		List<List<List<Integer>>> result = new ArrayList<List<List<Integer>>>();
		while (!conf.completeSearch()) {
			solveSingleSolution();
			if (Solution.size() > 0) {
				result.add(solToBoard(Solution));
			}
		}
		return result;
	}

	/**
	 * find a single solution. 
	 * @return a valid solution
	 */
	public List<List<Integer>> solveSingleSolution() {
		conf.setOneSolSearch(true);
		search(stack);
		return solToBoard(Solution);
	}

	/**
	 * search in one single step
	 * method won't work if a solution rotates or reflects, which requires multiple steps
	 */
	public List<List<Integer>> solveSingleStep() {
		conf.setOneSearch(true);
		search(stack);
		return solToBoard(Solution);
	}

	/**
	 * Reset the search. waiting for next file input.
	 */
	public void reset() {
		stack.clear();
		Solution.clear();
		conf.setCompleteSearch(false);
	}

	/**
	 * check if the last solution is found.
	 * @return
	 */
	public boolean checkFinish() {
		return Solution.finish();
	}


	/**
	 * Determine whether a duplicated tile has been used or tried.
	 * @return
	 */
	private boolean checkDupTileUsed(Trail trail) {
		
		int i = trail.size() - 1;
	
			Space x = trail.get(i);
			if (conf.replication()[x.tile_name] != x.tile_name) {
				int j = x.tile_name;
				while (j > conf.replication()[j]) {
					j = conf.replication()[j];
					if (link.check(j))
						return false;
				}
			}
	
		return true;
	}

	/**
	 * Push all the choices for next level search.
	 * So duplication can be eliminated here.
	 * @param trail
	 * @param c
	 */
	private void pushNextLevelSearch(Trail sp, Column col) {
		for (Space i = col.top; i != col; i = i.top) {
			sp.push(i);
			if (conf.reduceReplication()) {
				if (!checkDupTileUsed(sp))
					sp.pop();
			}
		}
	}

	/**
     check whether the first level of search is symmetric
	 * @param trail
	 * @param x
	 * @return
	 */
	private boolean checkSymmetric(Trail a, Space sp) {
	Trail t1 = new Trail();
		t1.push(sp);
		List<List<Integer>> s1 = solToBoard(t1);
		int[][] v1 = Symmetry.view(s1);
		for (int i = 0; i < a.size(); i++) {
			Trail t2 = new Trail();
			t2.push(a.get(i));
			List<List<Integer>> s2 = solToBoard(t2);
			int[][] v2 = Symmetry.view(s2);

			if (!Symmetry.isAsym(v1, v2)) {
				return true;
			}
		}
		return false;
	}

	/**
     pile up all of the searches in the first level together
	 * @param c
	 */
	private void saveSearch(Trail trail, Column c) {
		if (!conf.noSymmetry() || conf.startExcessFinding() ||
				conf.getfirstTile() < 0) {
			pushNextLevelSearch(trail, c);
			return;
		}

		int cnt = 0;
		for (Space i = c.top; i != c; i = i.top) {
			if (!checkSymmetric(trail, i)) {
				trail.push(i);
				cnt++;
			}
		}
		conf.setReduceSymmetry(true);
	
	}

	/**
	 * select a column to begin searching
	 *
	 
	 */
	private Column selectCol() {
		boolean a = true;
		Column col = link.head.right;

		if (a) {
			int s = Integer.MAX_VALUE;
			for (Column i = col; i != link.head; i = i.right) {
				if (i.col < link.numberofTile && conf.startExcessFinding()  ) {
					continue;
				}
				if (conf.reduceReplication() && i.col < link.numberofTile) {
					if(conf.replication()[i.col] != i.col) {
						continue;
					}
				}
				if (s > i.size ) {
					col = i;
					s = i.size;
				}
			}
		} 
		else {
			if (conf.startExcessFinding()) {
				while (col.col >= 0 && col.col < link.numberofTile) {
					col = col.right;
				}
			}

			if (conf.reduceReplication()) {
				while (col.col >= 0 && col.col < link.numberofTile && conf.replication()[col.col] != col.col){
					col = col.right;
				}
			}
		}
		return col;
	}

	/**
	 * Select first column to begin
	 *
	 * 
	 */
	private Column selectColOne() {
		if ( conf.startExcessFinding()||(!conf.noSymmetry() )) {
			return selectCol();
		}
		int a = conf.getfirstTile();
		if (a >= 0) {
			for (Column i = link.head.right; i != link.head; i = i.right) {
				if (i.col == a) {
					return i;
				}
				else if (i.col > a) {
					break;
				}
			}
		}
		return selectCol();
	}

	/**
	 * The method is used to cover column
	 *
	 * @param col
	 */
	private void cover(Column col) {
		col.right.left = col.left;
		col.left.right = col.right;
		for (Space i = col.down; i != col; i = i.down) {
			for (Space j = i.right; j != i; j = j.right) {
				j.down.top = j.top;
				j.top.down = j.down;
				j.column.size -= 1;
			}
		}
	}

	/**
	 * Like the one above, but this method is used to uncover Column 
	 *
	 * @param col
	 */
	private void uncover(Column col) {
	
		for (Space i = col.top; i != col; i = i.top) {
			for (Space j = i.left; j != i; j = j.left) {
				j.column.size += 1;
				j.down.top = j;
				j.top.down = j;
			}
		}
		col.right.left = col;
		col.left.right = col;
	}

	/**
	 * Searching 
	 *
	 * @param trail
	 */
	private void search(Trail trail) {
		Solution.setFinish(false);

	
		if (conf.checkUnsucess()) {
			conf.setCompleteSearch(true);
			return;
		}

		
		if (!conf.completeSearch() && trail.isEmpty()) {
			Column c = selectColOne();
			saveSearch(trail, c);
		}
		do {
			while (!Solution.isEmpty() && Solution.top() == trail.top()) {
				Space t = Solution.pop();
				trail.pop();
				for (Space i = t.left; i != t; i = i.left) {
					uncover(i.column);
				}
				uncover(t.column);
			}
			if (trail.size() == 0) {
				conf.setCompleteSearch(true);
				break; 
			}

			Space x = trail.top();
			Solution.push(x);
			cover(x.column);
			for (Space i = x.right; i != x; i = i.right) {
				cover(i.column);
			}

			Column c = selectCol();
			if (c.size > 0) {
				pushNextLevelSearch(trail, c);
				continue;
			} else {
			
				if (link.head.right == link.head || link.head.left.col < link.numberofTile) {
					if (conf.infoExchange) {
						Solution.print();
					}
					Solution.setFinish(true);
				}
			}
		} while (!conf.oneSearch() && !(conf.oneSolSearch() && Solution.finish()));

		return;
	} 

	/**
	 * print out a solution into board
	 * @param solution
	 * @return
	 */
	public List<List<Integer>> solToBoard(Trail trail) {
		if (trail.size() == 0) return null;

		List<List<Integer>> list = new ArrayList<List<Integer>>();
		for (int i = 0; i < trail.size(); i++) {
			List<Integer> listI = new ArrayList<Integer>();
			Space sp = trail.get(i);
			while (sp.left.col < sp.col) sp = sp.left;


			listI.add(sp.column.col);
			for (Space j = sp.right; j != sp; j = j.right) {
				listI.add(j.column.col - link.numberofTile);
			}

			list.add(listI);
		}
		return list;
	}

}
