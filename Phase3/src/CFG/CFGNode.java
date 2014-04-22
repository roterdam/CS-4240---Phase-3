package CFG;
import java.util.LinkedList;

/**
 * This class is the node that will be used in the Control Flow Graph
 * @author Crystal
 *
 */
public class CFGNode {
	/**
	 * The large string of information inside of a basic block
	 */
	private String IRcode,load,store;
	/**
	 * The next basic block
	 */
	private LinkedList<CFGNode> nextBlock;
	/**
	 * For ordering blocks
	 */
	private int blockNumber;
	/**
	 * Cnstructor
	 * @param code
	 */
	public CFGNode(String code,int blockNumber){
		IRcode=code;
		nextBlock=new LinkedList<CFGNode>();
		this.setBlockNumber(blockNumber);
	}
	/**
	 *
	 * @return the next basic block
	 */
	public LinkedList<CFGNode> getNextBlock() {
		return nextBlock;
	}
	/**
	 * Set the next basic block of the current one
	 * @param nextBlock
	 */
	public void setNextBlock(CFGNode nextBlock) {
		this.nextBlock.add(nextBlock);
	}
	/**
	 * This change the toString so that it will show the IR Code of the basic block. It'll be in a "box" so it's easier to see.
	 * Ex. [***
	 * 			 prod := 0
	 * 		    i := 1 
	 * 		***]
	 * 		[***
	 * 		 t1 := 4*i 
	 * 		***]
	 */
	 @Override public String toString() {
		return "[**********\n"+ IRcode +"\n**********]";
		 
	 }
	 
	 /**
	  * This will return the IRCode in the block, separated by \n for each line
	  * @return
	  */
	 public String getIrCode(){
		 return IRcode;
	 }
	 
	 public void setIrCode(String code){
		 IRcode=code;
	 }
	public String getLoad() {
		return load;
	}
	public void setLoad(String load) {
		this.load = load;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public int getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(int blockNumber) {
		this.blockNumber = blockNumber;
	}

}
