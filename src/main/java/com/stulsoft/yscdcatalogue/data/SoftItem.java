/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.data;

import org.apache.commons.lang3.StringUtils;

/**
 * Holds information about soft item.
 * 
 * @author Yuriy Stul
 *
 */
public class SoftItem {
	public static final String FIELD_NAME_NAME = "name";
	public static final String FIELD_NAME_TYPE = "type";
	public static final String FIELD_NAME_DISK = "disk";

	private String name;
	private SoftItemType type;
	private String diskId;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the diskId
	 */
	public String getDiskId() {
		return diskId;
	}

	/**
	 * @param diskId
	 *            the diskId to set
	 */
	public void setDiskId(String diskId) {
		this.diskId = diskId;
	}

	/**
	 * @return the type
	 */
	public SoftItemType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(SoftItemType type) {
		this.type = type;
	}

	protected SoftItem() {
	}

	/**
	 * @param name name of item
	 * @param type type of item
	 */
	public SoftItem(String name, SoftItemType type) {
		super();
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("name is null or empty.");
		}
		this.name = name;
		this.type = type;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		SoftItem other = (SoftItem) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
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
		return getName();
	}
}
