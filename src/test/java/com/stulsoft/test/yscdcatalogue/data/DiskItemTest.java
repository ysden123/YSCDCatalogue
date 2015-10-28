/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.test.yscdcatalogue.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.stulsoft.yscdcatalogue.data.DiskItem;
import com.stulsoft.yscdcatalogue.data.DiskItemType;

/**
 * @author Yuriy Stul
 *
 */
public class DiskItemTest {

	/**
	 * Test method for {@link com.stulsoft.yscdcatalogue.data.DiskItem#getFileName()}.
	 */
	@Test
	public void testGetFileName() {
		String expectedFileName = "d.ext";
		DiskItem i = new DiskItem("c:/a/b/c/" + expectedFileName, DiskItemType.FILE,null,null);
		String actualFileName = null;
		try {
			actualFileName = i.getFileName();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(expectedFileName, actualFileName);

		i = new DiskItem("c:\\a\\b\\c\\" + expectedFileName, DiskItemType.FILE,null,null);
		try {
			actualFileName = i.getFileName();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(expectedFileName, actualFileName);
	}
}
