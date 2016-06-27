package main.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BatchUtility {
	
	/*
	 * Batch Format
	 * ----------------------------------
	 * 
	 * Card Name|SetCode|4:Card Name|SetCode|3
	 * 
	 */
	
	
	
	public static void readBatch(File f){
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader(f));
			
			bf.readLine();
			
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
