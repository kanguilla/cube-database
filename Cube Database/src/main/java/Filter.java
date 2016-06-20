package main.java;

import java.util.ArrayList;
import java.util.Arrays;

public class Filter {
	public Filter (String proto){
		String[] args = proto.split(" ");
		
		for (String arg : args){
			String[] s = arg.split(":");
			if (s.length == 1){
				//This is a name
				if (!s[0].equals(" ")){
					names.add(s[0]);
				}
				continue;
			}
			if (s[0].equalsIgnoreCase("t")){
				//This is a type
				types.add(s[1]);
			}
		}
	}
	
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> types = new ArrayList<String>();
	
	public boolean check(Card c){
		
		for (String name : names){
			if (!c.name.contains(name)){
				return false;
			}
		}
		
		
		for (String type : c.types){
			if (!Arrays.asList(types).contains(type)){
				return false;
			}
		}
		
		
		return true;
	}

	
	public String toSQL(){
		String s = "select * from cards where name like \"%\" ";
		
		
		for (int i = 0; i < names.size(); i++){
			s += "and name like \"%" + names.get(i) + "%\" ";
		}
		
		for (int i = 0; i < types.size(); i++){
			s += "and (types like \"%" + types.get(i) + "%\" or subtypes like \"%" + types.get(i) + "%\") ";
		}
		
		s += ";";
		System.out.println("Filter: " + s);
		return s;
	}
	
}
