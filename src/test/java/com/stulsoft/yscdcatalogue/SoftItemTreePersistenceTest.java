/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stulsoft.yscdcatalogue.data.DiskItem;
import com.stulsoft.yscdcatalogue.data.DiskItemNode;
import com.stulsoft.yscdcatalogue.data.DiskItemTree;
import com.stulsoft.yscdcatalogue.data.DiskItemType;
import com.stulsoft.yscdcatalogue.data.SoftItem;
import com.stulsoft.yscdcatalogue.data.SoftItemNode;
import com.stulsoft.yscdcatalogue.data.SoftItemTree;
import com.stulsoft.yscdcatalogue.data.SoftItemType;
import com.stulsoft.yscdcatalogue.persistence.SoftItemTreePersistence;

import javafx.scene.control.TreeItem;

/**
 * @author Yuriy Stul
 *
 */
public class SoftItemTreePersistenceTest {

	/**
	 * Test method for {@link com.stulsoft.yscdcatalogue.persistence.SoftItemTreePersistence#load(java.lang.String)} and
	 * {@link com.stulsoft.yscdcatalogue.persistence.SoftItemTreePersistence#save(javafx.scene.control.TreeView, java.lang.String)}.
	 */
	@Test
	public void testSaveLoadTemp() {
		TreeItem<SoftItem> t = buildSoftTreeItem();
		try {
			final String fileName = "d:\\transmit\\test.json";
			SoftItemTreePersistence.save(t, fileName);

			SoftItemTreePersistence.load(fileName);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.stulsoft.yscdcatalogue.persistence.SoftItemTreePersistence#load(java.lang.String)} and
	 * {@link com.stulsoft.yscdcatalogue.persistence.SoftItemTreePersistence#save(javafx.scene.control.TreeView, java.lang.String)}.
	 */
	@Test
	public void testSaveLoad() {
		SoftItemTree softTree = buildSoftTree();
		ObjectMapper mapper = new ObjectMapper();
		try {
			String out = mapper.writeValueAsString(softTree);
			System.out.println("out:\n" + out);

			SoftItemTree softTreeLoaded = mapper.readValue(out, SoftItemTree.class);

			System.out.println("original: " + softTree);
			System.out.println("loaded  : " + softTree);
			System.out.format("\nOriginal and loaded are equal: %s\n", softTree.toString().equals(softTreeLoaded.toString()));

		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	private SoftItemTree buildSoftTree() {
		SoftItem rootSoftItem = new SoftItem("test root soft item", SoftItemType.CATEGORY);
		SoftItemTree softTree = new SoftItemTree(rootSoftItem);

		for (int i1 = 1; i1 <= 2; ++i1) {
			SoftItem softItem = new SoftItem("soft item 1 " + i1, SoftItemType.CATEGORY);
			SoftItemNode node1 = new SoftItemNode(softItem, null);
			softTree.getRoot().addChild(node1);

			DiskItemTree diskTree = new DiskItemTree(new DiskItem("disk: " + i1, DiskItemType.DISK, null, null));
			for (int j1 = 10; j1 <= 12; ++j1) {
				DiskItem diskItem = new DiskItem("fullPath 1 " + j1, DiskItemType.DIRECTORY, null, null);
				DiskItemNode diskItemNode = new DiskItemNode(diskItem, null);
				diskTree.getRoot().addChild(diskItemNode);
			}
			softItem.setDiskId(diskTree.getId());
		}

		return softTree;
	}

	private TreeItem<SoftItem> buildSoftTreeItem() {
		SoftItemTree softTree = buildSoftTree();
		TreeItem<SoftItem> treeItem = new TreeItem<SoftItem>(softTree.getRoot().getData());

		for (SoftItemNode child : softTree.getRoot().getChildren()) {
			addChild(treeItem, child);
		}

		return treeItem;
	}

	private void addChild(final TreeItem<SoftItem> treeItem, final SoftItemNode child) {
		TreeItem<SoftItem> subTreeItem = new TreeItem<SoftItem>(child.getData());
		treeItem.getChildren().add(subTreeItem);
		for (SoftItemNode softItemNode : child.getChildren()) {
			addChild(subTreeItem, softItemNode);
		}
	}
}
