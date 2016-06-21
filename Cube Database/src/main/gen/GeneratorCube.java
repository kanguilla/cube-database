package main.gen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GeneratorCube {

	public static String name = "cube";
	
	public static void main(String[] args) {
		System.out.println("Creating mtg database...\n");
		try {
			double time = System.currentTimeMillis();
			Class.forName("org.sqlite.JDBC");
			Connection database = DriverManager.getConnection("jdbc:sqlite:"+name+".db");
			Statement stat = database.createStatement();
			System.out.println("Connected to database ("+ (System.currentTimeMillis() - time)/1000 +")");
			
			time = System.currentTimeMillis();
			stat.executeUpdate("drop table if exists cards;");
			stat.executeUpdate("create table if not exists cards("
					+ "name varchar(50) primary key not NULL,"
					+ "setCode varchar(10),"
					+ "quantity int);");
			System.out.println("Created card table ("+ (System.currentTimeMillis() - time)/1000 +")");
			
			time = System.currentTimeMillis();
			stat.executeUpdate("drop table if exists archetypes;");
			stat.executeUpdate("create table if not exists archetypes("
					+ "name varchar(50) primary key not NULL);");
			System.out.println("Created archetype table ("+ (System.currentTimeMillis() - time)/1000 +")");
			
			time = System.currentTimeMillis();
			stat.executeUpdate("drop table if exists archMembers;");
			stat.executeUpdate("create table if not exists archmembers("
					+ "archName varchar(50) references archetypes(name), "
					+ "cardName varchar (50)references cards(name));");
			System.out.println("Created archMembers table ("+ (System.currentTimeMillis() - time)/1000 +")");
			
			//Sample Data
			time = System.currentTimeMillis();
			stat.execute("insert into cards (name, setCode, quantity) values ('Battlewise Hoplite', 'THS', 1);");
			System.out.println("Added sample data("+ (System.currentTimeMillis() - time)/1000 +")");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
