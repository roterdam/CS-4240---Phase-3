import java.util.ArrayList;

/**
 * This is the main class that will be used to create a Control Flow Graph
 * 
 * @author Crystal
 * 
 */
public class CreateCFG {

	public static void main(String[] args) {
		// Let's do some shit! YEAH

		/*
		 * Beautiful testing low level IR code below I got it from the slide on
		 * CFG analysis, so I'm assuming this is how all IR code input will be
		 */
		String lowLevelIRCode = "(1)   prod := 0\n(2)   i := 1\n(3)   t1 := 4 * i\n(4)   t2 := a[t1]\n"
				+ "(5)   t3 := 4 * i\n(6)   t4 := b[t3]\n(7)   t5 := t2 * t4\n"
				+ "(8)   t6 := prod + t5\n(9)   prod := t6\n(10)   t7 := i + 1\n"
				+ "(11)   i := t7\n(12)   if i <= 20 goto (3)";
		
		//So first things first, I have to identify the Leaders of the basic block
		ArrayList<String> leaders = findLeaders(lowLevelIRCode);
		System.out.println(leaders);
	}

	/**
	 * Leaders are the lines of code that begin a basic block.
	 * There are 3 rules that determine a leader:
	 * 1) The first statement in a program
	 * 2) A target of a branch
	 * 3) Follows a branch or return statement
	 * @return
	 */
	private static ArrayList<String> findLeaders(String irCode) {
		ArrayList<String> theLeaders = new ArrayList<String>();
		
		//Split up each line of the IR code
		String[] lines=irCode.split("\n");
		//now let's find us some leaders!
		theLeaders.add(lines[0]);	//OBEY rule 1
		
		for(int i=1;i<lines.length;i++){
			if(lines[i].contains("goto")){
				//OBEY rule 3
				if((i+1)>=lines.length){
					theLeaders.add("("+(lines.length+1)+")");
				}
				else{
					theLeaders.add(lines[i+1]);
				}
				
				//Figure out where the goto is going to...
				String[] goingTo =lines[i].split("goto ");
				char whatNumber = goingTo[1].charAt(1);
				theLeaders.add(lines[(Integer.parseInt(""+whatNumber))-1]);	//OBEY rule 2
			}
		}
		
		return theLeaders;
	}
}
