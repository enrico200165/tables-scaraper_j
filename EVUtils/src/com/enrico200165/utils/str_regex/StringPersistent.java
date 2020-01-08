package com.enrico200165.utils.str_regex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;


public class StringPersistent {

	private String fileName;
	String token;

	public StringPersistent(String fileName, String value) {
		super();
		this.fileName = fileName;
		restore();
	}

	
	public StringPersistent(String fileName) {
		super();
		this.fileName = fileName;
		token = null;
	}

	
	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
		persist();
	}

	public void persist() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(fileName);
			if (token == null) {
				out.println("");
			} else {
				out.println(token);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) out.close();
		}
	}

	public String it() {
		return this.token;
	}
	
	
	public String restore() {
		byte[] encoded;
		try {
			File f = new File(this.fileName);
			if(f.exists() && !f.isDirectory()) { 
				encoded = Files.readAllBytes(Paths.get(fileName));
				token = (new String(encoded)).trim();
			} else {
				token = "";
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			token = null;
		} finally {
			return token;
		}
	}
}
