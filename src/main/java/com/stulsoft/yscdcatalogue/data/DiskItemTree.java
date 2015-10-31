/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.data;

import java.util.List;
import java.util.Optional;

/**
 * Holds a disk tree data.
 * 
 * @author Yuriy Stul
 *
 */
public class DiskItemTree {
	private String id;
	private DiskItemNode root;

	/**
	 * Initializes a new instance of the DiskTree class.
	 */
	protected DiskItemTree() {
	}

	/**
	 * Initializes a new instance of the DiskTree class.
	 * 
	 * @param rootData
	 *            the root item.
	 */
	public DiskItemTree(DiskItem rootData) {
		root = new DiskItemNode(rootData, null);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the root item
	 */
	public DiskItemNode getRoot() {
		return root;
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public void setRoot(final DiskItemNode root) {
		if (root == null)
			throw new IllegalArgumentException("root is null.");
		this.root = root;
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
		result = prime * result + ((root == null) ? 0 : root.hashCode());
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
		DiskItemTree other = (DiskItemTree) obj;
		if (root == null) {
			if (other.root != null)
				return false;
		} else if (!root.equals(other.root))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DiskItemTree [id=" + id + ", root=" + root + "]";
	}

	/**
	 * Finds a node for specified data.
	 * 
	 * @param data
	 *            specifies the data.
	 * @return the node for specified data, if the node was found; null - otherwise.
	 */
	public DiskItemNode findNode(DiskItem data) {
		if (data == null) {
			throw new IllegalArgumentException("data is null.");
		}
		if (root.getData().equals(data)) {
			return root;
		}
		List<DiskItemNode> children = root.getChildren();
		Optional<DiskItemNode> optionalNode = children.stream().filter(n -> n.getData().equals(data)).findFirst();
		DiskItemNode foundNode = null;

		if (optionalNode.isPresent()) {
			foundNode = optionalNode.get();
		} else {
			foundNode = findNode(root, data);
		}

		return foundNode;
	}

	private DiskItemNode findNode(DiskItemNode node, DiskItem data) {
		List<DiskItemNode> children = node.getChildren();
		Optional<DiskItemNode> optionalNode = children.stream().filter(n -> n.getData().equals(data)).findFirst();
		DiskItemNode foundNode = null;
		if (optionalNode.isPresent()) {
			foundNode = optionalNode.get();
		} else {
			for (DiskItemNode child : children) {
				foundNode = findNode(child, data);
				if (foundNode != null) {
					break;
				}
			}
		}

		return foundNode;
	}
}
