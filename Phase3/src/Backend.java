import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Backend {
	
	Scanner irScanner;
	ArrayList<IRnode> irNodes;
	HashMap<String, Variable> variables;
	boolean inMain;
	int nextStackOffset;
	public final String STACK_POINTER = "($sp)";
	
	ArrayList<String> tempRegisters;
	int nextTempRegister;
	
	public Backend(String irFilePath) {
		
		try {
			irScanner = new Scanner(new File(irFilePath));
		}
		catch (Exception e) {
			System.err.println("File \"" + irFilePath + "\" not found");
		}
		
		irNodes = new ArrayList<IRnode>();
		variables = new HashMap<String, Variable>();
		inMain = false;
		nextStackOffset = 0;
		
		tempRegisters = new ArrayList<String>();
		populateRegisters();
		
	}
	
	private void populateRegisters() {
		tempRegisters.add("$t0");
		tempRegisters.add("$t1");
		tempRegisters.add("$t2");
		
		nextTempRegister = 0;
	}
	
	public void naive() {	
		
		while (irScanner.hasNext()) {
			String irLine = irScanner.nextLine();
			parseLine(irLine);
		}
		
		for (IRnode node : irNodes) {
			System.out.println(node);
			if (node.operands[0] != null && node.operands[0].equals("main:"))
				inMain = true;
		}
		
	}
	
	private void parseLine(String irLine) {
		
		Scanner scanner = new Scanner(irLine);
		
		String OP = scanner.next();
		if (OP.equals("assign,"))
			addAssign(scanner);
		else if (OP.equals("add,") || OP.equals("sub,") || OP.equals("mult,") || OP.equals("div,") || OP.equals("and,") || OP.equals("or,"))
			addBinaryOp(scanner, OP);
		else if (OP.equals("goto,")) {
			String label = scanner.next();
			label = label.substring(0, label.indexOf(","));
			irNodes.add(new IRnode("goto", label));
		}
		else if (OP.equals("breq,") || OP.equals("brneq,") || OP.equals("brlt,") || OP.equals("brgt,") || OP.equals("brgeq,") || OP.equals("brleq,"))
			addBranch(scanner, OP);
		else if (OP.equals("return,")) {
			String returnVar = scanner.next();
			returnVar = returnVar.substring(0, returnVar.indexOf(","));
			
			addVarToList(returnVar);
			String returnVarReg = loadStoreVar("load", returnVar);
			
			if (returnVarReg == null)
				returnVarReg = returnVar;
			
			irNodes.add(new IRnode("return", returnVarReg));
			
			nextTempRegister = 0;
			loadStoreVar("store", returnVar);
			nextTempRegister = 0;
		}
		else if (OP.equals("array_store,"))
			addArrayStore(scanner);
		else if (OP.equals("array_load,"))
			addArrayLoad(scanner);
		else if (OP.contains(":"))
			irNodes.add(new IRnode("label", OP));
		
	}
	
	private void addAssign(Scanner lineScanner) {
		
		String assignedVar = "", value = "";
		
		assignedVar = lineScanner.next();
		value = lineScanner.next();
		
		int comma = assignedVar.indexOf(",");
		assignedVar = assignedVar.substring(0, comma);
		
		comma = value.indexOf(",");
		value = value.substring(0, comma);
		
		addVarToList(assignedVar);
		addVarToList(value);
		
		String assignedVarRegister = loadStoreVar("load", assignedVar);
		String valueRegister = loadStoreVar("load", value);
		
		if (valueRegister == null)
			valueRegister = value;
		
		irNodes.add(new IRnode("assign", assignedVarRegister, valueRegister));
		
		nextTempRegister = 0;
		loadStoreVar("store", assignedVar);
		loadStoreVar("store", value);
		
		nextTempRegister = 0;
	}
	
	private String loadStoreVar(String loadOrStore, String var) {
		
		boolean isVariable = isVariable(var);
		String register = null;
		
		if (isVariable) {
			register = tempRegisters.get(nextTempRegister);
			nextTempRegister++;
			Variable variable = variables.get(var);
			irNodes.add(new IRnode(loadOrStore, register, variable.stackOffset + STACK_POINTER));
		}
		
		return register;
	}
	
	private void addVarToList(String var) {
		
		boolean isVariable = isVariable(var);
		
		if (isVariable) {
			
			if (!variables.containsKey(var)) {
				variables.put(var, new Variable(var, nextStackOffset));
				nextStackOffset++;
			}
			
		}
		
	}
	
	private boolean isVariable(String var) {
		
		boolean isVariable = false;
		
		try {
			Integer.parseInt(var);
		}
		catch (Exception e) {
			isVariable = true;
		}
		
		return isVariable;
	}
	
	private void addBinaryOp(Scanner lineScanner, String operation) {
		
		String operand1 = "", operand2 = "", assignedVar = "";
		
		// Get the variable names
		operand1 = lineScanner.next();
		operand2 = lineScanner.next();
		assignedVar = lineScanner.next();
		
		// Remove the commas
		String op = operation.substring(0, operation.indexOf(","));
		operand1 = operand1.substring(0, operand1.indexOf(","));
		operand2 = operand2.substring(0, operand2.indexOf(","));
		
		addVarToList(operand1);
		addVarToList(operand2);
		addVarToList(assignedVar);
		
		String operand1Reg = loadStoreVar("load", operand1);
		String operand2Reg = loadStoreVar("load", operand2);
		String assignedVarReg = loadStoreVar("load", assignedVar);
		
		if (operand2Reg == null) {
			operand2Reg = operand2;
			op += "i";
		}
		
		// Create and add the node to the list of nodes
		irNodes.add(new IRnode(op, operand1Reg, operand2Reg, assignedVarReg));
		
		nextTempRegister = 0;
		loadStoreVar("store", operand1);
		loadStoreVar("store", operand2);
		loadStoreVar("store", assignedVar);
		
		nextTempRegister = 0;
	}
	
	
	private void addBranch(Scanner lineScanner, String operation) {
		
		String op = operation.substring(0, operation.indexOf(",")); // removes the commas from the branch OP
		String operand1 = "", operand2 = "", afterBranchLabel = "";
		
		// Get the operands
		operand1 = lineScanner.next();
		operand2 = lineScanner.next();
		afterBranchLabel = lineScanner.next();
		
		// remove the commas from the operands
		operand1 = operand1.substring(0, operand1.indexOf(","));
		operand2 = operand2.substring(0, operand2.indexOf(","));
		
		addVarToList(operand1);
		addVarToList(operand2);
		
		String operand1Reg = loadStoreVar("load", operand1);
		String operand2Reg = loadStoreVar("load", operand2);
		
		if (operand1Reg == null)
			operand1Reg = operand1;
		if (operand2Reg == null)
			operand2Reg = operand2;
		
		// Create and add the node to the list
		irNodes.add(new IRnode(op, operand1Reg, operand2Reg, afterBranchLabel));
		
		nextTempRegister = 0;
		loadStoreVar("store", operand1);
		loadStoreVar("store", operand2);
		
		nextTempRegister = 0;
		
	}
	
	private void addArrayStore(Scanner lineScanner) {
		
		String arrName = "", offset = "", storeFrom = "";
		
		// Get operands
		arrName = lineScanner.next();
		offset = lineScanner.next();
		storeFrom = lineScanner.next();
		
		// Remove the commas from original string
		arrName = arrName.substring(0, arrName.indexOf(","));
		offset = offset.substring(0, offset.indexOf(","));
		
		addVarToList(arrName);
		addVarToList(storeFrom);
		
		String arrNameReg = loadStoreVar("load", arrName);
		String storeFromReg = loadStoreVar("load", storeFrom);
		
		if (storeFromReg == null)
			storeFromReg = storeFrom;
		
		// Create and add new node to the list
		irNodes.add(new IRnode("array_store", arrNameReg, offset, storeFromReg));
		
		nextTempRegister = 0;
		loadStoreVar("store", arrName);
		loadStoreVar("store", storeFrom);
		
		nextTempRegister = 0;
	}
	
	private void addArrayLoad(Scanner lineScanner) {
		
		String loadTo = "", array = "", offset = "";
		
		// Get the operands
		loadTo = lineScanner.next();
		array = lineScanner.next();
		offset = lineScanner.next();
		
		// Remove the commas from the original string
		loadTo = loadTo.substring(0, loadTo.indexOf(","));
		array = array.substring(0, array.indexOf(","));
		
		addVarToList(array);
		addVarToList(loadTo);
		
		String arrayReg = loadStoreVar("load", array);
		String loadToReg = loadStoreVar("load", loadTo);
		
		// Create and add the node to the list
		irNodes.add(new IRnode("array_load", loadToReg, arrayReg, offset));
		
		nextTempRegister = 0;
		loadStoreVar("store", array);
		loadStoreVar("store", loadTo);
		
		nextTempRegister = 0;
	}
	
	private class IRnode {
		
		String nodeType;
		String[] operands;
		
		public IRnode(String nodeType, String ...strings) {
			this.nodeType = nodeType;
			this.operands = strings;
		}
		
		public String toString() {
			String IRstring = nodeType + " ";
			
			for (int i = 0; i < operands.length; i++)
				IRstring += operands[i] + " ";
			
			return IRstring;
		}
	}
	
	private class Variable {
		
		String varName;
		int stackOffset;
		
		public Variable(String varName, int stackOffset) {
			this.varName = varName;
			this.stackOffset = stackOffset;
		}
		
		public boolean equals(Object o) {
			
			Variable compareTo = (Variable) o;
			
			if (o instanceof Variable && varName.equals(compareTo.varName) && stackOffset == compareTo.stackOffset)
				return true;
			else
				return false;
			
		}
		
		public int hashcode() {
			return varName.hashCode() + stackOffset;
		}
		
	}
	
	public static void main(String[] args) {
		Backend backend = new Backend("./ex1.ir");
		backend.naive();
	}
	
}
