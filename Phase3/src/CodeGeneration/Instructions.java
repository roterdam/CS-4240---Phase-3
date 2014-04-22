package CodeGeneration;

import static CodeGeneration.MIPSOPCODE.*;
import static CodeGeneration.REGISTERS.*;

public class Instructions {
	//private IROPCODE irOp;
	private MIPSOPCODE mipsOp;
	private REGISTERS regX, regY, regZ;
	private String offset;
	private static String immediate;
	private String label;
	private String text;
	
	public Instructions(IROPCODE op, REGISTERS regX, REGISTERS regY, REGISTERS regZ, 
			String offset, String immediate, String label) {
		this.mipsOp = mips(op);
		this.regX = regX;
		this.regY = regY;
		this.regZ = regZ;
		this.offset = offset;
		this.immediate = immediate;
		this.label = label;
	}
	
	public MIPSOPCODE getMipsOp() {
		return mipsOp;
	}
	public void setMipsOp(MIPSOPCODE mipsOp) {
		this.mipsOp = mipsOp;
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
	public String getText() {
		return label;
	}
	public void setText(String txt) {
		this.text = txt;
	}
	
	@Override
	public String toString() {
		if ((regX == null) && (regY == null) && (regZ == null) && (immediate == null) && (label == null) && (offset == null)) {
			return ("\t" + mipsOp);
		} else if ((mipsOp == null) && (regX == null) && (regY == null) && (regZ == null) && (immediate == null) && (offset == null)) {
			return ("" + label + ":");
		} else if ((regY == null) && (regZ == null) && (immediate == null) && (label == null) && (offset == null)) {
			return ("\t" + mipsOp + " " + regX);
		} else if ((regX == null) && (regY == null) && (regZ == null) && (immediate == null) && (offset == null)) {
				return ("\t" + mipsOp + " " + label);
		} else if ((regY == null) && (regZ == null) && (immediate == null) && (offset == null)) {
			return ("\t" + mipsOp + " " + regX + " " + label);
		} else if ((regY == null) && (regZ == null) && (offset == null)) {
				return ("\t" + mipsOp + " " + regX + " " + immediate + " " + label);	//for conditional branch
		} else if ((regY == null) && (regZ == null) && (label == null) && (offset == null)) {
				return ("\t" + mipsOp + " " + regX + " " + immediate);
		} else if ((regZ == null) && (immediate == null) && (label == null) && (offset == null)) {
			return ("\t" + mipsOp + " " + regX + " " + regY);
		} else if ((regZ == null) && (offset != null) && (immediate == null) && (label == null)) {
			return ("\t" + mipsOp + " " + regX + offset + "(" + regY + ")");
		} else if ((immediate == null) && (label == null) && (offset == null)) {
			return ("\t" + mipsOp + " " + regX + " " + regY + " " + regZ);
		} else if ((regZ == null) && (label == null) && (offset == null)){
			return ("\t" + mipsOp + " " + regX + " " + regY + " " + immediate);
		} else {
			return ("\t" + text);
		}
	}
	
	private static final MIPSOPCODE mips(IROPCODE op) {
		switch (op) {
			case ASSIGN:
				return null;
			case STORE:
				return sw;
			case ADD:
				if (immediate != null) {
					return addi;
				} else {
					return add;
				}
			case SUB:
				return sub;
			case MULT:
				return mul;
			case DIV:
				return div;
			case AND:
				if (immediate != null) {
					return andi;
				} else {
					return and;
				}
			case OR:
				if (immediate != null) {
					return ori;
				} else {
					return or;
				}
			case GOTO:
				return b;
			case BREQ:
				return beq;
			case BRNEQ:
				return bne;
			case BRGT:
				return bgt;
			case BRLT:
				return blt;
			case BRGEQ:
				return bge;
			case BRLEQ:
				return ble;
			case RETURN:
				return jr;
			/*case CALL:
				return ;
			case CALLR:
				return ;
			case ARRAY_STORE:
				return ;
			case "ARRAY_LOAD":
				return ;*/
			default:
				return null;
		}
	}
}
