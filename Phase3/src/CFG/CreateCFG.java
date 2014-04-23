package CFG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import sun.misc.Queue;

/**
 * This is the main class that will be used to create a Control Flow Graph
 * 
 * @author Crystal
 * 
 */
public class CreateCFG {

	/**
	 * This will build the basic blocks. The basic block corresponding to a
	 * leader consists of the leader, plus all statements up to but not
	 * including the next leader or up to the end of the program
	 * 
	 * @param leaders
	 * @return
	 */
	public HashSet<CFGNode> buildBlocks(HashSet<String> leaders, String irCode) {
		ArrayList<String> leaderList = new ArrayList<String>();
		for (String each : leaders) {
			leaderList.add(each);
		}

		HashSet<CFGNode> blocks = new HashSet<CFGNode>();
		String[] lines = irCode.split("\n");
		int blockNum = 0;

		for (int j = 0; j < leaders.size(); j++) { // for each leader
			String blockCode = leaderList.get(j);
			int found = 0;
			for (int i = 1; i < lines.length; i++) { // for each line
				// special case for the first leader
				if (j == 0) {
					if (!leaders.contains(lines[i])) {
						blockCode = blockCode + "\n" + lines[i];
					} else {
						break;
					}
				}
				// everyone else
				if (found == 1) { // if we are at the line after the leader
					if (!leaders.contains(lines[i])) {
						blockCode = blockCode + "\n" + lines[i];
					} else {
						found = 0;
						break;
					}
				}
				// go down to where the leader is first
				if (leaderList.get(j).compareTo(lines[i]) == 0) {
					found = 1;
				}
				// let it go
			}
			blocks.add(new CFGNode(blockCode, 0));
		}

		// now assign block numbers to each block
		int num = 1;
		for (String line : lines) {
			for (CFGNode each : blocks) {
				if (each.getIrCode().contains(line)
						&& each.getBlockNumber() == 0) {
					each.setBlockNumber(num);
					num++;
				}
			}
		}

		return blocks;
	}

	/**
	 * Leaders are the lines of code that begin a basic block. There are 3 rules
	 * that determine a leader: 1) The first statement in a program 2) A target
	 * of a branch 3) Follows a branch or return statement
	 * 
	 * @return
	 */
	public HashSet<String> findLeaders(String irCode) {
		HashSet<String> theLeaders = new HashSet<String>();

		// Split up each line of the IR code
		String[] lines = irCode.split("\n");
		// now let's find us some leaders!
		theLeaders.add(lines[0]); // OBEY rule 1

		for (int i = 1; i < lines.length; i++) {
			if (lines[i].contains("goto")) {
				// OBEY rule 3
				if ((i + 1) >= lines.length) {
					theLeaders.add("End of Code");
				} else {
					theLeaders.add(lines[i + 1]);
				}

				// Figure out where the goto is going to...
				String[] goingTo = lines[i].split(",");
				// //OBEY rule 2
				String where = goingTo[1].trim();
				// find that spot
				for (int j = 0; j < lines.length; j++) {
					if (lines[j].contains(where + ":")) {
						theLeaders.add(lines[j]); // OBEY rule2
					}
				}
			}
			if (lines[i].contains("breq") || lines[i].contains("brneq")
					|| lines[i].contains("brlt") || lines[i].contains("brgt")
					|| lines[i].contains("brgeq") || lines[i].contains("brleq")) {
				// OBEY rule 3
				if ((i + 1) >= lines.length) {
					theLeaders.add("End of Code");
				} else {
					theLeaders.add(lines[i + 1]);
				}

				// Figure out where the br is going to...
				String[] branch = lines[i].split(",");
				String where = branch[3].trim();
				// find that spot
				for (int j = 0; j < lines.length; j++) {
					if (lines[j].contains(where + ":")) {
						theLeaders.add(lines[j]); // OBEY rule2
					}
				}
			}
			//This is "Return"
			if (lines[i].contains("call")) {
			// // OBEY rule 3
			if ((i + 1) <= lines.length-1) {
				theLeaders.add(lines[i + 1]);
			}
			}
		}
		return theLeaders;
	}

