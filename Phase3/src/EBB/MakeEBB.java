package EBB;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import CFG.CFGNode;
import CFG.CreateCFG;
import CFG.RegisterColoring;

public class MakeEBB {
	private HashSet<CFGNode> blocks;
	String code;

	public MakeEBB(HashSet<CFGNode> blocks, String code) {
		this.blocks = blocks;
		this.code = code;
	}
	/**
	 * This does all the neccessary things to do register allocation by EBB
	 * @param code
	 * @return
	 */
	public ArrayList<String> doEBB(String code){
		ArrayList<String> theCode = new ArrayList<String>();
		
		CreateCFG cfg = new CreateCFG();
		 //first, make basic blocks and connect them like doing CFG
		 HashSet<CFGNode> EBBlocks = cfg.buildBlocks(cfg.findLeaders(code),
		 code);
		 CFGNode BBroot = cfg.createEdges(EBBlocks,code);
		
		 //follow the directed graph to create ebb
		// ArrayList<EBBNode> EBBGraph = ebbMethods.createEBBs(BBroot, BBroot.getNextBlock().get(0),new CFGNode("null",0));
		MakeEBB EBB = new MakeEBB(EBBlocks,code);
		ArrayList<EBBNode> ebbBlocks = EBB.makeBlocks();
		ebbBlocks = EBB.makeEdges(ebbBlocks);
		
		for(EBBNode each:ebbBlocks){
			RegisterColoring colorMe = new RegisterColoring(each);
			each.setIrCode(colorMe.makeNewIRCode());
		}
		
		String[] finalCode =EBB.printOutNewCode(ebbBlocks).split("\n");
		for(String each:finalCode){
			theCode.add(each);
		}
		
		return theCode;
	}
	/**
	 * An EBB is a set of blocks such that only the first one can have more than
	 * one predeccessor.
	 * 
	 * @return
	 */
	public ArrayList<EBBNode> makeBlocks() {
		ArrayList<EBBNode> theEBBs = new ArrayList<EBBNode>();

		// first off, let's put all of the CFG blocks in order, from block 1 -
		// block n
		ArrayList<CFGNode> orderedBlocks = new ArrayList<CFGNode>();
		for (int i = 1; i < blocks.size() + 1; i++) {
			for (CFGNode each : blocks) {
				if (each.getBlockNumber() == i) {
					orderedBlocks.add(each);
				}
			}
		}
		// now let's put each block into a list that means it has not been put
		// into an ebb (put it in the stack backwards so that block 1 is on top)
		Stack<CFGNode> notInEBB = new Stack<CFGNode>();
		for (int i = orderedBlocks.size() - 1; i > -1; i--) {
			notInEBB.add(orderedBlocks.get(i));
		}

		while (!notInEBB.isEmpty()) {
			// So now we need to start at the first node, and put it and it's
			// predicessor into an ebb
			CFGNode current = notInEBB.pop();
			EBBNode ebb = new EBBNode();
			ebb.addBlock(current);
			// the first block can have two predicessors
			if (current.getBlockNumber() == 1) {
				int count = 0;
				for (CFGNode each : current.getNextBlock()) {
					if (count < 2) {
						if (notInEBB.contains(each)) {
							ebb.addBlock(each);
							notInEBB.remove(each);
							count++;
						}
					}
				}
			}
			// all other blocks can only have 1 predicessor
			if (current.getBlockNumber() != 1) {
				int count = 0;
				if (current.getNextBlock() != null) {
					for (CFGNode each : current.getNextBlock()) {
						if (notInEBB.contains(each) && count != 1) {
							ebb.addBlock(each);
							notInEBB.remove(each);
							count++;
						}
					}
				}
			}
			theEBBs.add(ebb);
		}
		return theEBBs;
	}

	/**
	 * This will connect all of the blocks in the EBB
	 * 
	 * @param ebbBlocks
	 * @return
	 */
	public ArrayList<EBBNode> makeEdges(ArrayList<EBBNode> ebbBlocks) {
		// So to do this, let's just look at the last CFGNode in a block, and
		// then look at it's next nodes. Find what blocks those nodes are in,
		// and boom
		for(EBBNode block:ebbBlocks){
			CFGNode lastNode = block.getBlocks().get(block.getBlocks().size()-1);
			if(lastNode.getNextBlock()!=null){
				for(CFGNode node:lastNode.getNextBlock()){
					//look in the ebb blocks
					for(EBBNode ebbblock:ebbBlocks){
						if(ebbblock.getIRCode().contains(node.getIrCode())){
							block.addNextEBB(ebbblock);
						}
					}
				}
			}
		}
		return ebbBlocks;
	}
	/**
	 * Prints out the new EBB code with all the replacements
	 * @param ebbBlocks
	 * @return
	 */
	public String printOutNewCode(ArrayList<EBBNode> ebbBlocks) {
		String ircode = "";
		for (EBBNode each : ebbBlocks) {
			ircode = ircode + "\n" + each.getCode();
		}

		ircode = ircode.replaceFirst("\n", "");
		return ircode;
	}
}
