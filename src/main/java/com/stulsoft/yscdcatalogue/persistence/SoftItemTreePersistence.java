/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.persistence;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stulsoft.yscdcatalogue.Utils;
import com.stulsoft.yscdcatalogue.controller.MainViewController;
import com.stulsoft.yscdcatalogue.data.SoftItem;
import com.stulsoft.yscdcatalogue.data.SoftItemTree;

import javafx.scene.control.TreeItem;

/**
 * @author Yuriy Stul
 *
 */
public class SoftItemTreePersistence {
	private static final Logger logger = LogManager.getLogger(MainViewController.class);

	/**
	 * Loads a root tree item.
	 * 
	 * @param fileName
	 *            specifies the file
	 * @return the root tree item.
	 * @throws IOException
	 *             I/O exception
	 * @throws JsonMappingException
	 *             system error
	 * @throws JsonParseException
	 *             system error
	 */
	public static SoftItemTree load(final String fileName) throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isEmpty(fileName))
			throw new IllegalArgumentException("fileName is null or empty.");
		logger.info("Load from file {}", fileName);

		ObjectMapper mapper = new ObjectMapper();
		SoftItemTree softTree = mapper.readValue(new File(fileName), SoftItemTree.class);
		logger.info("Loading completed.");
		return softTree;
	}

	/**
	 * Saves the soft tree into file as Json text.
	 * 
	 * @param softTreeItem
	 *            the soft tree item
	 * @param fileName
	 *            the file name
	 * @throws IOException
	 *             I/O exception
	 * @throws JsonMappingException
	 *             system error
	 * @throws JsonGenerationException
	 *             system error
	 */
	public static void save(final TreeItem<SoftItem> softTreeItem, final String fileName) throws JsonGenerationException, JsonMappingException, IOException {
		if (softTreeItem == null)
			throw new IllegalArgumentException("softTreeItem is null.");
		if (StringUtils.isEmpty(fileName))
			throw new IllegalArgumentException("fileName is null or empty.");
		logger.info("Save into file {}", fileName);
		SoftItemTree softTree = Utils.buildSoftTree(softTreeItem);

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(fileName), softTree);

		logger.info("Saving completed.");
	}

	
}
