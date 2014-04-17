import java.util.HashSet;

/*
 * This is where the testing will take place
 */
public class RegisterAllocation {
	public static void main(String[] args) {
		CreateCFG cfg=new CreateCFG();
		
		
		// this is assuming we take out any blank lines and leading/trailing
		// white space
		String ex1 = "assign, X, 100, 10\nassign, Y, 100, 10\nassign, i, 0,\nassign, sum, 0,"
				+ "\nmain:\nbrgeq, i, 100, end_loop\narray_load, t1, X, i\narray_load, t2, Y, i\nmult, t3, t1, t2\n"
				+ "add, sum, sum, t3\nadd, i, i, 1\ngoto, main, ,\nend_loop:\ncall, printi, sum";
		String ex2 = "print:\ncall, printi, n\nmain:\ncall, print, 5";

		/* *****************This is the CFG testing code***************************/
		// So first things first, I have to identify the Leaders of the basic
		// block
		HashSet<String> leaders = cfg.findLeaders(ex1);
		// now we need to make the blocks
		HashSet<CFGNode> blocks = cfg.buildBlocks(leaders, ex1);
		CFGNode graph = cfg.createEdges(blocks,ex1);
		
		//Print out basic blocks
//		for(CFGNode each:blocks){
//			System.out.println("------This block--------");
//			System.out.println(each);
//			System.out.println("-----Can Go To ---------");
//			for(CFGNode eachNext:each.getNextBlock()){
//				System.out.println(eachNext);
//			}
//		}
//		
		//let's start playing with registers now
		for(CFGNode each:blocks){
			RegisterColoring colorMe = new RegisterColoring(each);
			System.out.println("****");
			colorMe.makeIRCode();
		}
	}
}
