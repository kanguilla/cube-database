package main.core;

import java.util.ArrayList;
import java.util.Arrays;

public class Filter2 {
	
	Node root;
	
	
	String ordering = "";
	String selection = "select * from cards where name like \"%\" ";
	
	public Filter2 (String proto){
		String[] args = proto.split(" ");
		
		for (String arg : args){
			System.out.print(toSQL(arg));
		}
	}

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> types = new ArrayList<String>();

	public String toSQL(String e){
		int i = 0;
		while (e.charAt(i) == '('){
			i++;
			e = e.substring(1);
		}
		int k = e.length() - 1;
		int j = e.length();
		while (e.charAt(k) == ')'){
			k--;
			e = e.substring(0, e.length()-1);
		}
		k = j - 1 - k;
		
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
		for (int c = 0; c < i; c++){
			out += "(";
		}
		out += e;
		for (int c = 0; c < k; c++){
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
