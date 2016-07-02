package main.core;

import java.util.ArrayList;
import java.util.Arrays;

public class Filter2 {
	
	
	public ArrayList<String> nodes = new ArrayList<String>();
	
	String ordering = "";
	String selection = "select * from cards where name like \"%\" ";
	
	public Filter2 (String proto){	
		parse(proto);
	}

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> types = new ArrayList<String>();

	public void parse2(String e){
		for (String s : e.split("\\(")){
			System.out.println(s);
		}
	}
	
	public void parse(String e){
		System.out.println("Parsing: " + e);
		// t:creature (r:rare or r:mythic)
		String s = e;
		ArrayList<String> elements = new ArrayList<String>();
		String sub = "";
		
		while (s.length() > 0){
			//System.out.println("Looking at the first character: " + s.charAt(0));
			char c = s.charAt(0);
			if (c == '('){
				elements.add(sub);
				parse(s.substring(1));
				break;
			}else if (c == ')'){
				elements.add(sub);
				parse(s.substring(1));
				break;
			}else{
				sub += c;
			}
			s = s.substring(1);
		}
		
		for (String k : elements){
			nodes.add(k);
			//nodes.addAll(Arrays.asList(k.split(" ")));
		}
	}
	
	
	public String toSQL(String e){
		int leftBraces = 0;
		while (e.charAt(leftBraces) == '('){
			leftBraces++;
			e = e.substring(1);
		}
		int rightBraces = 0;
		int j = e.length() - 1;
		while (e.charAt(j) == ')'){
			j--;
			rightBraces++;
			e = e.substring(0, e.length()-1);
		}
		
		//determine the query type:
		if(e.equalsIgnoreCase("and") | e.equalsIgnoreCase("or")){
			return e + " ";
		}
		
		String[] a = e.split(":");
		if(a.length > 1){
			if (a[0].equalsIgnoreCase("t")){
				e = "(UPPER(types) like UPPER('%" + a[1] + "%') or UPPER(subtypes) like UPPER('%" + a[1] + "%')) ";
			}
			if (a[0].equalsIgnoreCase("o")){
				e = "UPPER(text) like UPPER('%" + e + "%') ";
			}
			if (a[0].equalsIgnoreCase("r")){
				e = "UPPER(rarity) like UPPER('%" + e + "%') ";
			}
		}else{
			e = "UPPER(name) like UPPER('%" + e + "%') ";
		}
		
		String out = "";
		for (int c = 0; c < leftBraces; c++){
			out += "(";
		}
		out += e;
		for (int c = 0; c < rightBraces; c++){
			out += ")";
		}
		
		return out;
	}
	
	public String toSQL(){
		String s = selection;
		
		
		for (int i = 0; i < names.size(); i++){
			s += "and name like \"%" + names.get(i) + "%\" ";
		}
		
		for (int i = 0; i < types.size(); i++){
			s += "and (types like \"%" + types.get(i) + "%\" or subtypes like \"%" + types.get(i) + "%\") ";
		}
		s += " " + ordering;
		s += ";";
		System.out.println(s);
		return s;
	}


	public void setOrdering(String string) {
		ordering = string;
	}

	public void setSelection(String string) {
		selection = string;
	}
	
	public class Node{
		String e;
		Node left;
		Node right;
		public Node(String e, Node left, Node right){
			this.e = e;
			this.left = left;
			this.right = right;
		}
	}
	
}
