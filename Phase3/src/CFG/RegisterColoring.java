package CFG;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * This is the class that will take in a block and then read its information to
 * create a graph out of the registers and color them appropriately
 * 
 * @author Crystal
 * 
 */
public class RegisterColoring {
	private CFGNode theBlock;

	/**
	 * init
	 */
	public RegisterColoring(CFGNode block) {
		theBlock = block;
	}

	/**
	 * So in order to make the IR code, we first need to look at the block. Inx
	 * the block, look at each line of code. Take each line of code and change
	 * all of the variables to registers. Once these registers are made, we need
	 * to create a livelyhood chart for each register. After the chart is made,
	 * we can then make a graph. After making the graph, we color the graph. The
	 * number of colors is the real number of registers we need. Then we put the
	 * regiters back inthe IR code and return it.
	 */
	public String makeNewIRCode() {
		// get the irCoe from a block
		String[] lines = theBlock.getIrCode().split("\n");

		// for each line, change variables into registers
		ArrayList<RegisterNode> registers = new ArrayList<RegisterNode>();
		for (int i = 0; i < lines.length; i++) {
			ArrayList<RegisterNode> currentRegisters = makeRegisters(lines[i],
					i);

			for (RegisterNode each : currentRegisters) {
				registers.add(each);
			}
		}

		// now use the registers to determine liveliness
		registers = determineLiveliness(registers);
		// use the registers's liveliness to determine neighbors in a graph
		registers = determineNeighbors(registers);
		// next, color the graph!
		registers = colorRegisters(registers);

		String newIRCode = makeIRCode(registers, theBlock.getIrCode());
		return newIRCode;
	}

	/**
	 * This will go through an instruction and make registers using variables
	 * from the instruction
	 * 
	 * @param line
	 * @param lineNumber
	 * @return
	 */
	private ArrayList<RegisterNode> makeRegisters(String line, int lineNumber) {
		ArrayList<RegisterNode> list = new ArrayList<RegisterNode>();
		// split up the instruction and get rid of white space
		String[] instruction = line.split(",");
		for (int i = 0; i < instruction.length; i++) {
			instruction[i] = instruction[i].trim();
		}

		// binary operations
		List<String> binaryOp = Arrays.asList("add", "sub", "mult", "div",
				"and", "or");
		// branching
		List<String> branchOp = Arrays.asList("breq", "brneq", "brlt", "brgt",
				"brgeq", "brleq");

		// *************Make Some Registers ************************
		if (binaryOp.contains(instruction[0])) {
			// this is a binary operation
			// op, x, y, z
			list.add(new RegisterNode(instruction[1], lineNumber, ""));
			list.add(new RegisterNode(instruction[2], lineNumber, ""));
			try {
				Integer.parseInt(instruction[3]);
			} catch (Exception e) {
				list.add(new RegisterNode(instruction[3], lineNumber, ""));
			}
		}
		if (branchOp.contains(instruction[0])) {
			// this is a branch operation
			// op, y, z, label
			list.add(new RegisterNode(instruction[1], lineNumber, ""));
			try {
				Integer.parseInt(instruction[2]);
			} catch (Exception e) {
				list.add(new RegisterNode(instruction[2], lineNumber, ""));
			}
		}
		if (instruction[0].equals("assign")) {
			// this is an assign operation
			// op, x, y,
			// op,x, size, value,
			list.add(new RegisterNode(instruction[1], lineNumber, ""));
			if (instruction.length == 3) {
				try {
					Integer.parseInt(instruction[2]);
				} catch (NumberFormatException e) {
					list.add(new RegisterNode(instruction[2], lineNumber, ""));
				}
			}
		}
		if (instruction[0].equals("return")) {
			// this is a return operation
			// op, x,
			list.add(new RegisterNode(instruction[1], lineNumber, ""));
		}
		if (instruction[0].equals("callr")) {
			// this is a callr instruction
			// op,x,func,param...
			list.add(new RegisterNode(instruction[1], lineNumber, ""));
		}
		if (instruction[0].equals("array_store")) {
			// this is an array store
			// op, arrayname,offset,x,
			list.add(new RegisterNode(instruction[3], lineNumber, ""));
			list.add(new RegisterNode(instruction[1], lineNumber, ""));
			try {
				Integer.parseInt(instruction[2]);
			} catch (NumberFormatException e) {
				list.add(new RegisterNode(instruction[2], lineNumber, ""));
			}
		}
		if (instruction[0].equals("array_load")) {
			// this is an array load
			// op, x, arrayname, offset
			list.add(new RegisterNode(instruction[1], lineNumber, ""));
			list.add(new RegisterNode(instruction[2], lineNumber, ""));
			try {
				Integer.parseInt(instruction[3]);
			} catch (NumberFormatException e) {
				list.add(new RegisterNode(instruction[3], lineNumber, ""));
			}
		}

		return list;
	}

