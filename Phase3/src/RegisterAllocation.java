import java.util.ArrayList;
import java.util.HashSet;

import CFG.CFG;
import CFG.CFGNode;
import CFG.CreateCFG;
import CFG.RegisterColoring;
import EBB.EBBMethods;
import EBB.EBBNode;
import EBB.MakeEBB;

/*
 * This is where the testing will take place
 */
public class RegisterAllocation {
	public static void main(String[] args) {
		// this is assuming we take out any blank lines and leading/trailing
		// white space
		String ex1 = "X:, .space, 100,\nY:, .word, 100,\ni:, .word, 0,\nsum:, .word, 0,"
				+ "\nmain:\nbrgeq, i, 100, end_loop\narray_load, t1, X, i\narray_load, t2, Y, i\nmult, t3, t1, t2\n"
				+ "add, sum, sum, t3\nadd, i, i, 1\ngoto, main, ,\nend_loop:\ncall, printi, sum";
		String ex2 = "print:\ncall, printi, n\nmain:\ncall, print, 5";
		/****************** CFG ************************************* */
		CFG controlFlowGraph = new CFG();
		ArrayList<String> code = controlFlowGraph.doCFG(ex1);

		for (String each : code) {
			System.out.println(each);
		}
		/***************** EBB ****************************************/
		MakeEBB ebb = new MakeEBB(null, "");
		ArrayList<String> ebbCode = ebb.doEBB(ex1);
//		for (String each : ebbCode) {
//		System.out.println(each);
//	}
	}
}
