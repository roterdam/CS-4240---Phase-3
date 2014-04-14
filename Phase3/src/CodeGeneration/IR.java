package CodeGeneration;

import static CodeGeneration.IROPCODE.*;
import static CodeGeneration.REGISTERS.*;

public class IR {
	private IROPCODE irOp;
	private REGISTERS regX, regY, regZ;
	private String immediate, label;
	
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
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
