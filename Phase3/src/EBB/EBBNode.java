package EBB;

import java.util.ArrayList;

import CFG.CFGNode;

/**
 * An Extended Basic Block
 * 
 * @author Crystal
 * 
 */
public class EBBNode {
	/**
	 * The internal blocks
	 */
	private ArrayList<CFGNode> blocks;
	/**
	 * A list of the next EBB that this one can go to
	 */
	private ArrayList<EBBNode> nextEBB;
	/**
	 * IR Code
	 */
	private String code;

	/**
	 * init
	 */
	public EBBNode() {
		blocks = new ArrayList<CFGNode>();
		nextEBB = new ArrayList<EBBNode>();
	}

	/**
	 * Get a list of the basic blocks inside of the EBB
	 * 
	 * @return basic blocks inside of EBB
	 */
	public ArrayList<CFGNode> getBlocks() {
		return blocks;
	}

	/**
	 * Adds a basic block to the EBB
	 * 
	 * @param block
	 */
	public void addBlock(CFGNode block) {
		blocks.add(block);
	}

	/**
	 * 
	 * @return a list of the possible next EBB
	 */
	public ArrayList<EBBNode> getNextEBB() {
		return nextEBB;
	}

	/**
	 * Adds an EBB to the list of next EBBs
	 * 
	 * @param next
	 */
	public void addNextEBB(EBBNode next) {
		nextEBB.add(next);
	}

	/**
	 * Goes through the CFGNodes inside of the EBB and prints out the IR code
	 * 
	 * @return
	 */
	public String getIRCode() {
		String irCode = "";
		for (CFGNode each : blocks) {
			if (each != null) {
				irCode = irCode+each.getIrCode()+"\n";
			}
		}
		//remove last "\n"
		irCode = irCode.substring(0, irCode.lastIndexOf("\n"));
		code=irCode;
		return irCode;
	}
	/**
	 * Gets the code
	 * @return
	 */
	public String getCode(){
		return code;
	}
	/**
	 * Sets the irCode;
	 * @param makeNewIRCode
	 */
	public void setIrCode(String makeNewIRCode) {
		code=makeNewIRCode;		
	}
}
