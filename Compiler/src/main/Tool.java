package main;

import java.io.FileWriter;
import java.io.IOException;

public class Tool {
	
	public static void printline() {
		System.out.println("------------------------");
	}
	
	public static void writeline(String path, String line) {
		try {
            FileWriter writer = new FileWriter(path, true);
        	writer.write(line + "\n");
            writer.close();
		 }catch (IOException e) {
            e.printStackTrace();
		 }
	}
	
	
	
}
