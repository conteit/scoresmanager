package it.conteit.scoresmanager.control.workers;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

/**
 * This is the 3rd version of SwingWorker (also known as
 * SwingWorker 3), an abstract class that you subclass to
 * perform GUI-related work in a dedicated thread.  For
 * instructions on using this class, see:
 * 
 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 *
 * Note that the API changed slightly in the 3rd version:
 * You must now invoke start() on the SwingWorker after
 * creating it.
 */
public abstract class SwingWorker<T> {
    private T value;  // see getValue(), setValue()
    //private Thread thread;
    private boolean done = false;
    
    private ArrayList<IProgressListener> listeners = new ArrayList<IProgressListener>(); 
    
    /** 
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
    private static class ThreadVar {
        private Thread thread;
        ThreadVar(Thread t) { thread = t; }
        synchronized Thread get() { return thread; }
        synchronized void clear() { thread = null; }
    }

    private ThreadVar threadVar;

    /** 
     * Get the value produced by the worker thread, or null if it 
     * hasn't been constructed yet.
     */
    protected synchronized T getValue() { 
        return value; 
    }

    /** 
     * Set the value produced by worker thread 
     */
    private synchronized void setValue(T x) { 
        value = x; 
    }

    /** 
     * Compute the value to be returned by the <code>get</code> method. 
     */
    public abstract T construct();

    /**
     * Called on the event dispatching thread (not on the worker thread)
     * after the <code>construct</code> method has returned.
     */
    public void finished() {}
    
    public void done(){
    	setCompleted(true);
    }
    
    protected synchronized void setCompleted(boolean done){
    	this.done = done;
    }
    
    public synchronized boolean isCompleted(){
    	return done;
    }

    /**
     * A new method that interrupts the worker thread.  Call this method
     * to force the worker to stop what it's doing.
     */
    public void interrupt() {
    	notifyProgress("Aborting operation", "", -1, false);
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
        notifyProgress("Aborting operation", "", 100, true);
    }
    
    public boolean isCancelled(){
    	Thread t = threadVar.get();
    	if (t != null) {
            return t.isInterrupted();
        }
    	
    	return true;
    }

    /**
     * Return the value created by the <code>construct</code> method.  
     * Returns null if either the constructing thread or the current
     * thread was interrupted before a value was produced.
     * 
     * @return the value created by the <code>construct</code> method
     */
    public T get() {
        while (true) {  
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // propagate
                return null;
            }
        }
    }

    public synchronized void addProgressListener(IProgressListener l){
    	listeners.add(l);
    }

    public synchronized void removeProgressListener(IProgressListener l){
    	listeners.remove(l);
    }
    
    protected synchronized ArrayList<IProgressListener> getListeners(){
    	return listeners;
    }
    
    protected void notifyProgress(final String op, final String currStat, final int progress, final boolean done){
    	SwingUtilities.invokeLater(new Runnable(){

			public void run() {
				for(IProgressListener l : getListeners()){
					l.progressUpdate(op, currStat, progress, done);
		    	}
			}
		});
    }
    
    /**
     * Start a thread that will call the <code>construct</code> method
     * and then exit.
     */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {
           public void run() { finished(); }
        };

        Runnable doConstruct = new Runnable() { 
            public void run() {
                try {
                    setValue(construct());
                    done();
                }
                finally {
                    threadVar.clear();
                }

                SwingUtilities.invokeLater(doFinished);
            }
        };

        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }

    /**
     * Start the worker thread.
     */
    public void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
}