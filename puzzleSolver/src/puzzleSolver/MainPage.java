package puzzleSolver;

//external source used: website link: https://code.google.com/p/narorumo/wiki/SudokuDLX

/**This app heavily replies on Donald Knuth's DLX algorithm and "Automatic Sudoku Solving with Dancing Links"(see website: https://code.google.com/p/narorumo/wiki/SudokuDLX
*four of my app templates: link, extract, parse, and search modules, are derived from "Automatic Sudoku Solving with Dancing Links" but all after modifications.
*
*/

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;





import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;






import puzzleSolver.Parser;
import puzzleSolver.Tile;
import puzzleSolver.mainInterface;;


public class MainPage extends JPanel implements ActionListener {


	private mainInterface mainPanel;
	private List<List<List<Integer>>> solution;
	private char board[][];
	private List<Color> colors = null;
	private int position[];
	private JPanel puzzleBoard;
	private JPanel tileBoard;
	JPanel controlBoard;
	JCheckBox rotation;
	JCheckBox reflection;
	JCheckBox symmetry;
	JButton start;
	JButton stop;
	JPanel resultBoard;
	JLabel result;
	JLabel timeInfo;
	private final Color bgColor = Color.WHITE;	
	private static JMenuBar menu;
    private JButton open;
	private JFileChooser chooseFile;
	private boolean runningThread = false;
	private boolean pauseThread = false;	
	private int origin[] = { 20, 20 };
	private int originTile[] = { 20 + gridWidth / 2, 20 + gridWidth / 2 };
	private int X = 1;
	private int Y = 1;
	private static final int gridWidth = 3;
	private int sizeBlock;
	private int sizeTile;
	private totalSolution totalSol = null;
    private singleSol singlesol = null;
	private singleStep singlestep = null;
	private List<JPanel> bkList = null;
    private double time  = 0;
	private double beginTime = 0;
	private double endTime = 0;
	private double firstTime = 0;
	JLabel firstSolutionTime;
	
	
	
	
	
	
	
	
	/**
	 * 
	 * 
	 * set up one of the module components in the main interface - user panel   
	 * 
	 * 
	 */
	

