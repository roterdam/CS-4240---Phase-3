package CodeGeneration;

import java.util.LinkedList;

public class IRList {
	private LinkedList<IR> l;
	
	public void addIR(IR ir) {
		l.add(ir);
	}
	
	public LinkedList<IR> list() {
		return l;
	}

}
