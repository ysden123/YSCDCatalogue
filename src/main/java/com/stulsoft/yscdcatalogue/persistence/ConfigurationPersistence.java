/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.persistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stulsoft.yscdcatalogue.data.Configuration;

/**
 * Configuration persistence.
 * 
 * @author Yuriy Stul
 *
 */
public class ConfigurationPersistence {
	private static Logger logger = LogManager.getLogger(ConfigurationPersistence.class);

	/**
	 * Stores the configuration into file.
	 * 
	 * @param configuration
	 *            the configuration
	 * @throws Exception
	 *             an error was occurred
	 */
	public static void save(final Configuration configuration) throws Exception {
		if (configuration == null)
			throw new IllegalArgumentException("configuration is null.");
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(getConfFileName()), configuration);
		}
		catch (Exception e) {
			String message = String.format("Error during saving configuration into %s. Error: %s.", getConfFileName(), e.getMessage());
			logger.error(message, e);
			throw new Exception(message, e);
		}
	}

	/**
	 * Loads a configuration.
	 * 
	 * @return the configuration.
	 * @throws Exception
	 *             I/O error
	 */
	public static Configuration load() throws Exception {
		Configuration configuration;
		File file = null;
		try {
			file = new File(getConfFileName());
			if (!file.exists()) {
				configuration = new Configuration(null);
			} else {
				ObjectMapper mapper = new ObjectMapper();
				configuration = mapper.readValue(file, Configuration.class);
			}
		}
		catch (Exception e) {
			String message = String.format("Error during loading configuration from %s. Error: %s.", getConfFileName(), e.getMessage());
			logger.error(message, e);
			throw new Exception(message, e);
		}

		return configuration;
	}

	private static String getConfFileName() {
		final File userDataDirFile = new File(System.getProperty("user.home"), ".yscdcatalogue");
		if (!userDataDirFile.exists()) {
			userDataDirFile.mkdirs();
		}
		final String configFileName = (new File(userDataDirFile.getAbsolutePath(), "configuration.json")).getAbsolutePath();
		return configFileName;
	}

	public static void backup(final String fileName) throws IOException {
		File file = new File(fileName);
		if (file.exists()) {
			Path source = Paths.get(file.getAbsolutePath());
			Path target = Paths.get(file.getAbsolutePath().replace(".json", ".bak"));
			Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		}
	}
}
