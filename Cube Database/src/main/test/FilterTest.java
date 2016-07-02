package main.test;

import main.core.Filter2;

public class FilterTest {
	public static void main(String[] args){
		Filter2 f = new Filter2("c!gbm is:new (r:common or r:uncommon) t:creature (not is:splitmana)");
		for (int i = 0; i < f.nodes.size(); i++){
			System.out.println(i + ": " + f.nodes.get(i));
		}
	}
}
