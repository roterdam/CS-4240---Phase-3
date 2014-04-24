----------------------Read Me ----------------

/************************CFG**************************\
The CFG code is all under the CFG package in our project.
The files ands explaination are as follows:
1) CFG.java
This class is for use of other classes. It will go through and do
all of the stuff that is needed for making a CFG. It uses all of the
other CFG classes to: identify leaders in a basic block, create basic blocks
from those leaders, make a connected graph of each basic block (i.e. figure out each
block's possible successors), create registers from variables in each basic block,
color the registers, replace registers with temporaries whose numbers are equal to the
number of colors, load and store these registers.
2) CFGNode.java
This class is the node that will be used in the Control Flow Graph. It contains the
IR code that is contained in a basic block, the list of possible successors to the
basic block, and the block number for use of ordering the basic blocks.
3) CreateCFG.java
This is the main method class for CFG. It contains the following methods:
	a) findLeaders - This method follows the rules for finding leaders in IR code.
	Leaders are the lines of code that begin a basic block. There are 3 rules
	that determine a leader: 1) The first statement in a program 2) A target
	of a branch 3) Follows a branch or return statement(call statement)
	b) buildBlocks - This is the method that uses the leaders to build basic blocks.
	This will build the basic blocks. The basic block corresponding to a
	leader consists of the leader, plus all statements up to but not
	including the next leader or up to the end of the program
	c) createEdges - this will connect the basic blocks. There is a directed edge 
	from basic block B1 to basic block B2 in the CFG if: (1) There is a branch 
	from the last statement of B1 to the first statement of B2, or (2) Control
	flow can fall through from B1 to B2 because: (i) B2 immediately follows B1,
	 and (ii) B1 does not end with an unconditional branch(goto)
	d) printOutNewCode - This method orders the basic blocks and then prints out 
	the IR code 
4) RegisterColoring.java
This is the class that will take in a block and then read its information to
create a graph out of the registers and color them appropriately. It uses the 
following methods, which are all self explainatory by the names. For more detailed information,
read the java docs.
	makeRegisters, determineNeighbors, determineLiveliness, colorRegisters, makeIRCode, 
	determineSaves, determineLoads, makeNewIRCode
5) RegisterNode.java
	This class is used in the CFG graph coloring of registers. It is used to put a regsiter in a node.
	A register node contains:variable, color, lineNumber,first (instance) ,last (instance), neighbors;

To run the CFG code, all you have to do is use the following two lines of code:
CFG controlFlowGraph = new CFG();
ArrayList<String> code = controlFlowGraph.doCFG(ex1);

Where "ex1" is a String of IRcode with each line separated by "\n". You can also navigate to 
RegisterAllocation.java and uncomment the first for loop.

/****************************EBB *********************************************\
All of the EBB code is contained either in the EBB package, or uses some files from the CFG package.
EBB is Extended Basic Blocks.
Here are explainations of each EBB file:
1) EBBMethods
	Ignore this file. It is never used.
2) EBBNode
	This is a node representing an Extended Basic Block. It contains a list of the internal
	basic blocks, a list of the EBB successors, and the IR code for the EBB. 
3) MakeEBB
	This is the main method for EBB. It's methods are self explainatory. The main method is doEBB.
	It does the following: create basic blocks and connect them using CFG methods, make extended
	basic blocks (where the first block can have multiple successors, but other blocks can only have one),
	connect the basic blocks in a directed graph, do the register coloring and optimization as in CFG,
	replace variables with registers, and add loads and stores.


/*************** EBB AND CFG *********************\
Both EBB and CFG return an arraylist of strings, with each element in the arraylist being a line of the
IR code. Both EBB and CFG were tested using the following two IR codes:
// white space
		String ex1 =X:, .space, 100,
					Y:, .word, 100,
					i:, .word, 0,
					sum:, .word, 0,"
					main:
					brgeq, i, 100, end_loop
					array_load, t1, X, i
					array_load, t2, Y, i
					mult, t3, t1, t2
					add, sum, sum, t3
					add, i, i, 1
					goto, main, ,
					end_loop:
					call, printi, sum
		String ex2 = print:
					call, printi, 
					main:
					call, print, 5
For further information on methods, each of these files are java doc'd and commented.