	/**
	 * Goes through the list of registers and determines the liveliness. First
	 * we'll make a list of just the variable names Then we'll go through the
	 * list of registers and find the first and last occurance of each variable
	 * 
	 * @param registers
	 * @return
	 */
	private ArrayList<RegisterNode> determineLiveliness(
			ArrayList<RegisterNode> registers) {
		ArrayList<RegisterNode> newRegisters = new ArrayList<RegisterNode>();
		// get the variables
		ArrayList<String> variables = new ArrayList<String>();
		for (RegisterNode each : registers) {
			if (!variables.contains(each.getvariable())) {
				variables.add(each.getvariable());
			}
		}

		// determine the first and last occurances of each variable
		for (String each : variables) {
			int first = 9999, last = 9999; // meaning not instanciated
			for (RegisterNode eachNode : registers) {
				if (eachNode.getvariable().equals(each)) {
					// check if first has been assigned
					if (first == 9999) {// not assigned yet
						first = eachNode.getLineNumber();
					}
					// determine the last occurance
					if (last == 9999) {
						last = eachNode.getLineNumber();
					} else {
						if (eachNode.getLineNumber() > last) {
							last = eachNode.getLineNumber();
						}
					}
				}
			}
			// make a new node with that variable and it's first
			RegisterNode node = new RegisterNode(each, first, "");
			node.setFirst(first);
			node.setLast(last);
			newRegisters.add(node);
		}

		return newRegisters;
	}

	/**
	 * Use the liveliness of the registers to determine which ones are neighbors
	 * in a graph. To do this, look at each node's first and last occurance. If
	 * another node has a first or last in that range, then it is a neighbor.
	 * 
	 * @param registers
	 * @return
	 */
	private ArrayList<RegisterNode> determineNeighbors(
			ArrayList<RegisterNode> registers) {

		for (int i = 0; i < registers.size(); i++) {
			for (int j = 1; j < registers.size(); j++) {
				RegisterNode firstNode = registers.get(i);
				RegisterNode checkNode = registers.get(j);

				if (firstNode != checkNode
						&& checkNode.getFirst() >= firstNode.getFirst()
						&& checkNode.getFirst() <= firstNode.getLast()) {
					// && (checkNode.getLast() <= firstNode.getLast() ||
					// checkNode
					// .getLast() > firstNode.getLast())) {
					// means they are neighbors!
					firstNode.addNeighbor(checkNode);
					checkNode.addNeighbor(firstNode);
				}
			}
		}

		return registers;
	}

	/**
	 * Sorts register nodes in decreasing order
	 * 
	 * @author Crystal
	 * 
	 */
	static class PQsort implements Comparator<RegisterNode> {
		public int compare(RegisterNode o1, RegisterNode o2) {
			return o2.getNeighbors().size() - o1.getNeighbors().size();
		}
	}

	/**
	 * Next we need to color the registers. To do this, we'll start out by
	 * taking a node with the most neighbors and coloring it one color, then
	 * coloring each neighbor a different color. Then look at another node, and
	 * color it if it's not colored
	 * 
	 * @param registers
	 * @return
	 */
	private ArrayList<RegisterNode> colorRegisters(
			ArrayList<RegisterNode> registers) {
		// Comparator
		PQsort pqs = new PQsort();

		// Given G=(V,E):
		// Compute Degree(v) for all v in V.
		// Set uncolored = V sorted in decreasing order of Degree(v).
		PriorityQueue<RegisterNode> uncolored = new PriorityQueue<RegisterNode>(
				5, pqs);
		for (RegisterNode each : registers) {
			uncolored.add(each);
		}
		// set currentColor = 0.
		int currentColor = 0;
		ArrayList<RegisterNode> removeMe = new ArrayList<RegisterNode>();

		// while there are uncolored nodes:
		while (!uncolored.isEmpty()) {

			// remove anything that needs removed
			for (RegisterNode each : removeMe) {
				if (each != null) {
					uncolored.remove(each);
				}
			}
			removeMe.clear();
			if (!uncolored.isEmpty()) {
				// set A=first element of uncolored
				// remove A from uncolored
				RegisterNode A = uncolored.poll();
				// set Color(A) = currentColor
				A.setColor(currentColor);
				// set coloredWithCurrent = {A}
				ArrayList<RegisterNode> coloredWithCurrent = new ArrayList<RegisterNode>();
				coloredWithCurrent.add(A);
				// for each v in uncolored:
				for (RegisterNode v : uncolored) {
					// if v is not adjacent to anything in coloredWithCurrent:
					int notAdjacent = 1;

					if (!v.getNeighbors().isEmpty()) {
						for (RegisterNode each : v.getNeighbors()) {
							if (coloredWithCurrent.contains(each)) {
								notAdjacent = 0;
							}
						}
					}
					if (notAdjacent == 1) {
						// set Color(v)=currentColor.
						v.setColor(currentColor);
						// add v to currentColor.
						coloredWithCurrent.add(v);
						// remove v from uncolored.
						removeMe.add(v);
					}// end if
				}// end for
					// currentColor = currentColor + 1.
				currentColor = currentColor + 1;
			}
			// end while
		}

		return registers;
	}

	/**
	 * This will take in the irCode from a block and replace the variables with
	 * registers
	 * 
	 * @param registers
	 * @return
	 */
	private String makeIRCode(ArrayList<RegisterNode> registers, String code) {
		for (RegisterNode each : registers) {
			code = code.replace(", " + each.getvariable(),
					", $t" + each.getColor());
		}
		return code;
	}
}
