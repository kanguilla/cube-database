package main.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import main.core.Card;
import main.core.CardEntry;
import main.core.Database;

public class BatchUtility {
	
	/*
	 * Batch Format
	 * ----------------------------------
	 * 
	 * #Card Name:SetCode:4#Card Name:SetCode:3
	 * 
	 */
	
	
	
	public static void readBatch(String path, Database db){
		try {
			String s = new String(Files.readAllBytes(Paths.get(path)));
			String[] elements = s.split("\\#");
			for (String element : elements){
				String[] comp = element.split(":");
				
				ArrayList<Card> c = db.getMtgCards("select * from cards where name=\"" + comp[0].trim() + "\"");
				if(c.isEmpty()){
					System.out.println("No such card - " + comp[0].trim());
					continue;
				}
				if (c != null){
					if (comp.length == 1){
						db.addToCube(c.get(0), db.getMostRecentSet(c.get(0)), 1);
					}else if (comp.length == 2){
						db.addToCube(c.get(0), comp[1], 1);
					}else if (comp.length == 3){
						db.addToCube(c.get(0), comp[1], Integer.parseInt(comp[2]));
					}else{
						continue;
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
