package com.enrico200165.utils.ext_proc.single_thread;

import java.io.*;

/* Legge dagli streams su cui scrivono i java.lng.Process che eseguono i nostri comandi e
 * scrive quello che legge sulla console e, se non passato a null, su un output stream 
 * creato da noi
 * 
 * 
 * */

class StreamGobbler extends Thread {
	InputStream processOutStream;
	/* quello che per il java.langProcess è un output stream su cui scrivere
	 * per noi è un input stream da cui leggere
	 */
	String type;
	OutputStream optionalOutStream;

	StreamGobbler(InputStream is, String type) {
		this(is, type, null);
	}

	StreamGobbler(InputStream is, String type, OutputStream redirect) {
		this.processOutStream = is;
		this.type = type;
		this.optionalOutStream = redirect;
	}

	@Override
	public void run() {
		PrintWriter pw = null;
		String line = null;

		try {
			if (optionalOutStream != null) pw = new PrintWriter(optionalOutStream);

			BufferedReader br = new BufferedReader(new InputStreamReader(processOutStream));
			while ((line = br.readLine()) != null) {
				System.out.println(type + ">" + line);
				if (pw != null) pw.println(line);
			}
			if (pw != null) pw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
