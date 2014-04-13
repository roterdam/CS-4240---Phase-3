package CodeGeneration;

import java.util.LinkedList;

public class CodeGeneration {
	private LinkedList<Instructions> list;
	private LinkedList<IR> ir;
	private IRList l;
	private Instructions instr;
	private int i;
	
	public CodeGeneration() {
		list = new LinkedList<Instructions>();
		ir = l.list();
		i = 0;
	}
	
	public void instructList(IR irInstr) {
		for (IR r: ir) {
			/*if (r.getIrOp().equals(MIPSOPCODE.MULT)) {
				instr = new Instructions(r.getIrOp(), r.getRegX(), r.getRegY(), null, null, null);
				list.add(instr);
				instr = new Instructions(, r.getRegX(), r.getRegY(), null, null, null);
				list.add(instr);
				instr = new Instructions(, r.getRegX(), r.getRegY(), null, null, null);
				list.add(instr);
			} else {
				instr = new Instructions(r.getIrOp(), r.getRegX(), r.getRegY(), r.getRegZ(), r.getLabel(), r.getImmediate());
			}
			list.add(instr);*/
		}
	}
	
}
