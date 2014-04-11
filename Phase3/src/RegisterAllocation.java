import java.util.HashSet;

/*
 * This is where the testing will take place
 */
public class RegisterAllocation {
	public static void main(String[] args) {
		CreateCFG cfg=new CreateCFG();
		
		
		// this is assuming we take out any blank lines and leading/trailing
		// white space
		String highLevelIRCode = "assign, X, 100, 10\nassign, Y, 100, 10\nassign, i, 0,\nassign sum, 0,"
				+ "\nmain:\nbrgeq, i, 100, end_loop\narray_load, t1, X, i\narray_load, t2, Y, i\nmult, t3, t1, t2\n"
				+ "add, sum, sum, t3\nadd, i, i, 1\ngoto, main, ,\nend_loop:\ncall, printi, sum";

		/* *****************This is the CFG testing code***************************/
		// So first things first, I have to identify the Leaders of the basic
		// block
		HashSet<String> leaders = cfg.findLeaders(highLevelIRCode);
		// now we need to make the blocks
		HashSet<CFGNode> blocks = cfg.buildBlocks(leaders, highLevelIRCode);
	}
}
