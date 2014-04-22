package CodeGeneration;

import java.io.IOException;

public class FileOutput {
	public static void main(String[] args) {
		try(FileHandler fh = new FileHandler(args)){
			CodeGeneration cg = new CodeGeneration();
			fh.redirectConsole();
			
			for(Instructions i : cg.instructionList()){
				System.out.println(i.toString());	
			}

		} catch(IOException e){
			System.out.println("Error");
		}
	}
}
