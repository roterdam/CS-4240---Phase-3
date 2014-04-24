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
4) RegisterColoring.java
5) RegisterNode.java
For further information on methods, each of these files are java doc'd and commented.