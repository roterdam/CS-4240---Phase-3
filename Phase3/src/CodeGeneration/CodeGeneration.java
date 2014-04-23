package CodeGeneration;

import java.util.LinkedList;

public class CodeGeneration {
	private LinkedList<Instructions> list;
	private LinkedList<IR> ir;
	private int offset;
	private IRList l;
	private Instructions instr;
	private boolean call, callr;
	
	public CodeGeneration() {
		list = new LinkedList<Instructions>();
		call = false;
		callr = false;
		offset = 4;
		//ir = l.list();
	}
	
	public void addInstructions() {	
		instr = new Instructions(null, null, null, null, null, null, null);
		instr.setLabel(".text");
		list.add(instr);
		instr = new Instructions(null, null, null, null, null, null, null);
		instr.setLabel(".globl main");
		list.add(instr);
		instr = new Instructions(null, null, null, null, null, null, "main");
		list.add(instr);
		
		for (IR r: ir) {
			if (r.getIrOp().equals(IROPCODE.LOAD)) {
				instr = new Instructions(null, r.getRegX(), null, null, null, null, r.getLabel());
				instr.setMipsOp(MIPSOPCODE.la);
				list.add(instr);
				instr = new Instructions(null, r.getRegX(), r.getRegX(), null, null, null, null);
				instr.setMipsOp(MIPSOPCODE.lw);
				list.add(instr);
			} else if ((r.getIrOp().equals(IROPCODE.CALL)) && 
					((!call) || (r.getArgs().getFirst().getLabel().equals("printi")))) {
				if (r.getArgs().getFirst().getLabel().equals("printi")) {
					instr = new Instructions(null, REGISTERS.$v0, null, null, null, "1", null);
					instr.setMipsOp(MIPSOPCODE.li);
					list.add(instr);
					
					/**************************************************/
					/* need to change reg $s8, it may not be where the result is stored*/
					instr = new Instructions(null, REGISTERS.$a0, REGISTERS.$s8, null, null, null, null);
					instr.setMipsOp(MIPSOPCODE.move);
					list.add(instr);
					instr = new Instructions(null, null, null, null, null, null, null);
					instr.setMipsOp(MIPSOPCODE.syscall);
					list.add(instr);
					instr = new Instructions(null, REGISTERS.$ra, null, null, null, null, null);
					instr.setMipsOp(MIPSOPCODE.jr);
					list.add(instr);
				} else {
					call = true;
					instr = new Instructions(null, null, null, null, null, null, r.getLabel());
					instr.setMipsOp(MIPSOPCODE.jal);
					list.add(instr);
					
					for (IR ir: r.getArgs()) {
						saveArg(ir.getRegX());
					}
					startStack();
					for (IR ir: r.getArgs()) {
						loadArg(ir.getRegX());
					}
					instr = new Instructions(r.getIrOp(), r.getRegX(), r.getRegY(), r.getRegZ(), 
							null, r.getImmediate(), r.getLabel());
					list.add(instr);
				}
			} else if (call) {
				if (r.getIrOp().equals(IROPCODE.RETURN)) {
					call = false;
					endStack();
					instr = new Instructions(null, REGISTERS.$ra, null, null, null, null, null);
					instr.setMipsOp(MIPSOPCODE.jr);
					list.add(instr);
				} else {
					instr = new Instructions(r.getIrOp(), r.getRegX(), r.getRegY(), r.getRegZ(), 
							null, r.getImmediate(), r.getLabel());
					list.add(instr);
				}
			} else if (r.getIrOp().equals(IROPCODE.CALLR) && (!callr)) {
				callr = true;
				instr = new Instructions(null, null, null, null, null, null, r.getLabel());
				instr.setMipsOp(MIPSOPCODE.jal);
				list.add(instr);
				
				for (IR ir: r.getArgs()) {
					saveArg(ir.getRegX());
				}
				startStack();
				for (IR ir: r.getArgs()) {
					loadArg(ir.getRegX());
				}
				instr = new Instructions(r.getIrOp(), r.getRegX(), r.getRegY(), r.getRegZ(), 
						null, r.getImmediate(), r.getLabel());
				list.add(instr);
			} else if (callr) {
				if (r.getIrOp().equals(IROPCODE.RETURN)) {
					callr = false;
					endStack();
					instr = new Instructions(null, r.getRegX(), REGISTERS.$v0, null, null, null, null);
					instr.setMipsOp(MIPSOPCODE.move);
					list.add(instr);
					instr = new Instructions(null, REGISTERS.$ra, null, null, null, null, null);
					instr.setMipsOp(MIPSOPCODE.jr);
					list.add(instr);
				} else {
					instr = new Instructions(r.getIrOp(), r.getRegX(), r.getRegY(), r.getRegZ(), 
							null, r.getImmediate(), r.getLabel());
					list.add(instr);
				}
			} else if (r.getIrOp().equals(IROPCODE.ARRAY_STORE)) {
				/*********		$t5 contains the memory address of array	***********/
				instr = new Instructions(null, REGISTERS.$t5, null, null, 
						null, null, r.getLabel());
				instr.setMipsOp(MIPSOPCODE.la);
				list.add(instr);
				/*********		$t6 contains the index of the array		***********/
				instr = new Instructions(null, REGISTERS.$t6, r.getRegY(), null,
						null, null, null);
				instr.setMipsOp(MIPSOPCODE.move);
				list.add(instr);
				instr = new Instructions(IROPCODE.ADD, REGISTERS.$t6, REGISTERS.$t6, REGISTERS.$t6,
						null, null, null);
				list.add(instr);
				instr = new Instructions(IROPCODE.ADD, REGISTERS.$t6, REGISTERS.$t6, REGISTERS.$t6,
						null, null, null);
				list.add(instr);
				instr = new Instructions(IROPCODE.ADD, REGISTERS.$t5, REGISTERS.$t5, REGISTERS.$t6,
						null, null, null);
				list.add(instr);
				instr = new Instructions(null, r.getRegX(), REGISTERS.$t5, null,
						"0", null, null);
				instr.setMipsOp(MIPSOPCODE.lw);
				list.add(instr);
			} else if (r.getIrOp().equals(IROPCODE.ARRAY_LOAD)) {
				/*********		$t5 contains the memory address of array	***********/
				instr = new Instructions(null, REGISTERS.$t5, null, null, 
						null, null, r.getLabel());
				instr.setMipsOp(MIPSOPCODE.la);
				list.add(instr);
				/*********		$t6 contains the index of the array		***********/
				instr = new Instructions(null, REGISTERS.$t6, r.getRegY(), null,
						null, null, null);
				instr.setMipsOp(MIPSOPCODE.move);
				list.add(instr);
				instr = new Instructions(IROPCODE.ADD, REGISTERS.$t6, REGISTERS.$t6, REGISTERS.$t6,
						null, null, null);
				list.add(instr);
				instr = new Instructions(IROPCODE.ADD, REGISTERS.$t6, REGISTERS.$t6, REGISTERS.$t6,
						null, null, null);
				list.add(instr);
				instr = new Instructions(IROPCODE.ADD, REGISTERS.$t5, REGISTERS.$t5, REGISTERS.$t6,
						null, null, null);
				list.add(instr);
				instr = new Instructions(null, r.getRegX(), REGISTERS.$t5, null,
						"0", null, null);
				instr.setMipsOp(MIPSOPCODE.sw);
				list.add(instr);
			} else {
				instr = new Instructions(r.getIrOp(), r.getRegX(), r.getRegY(), r.getRegZ(), 
						null, r.getImmediate(), r.getLabel());
				list.add(instr);
			}
		}
	}
	
