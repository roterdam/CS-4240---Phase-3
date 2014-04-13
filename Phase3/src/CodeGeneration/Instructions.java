package CodeGeneration;

public class Instructions {
	//private IROPCODE irOp;
	private MIPSOPCODE mipsOp;
	private REGISTERS regX, regY, regZ;
	private String immediateL, immediateR, label;
	
	public Instructions(IROPCODE op, REGISTERS regX, REGISTERS regY, REGISTERS regZ, String immediateL, String immediateR, String label) {
		//this.mipsOp = mips(op);
		this.regX = regX;
		this.regY = regY;
		this.regZ = regZ;
		this.immediateL = immediateL;
		this.immediateR = immediateR;
		this.label = label;
	}
	
	/*public IROPCODE getIROp() {
		return irOp;
	}
	public void setIROp(IROPCODE irOp) {
		this.irOp = irOp;
	}*/
	
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
	public String getImmeR() {
		return immediateR;
	}
	public void setImmeR(String immediate) {
		this.immediateR = immediate;
	}
	
	public String getImmeL() {
		return immediateL;
	}
	public void setImmeL(String immediate) {
		this.immediateL = immediate;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		if ((regX == null) && (regY == null) && (regZ == null) && (immediateR == null) && (label == null)) {
			return ("\t" + mipsOp);
		} else if ((regY == null) && (regZ == null) && (immediateR == null) && (label == null)) {
			return ("\t" + mipsOp + " " + regX);
		} else if ((regX == null) && (regY == null) && (regZ == null) && (immediateR == null)) {
				return ("\t" + mipsOp + " " + label);
		} else if ((regY == null) && (regZ == null) && (immediateR == null)) {
			return ("\t" + mipsOp + " " + regX + " " + label);
		} else if ((regY == null) && (regZ == null)) {
				return ("\t" + mipsOp + " " + regX + " " + immediateR + " " + label);
		} else if ((regY == null) && (regZ == null) && (label == null)) {
				return ("\t" + mipsOp + " " + regX + " " + immediateR);
		} else if ((regZ == null) && (immediateR == null) && (label == null)) {
			return ("\t" + mipsOp + " " + regX + " " + regY);
		} else if ((regZ == null) && (immediateL != null) && (immediateR == null) && (label == null)) {
			return ("\t" + mipsOp + " " + regX + immediateL + "(" + regY + ")");
		} else if ((immediateR == null) && (label == null)) {
			return ("\t" + mipsOp + " " + regX + " " + regY + " " + regZ);
		} else if ((regZ == null) && (label == null)){
			return ("\t" + mipsOp + " " + regX + " " + regY + " " + immediateR);
		} else {
			return null;
		}
	}
	
	/*private static final MIPSOPCODE mips(IROPCODE op) {
		switch (opText) {
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			case "":
				return ;
			default:
				return ;
		}
	}*/
}
