/**
 * This class is the node that will be used in the Control Flow Graph
 * @author Crystal
 *
 */
public class CFGNode {
	/**
	 * The large string of information inside of a basic block
	 */
	private String IRcode;
	/**
	 * The next basic block
	 */
	private CFGNode nextBlock;
	/**
	 * Cnstructor
	 * @param code
	 */
	public CFGNode(String code){
		IRcode=code;
	}
	/**
	 *
	 * @return the next basic block
	 */
	public CFGNode getNextBlock() {
		return nextBlock;
	}
	/**
	 * Set the next basic block of the current one
	 * @param nextBlock
	 */
	public void setNextBlock(CFGNode nextBlock) {
		this.nextBlock = nextBlock;
	}
	/**
	 * This change the toString so that it will show the IR Code of the basic block. It'll be in a "box" so it's easier to see.
	 * Ex. [*** prod := 0
	 * 		    i := 1 ***][*** t1 := 4*i ***]
	 */
	 @Override public String toString() {
		return "[*** "+ IRcode +" ***]";
		 
	 }

}
