/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Holds the disk item information.
 * 
 * @author Yuriy Stul
 *
 */
public class DiskItem {
	public static final String FIELD_NAME_FULL_PATH = "fullPath";
	public static final String FIELD_NAME_TYPE = "type";

	private String storageName;
	private String fullPath;
	private DiskItemType type;
	private Long size;
	private Date date;
	private String comment;

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the storageName
	 */
	public String getStorageName() {
		return storageName;
	}

	/**
	 * @param storageName
	 *            the storageName to set
	 */
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	/**
	 * @return the fullPath
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * @param fullPath
	 *            the fullPath to set
	 */
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	/**
	 * @return the type
	 */
	public DiskItemType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(DiskItemType type) {
		this.type = type;
	}

	/**
	 * @return the size
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	/**
	 * @return the date
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	protected DiskItem() {
	}

	/**
	 * Initializes a new instance of the DiskItem class.
	 * 
	 * @param fullPath
	 *            specifies the full path.
	 * @param type
	 *            specifies the disk item type.
	 * @param size
	 *            a file size
	 * @param date
	 *            a file create date
	 */
	public DiskItem(final String fullPath, final DiskItemType type, final Long size, final Date date) {
		super();
		if (StringUtils.isEmpty(fullPath)) {
			throw new IllegalArgumentException("fullPath is null or empty.");
		}
		this.fullPath = fullPath;
		this.type = type;
		this.size = size;
		this.date = date;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((fullPath == null) ? 0 : fullPath.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((storageName == null) ? 0 : storageName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		DiskItem other = (DiskItem) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (fullPath == null) {
			if (other.fullPath != null)
				return false;
		} else if (!fullPath.equals(other.fullPath))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (storageName == null) {
			if (other.storageName != null)
				return false;
		} else if (!storageName.equals(other.storageName))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DiskItem [storageName=" + storageName + ", fullPath=" + fullPath + ", type=" + type + ", size=" + size + ", date=" + date + ", comment=" + comment + "]";
	}

	/**
	 * Returns a file name - the file name with extension for file
	 * 
	 * @return the file name - the file name with extension for file
	 * @throws FileNotFoundException
	 *             if the instance type is not FILE.
	 */
	public String extractFileName() throws FileNotFoundException {
		if (type != DiskItemType.FILE) {
			throw new FileNotFoundException("The item specifies a " + getType() + ". " + toString());
		}
		return Paths.get(getFullPath()).getFileName().toString();
	}

	/**
	 * @return the disk item name
	 */
	@JsonIgnore
	public String getFileName() {
		String buffer;

		switch (getType()) {
		case DISK:
			buffer = getStorageName();
			break;
		case DIRECTORY:
			int index = getFullPath().lastIndexOf(File.separator);

			buffer = index > -1 ? getFullPath().substring(index + 1) : "";
			break;
		case FILE:
			try {
				buffer = extractFileName();
			}
			catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			break;
		default:
			buffer = "ERROR. Unsupported type: " + type;
			break;
		}

		return buffer;
	}

	/**
	 * @return the file creation date
	 */
	@JsonIgnore
	public String getFileDate() {
		if (getType() == DiskItemType.FILE && date != null) {
			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
		} else {
			return "";
		}
	}

	/**
	 * @return the file creation date
	 */
	@JsonIgnore
	public String getFileSize() {
		if (getType() == DiskItemType.FILE && size != null) {
			return String.valueOf(size);
		} else {
			return "";
		}
	}
}
