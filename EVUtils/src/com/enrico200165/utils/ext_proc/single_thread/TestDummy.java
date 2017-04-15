package com.enrico200165.utils.ext_proc.single_thread;

public class TestDummy {

	public static void main(String[] extArgs) {
		
		ExecutorProcessBuilder e = new ExecutorProcessBuilder();
		
		String[] args = {"dir"};		
		e.exec(args, true);
		e.quickDirtyExec("ssh enrico@kissako.it -p 7822 'ls' ", true);
	}
	
}
