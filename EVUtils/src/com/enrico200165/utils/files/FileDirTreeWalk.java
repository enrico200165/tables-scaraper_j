package com.enrico200165.utils.files;

import java.io.*;
import java.util.ArrayList;

import com.enrico200165.utils.various.Utl;
import org.apache.log4j.Logger;


public class FileDirTreeWalk {

	public FileDirTreeWalk(String baseName, String dirFilter, String fileFilter, String ignFileFilter) {
		super();
		_fileFilter = fileFilter;
		_dirFilter = dirFilter;
		_baseName = baseName;
		_globTextFileLine = null;
		this.ignFileFilter = ignFileFilter;
	}

	public FileDirTreeWalk(String baseName) {
		super();
		_fileFilter = "";
		_dirFilter = "";
		this._baseName = baseName;
	}

	/**
	 * @param dirName
	 *            null to use the member dir name
	 * @param fPr
	 * @return
	 */
	public boolean navigate(String dirName, FileProcessorI fp) {
		String curDir;
		if (dirName == null || dirName.equals("")) {
			curDir = _baseName;
			_globTextFileLine = new ArrayList<TextFileLine>();
		} else {
			curDir = dirName;
		}

		File dir = new File(curDir);
		if (!dir.exists()) {
			log.error(_baseName + " does not  exist");
			return false;
		}
		if (!dir.isDirectory()) {
			log.error(_baseName + " is not a directory");
			return false;
		}

		String[] children = dir.list();
		for (int i = 0; i < children.length; i++)
			children[i] = curDir + "\\" + children[i];

		for (String child : children) {
			File f = new File(child);
			if (f.isDirectory()) {
				if (_dirFilter != null && !_dirFilter.equals(""))
					if (!child.matches(_dirFilter)) {
						log.info("ignoro dir not matching patter:\n" + f.getName());
						continue;
					}
				if (!navigate(child, null)) {
					log.error("");
					return false;
				}
			}
			if (f.isFile()) {
				if (_fileFilter != null && !_fileFilter.equals(""))
					if (!child.matches(_fileFilter)) {
						// log.info("ignoro file "+f.getName()+" not matching pattern: "+_fileFilter);
						continue;
					}
				if (ignFileFilter != null && !ignFileFilter.equals(""))
					if (child.matches(ignFileFilter)) {
						// log.info("ignoro file "+f.getName()+" not matching pattern: "+_fileFilter);
						continue;
					}
				FileHelper fpr = new FileHelper(this._baseName, f.getAbsolutePath(),"", Utl.ENCODING_UTF8);
				fpr.setExtBufferPar(_globTextFileLine);
				if (!fpr.performOnFileOrDir(f)) {
					log.error("");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @return the _globTextFileLine
	 */
	public ArrayList<TextFileLine> get_globTextFileLine() {
		return _globTextFileLine;
	}

	String _fileFilter;
	String ignFileFilter;
	String _dirFilter;
	String _baseName;
	ArrayList<TextFileLine> _globTextFileLine;

	private static org.apache.log4j.Logger log = Logger.getLogger(FileDirTreeWalk.class);
}
