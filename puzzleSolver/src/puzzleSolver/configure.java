package puzzleSolver;



import java.util.List;

import puzzleSolver.Tile;



public class configure {


	public final char space = ' ';

	public boolean infoExchange = true;

	public Tile tilesBoard = null;
	public List<Tile> tileL = null;
	private boolean completed = false;
	private boolean oneSeach = false;
	private boolean oneSolSea = false;
	private boolean reduceSym = true;
	private boolean noDup = true;
	private int[] replicate = null;
	private int[] replicate2 = null;
	private int[] replicate3 = null;
	private int tile_number = -1;
	private boolean reduceSymII = false;
	private boolean rotation = false;
	private boolean flipflop = false;
	private boolean excess = false;
	private boolean nospace = false;
	private boolean notReachable = false;


//configure constructor. Does not do anything
	public configure() {}

	/*
	 * 
	 * reset search again
	 */
	public void reset(){
		completed = false;
		oneSeach = false;
		oneSolSea = false;
		reduceSym = false;
	}
   /*
    * tiles to begin rotation 
    * 
    * 
    */
	public boolean startRotation() {
		return rotation; 
		}
	/*
	 * allow tile to be able to rotate
	 * 
	 */
	
	public void setRotation(boolean a) { 
		rotation = a; 
		}

	/*
	 * 
	 * tile starts flipping
	 * 
	 */
	
	public boolean startFlipping() { 
		return flipflop; 
		}
	
	
	/**
	 * 
	 * 
	 * allow tiles to be able to flip
	 */
	
	
	public void setFlipping(boolean a) { 
		flipflop = a;
		}

	
	/**
	 * 
	 * looking for extra solutions
	 * 
	 * @return
	 */
	public boolean startExcessFinding() { 
		return excess; 
		}
	
	/**
	 * 
	 * allow the system to look for extra solutions
	 * 
	 * @param b
	 */
	
	
	public void setExcessFinding(boolean a) { 
		excess = a; 
		}

	
	/**
	 * 
	 * check failed attempt
	 * 
	 * @return
	 */
	public boolean checkUnsucess() {
		return  notReachable ||nospace ;
	}
	
	/*  
	 * 
	 * check whether the space is sufficient to contain all of the tiles
	 * 
	 */
	public void setNoSpace(boolean a) { 
		nospace = a; 
		}
	
	/**
	 * 
	 * check whether there are sufficient tiles in a file((this is just an indication of current system state))
	 * 
	 * @param a
	 */
	public void setNotReachable(boolean a) {
		notReachable = a; 
		}
	/**
	 * 
	 * same as setNoSPace
	 * 
	 * @return
	 */
	
	public boolean noSpace() { 
		return nospace; 
		}
	/**
	 * 
	 * file does not contain sufficient tiles
	 * 
	 * @return
	 */
	
	public boolean cantReach() {
		return notReachable; 
		}
/**
 * 
 * all searches completed
 * 
 * @return
 */
	public boolean completeSearch() {
		return completed; 
		}
	/**
	 * 
	 * all searches completed(this is just an indication of current system state)
	 * 
	 * @param a
	 */
	
	public void setCompleteSearch(boolean a) { 
		completed = a; 
		}

	
	/**
	 * 
	 * search one single step
	 * 
	 * @return
	 */
	public boolean oneSearch() { 
		return oneSeach; 
		}
	/**
	 * 
	 * search one single step(just an indication of current state)
	 * 
	 * @param a
	 */
	
	public void setOneSearch(boolean a) { 
		oneSeach = a;
		}

	
	/**
	 * 
	 * search a single solution
	 * 
	 * @return
	 */
	public boolean oneSolSearch() { 
		return oneSolSea; 
		}
	/**
	 * 
	 * search one single solution(just an indication of current state)
	 * 
	 * @param b
	 */
	
	public void setOneSolSearch(boolean b) { 
		oneSolSea = b;
		}

