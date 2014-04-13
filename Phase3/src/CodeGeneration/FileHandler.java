package CodeGeneration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileHandler {
	public static void main(String[] args) {
		try {
			OutputStream output = new FileOutputStream("mips.s");
			
			//output.write(data);
			//output.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
