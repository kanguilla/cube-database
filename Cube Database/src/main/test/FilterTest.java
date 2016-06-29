package main.test;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import main.core.Filter;

public class FilterTest {
	public static void main(String args[]){
		String s = "t:creature cmc=9";
		System.out.println("   " + s + "\n-> " + Filter.toSQL(s));
	}
}
