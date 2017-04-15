package com.enrico200165.utils.ext_proc.multi_thread;

/* Don't know if it's a real semaphore
 * used as a semaphore to make stream runnables wait or read the output 
 * of the command 
 * */
public class CanGo {

	private boolean canGo = false;

	public CanGo(String flagNamePar) {
		super();
		this.flagName = flagNamePar;
	}

	public synchronized boolean isGetIt() {
		return canGo;
	}

	public synchronized void setCanGo(boolean goPar) {
		if (goPar)
			System.err.println("can go: "+this.flagName);
		else
			System.err.println("can NOT go (wait): "+this.flagName);
		this.canGo = goPar;
		notifyAll();
	}

	String flagName;
}
