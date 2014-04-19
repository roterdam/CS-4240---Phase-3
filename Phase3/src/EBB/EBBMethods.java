package EBB;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import CFG.CFGNode;

public class EBBMethods {
	private CFGNode entry;
	private Queue<CFGNode> EbbRoots;
	private ArrayList<ArrayList<EBBNode>> AllEbbs;
	
	/**
	 * init
	 */
	public EBBMethods(){
		AllEbbs = new ArrayList<ArrayList<EBBNode>>();
	}
	
	/**
	 * Follow psuedocode
	 * @param bBroot
	 * @return
	 */
	public ArrayList<EBBNode> createEBBs(CFGNode bBroot, CFGNode successor, CFGNode predicessor) {
		return Build_All_Ebbs(entry,entry.getNextBlock(),null);
	}
	
	public ArrayList<EBBNode> Build_All_Ebbs(CFGNode r,LinkedList<CFGNode> Succ,CFGNode Pred){
		ArrayList<EBBNode> nodes= new ArrayList<EBBNode>();
		
		CFGNode x;
		ArrayList<CFGNode> s;
		
		EbbRoots.add(r);

		while(!EbbRoots.isEmpty()){
			x = EbbRoots.poll();
			//if()
		}
		return nodes;
	}

}
