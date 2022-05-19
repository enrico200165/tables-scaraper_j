package com.enrico200165.utils.ext_proc.single_thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ExecutorRuntime extends aExecutor {
	/*
	 */
	@Override
	public void exec(String argsPar[], boolean isShellCommand) {
		if (argsPar.length < 1) {
			System.err.println("!!!!");
			System.exit(1);
		}

		String[] args = executeHelper(argsPar, isShellCommand);

		try {
			Process proc = Runtime.getRuntime().exec(args);
			// capture and output error and output of Process/command
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			int exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal);
		} catch (Throwable t) {
			log.error(t);
			t.printStackTrace();
		}
	}


	private static Logger log = LogManager.getLogger(ExecutorRuntime.class);

}