	private void buildUserControl() {

	
		controlBoard = new JPanel();
		controlBoard.setBackground(bgColor);
		controlBoard.setLocation(535, 453);
		controlBoard.setSize(410, 91);
	
		controlBoard.setVisible(true);
		controlBoard.setFocusable(true);
		controlBoard.setBorder(BorderFactory.createTitledBorder("User Panel "));

		controlBoard.setLayout(null);

		rotation = new JCheckBox("Add Rotation");
		rotation.setBackground(bgColor);
		rotation.setFont(new Font("Eras Demi ITC", Font.PLAIN, 12));
		rotation.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rotation.setSelected(false);
		rotation.setSize(119, 30);
		rotation.setLocation(10, 23);
		rotation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rotation.isSelected())
					mainPanel.configure.setRotation(true);
				else {
					mainPanel.configure.setRotation(false);
					reflection.setSelected(false);
					mainPanel.configure.setFlipping(false);
				}
			}

		});
		controlBoard.add(rotation);

		reflection = new JCheckBox("Add Reflection");
		reflection.setBackground(bgColor);
		reflection.setFont(new Font("Eras Demi ITC", Font.PLAIN, 12));
		reflection.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		reflection.setSelected(false);
		reflection.setSize(130, 30);
		reflection.setLocation(144, 23);
		reflection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (reflection.isSelected()) {
					mainPanel.configure.setFlipping(true);
					rotation.setSelected(true);
					mainPanel.configure.setRotation(true);
				} else
					mainPanel.configure.setFlipping(false);
			}

		});
		controlBoard.add(reflection);

		symmetry = new JCheckBox("No Symmetry");
		symmetry.setBackground(bgColor);
		symmetry.setFont(new Font("Eras Demi ITC", Font.PLAIN, 12));
		symmetry.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		symmetry.setSelected(true);
		symmetry.setSize(113, 30);
		symmetry.setLocation(294, 23);
		symmetry.setEnabled(true);
		symmetry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (symmetry.isSelected())
					mainPanel.configure.setNoSymmetry(true);
				else
					mainPanel.configure.setNoSymmetry(false);
			}

		});
		controlBoard.add(symmetry);

		start = new JButton("Start");
		start.setBackground(bgColor);
		start.setFont(new Font("Eras Demi ITC", Font.BOLD, 12));
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				totalSol = new totalSolution();
				totalSol.execute();
			}

		});
		start.setSize(71, 30);
		start.setLocation(203, 53);
		controlBoard.add(start);

		stop = new JButton("Stop");
		stop.setBackground(bgColor);
		stop.setFont(new Font("Eras Demi ITC", Font.BOLD, 12));
		stop.setEnabled(false);
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pauseThread = false;
				if (singlesol != null && singlesol.getState() == SwingWorker.StateValue.STARTED) {
					singlesol.cancel(true);
					result.setText("process has been cancelled");
				} 
				else if (singlestep != null	&& singlestep.getState() == SwingWorker.StateValue.STARTED) {
					singlestep.cancel(true);
					result.setText("process has been cancelled");
				    
				}
				else if (totalSol != null && totalSol.getState() == SwingWorker.StateValue.STARTED) {
					  totalSol.cancel(true);
					  result.setText("process has been cancelled");
				}
			}

		});
		stop.setSize(68, 30);
		stop.setLocation(284, 53);
		controlBoard.add(stop);
	}

	
	

	/**
	 * 
	 * 
	 * set up one of the module components in the main interface - result panel 
	 * 
	 * 
	 */
	private void buildResult() {

		
		resultBoard = new JPanel();
		resultBoard.setBackground(bgColor);
		resultBoard.setSize(515, 92);
		resultBoard.setLocation(10, 453);
		resultBoard.setVisible(true);
		resultBoard.setFocusable(true);
		resultBoard.setBorder(BorderFactory.createTitledBorder("Results"));
		resultBoard.setLayout(null);

		result = new JLabel("");
		result.setBackground(bgColor);
		result.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		result.setSize(new Dimension(160, 30));
		result.setLocation(10, 20);
		result.setVisible(true);
		resultBoard.add(result);
	}

	

	/**
	 * 
	 * 
	 * set up one of the module components in the main interface - main board panel, which is to show the resulting puzzles   
	 * 
	 * 
	 */
	
	private void buildMainBoard() {
		puzzleBoard = new JPanel();
		puzzleBoard.setBackground(bgColor);
		puzzleBoard.setLayout(null);
		puzzleBoard.setLocation(10, 10);
		puzzleBoard.setSize(669, 443);
		puzzleBoard.setOpaque(true);
		puzzleBoard.setVisible(true);
		puzzleBoard.setFocusable(true);
		puzzleBoard.setBorder(BorderFactory.createTitledBorder("Main Board"));
	}
	
	

	/**
	 * 
	 * 
	 * set up one of the module components in the main interface - tile board, which is used to display the tiles that read from a file  
	 * 
	 * 
	 */

	private void buildTileBoard() {
		tileBoard = new JPanel();
		tileBoard.setBackground(bgColor);
		tileBoard.setLayout(null);
		tileBoard.setLocation(689, 10);
		tileBoard.setSize(256, 443);
		tileBoard.setVisible(true);
		tileBoard.setFocusable(true);
		tileBoard.setBorder(BorderFactory.createTitledBorder("Tiles"));
	}

	
	/**
	 * 
	 * 
	 * 
	 * this method comes from Donald Knuth's DLX algorithm, which is used to solve the connection of sudoku problem. 
	 * website link: https://code.google.com/p/narorumo/wiki/SudokuDLX
	 * 
	 * 
	 * Here, like sudoku, the method is used to display the tiles in the tile board
	 * 
	 */
	
	private void buildTList(List<Tile> tiles, Tile board) {

		
		boolean character = false;
		char c = ' ';
		for (int i = 0; i < board.characterSet.length && !character; i++) {
			for (int j = 0; j < board.characterSet[0].length && !character; j++) {
				if (board.characterSet[i][j] != ' ') {
					if (c == ' ') {
						c = board.characterSet[i][j];
					} else {
						if (board.characterSet[i][j] != c)
							character = true;
					}
				}
			}
		}

		final int length =  160;
		final int width =  385;

		JPanel content = new JPanel(null);
		content.setSize(length, width);
		content.setLocation(15, 30);
		content.setBackground(bgColor);
		int[][] pack = Tile.packTiles(tiles, (double) width
				/ length);

		int grid = Math.min((length - 1) / pack[0].length,
				(width - 1) / pack.length);
		
		
		int[][] input = new int[tiles.size()][2];
		for (int i = 0; i < tiles.size(); i++)
			input[i][0] = -1;
		for (int i = 0; i < pack.length; i++) {
			for (int j = 0; j < pack[0].length; j++) {
				int id = pack[i][j];
				if (id >= 0) {
					if (input[id][0] < 0) {
						input[id][0] = i;
						input[id][1] = j;
					}
					int y = i - input[id][0];
					int x = j - input[id][1];
					char t = tiles.get(id).characterSet[y][x];
					if (t != ' ') {
						JPanel block = new JPanel();
						block.setBackground(colors.get(pack[i][j]));
						block.setLocation(j * grid, i * grid);
						block.setBorder(new LineBorder(Color.black));
						block.setSize(grid + 1, grid + 1);
						if (character && grid > 5) {
							
							JLabel label = new JLabel(Character.toString(t));
							label.setSize(grid - 2, grid - 2);
							label.setFont(new Font("Arial", Font.PLAIN, grid - 2));
							label.setVerticalAlignment(SwingConstants.CENTER);
							label.setHorizontalAlignment(SwingConstants.CENTER);
							label.setLocation(j * grid + 1, i * grid + 1);
							label.setOpaque(false); 
							label.setVisible(true);
							content.add(label);
						}
						content.add(block);
					}
				}
			}
		}
		tileBoard.add(content);
	}

	private void readFile() {
		menu = new JMenuBar();
		menu.setBackground(Color.WHITE);
		menu.setOpaque(true);
		open = new JButton("Open File");
		open.setMnemonic(KeyEvent.VK_R);
		open.addActionListener(this);
		menu.add(open);


		chooseFile = new JFileChooser();
		chooseFile.setCurrentDirectory(new File("."));
	}

	/**
	 *
	 *
	 * set up the entire board and draw color in the board
	 *
	 *
	 */
	private void buildBoard(char[][] b) {

		bkList = new ArrayList<JPanel>();
		board = new char[b.length][b[0].length];
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				board[i][j] = b[i][j];
			}
		}

		sizeBlock = Math.min((235 - origin[0] )
				/ board[0].length, (455 - origin[1] )
				/ board.length)
				- gridWidth;
		sizeTile = sizeBlock + gridWidth;

		Set<Character> set = new HashSet<Character>();
		int length = board[0].length;
		int width = board.length;
		
		X = 535 / 2
				- (length * (sizeBlock + gridWidth) + gridWidth) / 2 - 18;
		Y = 535 / 2
				- (width * (sizeBlock + gridWidth) + gridWidth) / 2 - 10;

		int singleWidth[] = { length * (sizeBlock + gridWidth) + gridWidth, gridWidth };
		int singleLength[] = { gridWidth, width * (sizeBlock + gridWidth) + gridWidth };
		JPanel blockHeight[] = new JPanel[width + 1];
		JPanel blockWidth[] = new JPanel[length + 1];
		JPanel singlebk_width[] = new JPanel[width + 1];
		JPanel siglebkv_length[] = new JPanel[length + 1];
		for (int i = 0; i <= width; i++) {
			singlebk_width[i] = new JPanel();
			singlebk_width[i].setBackground(Color.white);
			singlebk_width[i].setSize(singleWidth[0] - 2, singleWidth[1] - 2);
			singlebk_width[i].setLocation(origin[0] + X, origin[1] + (sizeBlock + gridWidth) * i + Y);
			singlebk_width[i].setOpaque(true);
			singlebk_width[i].setVisible(true);
			puzzleBoard.add(singlebk_width[i]);
		}

		for (int j = 0; j <= length; j++) {
			siglebkv_length[j] = new JPanel();
			siglebkv_length[j].setBackground(Color.white);
			siglebkv_length[j].setSize(singleLength[0] - 2, singleLength[1] - 2);
			siglebkv_length[j].setLocation(origin[0] + (sizeBlock + gridWidth) * j + X, origin[1] + Y);
			siglebkv_length[j].setOpaque(true);
			siglebkv_length[j].setVisible(true);
			puzzleBoard.add(siglebkv_length[j]);
		}

		for (int i = 0; i <= width; i++) {
			blockHeight[i] = new JPanel();
			blockHeight[i].setBackground(Color.black);
			blockHeight[i].setSize(singleWidth[0], singleWidth[1]);
			blockHeight[i].setLocation(origin[0] + X - 1, origin[1] + (sizeBlock + gridWidth) * i + Y - 1);
			blockHeight[i].setOpaque(true);
			blockHeight[i].setVisible(true);
			puzzleBoard.add(blockHeight[i]);
		}

		for (int j = 0; j <= length; j++) {
			blockWidth[j] = new JPanel();
			blockWidth[j].setBackground(Color.black);
			blockWidth[j].setSize(singleLength[0], singleLength[1]);
			blockWidth[j].setLocation(origin[0] + (sizeBlock + gridWidth) * j + X - 1, origin[1] + Y - 1);
			blockWidth[j].setOpaque(true);
			blockWidth[j].setVisible(true);
			puzzleBoard.add(blockWidth[j]);
		}


		for (int i = 0; i < width; i++) {
			for (int j = 0; j < length; j++) {
				if (board[i][j] == ' ') {

					JPanel block = new JPanel() {
						@Override
						public void paintComponent(Graphics g) {
							for (int i = 0; i < sizeTile; i += 4) {
								g.drawLine(sizeTile - i, 0, 0, sizeTile - i);
								g.drawLine(sizeTile, i, i, sizeTile);
							}
						}
					};
					block.setSize(sizeTile, sizeTile);
					int x = originTile[0] + (j) * sizeTile + X;
					int y = originTile[1] + (i) * sizeTile + Y;
					block.setLocation(x, y);
					block.setVisible(true);
					puzzleBoard.add(block);
				} else
					set.add(board[i][j]);
			}
		}
		if (set.size() > 1) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < length; j++) {
					if (board[i][j] == ' ')
						continue;
					JLabel block = new JLabel(Character.toString(board[i][j]));

					int fontSize = 0;
					if (sizeTile < 5) continue;
					else if (sizeTile < 12) fontSize = sizeTile - 2;
					else if (sizeTile < 20) fontSize = 10;
					else fontSize = sizeTile / 2;
					block.setSize(fontSize, fontSize);
					block.setFont(new Font("Arial", Font.PLAIN, fontSize));
					int x = originTile[0] + (j) * sizeTile + X
							+ (sizeTile - fontSize) / 2;
					int y = originTile[1] + (i) * sizeTile + Y
							+ (sizeTile - fontSize) / 2;
					block.setLocation(x, y);

					block.setVerticalAlignment(SwingConstants.CENTER);
					block.setHorizontalAlignment(SwingConstants.CENTER);
					block.setOpaque(false);
					block.setVisible(true);
					puzzleBoard.add(block);
				}
			}
		}
	}

	/*
	 * 
	 * 
	 * display how many solutions when a file is parsed in
	 * 
	 * 
	 */
	public void display(int id) {
		if (id >= solution.size()) {
			System.err.println("Index Error");
			return;
		}
		displayStep(solution.get(id));
	}

	public void displayStep(List<List<Integer>> pos) {

		int number = pos.size();

		for (int i = 0; i < number; i++) {
			List<Integer> tilePos = new ArrayList<Integer>();
			tilePos = pos.get(i);
			Color c = colors.get(tilePos.get(0));
			for (int j = 1; j < tilePos.size(); j++) {
				JPanel block = new JPanel();
				block.setBackground(c);
				block.setSize(sizeTile, sizeTile);
				int x = originTile[0] + (position[tilePos.get(j)] % (board[0].length)) * sizeTile + X;
				int y = originTile[0] + (position[tilePos.get(j)] / (board[0].length)) * sizeTile + Y;
				block.setLocation(x, y);
				block.setOpaque(true);
				block.setVisible(true);
				puzzleBoard.add(block);
				bkList.add(block);
			}
		}
		puzzleBoard.repaint();
	} 

	private void clear() {
		for (JPanel p: bkList) {
			puzzleBoard.remove(p);
		}
		puzzleBoard.repaint();
	}
  
	/**
	 * if there are roles in a tile list, draw the holes in the main board
	 */
	private void arrangePosition() {
		position = new int[board.length * board[0].length];
		for (int j = 0; j < position.length; j++)
			position[j] = j;
		List<Integer> missing = new ArrayList<Integer>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == ' ')
					missing.add(i * board[0].length + j);
			}
		}

		for (int i = 0; i < missing.size(); i++) {
			for (int j = missing.get(i) - i; j < position.length; j++)
				position[j]++;
		}
	}

	/**
	 * draw color each tile
	 *
	 */
	private List<Color> color(int n) {
		List<Color> colors = new ArrayList<Color>();
		colors.addAll(Arrays.asList(Color.red, Color.green, Color.blue,new Color(148, 0, 211), new Color(135, 51, 36), Color.gray, Color.pink, new Color(175, 255, 225), new Color(130, 175, 190),
				Color.yellow, Color.cyan, new Color(46, 139, 87)));
		return colors;
	}

	
	/**
	 * 
	 * 
	 * show symmetric, rotation, and reflection methods in user panel
	 */
	
	private void addToUserControl(boolean a) {
		rotation.setEnabled(a);
		reflection.setEnabled(a);
		symmetry.setEnabled(a);
		start.setEnabled(a);
		stop.setEnabled(a);
	}

	/**
	 * 
	 * does not do anything... but in other to make it be consistent with other modules.
	 * 
	 * @param b
	 */
	private void addToResult(boolean b) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == open) {
			if ((totalSol != null && totalSol.getState() == SwingWorker.StateValue.STARTED) || (singlesol != null && singlesol.getState() == SwingWorker.StateValue.STARTED) || (singlestep != null && singlestep.getState() == SwingWorker.StateValue.STARTED) ) {
				JOptionPane.showConfirmDialog(null, "Please stop searching first.", "warning", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			} 
			else if (runningThread) { 
				JOptionPane.showConfirmDialog(null, "Please stop autoplay first.",	"Warning", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			}

	
			File f= null;
			if (chooseFile.showOpenDialog(MainPage.this) == JFileChooser.APPROVE_OPTION) {
				f = chooseFile.getSelectedFile();
			}
			else {return;}

			tileBoard.removeAll();
			puzzleBoard.removeAll();
			rotation.setSelected(false);
			reflection.setSelected(false);
			symmetry.setSelected(true);
			puzzleBoard.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Puzzle ("+ f.getName() + ")"),BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			Parser parse = new Parser(f.getAbsolutePath());
			List<Tile> list = parse.extractPiece();
			Tile board = list.get(0);
			list.remove(0);
			colors = color(list.size());
			mainInterface a = new mainInterface(board, list);
			this.mainPanel= a;
			addToUserControl(true);
			int d = 0;
			for (Tile t: list) {
				d += t.plane;
			}
			buildBoard(board.characterSet);
			arrangePosition();
			buildTList(list, board);
			repaint();
		}
	}

	/*
	 * 
	 * 
	 * calculate total solutions
	 * 
	 */
	
	private class totalSolution extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() {

			solution = new ArrayList<List<List<Integer>>>();

			addToUserControl(false);
			addToResult(false);
			stop.setEnabled(true);
	
			clear();

			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
			mainPanel.configure.reset();
	     	mainPanel.preCheck();
	     	mainPanel.reSearch();
			result.setText("Waiting for solutions");
			beginTime = System.nanoTime();
			List<List<Integer>> sol = mainPanel.findNext();
			int n = 0;
			if (sol != null && !isCancelled()) {
				clear();
				displayStep(sol);
				publish();
			}
			while (sol != null && !isCancelled()) {
				solution.add(sol);
				sol = mainPanel.findNext();
				publish();
				n++;
			}
			endTime = System.nanoTime();
			time = (endTime - beginTime)*0.000000001;
			firstTime = ((endTime - beginTime)/n)*0.000000001;
			return null;
		}

		@Override
		protected void done() {
			addToUserControl(true);
			addToResult(true);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			updateResult(false);
			start.requestFocus();
		}

		@Override
		protected void process(List<Void> p) {
			if (!isCancelled())
				if (solution.size() <= 1){
					result.setText("Searched " + solution.size() + " solution. "  );
			       }
				else{
					result.setText("Searched " + solution.size() + " solutions. " );
					
				}
		}

	}

	
	/*
	 * 
	 * 
	 * find out each possible solution 
	 * 
	 */
	
	private class singleSol extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() {
			solution = new ArrayList<List<List<Integer>>>();

	
			addToUserControl(false);
			addToResult(false);
	
			stop.setEnabled(true);
		
			clear();

			
			mainPanel.configure.reset();
			mainPanel.preCheck();
			mainPanel.reSearch();
	
			result.setText("In process ");


			List<List<Integer>> sol = mainPanel.findNext();
			while (sol != null && !isCancelled()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.err.println("Sorry! Unknown Interruption");
				}
				while (pauseThread) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						System.err.println("Sorry! Unknown Interruption");
					}
				}
				solution.add(sol);
				publish();
				sol = mainPanel.findNext();
			}
			return null;
		}

		@Override
		protected void done() {
			addToUserControl(true);
			addToResult(true);

			updateResult(true);
	
		}

		@Override
		protected void process(List<Void> p) {
			clear();
			displayStep(solution.get(solution.size() - 1));
			if (!isCancelled())
				if (solution.size() <= 1)
					result.setText("Searched " + solution.size() + " solution");
				else
					result.setText("Searched " + solution.size() + " solutions");
		}

	}

	
	private class singleStep extends
			SwingWorker<Void, List<List<Integer>>> {

		@Override
		protected Void doInBackground() {
			solution = new ArrayList<List<List<Integer>>>();

			addToUserControl(false);
			addToResult(false);
			stop.setEnabled(true);
	
			clear();

		
			mainPanel.configure.reset();
			mainPanel.preCheck();
			mainPanel.reSearch();
	

			result.setText("Searched ");
		

			List<List<Integer>> sol = mainPanel.nextStep();
			while (sol != null && !isCancelled()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.err.println("Sleep Interrupt!");
				}
				while (pauseThread) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						System.err.println("Sleep Interrupt!");
					}
				}
				publish(sol);
				sol = mainPanel.nextStep();
				if (mainPanel.checkLastSolution()) solution.add(sol);
			}
			return null;
		}

		@Override
		protected void done() {

			addToUserControl(true);
			addToResult(true);

			updateResult(true);
			clear();
			if (solution.size() > 0)
				displayStep(solution.get(solution.size() - 1));

		}

		@Override
		protected void process(List<List<List<Integer>>> r) {
			clear();
			displayStep(r.get(r.size() - 1));
			if (!isCancelled())
				if (solution.size() <= 1)
					result.setText("Searched " + solution.size() + " solution");
				else
					result.setText("Searched " + solution.size() + " solutions");
		}

	}
	/*
	 * 
	 * after finding out solutions, update the result panel
	 * 
	 * 
	 */

	private void updateResult(boolean lastSolution) {
		if (solution.size() == 0) {
			if (mainPanel.configure.cantReach())
				result.setText("Positions cannot be reached");
			else if (mainPanel.configure.noSpace())
				result.setText("No enough tiles");
			else
				result.setText("No Solution");
	
		} else {
			if (solution.size() == 1) {
				result.setText("Only 1 solution ");
				timeInfo.setText("Total time : "+ time +  " s");
				firstSolutionTime.setText("Time to find out the first solution : " + time +  " s" );
					
			
			}
			else {
				result.setText(solution.size() + " solutions " );
				timeInfo.setText("Total time : "+time +  " s");
				firstSolutionTime.setText("Time to find out the first solution : " + firstTime +  " s" );
			}
		}
	}	
	
	/**
	 * 
	 * put all components/modules, like result/user control/main board/tile board, into one interface
	 * 
	 * 
	 */
	public MainPage() {
		this.setLayout(null);
		this.setBackground(bgColor);

		readFile();
		buildUserControl();
		buildResult();
		buildMainBoard();
		buildTileBoard();

		this.add(controlBoard);
		


		this.add(resultBoard);
		
		 timeInfo = new JLabel(" ");
		timeInfo.setBounds(295, 20, 210, 31);
		resultBoard.add(timeInfo);
		
		firstSolutionTime = new JLabel(" ");
		firstSolutionTime.setBounds(20, 61, 416, 20);
		resultBoard.add(firstSolutionTime);
		this.add(puzzleBoard);
		this.add(tileBoard);

		addToUserControl(false);
		addToResult(false);

	
		this.setPreferredSize(new Dimension(955, 555));
		this.setVisible(true);
	}

	/**
	 * 
	 * 
	 * main method to run all of these methods
	 * @param args
	 */
	

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
	
				JFrame contentBoard = new JFrame("Micro-Puzzle App");
				contentBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
				contentBoard.setResizable(false);
				MainPage main = new MainPage();
				contentBoard.setContentPane(main);
				contentBoard.setJMenuBar(menu);
				contentBoard.pack();
				contentBoard.setLocationRelativeTo(null); 
				contentBoard.setVisible(true);
				
			}

		});

	}
}
