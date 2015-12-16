package puzzleSolver;
//external source used: website link: https://code.google.com/p/narorumo/wiki/SudokuDLX
import java.util.Stack;

import puzzleSolver.Space;



public class Trail {


	private Stack<Space> stack = null;
	private boolean finish = false;



	public Trail() {
		stack = new Stack<Space>();
	}

	public void push(Space c) { 
		stack.push(c); 
		}
	public Space pop() { 
		return stack.pop(); 
		}
	
	public Space top() { 
		return stack.lastElement(); 
		}
	public int size() { 
		return stack.size(); 
		}
	public Space get(int i) { 
		return stack.get(i);
		}
	public boolean isEmpty() { 
		return stack.size() == 0;
		}
	public void clear() {
		stack.clear(); 
		}
	public boolean finish() { 
		return finish;
		}
	public void setFinish(boolean b) {
		finish = b; 
		}

	public void print() {
		for (int i = 0; i < stack.size(); i++) {
			Space x = stack.get(i);
		}
	}

}

