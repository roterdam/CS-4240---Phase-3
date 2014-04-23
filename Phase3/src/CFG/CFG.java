package CFG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
/**
 * This class is for use of other classes. It will go through and do all of the CFG stuff.
 * @author Crystal
 *
 */
public class CFG {
	private ArrayList<String> IRcode;
	
	public CFG() {
		IRcode = new ArrayList<String>();
	}
	
	public ArrayList<String> doCFG(String originalCode){
		CreateCFG cfg=new CreateCFG();
		// So first things first, I have to identify the Leaders of the basic
		// block
		HashSet<String> leaders = cfg.findLeaders(originalCode);
		// now we need to make the blocks
		HashSet<CFGNode> blocks = cfg.buildBlocks(leaders, originalCode);
		//make a connected graph of blocks
		CFGNode graph = cfg.createEdges(blocks,originalCode);
		//let's start playing with registers now
		for(CFGNode each:blocks){
			RegisterColoring colorMe = new RegisterColoring(each);
			each.setIrCode(colorMe.makeNewIRCode());
		}
		
		String[] code =cfg.printOutNewCode(blocks).split("\n");
		for(String each:code){
			IRcode.add(each);
		}
		return IRcode;
	}

}
