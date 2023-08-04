package com.enrico200165.utils.ext_proc.single_thread;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import java.util.ArrayList;

public abstract class aExecutor implements iExecutor {

	public aExecutor() {
		super();
	}

	/**
	 * Prepends the shell name (based on OS), to the command
	 * Not using another vectors as it is not only the name of the shell that
	 * we prepend but also an option, so in theory the size of the new list is
	 * unknown
	 * so we must use a dynamic list
	 * 
	 * @param argc
	 *            : arfuments and options without the shell
	 * @return vector with shell and its options, followed by arguments
	 */
	protected String[] prependRightShell(String[] argc) {
		ArrayList<String> cmd = new ArrayList<String>();
		// contiene il nome della shell e gli argomenti

		String osName = System.getProperty("os.name");
		if (osName.equals("Windows NT") || osName.equals("Windows 7")) {
			cmd.add("cmd.exe");
			cmd.add("/C");
		} else if (osName.equals("Windows 95")) {
			cmd.add("command.com");
			cmd.add("/C");
		} else {
			log.log(Level.SEVERE, "OS not recognized, exiting (FIX should be TRIVIAL, just add it");
			System.exit(1);
			return null;
		}
		// append the cmd line arguments to the command line
        Collections.addAll(cmd, argc);

		// spostiamo nell'array

		String[] adjstLength = new String[cmd.size()];
		// --- se l'array non è tutto riempito si ottiene errore ---
		for (int j = 0; j < adjstLength.length; j++)
			adjstLength[j] = cmd.get(j);

		return adjstLength;
	}

	// utility function
	String cmdToString(String[] args) {
		String s = "";
		for (int i = 0; i < args.length; i++)
			s += " " + args[i];
		return s;
	}

	String[] executeHelper(String[] arg, boolean isShellCommand) {
		if (arg.length < 1) {
			System.err.println("!!!!");
			System.exit(1);
		}

		String[] args = isShellCommand ? prependRightShell(arg) : arg;

		log.info("Executing: " + cmdToString(args));		
		return args;
	}

	@Override
	public void quickDirtyExec(String argsString, boolean isShellCommand) {
		String[] args = new String[1];
		args[0] = argsString;
		exec(args, isShellCommand);
	}

	
	abstract public void exec(String argsPar[], boolean isShellCommand);

	private static final Logger log = LogManager.getLogManager().getLogger(aExecutor.class.getName());
}