	private void startStack() {
		/***** create stack *****/
		instr = new Instructions(IROPCODE.ADD, REGISTERS.$sp, REGISTERS.$sp, null, 
				null, "-3", null);
		list.add(instr);
		instr = new Instructions(IROPCODE.STORE, REGISTERS.$ra, REGISTERS.$sp, null, 
				"1", null, null);
		list.add(instr);
		instr = new Instructions(IROPCODE.STORE, REGISTERS.$s8, REGISTERS.$sp, null, 
				"0", null, null);
		list.add(instr);
		instr = new Instructions(IROPCODE.ADD, REGISTERS.$s8, REGISTERS.$sp, null, 
				null, "-1", null);
		list.add(instr);
	}
	
	private void endStack() {
		/***** save answer in RV *****/
		instr = new Instructions(null, REGISTERS.$v0, REGISTERS.$s8, null,
				"3", null, null);
		instr.setMipsOp(MIPSOPCODE.sw);
		list.add(instr);
		
		/***** restore stack *****/
		instr = new Instructions(IROPCODE.ADD, REGISTERS.$sp, REGISTERS.$s8, null, 
				null, "1", null);
		list.add(instr);
		instr = new Instructions(null, REGISTERS.$s8, REGISTERS.$sp, null, 
				"0", null, null);
		instr.setMipsOp(MIPSOPCODE.lw);
		list.add(instr);
		instr = new Instructions(IROPCODE.ADD, REGISTERS.$sp, REGISTERS.$sp, null, 
				null, "1", null);
		list.add(instr);
		instr = new Instructions(null, REGISTERS.$ra, REGISTERS.$sp, null, 
				"0", null, null);
		instr.setMipsOp(MIPSOPCODE.lw);
		list.add(instr);
		instr = new Instructions(IROPCODE.ADD, REGISTERS.$sp, REGISTERS.$sp, null, 
				null, "1", null);
		list.add(instr);
		instr = new Instructions(null, REGISTERS.$v0, REGISTERS.$sp, null, 
				"0", null, null);
		instr.setMipsOp(MIPSOPCODE.lw);
		list.add(instr);
		instr = new Instructions(IROPCODE.ADD, REGISTERS.$sp, REGISTERS.$sp, null, 
				null, "2", null);
		list.add(instr);
	}
	
	private void saveArg(REGISTERS reg) {
		instr = new Instructions(IROPCODE.ADD, REGISTERS.$sp, REGISTERS.$sp, null, 
				null, "-1", null);
		list.add(instr);
		instr = new Instructions(IROPCODE.ADD, reg, REGISTERS.$sp, null, 
				"0", null, null);
		list.add(instr);
	}
	
	private void loadArg(REGISTERS reg) {
		instr = new Instructions(null, reg, REGISTERS.$s8, null, 
				("" + offset), null, null);
		instr.setMipsOp(MIPSOPCODE.lw);
		list.add(instr);
		offset++;
	}
	
	public LinkedList<Instructions> instructionList() {
		return list;
	}
}
