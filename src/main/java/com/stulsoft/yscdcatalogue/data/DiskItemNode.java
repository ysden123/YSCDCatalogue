/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Holds a tree node data about DiskItem.
 * 
 * @author Yuriy Stul
 *
 */
public class DiskItemNode {
	public static final String FIELD_NAME_DATA = "data";
	public static final String FIELD_NAME_CHILDREN = "children";

	DiskItem data;
	private DiskItemNode parent;
	List<DiskItemNode> children = new ArrayList<DiskItemNode>();

	/**
	 * @return the data
	 */
	public DiskItem getData() {
		return data;
	}

	/**
	 * @return the parent
	 */
	@JsonIgnore
	public DiskItemNode getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            specifies the parent to set.
	 */
	public void setParent(DiskItemNode parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	public List<DiskItemNode> getChildren() {
		return children;
	}

	protected DiskItemNode() {
	}

	/**
	 * Initializes a new instance of the class DiskItemNode
	 * 
	 * @param nodeData
	 *            specifies the node data object.
	 * @param parent
	 *            specifies the parent, optional, may be null.
	 */
	public DiskItemNode(final DiskItem nodeData, final DiskItemNode parent) {
		if (nodeData == null)
			throw new IllegalArgumentException("nodeData is null.");
		data = nodeData;
		this.parent = parent;
	}

	/**
	 * Initializes a new instance of the class DiskItemNode
	 * 
	 * @param nodeData specifies
	 *            the node data object.
	 * @param parent
	 *            specifies the parent, optional, may be null.
	 * @param children
	 *            collections of children
	 */
	public DiskItemNode(final DiskItem nodeData, final DiskItemNode parent, final Collection<DiskItemNode> children) {
		if (nodeData == null)
			throw new IllegalArgumentException("nodeData is null.");
		if (children == null)
			throw new IllegalArgumentException("children is null.");
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
	public void addChild(DiskItemNode child) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiskItemNode other = (DiskItemNode) obj;
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
		StringBuilder buffer = new StringBuilder("DiskItemNode [data=" + data + ", parent=" + (parent == null ? "null" : "not null") + ", children=[");
		List<String> childrenTexts = new ArrayList<String>();
		children.forEach(c -> childrenTexts.add(c.toString()));
		buffer.append(String.join(", ", childrenTexts));
		buffer.append("]");
		return buffer.toString();
	}
}
