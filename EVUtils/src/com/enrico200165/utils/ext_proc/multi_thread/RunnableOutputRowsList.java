package com.enrico200165.utils.ext_proc.multi_thread;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author enrico
 *         Contenitore per l'output dei comandi
 *         lista di righe di testo
 */
public class RunnableOutputRowsList {

	RunnableOutputRowsList(String namepar) {
		rows = new ArrayList<String>(10);
		this.name = namepar;
	}

	void clear() {
		synchronized (rows) {
			rows.clear();
		}
	}

	void add(String s, PrintStream echoIt, String prefix) {
		synchronized (rows) {
			rows.add(s);
			if (echoIt != null) echoIt.println(prefix + s);
			rows.notifyAll();
		}
	}

	void dump(PrintStream out, String prefix) {
		synchronized (rows) {
			for (String r : rows) {
				out.println(prefix+r);
			}
			out.flush();
		}
	}

	List<String> extractAndRemove(boolean dump, String dumpPrefix) {
		List<String> out = new ArrayList<String>(rows.size());
		synchronized (rows) {
			out.addAll(rows);
			clear();
			rows.notifyAll();
		}
		System.out.println("returning nr lines:" + out.size());
		if (dump) this.dump(System.out, dumpPrefix);
		return out;
	}

	List<String> rows;
	String name;
}
