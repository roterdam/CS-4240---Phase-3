package CodeGeneration;

import java.util.LinkedList;

public class IRList {
	private LinkedList<IR> irList;
	private LinkedList naive;
	private LinkedList cfg;
	private LinkedList ebb;
	
	public IRList() {
		irList = new LinkedList<IR>();
		naive = new LinkedList();
		cfg = new LinkedList();
		ebb = new LinkedList();
	}
	public void addIR() {
		/*for (//traverse nodes) {
			for (int i = 0; i < str.length; i++) {
				if (!str.charAt(i).equals(',')) {
					ir += str.charAt(i);
				}
			}
		}*/
	}
	
	public LinkedList<IR> list() {
		return irList;
	}

}
