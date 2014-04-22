package CodeGeneration;

import static CodeGeneration.IROPCODE.*;

import java.util.LinkedList;

public class IR {
	private IROPCODE irOp;
	private REGISTERS regX, regY, regZ;
	private String immediate, label, label2, offset;
	private LinkedList<IR> args;
	
	public IR (String op, REGISTERS regX, REGISTERS regY, REGISTERS regZ, 
			String offset, String immediate, String label, String label2, LinkedList<IR> args) {
		this.irOp = IR(op);
		this.regX = regX;
		this.regY = regY;
		this.regZ = regZ;
		this.setOffset(offset);
		this.immediate = immediate;
		this.label = label;
		this.setLabel2(label2);
		this.setArgs(args);
	}
	
	public IROPCODE getIrOp() {
		return irOp;
	}
	public void setIrOp(IROPCODE irOp) {
		this.irOp = irOp;
	}
	public REGISTERS getRegX() {
		return regX;
	}
	public void setRegX(REGISTERS regX) {
		this.regX = regX;
	}
	public REGISTERS getRegY() {
		return regY;
	}
	public void setRegY(REGISTERS regY) {
		this.regY = regY;
	}
	public REGISTERS getRegZ() {
		return regZ;
	}
	public void setRegZ(REGISTERS regZ) {
		this.regZ = regZ;
	}
	public String getImmediate() {
		return immediate;
	}
	public void setImmediate(String immediate) {
		this.immediate = immediate;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	private static final IROPCODE IR(String op) {
		switch (op) {
			case "load":
				return LOAD;
			case "store":
				return STORE;
			case "assign":
				return ASSIGN;
			case "add":
				return ADD;
			case "sub":
				return SUB;
			case "mult":
				return MULT;
			case "div":
				return DIV;
			case "and":
				return AND;
			case "or":
				return OR;
			case "goto":
				return GOTO;
			case "breq":
				return BREQ;
			case "brneq":
				return BRNEQ;
			case "brlt":
				return BRLT;
			case "brgt":
				return BRGT;
			case "brgeq":
				return BRGEQ;
			case "brleq":
				return BRLEQ;
			case "return":
				return RETURN;
			case "call":
				return CALL;
			case "callr":
				return CALLR;
			case "array_store":
				return ARRAY_STORE;
			case "array_load":
				return ARRAY_LOAD;
			default:
				return null;
		}
	}

	public LinkedList<IR> getArgs() {
		return args;
	}

	public void setArgs(LinkedList<IR> args) {
		this.args = args;
	}

	public String getLabel2() {
		return label2;
	}

	public void setLabel2(String label2) {
		this.label2 = label2;
	}
}
