/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.stulsoft.yscdcatalogue.Utils;
import com.stulsoft.yscdcatalogue.data.DiskItem;
import com.stulsoft.yscdcatalogue.data.DiskItemNode;
import com.stulsoft.yscdcatalogue.data.DiskItemTree;
import com.stulsoft.yscdcatalogue.data.DiskItemType;

/**
 * File tree walker.
 * 
 * @author Yuriy Stul
 *
 */
public class FileTreeWalk {
	private static Logger logger = LogManager.getLogger(FileTreeWalk.class);

	/**
	 * Builds a tree of the DiskItem elements.
	 * 
	 * @param startFolder
	 *            start folder
	 * @return the tree of the DiskItem elements.
	 * @throws IOException
	 *             if an error was occurred during walking through file tree.
	 */
	public static DiskItemTree buildFileTree(final String startFolder) throws IOException {
		if (StringUtils.isEmpty(startFolder)) {
			throw new IllegalArgumentException("startFolder is null or empty.");
		}
		Path startPath = Paths.get(startFolder);
		DiskItemTree t = new DiskItemTree(new DiskItem(startFolder, DiskItemType.DIRECTORY, null, null));
		Files.walk(startPath).sorted((p1, p2) -> p1.toString().compareTo(p2.toString())).forEach(p -> {
			DiskItem item = null;
			if (p.toFile().isDirectory()) {
				item = new DiskItem(p.toString(), DiskItemType.DIRECTORY, null, null);
			} else {
				try {
					BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
					item = new DiskItem(p.toString(), DiskItemType.FILE, attr.size(), new Date(attr.creationTime().toMillis()));
				}
				catch (Exception e) {
					String msg = String.format("Failed getting file attribute for %s. Error: %s", p.toString(), e.getMessage());
					logger.error(msg, e);
				}
			}

			if (item != null) {
				DiskItemNode node = new DiskItemNode(item, null);
				if (t.findNode(node.getData()) == null) {
					DiskItem parentItem = new DiskItem(p.getParent().toString(), DiskItemType.DIRECTORY, null, null);
					DiskItemNode parent = t.findNode(parentItem);
					if (parent != null) {
						parent.addChild(node);
					}
				}
			}
		});

		t.getRoot().getData().setType(DiskItemType.DISK);
		t.getRoot().getData().setStorageName(Utils.getStorageName(new File(startFolder)));

		String id = UUID.randomUUID().toString();
		t.setId(id);
		
		return t;
	}
}
