package com.enrico200165.utils.ext_proc.multi_thread;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DoProcessBuilder {

	String threadsStateStr(Thread mainn, Thread err, Thread out) {
		String s = "";
		if (!mainn.getState().toString().equals("RUNNABLE")) s += ("main:" + mainn.getState());
		if (!err.getState().toString().equals("RUNNABLE")) s += ("err:" + err.getState());
		if (!out.getState().toString().equals("RUNNABLE")) s += ("out:" + out.getState());
		return s;
	}

	void logThreadsState(Thread mainn, Thread err, Thread out) {
		String s = threadsStateStr(mainn, err, out);
		System.out.println(s);
	}

	
	void esegui(Barrier barrier, CanGo getOutput, List<String> cmd) throws InterruptedException, IOException {
		barrier.reset();
		getOutput.setCanGo(true);

		Process process = (new ProcessBuilder(cmd)).start();
		CommandOutputGobbler errorGobbler = new CommandOutputGobbler(process.getErrorStream(), "error", barrier, getOutput, 2 * 1000);
		CommandOutputGobbler outputGobbler = new CommandOutputGobbler(process.getInputStream(), "output", barrier, getOutput, 2 * 1000);
		assert (errorGobbler.barrier() == outputGobbler.barrier);
		(new Thread(errorGobbler, "error_thread")).start();
		(new Thread(outputGobbler, "output_thread")).start();
		process.waitFor();
		errorGobbler.dumpOutput(System.err);
		outputGobbler.dumpOutput(System.out);

		PrintWriter writeToProcess = new PrintWriter(process.getOutputStream());

		/* in case one needs to write input
		 * writeToProcess.println("write input");
		 * writeToProcess.flush();
		 */
		
	}
	
	public static void main(String args[]) throws Exception {

		List<String> cmd = new ArrayList<String>();
		// cmd.add("ping"); cmd.add("google.com");
		cmd.add("netstat"); // cmd.add("autoexec.bat");
		// --- start and setup ---
		CanGo getOutput = new CanGo("readCmdOutputs");
		Barrier barrier = new Barrier(2, getOutput);

		DoProcessBuilder pb = new DoProcessBuilder();
		pb.esegui(barrier,getOutput,cmd);
		
		System.exit(0);
	}
}
