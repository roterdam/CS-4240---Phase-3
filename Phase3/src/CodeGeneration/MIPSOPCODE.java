package CodeGeneration;

public enum MIPSOPCODE {
	la, lw, add, sub, mul, div, and, or, b, beq, bne, bgt, blt, bge, ble, jr;
	public static final int length = MIPSOPCODE.values().length;
}
