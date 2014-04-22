package CodeGeneration;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler implements AutoCloseable{
	public static final String FILE_TYPE = ".ir";
	public static final String OUTPUT_TYPE= ".s";
	private boolean redirectedConsole;
	private PrintStream out;
	private String[] args;
	
	public FileHandler(String[] args){
		this.args = args;
	}

	public void redirectConsole() throws IOException{
		if(!redirectedConsole){
			String filename = checkFilename();
			String outputName = createOutputFilename(filename);
			out = new PrintStream(outputName);
			System.setOut(out);
		}
	}
	
	@Override
	public void close(){
		out.close();
	}
	
	public String getInput() throws IOException{
		String filename = checkFilename();
		return readFile(filename, Charset.defaultCharset());
	}
	
	private boolean isFileType(String name) {
		if(name.length() <= FILE_TYPE.length()) {
			return false;
		}
		String fileType = name.substring(name.length() - FILE_TYPE.length());
		return fileType.equalsIgnoreCase(FILE_TYPE);
	}
	
	private String checkFilename() throws IOException {
		if(args.length < 1){
			throw new IOException("No arguments");
		}
		String filename = args[0];
		File file = new File(filename);
		if(!file.exists()){
			throw new IOException("Supplied file does not exist");
		}
		if(!file.isFile()){
			throw new IOException("Supplied file is not a file");
		}
		if(!file.canRead()){
			throw new IOException("Unable to read file");
		}
		if(!isFileType(file.getName())){
			throw new IOException("Incorrect file type");
		}
		return filename;
	}
	
	private String createOutputFilename(String filename){
		File inputFile = new File(filename);
		String inputFilename = inputFile.getName();
		String inputName = inputFilename.substring(0, inputFilename.length() - FILE_TYPE.length());
		return inputFile.getParent() + File.separator + inputName + OUTPUT_TYPE;
	}
	
	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
}
