/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.test.yscdcatalogue.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.stulsoft.yscdcatalogue.data.SoftItem;
import com.stulsoft.yscdcatalogue.data.SoftItemNode;
import com.stulsoft.yscdcatalogue.data.SoftItemType;
import com.stulsoft.yscdcatalogue.data.SoftItemTree;

/**
 * @author Yuriy Stul
 *
 */
public class SoftItemTreeTest {
	private final SoftItem testData = new SoftItem("test text", SoftItemType.CATEGORY);

	/**
	 * Test method for {@link com.stulsoft.yscdcatalogue.data.SoftItemTree#Tree(java.lang.Object)}.
	 */
	@Test
	public void testTree() {
		SoftItemTree t = new SoftItemTree(testData);
		assertNotNull(t);
	}

	/**
	 * Test method for {@link com.stulsoft.yscdcatalogue.data.SoftItemTree#getRoot()}.
	 */
	@Test
	public void testGetRoot() {
		SoftItemTree t = new SoftItemTree(testData);
		assertNotNull(t);
		SoftItemNode expectedRoot = new SoftItemNode(testData, null);
		SoftItemNode actualRoot = t.getRoot();
		assertEquals(expectedRoot, actualRoot);
	}

	/**
	 * Test method for {@link com.stulsoft.yscdcatalogue.data.SoftItemTree#toString()}.
	 */
	@Test
	public void testToString() {
		SoftItemTree t = new SoftItemTree(testData);
		// System.out.println(t.toString());
		assertEquals("SoftTree [root=SoftItemNode [data=test text, parent=null, children=[]]", t.toString());
	}

	/**
	 * Test method for {@link com.stulsoft.yscdcatalogue.data.SoftItemTree#findNode(Object)}.
	 */
	@Test
	public void testFindNode() {
		SoftItemTree t = new SoftItemTree(testData);
		SoftItemNode foundNode = t.findNode(testData);
		assertNotNull(foundNode);
		assertEquals(testData, foundNode.getData());

		SoftItem testData1 = new SoftItem(testData.getName() + 1, SoftItemType.CATEGORY);
		SoftItemNode n1 = new SoftItemNode(testData1, null);
		t.getRoot().addChild(n1);
		foundNode = t.findNode(testData1);
		assertNotNull(foundNode);
		assertEquals(testData1, foundNode.getData());

		SoftItem testData2 = new SoftItem(testData.getName() + 2, SoftItemType.CATEGORY);
		SoftItemNode n2 = new SoftItemNode(testData2, null);
		n1.addChild(n2);
		foundNode = t.findNode(testData2);
		assertNotNull(foundNode);
		assertEquals(testData2, foundNode.getData());

		SoftItem testData3 = new SoftItem(testData.getName() + 3, SoftItemType.CATEGORY);
		foundNode = t.findNode(testData3);
		assertNull(foundNode);
	}
}
