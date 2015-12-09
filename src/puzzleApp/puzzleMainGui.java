package puzzleApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;



public class puzzleMainGui extends JFrame {

	private JPanel contentPane;
	//private JPanel display;
	//private JPanel lists;
	private List<List<List<Integer>>> solution;
	private char board[][];
	private List<Color> color = null;

	private int map[];



	


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					puzzleMainGui frame = new puzzleMainGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public puzzleMainGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnReadFile = new JButton("Read File");
		btnReadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser choice = new JFileChooser();
				choice.showOpenDialog(null);
				File f = choice.getSelectedFile();
			}
		});
		btnReadFile.setBounds(27, 710, 89, 23);
		contentPane.add(btnReadFile);
		
		JPanel displayPlane = new JPanel();
		displayPlane.setBounds(10, 27, 450, 616);
		contentPane.add(displayPlane);
		
		JPanel tiles = new JPanel();
		tiles.setBounds(488, 27, 336, 616);
		contentPane.add(tiles);
		
		JPanel results = new JPanel();
		results.setBounds(153, 665, 671, 86);
		contentPane.add(results);
	}
}
