package CodeGeneration;

import java.util.LinkedList;
import java.util.List;

public class IRList {
	private IR ir;
	private String str;
	private String op;
	private REGISTERS rx, ry, rz;
	private String label;
	private String offset;
	private String imme;
	private LinkedList<IR> irList;
	
	public IRList() {
		irList = new LinkedList<IR>();
		str = "";
		op = null;
		rx = null;
		ry = null;
		rz = null;
		label = null;
		offset = null;
		imme = null;
	}
	
	private void reset() {
		str = "";
		op = null;
		rx = null;
		ry = null;
		rz = null;
		label = null;
		offset = null;
		imme = null;
	}
	
	private boolean isInt(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public void addIR(List<String> inst) {
		for (String s: inst) {
			for (int i = 0; i < s.length(); i++) {
				if ((!(s.charAt(i) == ',')) || (!(s.charAt(i) == ' '))) {
					str += str.charAt(i);
				} else {
					for (IROPCODE opCode: IROPCODE.values()) {
						if (str.equals(opCode)) {
							op = str;
						}
					}
					
					for (REGISTERS r: REGISTERS.values()) {
						if (str.equals(r)) {
							if (rx != null) {
								rx = r;
							} else if (ry != null) {
								ry = r;
							} else {
								rz = r;
							}
						}
					}
					
					if ((op == null) || ((rx == null) && (ry == null) && (rz == null))) {
						label = str;
					} else if ((ry == null) && (rz == null) && (!isInt(str))) {
						label = str;
					} else if ((rx != null) && (isInt(str))) {
						imme = str;
					} else if ((imme != null) && (rx != null)) {
						label = str;
					} else if ((imme != null) && (ry != null)) {
						imme = null;
						offset = str;
					} else if ((rx != null) && (ry != null) && (isInt(str))) {
						imme = str;
					}
					
					// store result and reset all value to null for next instruction
					if ((op == null) && (rx == null) && (ry == null) && (rz == null) 
						&& (imme == null) && (offset == null) && (label == null) && (!str.equals(" ")) && (!str.equals(","))) {
						
					} else {
						irList.add(new IR(op, rx, ry, rz, offset, imme, label, null, null));
				 	}
				 	reset();
				}
			}
		}
	}
	
	public LinkedList<IR> list() {
		return irList;
	}

}
