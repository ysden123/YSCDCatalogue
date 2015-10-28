/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.data;

import java.util.List;
import java.util.Optional;

/**
 * Holds a soft tree data.
 * 
 * @author Yuriy Stul
 */
public class SoftItemTree {
	private SoftItemNode root;
	
	/**
	 * Initializes a new instance of the SoftTree class.
	 */
	protected SoftItemTree() {
	}

	/**
	 * Initializes a new instance of the SoftTree class.
	 * 
	 * @param rootData
	 *            the root item.
	 */
	public SoftItemTree(SoftItem rootData) {
		root = new SoftItemNode(rootData, null);
	}

	/**
	 * @return the root item
	 */
	public SoftItemNode getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(SoftItemNode root) {
		this.root = root;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((root == null) ? 0 : root.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		SoftItemTree other = (SoftItemTree) obj;
		if (root == null) {
			if (other.root != null)
				return false;
		} else if (!root.equals(other.root))
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
		return "SoftTree [root=" + root + "]";
	}

	/**
	 * Finds a node for specified data.
	 * 
	 * @param data
	 *            specifies the data.
	 * @return the node for specified data, if the node was found; null - otherwise.
	 */
	public SoftItemNode findNode(SoftItem data) {
		if (data == null) {
			throw new IllegalArgumentException("data is null.");
		}
		if (root.getData().equals(data)) {
			return root;
		}
		List<SoftItemNode> children = root.getChildren();
		Optional<SoftItemNode> optionalNode = children.stream().filter(n -> n.getData().equals(data)).findFirst();
		SoftItemNode foundNode = null;

		if (optionalNode.isPresent()) {
			foundNode = optionalNode.get();
		} else {
			foundNode = findNode(root, data);
		}

		return foundNode;
	}

	private SoftItemNode findNode(SoftItemNode node, SoftItem data) {
		List<SoftItemNode> children = node.getChildren();
		Optional<SoftItemNode> optionalNode = children.stream().filter(n -> n.getData().equals(data)).findFirst();
		SoftItemNode foundNode = null;
		if (optionalNode.isPresent()) {
			foundNode = optionalNode.get();
		} else {
			for (SoftItemNode child : children) {
				foundNode = findNode(child, data);
				if (foundNode != null) {
					break;
				}
			}
		}

		return foundNode;
	}
}