	/**
	 * 
	 * 
	 * rejects symmetry
	 * @return
	 */
	
	public boolean noSymmetry() { 
		return reduceSym; 
		}
	
	/**
	 * 
	 * 
	 * rejects symmetry(current system state)
	 * @param b
	 */
	
	
	public void setNoSymmetry(boolean b) { 
		reduceSym = b; 
		}
   /**
    * 
    * 
    * get the first tile of the incoming file
    * @return
    */
	
	public int getfirstTile() {
		return tile_number; 
		}
	
	/**
	 * get the first tile of the incoming file(current system state)
	 * 
	 * 
	 * @param id
	 */
	public void setfirstTile(int id) { 
		tile_number = id; 
		
	}
   /**
    * 
    * 
    * minimize symmetry 
    * @return
    */
	public boolean reduceSymmetry() { 
		return reduceSymII; 
		}
	 /**
	    * 
	    * 
	    * minimize symmetry (current system state)  
	    * @return
	    */
	
	public void setReduceSymmetry(boolean b) { 
		reduceSymII = b;
		
	}
 /**
  * 
  * 
  * minimize duplicate
  * @return
  */
	public boolean reduceReplication() {
		return noDup; 
		
	}
	/**
	 * 
	 * same as above
	 */

	
	
	public void selfReduceReplication() {
		for (int i = 0; i < replicate.length; i++) {
			if ((replicate[i] != i) ||
					(replicate2[i] != i && rotation) ||
					(replicate3[i] != i && flipflop)) {
				noDup = true;
				return;
			}
		}
		noDup = false;
	}

  /**
   * 
   * 
   * allow replicates when flipping and rotating
   * @return
   */
	public int[] replication() {
		if (flipflop) return replicate3;
		if (rotation) return replicate2;
		return replicate;
	}


	/**
	 * the method is used to check the duplicated tiles
	 * 
	 */
	public void identifyReplicate(List<Tile> list) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setNumber(i);
			list.get(i).setReplicate(-1);
			list.get(i).setReplicateI(-1);
			list.get(i).setReplicateII(-1);
		}

		int a = 0;
		int b;
		while (a < list.size()) {
			for (b = a; b < list.size(); b++) {
				if (list.get(b).plane != list.get(a).plane) {
					break;
				}
			}
			b--;

		
			for (int i = b; i >= a; i--) {
				Tile resultTile = list.get(i);
				if (resultTile.getReplicate() == -1) {
					resultTile.setReplicate(i);
					Tile lastTile = resultTile;
					for (int j = i - 1; j >= a; j--) {
						Tile newTile = list.get(j);
						if (newTile.equal(resultTile)) {
							lastTile.setReplicate(j);
							newTile.setReplicate(i);
							lastTile = newTile;
						}
					}
				}
				if (resultTile.getReplicateI() == -1) {
					resultTile.setReplicateI(i);
					Tile lastII = resultTile;
					for (int j = i - 1; j >= a; j--) {
						Tile last = list.get(j);
						if (last.equalI(resultTile)) {
							lastII.setReplicateI(j);
							last.setReplicateII(i);
							lastII = last;
						}
					}
				}
				if (resultTile.getReplicateII() == -1) {
					resultTile.setReplicateII(i);
					Tile lastIII = resultTile;
					for (int j = i - 1; j >= a; j--) {
						Tile last = list.get(j);
						if (last.equalII(resultTile)) {
							lastIII.setReplicateII(j);
							last.setReplicateII(i);
							lastIII = last;
						}
					}
				}
			}

	
			a = b + 1;
		}

		replicate = new int[list.size()];
		replicate2 = new int[list.size()];
		replicate3 = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			replicate[i] = list.get(i).getReplicate();
			replicate2[i] = list.get(i).getReplicateI();
			replicate3[i] = list.get(i).getReplicateII();
		}
	}



}

