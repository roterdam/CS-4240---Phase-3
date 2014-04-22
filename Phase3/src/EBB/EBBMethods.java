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
		ArrayList<EBBNode> s = null;
		
		EbbRoots.add(r);

		while(!EbbRoots.isEmpty()){
			x = EbbRoots.poll();
			int sInAllEbbs=0;
			for(EBBNode each:s){
				if(AllEbbs.contains(s)){
					sInAllEbbs=1;
				}
				else{
					sInAllEbbs=0;
					break;
				}
			}
			if(sInAllEbbs==1 && !s.get(0).equals(x)){
				s.add(Build_Ebb(x,Succ,Pred));
				AllEbbs.add(s);
				}
		}
		return nodes;
	}

	private EBBNode Build_Ebb(CFGNode r, LinkedList<CFGNode> succ, CFGNode pred) {
		EBBNode Ebb = new EBBNode();
		Add_Bbs(r,Ebb,succ,pred);
		return Ebb;
	}

	private void Add_Bbs(CFGNode r, EBBNode ebb, LinkedList<CFGNode> succ,
			CFGNode pred) {
		
		CFGNode x = new CFGNode(null, 0);
		ebb.addBlock(r);
		for(CFGNode each: r.getNextBlock()){
			if(pred.equals(each) && !ebb.getIRCode().contains(each.getIrCode())){
				Add_Bbs(each, ebb, succ, pred);
			}
			else if(!EbbRoots.contains(each)){
				EbbRoots.add(each);
			}
		}
	}
	

}
