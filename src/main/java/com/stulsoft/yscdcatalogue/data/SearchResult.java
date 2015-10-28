/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.data;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeItem;

/**
 * Holds result of search
 * 
 * @author Yuriy Stul
 *
 */
public class SearchResult {
	private SimpleStringProperty categoryName;
	private SimpleStringProperty diskName;
	private SimpleStringProperty fileName;
	private TreeItem<SoftItem> treeItem;

	/**
	 * @return the category name
	 */
	public String getCategoryName() {
		return categoryName.getValue();
	}

	/**
	 * @param categoryName
	 *            the category name to set
	 */
	public void setCategoryName(final String categoryName) {
		if (StringUtils.isEmpty(categoryName)) {
			throw new IllegalArgumentException("categoryName is null or empty.");
		}
		this.categoryName.setValue(categoryName);
	}

	/**
	 * @return the disk name
	 */
	public String getDiskName() {
		return diskName.getValue();
	}

	/**
	 * @param diskName
	 *            the disk to set
	 */
	public void setDiskName(final String diskName) {
		if (StringUtils.isEmpty(diskName)) {
			throw new IllegalArgumentException("diskName is null or empty.");
		}
		this.diskName.set(diskName);
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName.getValue();
	}

	/**
	 * @param fileName
	 *            the file name to set
	 */
	public void setFileName(final String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			throw new IllegalArgumentException("fileName is null or null.");
		}

		this.fileName.setValue(fileName);
	}

	/**
	 * @return the treeItem
	 */
	public TreeItem<SoftItem> getTreeItem() {
		return treeItem;
	}

	/**
	 * @param treeItem
	 *            the treeItem to set
	 */
	public void setTreeItem(TreeItem<SoftItem> treeItem) {
		this.treeItem = treeItem;
	}

	/**
	 * @param categoryName
	 *            the category
	 * @param diskName
	 *            the disk
	 * @param fileName
	 *            the folder/file
	 * @param treeItem
	 *            the tree view item
	 */
	public SearchResult(final String categoryName, final String diskName, final String fileName, final TreeItem<SoftItem> treeItem) {
		super();
		this.categoryName = new SimpleStringProperty(categoryName);
		this.diskName = new SimpleStringProperty(diskName);
		this.fileName = new SimpleStringProperty(fileName);
		this.treeItem = treeItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryName == null) ? 0 : categoryName.hashCode());
		result = prime * result + ((diskName == null) ? 0 : diskName.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchResult other = (SearchResult) obj;
		if (categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!categoryName.equals(other.categoryName))
			return false;
		if (diskName == null) {
			if (other.diskName != null)
				return false;
		} else if (!diskName.equals(other.diskName))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SearchResult [category=" + categoryName + ", disk=" + diskName + ", file=" + fileName + "]";
	}
}
