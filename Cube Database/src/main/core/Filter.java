package main.core;

import java.util.ArrayList;
import java.util.Arrays;

public class Filter {
	
	
	private ArrayList<Object> elements = new ArrayList<Object>();

	String selection = "select * from cards where name in (select name from cards join contents on name=cardName where name like '%' ";
	String ordering = " ";
	
	public Filter (String proto){
		//System.out.println("CNS FILTER");
		parse(proto);
	}
	
	public void print(){
		for (int i = 0; i < elements.size(); i++){
			System.out.println(i + ": " + elements.get(i).getClass().getName() + "   " + elements.get(i));
		}
	}
	
	public void parse(String e){
		if (e.trim().length() == 0){
			return;
		}
		//System.out.println("Parsing : " + e);
		ArrayList<Object> out = new ArrayList<Object>();
		String sub = "";
		int start = 0;
		boolean quote = false;
		boolean para = false;
		for (char c : e.toCharArray()){
			//System.out.println("Looking at the first character: " + s.charAt(0));
			if (c == ' ' && start == 0 && !quote){
				if(!para){
					out.add(sub);
				}else{
					out.add(new Filter(sub.substring(1, sub.length()-1)));
					para = false;
				}
				sub = "";
			}else if (c == '"'){
				quote = !quote;
			}else if (c == '('){
				start++;
				if (start == 1){
					out.add(sub);
					sub = "";
				}
			}else if (c == ')'){
				start--;
				if (start == 0){
					para = true;
				}
			}
			sub += c;
		}
		if(!para){
			out.add(sub);
		}else{
			out.add(new Filter(sub.substring(1, sub.length()-1)));
			para = false;
		}
		ArrayList<Object> out2 = new ArrayList<Object>();
		for (Object s : out){
			if (s instanceof String){
				if (((String) s).trim().length() > 0){
					out2.add(((String) s).trim());
				}
			}else{
				out2.add(s);
			}
		}
		out2 = conjugate(out2);
		elements = out2;
	}
	
	
	
	public ArrayList<Object> conjugate(ArrayList<Object> a){
		if (a.size() <= 1){
			return a;
		}
		boolean conj = true;
		for (int i = 0; i < a.size(); i++){
			if (a.get(i) instanceof String){
				String s = (String) a.get(i);
				if (s.equalsIgnoreCase("and") || s.equalsIgnoreCase("or")){
					conj = true;
				}else{
					
					if (conj){
						conj = false;
					}else{
						conj = true;
						a.add(i, "and");
					}
				}
			}
		}
		return a;
	}
	
	public String toSQL(){
		String sql = selection;
		if (!elements.isEmpty()){
			sql += " and ";
		}
		for (Object o : elements){
			if (o instanceof String){	
				String s = ((String) o);
				
				if(s.equalsIgnoreCase("and") || s.equalsIgnoreCase("or")){
					sql += " " + s;
					continue;
				}
				
				if (s.startsWith("t:")){
					String t = s.split(":")[1];
					sql += " (UPPER(types) like UPPER('%" + t + "%') or UPPER(subtypes) like UPPER('%" + t + "%'))";
					continue;
				}
				
				if (s.startsWith("c!")){
					String t = s.split("!")[1];
					
					if(t.endsWith("m")){
						t = t.substring(0, t.length()-1);
						sql += " colors = '" + t.toUpperCase() + "'";
					}else{
						String sc = "%" + String.join("%", t.split("")) + "%";
						sql += " (";
						
						for (String c : t.split("")){
							sql += " colors = '" + c.toUpperCase()+ "' or ";
						}
						
						sql += " name like '.') ";
						
					}
					
					continue;
				}
				
				if (s.startsWith("c:")){
					String t = s.split(":")[1];
					
					if(t.endsWith("m")){
						t = t.substring(0, t.length()-1);
						String sc = "%" + String.join("%", t.split("")) + "%";
						sql += " colors like '" + sc + "'";
					}else{
						sql += " (";
						
						for (String c : t.split("")){
							sql += " colors like '%" + c.toUpperCase()+ "%' or ";
						}
						
						sql += " name like '.') ";
						
					}
					continue;
				}
						
				if (s.startsWith("r:")){
					String t = s.split(":")[1];
					sql += " UPPER(rarity)=UPPER('"+t+"') ";
					continue;
				}
				
				if (s.startsWith("o:")){
					String t = s.split(":")[1];
					t = t.replace("\"", " ").trim();
					sql += " UPPER(text) like UPPER('%" + t + "%') ";
					continue;
				}
				
				if (s.startsWith("is:")){
					String t = s.split(":")[1];
					switch(t){
					case "new":
						sql += " frame='new'";
					}
					continue;
				}
				
				if (s.startsWith("\"")){
					sql += " name like '%" + s.substring(1, s.length()-1) + "%' ";
					continue;
				}
				
				sql += " name like '%" + s + "%' ";
				
			}else if (o instanceof Filter){
				Filter f = (Filter) o;
			}
		}
		return sql + " ) " + ordering + ";";
	}

	public void setSelection(String string) {
		selection = string;
	}

	public void setOrdering(String string) {
		ordering = string;
	}
}
