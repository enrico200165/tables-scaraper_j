package com.enrico200165.utils.net;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;



public class SFTPTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sftp = new SftpHelper();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpLoad() {
		sftp = new SftpHelper();

		sftp.connect();
		
		sftp.upload("c:\\build.ini", "pippo.deleteme");
		SftpHelper.output(sftp.ls("."));
		
		sftp.disconnect();
	}

	SftpHelper sftp;
}
