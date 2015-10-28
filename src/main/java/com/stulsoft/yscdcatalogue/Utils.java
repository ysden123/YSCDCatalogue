/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.stulsoft.yscdcatalogue.data.SoftItem;
import com.stulsoft.yscdcatalogue.data.SoftItemNode;
import com.stulsoft.yscdcatalogue.data.SoftItemTree;

import javafx.scene.control.TreeItem;

/**
 * @author Yuriy Stul
 *
 */
public class Utils {

	/**
	 * Builds an application title.
	 * 
	 * @param file
	 *            current open file
	 * @return the application title.
	 */
	public static String getTitle(final File file) {
		String title = String.format("%s %s%s", CommonConstants.APPLICATION_NAME, CommonConstants.APPLICATION_VERSION, getFileName(file));
		return title;
	}

	private static String getFileName(final File file) {
		String fileName = "";
		if (file != null) {
			String fileNameWithExtension = file.getName();
			int i = fileNameWithExtension.lastIndexOf(".");
			if (i > 0) {
				fileName = ", " + fileNameWithExtension.substring(0, i);
			}
		}
		return fileName;
	}

	/**
	 * Builds a SoftItemTree from TreeItem
	 * 
	 * @param softTreeItem
	 *            the TreeItem
	 * @return the SoftItemTree from TreeItem
	 */
	public static SoftItemTree buildSoftTree(final TreeItem<SoftItem> softTreeItem) {
		SoftItem rootSoftItem = softTreeItem.getValue();
		SoftItemTree softTree = new SoftItemTree(rootSoftItem);
		softTree.getRoot().setTreeItem(softTreeItem);

		for (final TreeItem<SoftItem> child : softTreeItem.getChildren()) {
			addChild(softTree.getRoot(), child);
		}

		return softTree;
	}

	private static void addChild(final SoftItemNode softItemNode, final TreeItem<SoftItem> child) {
		SoftItemNode childSoftItemNode = new SoftItemNode(child.getValue(), null);
		childSoftItemNode.setTreeItem(child);
		softItemNode.addChild(childSoftItemNode);
		for (TreeItem<SoftItem> subChild : child.getChildren()) {
			addChild(childSoftItemNode, subChild);
		}
	}

	/**
	 * Returns a storage name (volume name).
	 * 
	 * @param file
	 *            the file.
	 * @return the storage name (volume name).
	 * @throws IOException
	 *             I/O exception.
	 */
	public static String getStorageName(final File file) throws IOException {
		Path path = Paths.get(file.getPath());
		FileStore fs = Files.getFileStore(path);
		String storageName = fs.name();
		return storageName;
	}
}
