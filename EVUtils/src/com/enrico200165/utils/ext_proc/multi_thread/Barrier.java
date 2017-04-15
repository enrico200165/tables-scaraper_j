package com.enrico200165.utils.ext_proc.multi_thread;

/**
 * A very simple barrier wait. Once a thread has requested a wait on the barrier
 * with waitForRelease, it cannot fool the barrier into releasing by "hitting"
 * the barrier multiple times-- the thread is blocked on the wait().
 */
public class Barrier {

	public Barrier(int nrWaitedThreadsPar, CanGo getOutputs) {
		this.nrWaitedThreads = nrWaitedThreadsPar;
		this.getOutputs = getOutputs;
	}

	synchronized public void reset() {
		nrThreadsWaiting = 0;
	}

	public synchronized void updateBarrier(String id /* non usato */) throws InterruptedException {
		String threadName = "thread name: " + Thread.currentThread();
		nrThreadsWaiting++;
		// System.out.println("nrThreadsWaiting updated to "+nrThreadsWaiting+
		// " by "+threadName);

		// The final thread to reach barrier resets barrier and releases all
		// threads
		if (nrThreadsWaiting == this.nrWaitedThreads) {
			System.err.println("\nbarrier " + " opened by: " + threadName);
			// reset(); don't do it, makes the waiting thread remain trapped in
			// the loop
			// getOutputs.setGetIt(false);
			notifyAll();
		} else { // wait other threads
			while (nrThreadsWaiting < this.nrWaitedThreads) {
				System.err.println(threadName + " starts WAIT on barrier");
				this.wait();
			}
		}
		System.out.println(threadName + " exited barrier");
	}

	public synchronized boolean completed() {
		return (nrThreadsWaiting == this.nrWaitedThreads);
	}

	protected CanGo getOutputs;
	protected int nrWaitedThreads;
	protected int nrThreadsWaiting = 0;
}
