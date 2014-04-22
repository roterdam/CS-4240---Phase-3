package CodeGeneration;

public enum MIPSOPCODE {
	la, lw, li, sw, add, addi, sub, mul, div, and, andi, or, ori, b, beq, bne, bgt, blt, bge, ble, jr, move, syscall;
	public static final int length = MIPSOPCODE.values().length;
}
