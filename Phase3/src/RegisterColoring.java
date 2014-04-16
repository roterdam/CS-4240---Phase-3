/**
 * This is the class that will take in a block and then read its information to
 * create a graph out of the registers and color them appropriately
 * 
 * @author Crystal
 * 
 */
public class RegisterColoring {
	private CFGNode theBlock;

	/**
	 * init
	 */
	public RegisterColoring(CFGNode block) {
		theBlock = block;
	}

	/**
	 * So in order to make the IR code, we first need to look at the block. In
	 * the block, look at each line of code. Take each line of code and change
	 * all of the variables to registers. Once these registers are made, we need
	 * to create a livelyhood chart for each register. After the chart is made,
	 * we can then make a graph. After making the graph, we color the graph. The
	 * number of colors is the real number of registers we need. Then we put the
	 * regiters back inthe IR code and return it.
	 */
	public String makeIRCode() {
		Backend backend = new Backend();
		String[] lines = theBlock.getIrCode().split("\n");
		for(String each:lines){
			System.out.println("Line is: "+each);
			backend.parseLine(each, theBlock);
			System.out.println("IR Nodes are: ");
			for(Backend.IRnode eachNode: (backend.irNodes)){
				System.out.println(eachNode.toString());
			}
		}
		//get the irNodes
		return "";
	}
}
