import java.util.ArrayList;

/**
 * This class is used in the CFG graph coloring of registers. It is used to put
 * a regsiter in a node
 * 
 * @author Crystal
 * 
 */
public class RegisterNode {
	private String variable, type;
	private int color, lineNumber,first,last;
	private ArrayList<RegisterNode> neighbors;
	
	/**
	 * Init register node
	 * @param variable
	 * @param line
	 * @param type
	 */
	public RegisterNode(String variable, int line, String type){
		this.setvariable(variable);
		setLineNumber(line);
		this.setType(type);
	}

	public String getvariable() {
		return variable;
	}

	public void setvariable(String variable) {
		this.variable = variable;
	}

	public ArrayList<RegisterNode> getNeighbors() {
		return neighbors;
	}

	public void addNeighbor(RegisterNode neighbor) {
		this.neighbors.add(neighbor);
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}
	
}
