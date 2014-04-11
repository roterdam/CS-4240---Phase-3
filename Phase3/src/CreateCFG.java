import java.util.ArrayList;
import java.util.HashSet;

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
	public HashSet<CFGNode> buildBlocks(HashSet<String> leaders,
			String irCode) {
		ArrayList<String> leaderList = new ArrayList<String>();
		for (String each : leaders) {
			leaderList.add(each);
		}

		HashSet<CFGNode> blocks = new HashSet<CFGNode>();
		String[] lines = irCode.split("\n");

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
				if (found == 1) {	//if we are at the line after the leader
					if(!leaders.contains(lines[i])){
						blockCode = blockCode + "\n" + lines[i];
					} else {
						found=0;
						break;
					}
				}
				// go down to where the leader is first
				if (leaderList.get(j).compareTo(lines[i]) == 0) {
					found = 1;
				}
				// let it go

			}
			blocks.add(new CFGNode(blockCode));
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
			if (lines[i].contains("breq")||lines[i].contains("brneq")||lines[i].contains("brlt")||lines[i].contains("brgt")||lines[i].contains("brgeq")||lines[i].contains("brleq")) {
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
//			//This is "Return"
//			if (lines[i].contains("call")) {
//				// OBEY rule 3
//				if ((i + 1) >= lines.length) {
//					theLeaders.add("End of Code");
//				} else {
//					theLeaders.add(lines[i + 1]);
//				}
//			}
		}
		return theLeaders;
	}
	
	/**
	 * There is a directed edge from basic block B1 to basic block B2 in the CFG  if:
	(1) There is a branch from the last statement of B1 to the first
         statement of B2, or
	(2) Control flow can fall through from B1 to B2 because:
           (i) B2 immediately follows B1, and 
           (ii) B1 does not end with an unconditional branch
	 * @param blocks
	 * @return
	 */
	public CFGNode createEdges(HashSet<CFGNode> blocks){
		ArrayList<CFGNode> blockList = new ArrayList<CFGNode>();
		for (CFGNode each : blocks) {
			blockList.add(each);
		}
		
		CFGNode startNode=blockList.get(0);
		
		return startNode;
	}
}
