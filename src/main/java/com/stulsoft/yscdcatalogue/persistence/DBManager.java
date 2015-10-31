/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.persistence;

import java.io.File;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentNavigableMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stulsoft.yscdcatalogue.data.DiskItemTree;
import com.stulsoft.yscdcatalogue.data.SoftItemTree;

/**
 * DB manager
 * 
 * @author Yuriy Stul
 *
 */
public class DBManager {
	private static Logger logger = LogManager.getLogger(DBManager.class);
	private volatile static DBManager instance = null;

	private NavigableSet<String> softItemTreeSet;
	private ConcurrentNavigableMap<String, String> diskItemTreeMap;
	private DB db;

	private DBManager() {
		logger.debug("Creating instance of DB Manager");
		initialize();
	}

	public static DBManager getInstance() {
		if (instance == null) {
			synchronized (DBManager.class) {
				if (instance == null) {
					instance = new DBManager();
				}
			}
		}
		return instance;
	}

	private void initialize() {
		db = DBMaker.newFileDB(new File("d:/work/testMapDB.db")).closeOnJvmShutdown().make();
		softItemTreeSet = db.getTreeSet("softItemTree");
		diskItemTreeMap = db.getTreeMap("diskItemTree");
	}

	public void saveSoftItemTree(final SoftItemTree tree) throws Exception {
		if (tree == null)
			throw new IllegalArgumentException("tree is null.");
		softItemTreeSet.clear();
		ObjectMapper mapper = new ObjectMapper();
		String json;
		try {
			json = mapper.writeValueAsString(tree);
		}
		catch (Exception e) {
			String message = String.format("Error during creating Json for SoftItemTree. Error: %s.", e.getMessage());
			logger.error(message, e);
			throw new Exception(message, e);
		}

		softItemTreeSet.add(json);
		db.commit();
	}

	public SoftItemTree getSoftItemTree() throws Exception {
		String json = softItemTreeSet.first();
		ObjectMapper mapper = new ObjectMapper();
		SoftItemTree tree;
		try {
			tree = mapper.readValue(json, SoftItemTree.class);
		}
		catch (Exception e) {
			String message = String.format("Error during reading Json for SoftItemTree. Error: %s.", e.getMessage());
			logger.error(message, e);
			throw new Exception(message, e);
		}

		return tree;
	}

	public void saveDiskItemTree(final DiskItemTree tree) throws Exception {
		if (tree == null)
			throw new IllegalArgumentException("tree is null.");
		ObjectMapper mapper = new ObjectMapper();
		String json;
		try {
			json = mapper.writeValueAsString(tree);
		}
		catch (Exception e) {
			String message = String.format("Error during creating Json for DiskItemTree. Error: %s.", e.getMessage());
			logger.error(message, e);
			throw new Exception(message, e);
		}
		diskItemTreeMap.put(tree.getId(), json);
		db.commit();
	}

	public DiskItemTree getDiskItemTree(final String id) throws Exception {
		if (StringUtils.isEmpty(id))
			throw new IllegalArgumentException("id is null or empty.");
		String json = diskItemTreeMap.get(id);
		DiskItemTree tree = null;

		if (!StringUtils.isEmpty(json)) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				tree = mapper.readValue(json, DiskItemTree.class);
			}
			catch (Exception e) {
				String message = String.format("Error during reading Json for DiskItemTree. Error: %s.", e.getMessage());
				logger.error(message, e);
				throw new Exception(message, e);
			}
		}

		return tree;
	}
}
