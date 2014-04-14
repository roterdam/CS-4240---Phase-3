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
	
	public void instructList(IR irInstr) {
		for (IR r: ir) {
			if (r.getIrOp().equals(IROPCODE.ASSIGN)) {
				instr = new Instructions(null, r.getRegX(), null, null, null, null, r.getLabel());
				instr.setMipsOp(MIPSOPCODE.la);
				list.add(instr);
				instr.setMipsOp(MIPSOPCODE.lw);
				//instr = new Instructions(null, r.getRegX(), r.getRegY(), null, null, null, null);
				list.add(instr);
			} else {
				instr = new Instructions(r.getIrOp(), r.getRegX(), r.getRegY(), r.getRegZ(), 
						null, r.getImmediate(), r.getLabel());
				list.add(instr);
			}
		}
	}
	
	public LinkedList<Instructions> instList() {
		return list;
	}
}
