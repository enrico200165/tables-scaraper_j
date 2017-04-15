package com.enrico200165.utils.ext_proc.single_thread;




/**
 * @author enrico
 * Usa ProcessBuilder
 */
public class ExecutorProcessBuilder extends aExecutor {

	@Override
	public void exec(String argsPar[], boolean isShellCommand) {

		String[] args = executeHelper(argsPar, isShellCommand);
		try {

			Process process = new ProcessBuilder(args).start();
			StreamGobbler errorGobbler = new StreamGobbler(
					process.getErrorStream(), "ERROR");
			StreamGobbler outputGobbler = new StreamGobbler(
					process.getInputStream(), "OUTPUT");
			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			int exitVal = process.waitFor();
			System.out.println("ExitValue: " + exitVal);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
