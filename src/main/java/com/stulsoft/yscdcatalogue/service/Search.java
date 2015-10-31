/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.service;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.stulsoft.yscdcatalogue.data.DiskItemNode;
import com.stulsoft.yscdcatalogue.data.DiskItemTree;
import com.stulsoft.yscdcatalogue.data.SearchResult;
import com.stulsoft.yscdcatalogue.data.SoftItem;
import com.stulsoft.yscdcatalogue.data.SoftItemNode;
import com.stulsoft.yscdcatalogue.data.SoftItemTree;
import com.stulsoft.yscdcatalogue.data.SoftItemType;
import com.stulsoft.yscdcatalogue.persistence.DBManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * Finds items.
 * 
 * @author Yuriy Stul
 *
 */
public class Search {
	private Logger logger = LogManager.getLogger(Search.class);
	private final SoftItemTree softItemTree;
	private final String searchText;

	/**
	 * @param softItemTree
	 *            the three with Soft Items
	 * @param searchText
	 *            the search text
	 */
	public Search(final SoftItemTree softItemTree, final String searchText) {
		super();
		if (softItemTree == null) {
			throw new IllegalArgumentException("softItemTree is null.");
		}
		if (StringUtils.isEmpty(searchText))
			throw new IllegalArgumentException("searchText is null or empty.");
		this.softItemTree = softItemTree;
		this.searchText = searchText;
	}

	/**
	 * Finds items.
	 * 
	 * @return the collection of the found items
	 */
	public ObservableList<SearchResult> find() {
		logger.debug("Starting search for {}", searchText);
		ObservableList<SearchResult> results = FXCollections.observableArrayList();
		find(results, softItemTree.getRoot());
		logger.debug("{} entries were found.", results.size());
		return results;
	}

	private void find(final Collection<SearchResult> results, final SoftItemNode node) {
		if (node.getData().getType() == SoftItemType.DISK) {
			logger.debug("Looking inside {}", node.getData().getName());
			DiskItemTree diskItemTree;
			try {
				diskItemTree = DBManager.getInstance().getDiskItemTree(node.getData().getDiskId());
			}
			catch (Exception e) {
				String msg = String.format("Failed getting disk for %s, Error: %s", node.getData().getName(), e.getMessage());
				logger.error(msg, e);
				return;
			}
			
			DiskItemNode rootNode = diskItemTree.getRoot();
			
			//@formatter:off
			findInDisk(results, 
					rootNode, 
					node.getParent().getData().getName(), 
					rootNode.getData().getStorageName(), 
					node.getTreeItem());
			//@formatter:on
		}
		for (SoftItemNode child : node.getChildren()) {
			find(results, child);
		}
	}

	private void findInDisk(final Collection<SearchResult> results, final DiskItemNode diskItemNode, final String categoryName, final String diskName, final TreeItem<SoftItem> treeItem) {
		//@formatter:off
		if (StringUtils.containsIgnoreCase(diskItemNode.getData().getFullPath(), searchText)
				|| StringUtils.containsIgnoreCase(diskItemNode.getData().getComment(), searchText)
				|| StringUtils.containsIgnoreCase(diskItemNode.getData().getStorageName(), searchText)) {
			SearchResult result = new SearchResult(categoryName, diskName, diskItemNode.getData().getFullPath(), treeItem);
			results.add(result);
		}
		//@formatter:on
		for (DiskItemNode child : diskItemNode.getChildren()) {
			findInDisk(results, child, categoryName, diskName, treeItem);
		}
	}
}
