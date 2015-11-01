/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.persistence;

import com.stulsoft.yscdcatalogue.Utils;
import com.stulsoft.yscdcatalogue.data.SoftItem;
import com.stulsoft.yscdcatalogue.data.SoftItemTree;

import javafx.scene.control.TreeItem;

/**
 * @author Yuriy Stul
 *
 */
public class SoftItemTreePersistence {

	/**
	 * Loads a root tree item.
	 * 
	 * @return the root tree item.
	 * @throws Exception
	 *             I/O exception
	 */
	public static SoftItemTree load() throws Exception {
		SoftItemTree softTree = DBManager.getInstance().getSoftItemTree();
		return softTree;
	}

	/**
	 * Saves the soft tree into file as Json text.
	 * 
	 * @param softTreeItem
	 *            the soft tree item
	 * @throws Exception
	 *             I/O exception
	 */
	public static void save(final TreeItem<SoftItem> softTreeItem) throws Exception {
		if (softTreeItem == null)
			throw new IllegalArgumentException("softTreeItem is null.");
		SoftItemTree softTree = Utils.buildSoftTree(softTreeItem);

		DBManager.getInstance().saveSoftItemTree(softTree);
	}

}
