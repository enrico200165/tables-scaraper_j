package com.enrico200165.utils.files;

import com.enrico200165.utils.str_regex.NameValuePairString;
import com.enrico200165.utils.various.Utl;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author enrico00165
 *         It should contain helper functions for file management
 *         currently: 
 *         - reading a file into an array of lines 
 *         - writing a file,
 *         extensively using functionoids passed as parameters
 *         VERY USEFUL for my quick&dirty rubbish
 */
public class FileHelper implements FileProcessorI {


	void replaceInFile(String outputFilePath, List<NameValuePairString> replacements) {
		TextLineProcessorI proc = new TextFileProcReplRegexes(replacements);
		processToOtherFile(null,outputFilePath,"\n");
	}

	
	public static String adjustPath(String winpath) {
		String sep = System.getProperty("file.separator");
		// String ret = winpath.replaceAll("\\\\", sep);
		// ret = winpath.replaceAll(sep+sep, sep);
		String ret = winpath.replaceAll("\\\\","/");
		return ret;
	}


	
	public FileHelper(String rootPath, String pathName, String ext, String encoding) {
		super();

		_rootPath = (rootPath != null) ? adjustPath(rootPath) : "";
		_pathName = (pathName != null) ? adjustPath(pathName) : "";
		_ext = (ext != null) ? ext : "";
		_lines = new ArrayList<TextFileLine>();
		_file = new File(pathName);
		if (encoding == null || encoding.equals("")) {
			_encoding = Utl.ENCODING_UTF8;
			log.info("encoding not provided, using default: " + _encoding);
		} else
			_encoding = encoding;

		extBufferPar = null;
		intoInterBuff = false;
	}


	public static boolean fileExists(String dirPathName) {
		File f = new File(dirPathName);
		return f.exists();
	}


	public static boolean dirExists(String dirPathName) {
		File f = new File(dirPathName);
		if (!f.exists())
			return false;
		return f.isDirectory();
	}

	public static boolean pathNameExists(String pathName, boolean reportError) {

		File f = new File(pathName);
		if (!f.exists()) {
			if (reportError) {
				log.log(Level.SEVERE, "not existing pathName: " + pathName);
			}
			return false;
		}
		return true;
	}


	public boolean processToOtherFile(TextLineProcessorI processor, String outputFilePath, String endLine) {

		List<String> outputLines = new ArrayList<String>();
		readAll(true, null); // adesso abbiamo linee in buffer

		for (TextFileLine line : _lines) {
			String s = processor.processLine(line.get_line());
			outputLines.add(s);
		}

		FileHelper fh = new FileHelper(null,outputFilePath,null,"UTF-8");
		return fh.writeTxtLinesList(outputLines,endLine);
	}




	
	public boolean performOnFileOrDir(File f) {
		return readAll(intoInterBuff, extBufferPar);
	}


	public boolean writeTxtLinesList(List<String> stringsList, String separator) {

		List<IRenderableAsTextLine> renderableAsTextLines = new ArrayList<IRenderableAsTextLine>();
		for (String s : stringsList) {
			renderableAsTextLines.add(new IRenderableAsTexLineStringWrapper(s));
		}
		writeList(renderableAsTextLines, null, null, null,new TexLineRendererString(),0, Utl.NOT_INITIALIZED_INT, Utl.NOT_INITIALIZED_INT, true, separator);
		return true;
	}


	/**
	 * RICOSTRUZIONE A POSTERIORI, non necessariamente corretta
	 * Scrive, su un file di testo, una lista di entità, eventualmente dividendo su file1, file2 ... fileN
	 * @param entries
	 * @param header
	 * @param footer
	 * @param filter
	 * @param renderer
	 * @param first
	 * @param last
	 * @param maxPerFile
	 * @param overwriteFile
     * @param delimiter
     * @return
     */

