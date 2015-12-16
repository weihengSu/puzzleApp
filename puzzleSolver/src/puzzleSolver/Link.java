package puzzleSolver;


//source used: website link: https://code.google.com/p/narorumo/wiki/SudokuDLX
import puzzleSolver.Extract;
import puzzleSolver.Space;
import puzzleSolver.Column;
import puzzleSolver.configure;

public class Link {
	private configure conf = null;

	private Extract extract = null;

	private Column[] columnMatrix = null;
	private Space[][] spaceMatrix = null;

	public Column head = null;
	public int numberofTile = 0;


	public int numberofSpace = 0;

	public int numofCol = 0;

	public int numofRow = 0;

	
	
	
	public Link(Extract a, configure b) {
		extract = a;
		conf = b;
		numberofTile = extract.numberOfTiles;
		numberofSpace = extract.numberOfSpace;
		numofCol = extract.numberOfCol;
		numofRow = extract.numberOfRow;

		columnMatrix = new Column[numofCol];
		spaceMatrix = new Space[numofRow][numofCol];

		head = init();



		select();
	}

	/**
	   verify whether a column is readable or reachable
	 */
	public boolean check(int columns) {
	  
		for (Column i = head.right ; i.col <= columns && i != head; i = i.right) {
			if (i.col == columns) {
				return true;
			}
		}
		return false;
	}

 /**
  * initialize tiles and build connections among tiles
  * 
  * @return
  */
	private Column init() {
		Column cl = new Column(); 
		cl.row = -1;
		cl.col = -1;
		for (int i = 0; i < extract.numberOfCol; i++) {
			Column newColumn = new Column();	
			newColumn.top = newColumn;
			newColumn.down = newColumn;
			newColumn.column = newColumn;
			newColumn.row = -1;
			newColumn.col = i;
	
			if (i == 0) {
				newColumn.left = cl;
				cl.right = newColumn;
			}
			else {
				newColumn.left = columnMatrix[i - 1];
				columnMatrix[i - 1].right = newColumn;
				if (i == numofCol - 1) {
					newColumn.right = cl;
					cl.left = newColumn;
				}
			}
			columnMatrix[i] = newColumn;
			if (i < numberofTile) {
				columnMatrix[i].numCol = "T" + Integer.toString(i);
				columnMatrix[i].tile_name = i;
			}
			else {
				columnMatrix[i].numCol = "_" + Integer.toString(i - numberofTile);
				columnMatrix[i].tile_name = -1;
			}
		}


		conf.setNotReachable(false);
		for (int j = 0; j < numofCol; j++) {
			int n = 0;
			for (int i = 0; i < numofRow; i++) {
				if (extract.extract[i][j] != 0) {
					Space sp = new Space();
					sp.row = i;
					sp.col = j;
					spaceMatrix[i][j] = sp;
					n++;
				} else {
					spaceMatrix[i][j] = null;
				}
			}
			if (n == 0 && j >= numberofTile) {
				conf.setNotReachable(true);
				if (conf.infoExchange)
					System.out.println("Fail.");
				return null;
			}
		}

		for (int i = 0; i < numofRow; i++) {
			Space left = null;
			Space p = null;
			Space c = null;
			for (int j = 0; j < numofCol; j++) {
				if (spaceMatrix[i][j] != null) {
					c = spaceMatrix[i][j];
					if (left == null) {
						left = c;
						p = c;
					} else {
						c.left = p;
						p.right = c;
						p = c;
					}
					c.tile_name = left.col;  
				}
			}
			c.right = left;
			left.left = c;
		}
		for (int i = 0; i < numofCol; i++) {
			Space last = null;
			Space now = columnMatrix[i];
			int a = 0;
			for (int j = 0; j < numofRow; j++) {
				if (spaceMatrix[j][i] != null) {
					now = spaceMatrix[j][i];
					now.column = columnMatrix[i];
					a++;
					if (last == null) {
						now.top = columnMatrix[i];
						columnMatrix[i].down = now;
						last = now;
					} else {
						now.top = last;
						last.down = now;
						last = now;
					}
				}
			}
			now.down = columnMatrix[i];
			columnMatrix[i].top = now;
			columnMatrix[i].size = a;
		}

		return cl;
	}



	/**
	 * 
	 * reading tiles from tile, we need a leading tile to be parsed into
	 * the method selects the first tile
	 * 
	 */
	private void select() {
		int a = -1, b = Integer.MAX_VALUE;
		if (!conf.startExcessFinding())
		for (Column col3 = head.right; col3.col < numberofTile; col3 = col3.right) {
			if (conf.tileL.get(col3.col).characterSetII.size() == 8) {
				if (col3.size < b) {
					a = col3.col;
					b = col3.size;
				}
			}
		}
		conf.setfirstTile(a);
	}

}
