package CodeGeneration;

public enum IROPCODE {
	ASSIGN, ADD, SUB, MULT, DIV, AND, OR, GOTO, BREQ, BRNEQ, BRLT, BRGT, BRGEQ, BRLEQ,
	RETURN, CALL, CALLR, ARRAY_STORE, ARRAY_LOAD;
	public static final int length = IROPCODE.values().length;
}