	public boolean writeList(List<IRenderableAsTextLine> entries, String header, String footer, IEVRenderableFilter filter,
			ITexLineRenderer renderer, int first, int last, int maxPerFile, boolean overwriteFile, String delimiter) {

		if (entries.size() == 0) {
			log.log(Level.SEVERE, "attempt to write to file when empty");
			return false;
		}
		if (!overwriteFile && (new File(_pathName)).exists()) {
			log.log(Level.WARNING, "file exists, will not overwrite it, file: " + fullPath());
			return false;
		}

		FileOutputStream fos = null;
		OutputStreamWriter outwriter = null;
		String line = "";
		int pathNr = 1;
		try {
			fos = (maxPerFile > 1) ? new FileOutputStream(fullPath(pathNr)) : new FileOutputStream(fullPath());
			outwriter = new OutputStreamWriter(fos, _encoding);

			if (header != null && header.length() > 0) {
				outwriter.write(header + "\n");
			}

			long scanned = 0;
			int written = 0;
			int inFile = 0;
			for (IRenderableAsTextLine e : entries) {
				scanned++;
				if (first != Utl.NOT_INITIALIZED_INT && scanned < first) continue;
				if (last != Utl.NOT_INITIALIZED_INT && scanned > last) break;
				if (filter != null && !filter.includeIt(e)) continue;

				// eventually new file
				if (maxPerFile != Utl.NOT_INITIALIZED_INT && (written % maxPerFile) == 0 && written != 0) {
					log.info("closing file, written: total, in file): " + written + ", " + inFile);
					outwriter.write(footer);
					pathNr++;
					outwriter.flush();
					outwriter.close();
					outwriter = null;
					fos.flush();
					fos.close();
					fos = null;
					fos = new FileOutputStream(fullPath(pathNr));
					outwriter = new OutputStreamWriter(fos, _encoding);
					outwriter.write(header);
					inFile = 0;
				}
				line = renderer.render(e, scanned, written, 0);
				if (delimiter != null && delimiter.length() > 0)
					line += delimiter;
				log.log( Level.FINE, "writing #: " + scanned + " " + line);
				outwriter.write(line);
				++written;
				++inFile;
			}
			if (footer != null)
			outwriter.write(footer);
			else log.log(Level.WARNING, "footer null for file: "+getPathName());
			outwriter.flush();
			outwriter.close();
			outwriter = null;
			fos.flush();
			fos.close();
			fos = null;
			log.info("in " + System.getProperty("user.dir") + " scritto: " + this._pathName + " \n# esaminati: " + (scanned) + " scritti: "
					+ (written));
			Runtime rt = Runtime.getRuntime();
			// rt.exec("firefox "+pathName);
			return true;
		} catch (IOException e) {
			log.log(Level.SEVERE, "Eccezione di IO", e.toString());
		} finally {
			System.gc();
		}
		return false;
	}

	
	public static boolean CopyFile(File in, File out) {
		if (!in.exists()) {
			log.log(Level.SEVERE, "FileDirTreeWalk: " + in.getAbsolutePath() + " non existing, cancel copy");
			return false;
		}
		/*
		 * andrebbe controllata la directory if (!out.exists()) {
		 * log.log(Level.SEVERE, "FileDirTreeWalk: " + out.getAbsolutePath() +
		 * " non existing, cancel copy"); return false; }
		 */
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(in);
			fos = new FileOutputStream(out);
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
			fis.close();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	/**
	 * Reads lines in the member "buffer" if not null Appends lines to the
	 * "buffer" passed as parameter if not null
	 * 
	 * @return
	 */
	public boolean readAll(boolean readIntoInternBuffer, List<TextFileLine> appendToGloBuffer) {
		boolean rc = true;
		InputStreamReader isr;
		BufferedReader bufferedReader;

		if (!(new File(_pathName)).exists()) {
			log.log(Level.SEVERE, "read non existing file: " + _pathName);
			return false;
		}

		try {
			log.info("Dir di lavoro: " + System.getProperty("user.dir") + "\nopening: " + _pathName);
			FileInputStream fis = new FileInputStream(getPathName());
			if (!fileExists(getPathName())) {
				log.log(Level.SEVERE, "file does not exitst: "+getPathName());
				return false;
			}
			isr = new InputStreamReader(fis, _encoding);
			bufferedReader = new BufferedReader(isr);
			log.log( Level.FINE, "Encoding: in lettura " + isr.getEncoding());

			String line;
			int lineNr;
			String relPath = _pathName.substring(_rootPath.length(), _pathName.length());
			for (lineNr = 1; (line = bufferedReader.readLine()) != null; lineNr++) {
				if (readIntoInternBuffer) _lines.add(new TextFileLine(relPath, lineNr, line));
				if (appendToGloBuffer != null) appendToGloBuffer.add(new TextFileLine(relPath, lineNr, line));
			}
			bufferedReader.close();
			log.log( Level.FINE, "elaborate nr linee input: " + lineNr);
		} catch (IOException e) {
			log.log(Level.SEVERE, "", e.toString());
			return false;
		} catch (Exception e) {
			log.log(Level.SEVERE, "Exception", e.toString());
			return false;
		}

		return rc;

	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		_encoding = encoding;
	}

	/**
	 * @return the pathName
	 */
	public String getPathName() {
		return _pathName;
	}

	String fullPath(int fileNr) {
		String s = "";
		s += (this._rootPath != null) ? this._rootPath : "";
		s += (this._rootPath != null && this._rootPath.length() > 0 && this._pathName!= null) ? "/" : "";
		s += (this._pathName!= null) ? this._pathName : "";
		s += (fileNr >= 0) ? ("_" + String.format("%03d", fileNr)) : "";
		s += (this._ext != null) ? ("."+this._ext) : "";
		s = adjustPath(s);
		return s;
	}

	String fullPath() {
		return fullPath(-1);
	}

	/**
	 * @param extBufferPar
	 *            the extBufferPar to set
	 */
	public void setExtBufferPar(ArrayList<TextFileLine> extBufferPar) {
		this.extBufferPar = extBufferPar;
	}

	/**
	 * @return the lines
	 */
	public ArrayList<String> getLines() {
		ArrayList<String> locBuffer = new ArrayList<String>();
		for (TextFileLine tfl : _lines)
			locBuffer.add(tfl.get_line());
		return locBuffer;
	}

	File _file;
	String _rootPath;
	String _pathName;
	String _ext;
	String _encoding;
	ArrayList<TextFileLine> _lines;

	ArrayList<TextFileLine> extBufferPar; // holds parameter for functionoid
											// perform()
	boolean intoInterBuff; // holds parameter for functionoid perform()

	private static Logger log = LogManager.getLogManager().getLogger(FileHelper.class.getName());
}
