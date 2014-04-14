package CodeGeneration;

import java.util.LinkedList;

public class CodeGeneration {
	private LinkedList<Instructions> list;
	private LinkedList<IR> ir;
	private IRList l;
	private Instructions instr;
	
	public CodeGeneration() {
		list = new LinkedList<Instructions>();
		//ir = l.list();
	}
	
	public void addInstructions() {
		for (IR r: ir) {
			if (r.getIrOp().equals(IROPCODE.ASSIGN)) {
				/*instr = new Instructions(null, r.getRegX(), null, null, null, null, r.getLabel());
				instr.setMipsOp(MIPSOPCODE.la);
				list.add(instr);
				instr = new Instructions(null, r.getRegX(), r.getRegY(), null, null, null, null);
				instr.setMipsOp(MIPSOPCODE.lw);
				list.add(instr);*/
			} else if (r.getIrOp().equals(IROPCODE.CALL)) {
				
			} else if (r.getIrOp().equals(IROPCODE.CALLR)) {
				
			} else if (r.getIrOp().equals(IROPCODE.ARRAY_STORE)) {
				
			} else if (r.getIrOp().equals(IROPCODE.ARRAY_LOAD)) {
				
			} else {
				instr = new Instructions(r.getIrOp(), r.getRegX(), r.getRegY(), r.getRegZ(), 
						null, r.getImmediate(), r.getLabel());
				list.add(instr);
			}
		}
	}
	
	public LinkedList<Instructions> instructionList() {
		return list;
	}
}
