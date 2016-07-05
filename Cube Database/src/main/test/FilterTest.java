package main.test;

import main.core.Filter;

public class FilterTest {
	public static void main(String[] args){
		Filter f = new Filter("c:gbm or test is:new and (r:spec r:uncommon) t:creature (not is:splitmana)");
		System.out.println("c:gbm or test is:new and (r:spec r:uncommon) t:creature (not is:splitmana)");
		f.print();
		System.out.println("-------------------");
		System.out.println(f.toSQL());
	}
}
