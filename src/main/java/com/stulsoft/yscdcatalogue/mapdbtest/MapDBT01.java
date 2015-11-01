/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.mapdbtest;

import java.io.File;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentNavigableMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 * 1st tests for MapBB
 * 
 * @author Yuriy Stul
 *
 */
public class MapDBT01 {

	/**
	 * @param args app arguments
	 */
	public static void main(String[] args) {
		test01();
		test02();
		test03();
		test04();
		test05();
		test06();
		test07();
	}

	private static void test01() {
		System.out.println("==>test01");
		DB db = DBMaker.newMemoryDB().make();

		ConcurrentNavigableMap<String, String> treeMap = db.getTreeMap("map");
		treeMap.put("something", "here");

		db.commit();
		db.close();
		System.out.println("<==test01");
	}

	private static void test02() {
		System.out.println("==>test02");
		DB db = DBMaker.newFileDB(new File("d:/work/mapDB01.db")).make();

		ConcurrentNavigableMap<String, String> treeMap = db.getTreeMap("map");
		treeMap.put("something", "here");

		db.commit();
		db.close();
		System.out.println("<==test02");
	}

	private static void test03() {
		System.out.println("==>test03");
		DB db = DBMaker.newFileDB(new File("d:/work/mapDB01.db")).make();

		ConcurrentNavigableMap<String, String> treeMap = db.getTreeMap("map");
		System.out.println(treeMap.get("something"));

		db.commit();
		db.close();
		System.out.println("<==test03");
	}

	private static void test04() {
		System.out.println("==>test04");
		DB db = DBMaker.newFileDB(new File("d:/work/mapDB02.db")).make();
		ConcurrentNavigableMap<String, String> treeMap = db.getTreeMap("treeMap");
		String result = treeMap.put("key1", "value1");
		System.out.println("result: " + result);
		result = treeMap.put("key1", "value1");
		System.out.println("result: " + result);
		result = treeMap.put("key1", "value1");
		System.out.println("result: " + result);
		
		treeMap.entrySet().forEach(e->System.out.format("key: %s, value: %s\n", e.getKey(), e.getValue()));
		System.out.println("<==test04");
	}
	
	private static void test05() {
		System.out.println("==>test05");
		DB db = DBMaker.newFileDB(new File("d:/work/mapDB02.db")).make();
		ConcurrentNavigableMap<String, String> treeMap = db.getTreeMap("treeMap");
		String result = treeMap.put("key1", "value1");
		System.out.println("result: " + result);
		result = treeMap.put("key1", "value1");
		System.out.println("result: " + result);
		result = treeMap.put("key1", "value1");
		System.out.println("result: " + result);
		
		treeMap.entrySet().forEach(e->System.out.format("key: %s, value: %s\n", e.getKey(), e.getValue()));
		System.out.println("<==test05");
	}
	
	private static void test06() {
		System.out.println("==>test06");
		DB db = DBMaker.newFileDB(new File("d:/work/mapDB03.db")).make();
		NavigableSet<String> treeSet = db.getTreeSet("treeSet");
		boolean result = treeSet.add("value1");
		System.out.println("result: " + result);
		db.commit();
		db.close();
		
		db = DBMaker.newFileDB(new File("d:/work/mapDB03.db")).make();
		treeSet = db.getTreeSet("treeSet");
		System.out.println("treeSet.isEmpty(): " + treeSet.isEmpty());
		db.close();
		System.out.println("<==test06");
	}
	
	private static void test07() {
		System.out.println("==>test07");
		DB db = DBMaker.newFileDB(new File("d:/work/mapDB07.db")).closeOnJvmShutdown().make();
		NavigableSet<String> treeSet = db.getTreeSet("treeSet");
		System.out.println("(0)treeSet.isEmpty(): " + treeSet.isEmpty());
		treeSet.clear();
		boolean result = treeSet.add("value1");
		System.out.println("result: " + result);
		System.out.println("(1)treeSet.isEmpty(): " + treeSet.isEmpty());
		db.commit();
//		db.close();
//		
//		db = DBMaker.newFileDB(new File("d:/work/mapDB07.db")).make();
//		treeSet = db.getTreeSet("treeSet");
//		System.out.println("treeSet.isEmpty(): " + treeSet.isEmpty());
//		db.close();
		System.out.println("<==test07");
	}
}
