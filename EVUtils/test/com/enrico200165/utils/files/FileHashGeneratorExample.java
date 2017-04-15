package com.enrico200165.utils.files;

import com.enrico200165.utils.net.security.HashGenerationException;
import com.enrico200165.utils.net.security.HashGeneratorUtils;

import java.io.File;

/**
 * Test generating hash values from File.
 * @author www.codejava.net
 *
 */
public class FileHashGeneratorExample {

	public static void main(String[] args) {
		try {
			String filePath = args[0];
			System.out.println("File Path: " + filePath);
			File file = new File(filePath);
			
			String md5Hash = HashGeneratorUtils.generateMD5(file);
			System.out.println("MD5 Hash: " + md5Hash);
			
			String sha1Hash = HashGeneratorUtils.generateSHA1(file);
			System.out.println("SHA-1 Hash: " + sha1Hash);

			String sha256Hash = HashGeneratorUtils.generateSHA256(file);
			System.out.println("SHA-256 Hash: " + sha256Hash);			

		} catch (HashGenerationException ex) {
			ex.printStackTrace();
		}
	}

}
