
package com.enrico200165.utils.ext_proc.single_thread;

public interface iExecutor {

	void exec(String argsPar[], boolean isShellCommand);

	// takes only a string, for trivial cases
	void quickDirtyExec(String argsString, boolean isShellCommand);
	
}