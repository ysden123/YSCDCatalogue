/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.test.yscdcatalogue.persistence;

import static org.junit.Assert.*;

import org.junit.Test;

import com.stulsoft.yscdcatalogue.data.Configuration;
import com.stulsoft.yscdcatalogue.persistence.ConfigurationPersistence;

/**
 * @author Yuriy Stul
 *
 */
public class ConfigurationPersistenceTest {

	/**
	 * Test method for {@link com.stulsoft.yscdcatalogue.persistence.ConfigurationPersistence#save(com.stulsoft.yscdcatalogue.data.Configuration)} and
	 * {@link com.stulsoft.yscdcatalogue.persistence.ConfigurationPersistence#load()}.
	 */
	@Test
	public void testSaveLoad() {
		Configuration expectedConfiguration = new Configuration("fileName");
		try {
			ConfigurationPersistence.save(expectedConfiguration);
			Configuration actualConfiguration = ConfigurationPersistence.load();
			assertEquals(expectedConfiguration, actualConfiguration);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
