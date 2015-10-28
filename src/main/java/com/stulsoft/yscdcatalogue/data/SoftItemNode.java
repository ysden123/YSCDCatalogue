/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.control.TreeItem;

/**
 * Holds a tree node data about SoftItem.
 * 
 * @author Yuriy Stul
 *
 */
public class SoftItemNode {
	public static final String FIELD_NAME_DATA = "data";
	public static final String FIELD_NAME_CHILDREN = "children";

	private SoftItem data;
	private SoftItemNode parent;
	private List<SoftItemNode> children = new ArrayList<SoftItemNode>();
	private TreeItem<SoftItem> treeItem;

	/**
	 * @return the data
	 */
	public SoftItem getData() {
		return data;
	}

	/**
	 * @return the parent
	 */
	@JsonIgnore
	public SoftItemNode getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            specifies the parent to set.
	 */
	public void setParent(final SoftItemNode parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	public List<SoftItemNode> getChildren() {
		return children;
	}

	/**
	 * @return the treeItem
	 */
	@JsonIgnore
	public TreeItem<SoftItem> getTreeItem() {
		return treeItem;
	}

	/**
	 * @param treeItem the treeItem to set
	 */
	public void setTreeItem(TreeItem<SoftItem> treeItem) {
		this.treeItem = treeItem;
	}

	protected SoftItemNode() {
	}

	/**
	 * Initializes a new instance of the class SoftItemNode
	 * 
	 * @param nodeData
	 *            specifies the node data object.
	 * @param parent
	 *            specifies the parent, optional, may be null.
	 */
	public SoftItemNode(final SoftItem nodeData, final SoftItemNode parent) {
		data = nodeData;
		this.parent = parent;
	}

	/**
	 * Initializes a new instance of the class SoftItemNode
	 * 
	 * @param nodeData
	 *            specifies the node data object.
	 * @param parent
	 *            specifies the parent, optional, may be null.
	 * @param children
	 *            collection of children
	 */
	public SoftItemNode(final SoftItem nodeData, final SoftItemNode parent, final Collection<SoftItemNode> children) {
		data = nodeData;
		this.parent = parent;
		this.children.addAll(children);
	}

	/**
	 * Adds a child node.
	 * 
	 * @param child
	 *            specifies the child node.
	 */
	public void addChild(final SoftItemNode child) {
		if (child == null) {
			throw new IllegalArgumentException("child is null");
		}
		if (child.equals(this)) {
			throw new IllegalArgumentException(String.format("child \"%s\" is same as parent \"%s\".", child, this));
		}
		child.setParent(this);
		children.add(child);
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
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		SoftItemNode other = (SoftItemNode) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
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
		StringBuilder buffer = new StringBuilder("SoftItemNode [data=" + data + ", parent=" + (parent == null ? "null" : "not null") + ", children=[");
		List<String> childrenTexts = new ArrayList<String>();
		children.forEach(c -> childrenTexts.add(c.toString()));
		buffer.append(String.join(", ", childrenTexts));
		buffer.append("]");
		return buffer.toString();
	}
}
