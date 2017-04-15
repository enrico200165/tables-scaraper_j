package com.enrico200165.utils.ext_proc.multi_thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.List;

/*
 * Legge uno stream di output di un java.lang.Process
 * typicamente ne creo due, uno attaccato all'output uno all'error
 * 
 */
public class CommandOutputGobbler implements Runnable {

	public void setWaitMsec(long val) {
		this.maxWaitMsec = val;
	}

	public Barrier barrier() {
		return barrier;
	}

	CommandOutputGobbler(InputStream is, String name, Barrier barrier, CanGo getOutputs, long waitMsec) {
		this(is, name, null, barrier, getOutputs, waitMsec);
	}

	CommandOutputGobbler(InputStream isPar, String name, OutputStream redirect, Barrier barrier, CanGo getOutputs, long waitMsec) {
		this.is = isPar;
		isChannel = null;
		this.name = name;
		this.os = redirect;
		output = new RunnableOutputRowsList("buf of "+this.name);
		this.barrier = barrier;
		this.getOutputsFlag = getOutputs;
		this.maxWaitMsec = waitMsec;
	}

	/*
	 * forse non più necessaria, solo utile nel caso di riutilizzo di threads
	 * su più comandi, abbandonato ora
	 */
	void waitForReadAllowed() throws InterruptedException {
		synchronized (getOutputsFlag) {
			while (!getOutputsFlag.isGetIt()) {
				getOutputsFlag.wait();
			}
		}
		// System.err.println("ok, can read now");
	}

	void simpleMindedRead() throws IOException {
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while ((line = br.readLine()) != null) {
			output.add(line, null,"dump-while reading");
		}
	}

	void newIoRead() {
		// Defaults to READ
		try {
			this.isChannel = Channels.newChannel(this.is);
			ByteBuffer buf = ByteBuffer.allocate(1024);

			// Read the bytes with the proper encoding for this platform. If
			// you skip this step, you might see something that looks like
			// Chinese characters when you expect Latin-style characters.
			// EV qui devo metterlo a mano se opero su sistema remoto???
			// Charset charset = Charset.defaultCharset();
			// Charset charset = Charset.forName("UTF-8");

			String encoding = System.getProperty("file.encoding");
			while (isChannel.read(buf) > 0) {
				buf.rewind();
				String read = Charset.forName(encoding).decode(buf).toString();
				this.output.add(read, System.out,"dump while reading");
				buf.flip();
			}
		} catch (IOException x) {
			System.out.println("caught exception: " + x + " Esco");
		}
	}

	public void run() {
		long durBufferUnavailable = 0;
		try {
			PrintWriter pw = null;
			if (os != null) pw = new PrintWriter(os);

			waitForReadAllowed();
			//simpleMindedRead();
			newIoRead();

			barrier.updateBarrier(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> extractAndRemove(boolean dump) {
		return output.extractAndRemove(dump,"extracting");
	}

	public void dumpOutput(PrintStream out) {
		output.dump(out, this.name);
	}

	InputStream is;
	RunnableOutputRowsList output;
	Barrier barrier;
	CanGo getOutputsFlag;
	ReadableByteChannel isChannel;
	String name;
	OutputStream os;
	long maxWaitMsec;
}