	/**
	 * There is a directed edge from basic block B1 to basic block B2 in the CFG
	 * if: (1) There is a branch from the last statement of B1 to the first
	 * statement of B2, or (2) Control flow can fall through from B1 to B2
	 * because: (i) B2 immediately follows B1, and (ii) B1 does not end with an
	 * unconditional branch
	 * 
	 * @param blocks
	 * @return
	 */
	public CFGNode createEdges(HashSet<CFGNode> blocks, String irCode) {
		// Split up each line of the IR code
		String[] lines = irCode.split("\n");

		// Get the blocks in a list
		ArrayList<CFGNode> blockList = new ArrayList<CFGNode>();
		for (CFGNode each : blocks) {
			blockList.add(each);
		}

		CFGNode startNode = null;
		// find the start node
		for (CFGNode each : blocks) {
			if (each.toString().contains(lines[0])) {
				startNode = each;
			}
		}

		// *************** deal with rule 2 for directed edges
		// *******************
		Stack<CFGNode> nodeStack = new Stack<CFGNode>();
		for (CFGNode each : blocks) {
			nodeStack.add(each);
		}

		CFGNode current = startNode; // so first start with the current node
		while (!nodeStack.isEmpty()) {
			String[] elements = current.toString().split("\n");
			// look at it's last element-1 for the last line of code in it
			String element = elements[elements.length - 2];
			if (!element.contains("goto")) { // if the line isn't goto,
				// then navigate to that line in the ircode
				String nextElement = "";
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].compareTo(element) == 0) {
						if (i + 1 < lines.length) {
							// look at the next line in ircode
							nextElement = lines[i + 1];
						}
					}
				}
				if (nextElement.compareTo("") != 0) {
					CFGNode nextNode = null;
					// find that line in another node
					for (CFGNode each : blocks) {
						if (each.toString().contains(nextElement)) {
							nextNode = each;
						}
					}
					// set the next of current node to the node
					current.setNextBlock(nextNode);
					nodeStack.remove(current);
					current = nextNode;
				}
			}
			// if goto
			else {
				nodeStack.remove(current);
				if (!nodeStack.isEmpty()) {
					current = nodeStack.pop();
				} else {
					// you can't go any farther because the blocks don't connect
					return startNode;
				}
			}
		}
		// ****************done with rule 2
		// ********** work on rule 1
		for (String line : lines) {
			if (line.contains("breq") || line.contains("brneq")
					|| line.contains("brlt") || line.contains("brgt")
					|| line.contains("brgeq") || line.contains("brleq")) {
				// find the block with that line
				CFGNode block = null;
				for (CFGNode eachBlock : blocks) {
					if (eachBlock.toString().contains(line)) {
						block = eachBlock;
						break;
					}
				}
				// Figure out where the br is going to...
				String[] branch = line.split(",");
				String where = branch[3].trim();
				// find the node with that
				for (CFGNode eachNode : blocks) {
					if (eachNode.toString().contains(where + ":")) {
						block.setNextBlock(eachNode);
						break;
					}
				}
			}
			if (line.contains("goto")) {
				// find the block with that line
				CFGNode block = null;
				for (CFGNode eachBlock : blocks) {
					if (eachBlock.toString().contains(line)) {
						block = eachBlock;
						break;
					}
				}
				// Figure out where the goto is going to...
				String[] goingTo = line.split(",");
				String where = goingTo[1].trim();
				// find the node with that
				for (CFGNode eachNode : blocks) {
					if (eachNode.toString().contains(where + ":")) {
						block.setNextBlock(eachNode);
						break;
					}
				}
			}
		}
		return startNode;
	}

	/**
	 * This method will go through and print out the ir code in order
	 * 
	 * @param ex1
	 * @param blocks
	 */
	public String printOutNewCode(HashSet<CFGNode> blocks) {
		String ircode = "";
		int num = 1;
		while (num < blocks.size() + 1) {
			for (CFGNode each : blocks) {
				if (each.getBlockNumber() == num) {
					ircode = ircode + "\n***\n" + each.getIrCode();
				}
			}
			num++;
		}

		ircode = ircode.replaceFirst("\n", "");
		return ircode;
	}
}
