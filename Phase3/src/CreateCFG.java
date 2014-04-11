import java.util.ArrayList;
import java.util.HashSet;

/**
 * This is the main class that will be used to create a Control Flow Graph
 * 
 * @author Crystal
 * 
 */
public class CreateCFG {

	public static void main(String[] args) {
		// Let's do some shit! YEAH

		/*
		 * Beautiful testing low level IR code below I got it from the slide on
		 * CFG analysis, so I'm assuming this is how all IR code input will be
		 */
		/*
		 * String lowLevelIRCode =
		 * "(1)   prod := 0\n(2)   i := 1\n(3)   t1 := 4 * i\n(4)   t2 := a[t1]\n"
		 * + "(5)   t3 := 4 * i\n(6)   t4 := b[t3]\n(7)   t5 := t2 * t4\n" +
		 * "(8)   t6 := prod + t5\n(9)   prod := t6\n(10)   t7 := i + 1\n" +
		 * "(11)   i := t7\n(12)   if i <= 20 goto (3)";
		 */
		
		//this is assuming we take out any blank lines and leading/trailing white space
		String highLevelIRCode = "assign, X, 100, 10\nassign, Y, 100, 10\nassign, i, 0,\nassign sum, 0,"
				+ "\nmain:\nbrgeq, i, 100, end_loop\narray_load, t1, X, i\narray_load, t2, Y, i\nmult, t3, t1, t2\n"
				+ "add, sum, sum, t3\nadd, i, i, 1\ngoto, main, ,\nend_loop:\ncall, printi, sum";

		// So first things first, I have to identify the Leaders of the basic
		// block
		HashSet<String> leaders = findLeaders(highLevelIRCode);

		for (String each : leaders) {
			System.out.println(each);
		}
	}

	/**
	 * Leaders are the lines of code that begin a basic block. There are 3 rules
	 * that determine a leader: 1) The first statement in a program 2) A target
	 * of a branch 3) Follows a branch or return statement
	 * 
	 * @return
	 */
	private static HashSet<String> findLeaders(String irCode) {
		HashSet<String> theLeaders = new HashSet<String>();

		// Split up each line of the IR code
		String[] lines = irCode.split("\n");
		// now let's find us some leaders!
		theLeaders.add(lines[0]); // OBEY rule 1

		for (int i = 1; i < lines.length; i++) {
				if (lines[i].contains("goto")) {
					// OBEY rule 3
					if ((i + 1) >= lines.length) {
						// theLeaders.add("("+(lines.length+1)+")");
						theLeaders.add("End of Code");
					} else {
						theLeaders.add(lines[i + 1]);
					}

					// Figure out where the goto is going to...
					String[] goingTo = lines[i].split(","); // originally was
															// split at goto
					// char whatNumber = goingTo[1].charAt(1);
					// theLeaders.add(lines[(Integer.parseInt(""+whatNumber))-1]);
					// //OBEY rule 2
					String where = goingTo[1].trim();
					// find that spot
					for (int j = 0; j < lines.length; j++) {
						if (lines[j].contains(where + ":")) {
							theLeaders.add(lines[j]); // OBEY rule2
						}
					}
				}
				if (lines[i].contains("br")) {
					// OBEY rule 3
					if ((i + 1) >= lines.length) {
						// theLeaders.add("("+(lines.length+1)+")");
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
		}
		return theLeaders;
	}
}
