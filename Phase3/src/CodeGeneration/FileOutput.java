package CodeGeneration;

import java.io.IOException;

public class FileOutput {
	public static void main(String[] args) {
		try(FileHandler fh = new FileHandler(args)){
			CodeGeneration cg = new CodeGeneration();
			fh.redirectConsole();
			
			System.out.println("\t.text");
			System.out.println("\t.glbl main");
			for(Instructions i : cg.instructionList()){
				System.out.println(i.toString());	
			}

		} catch(IOException e){
			System.out.println("Error");
		}
	}
}
