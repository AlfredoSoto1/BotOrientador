package java.service.discord.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Alfredo
 */
public class AsyncTaskQueue {
	private final BlockingQueue<Runnable> taskQueue; // Thread-safe queue for storing tasks
    private final ExecutorService executorService;   // Executor service for managing worker threads
    private final AtomicBoolean isProcessing;        // Flag to check if a worker is currently processing the queue

    public AsyncTaskQueue() {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.executorService = Executors.newSingleThreadExecutor();
        this.isProcessing = new AtomicBoolean(false);
    }

    public void addTask(Runnable task) {
        taskQueue.add(task); 
        processQueue(); 
    }

    public void shutdown() {
    	// Gracefully shut down the executor service
    	executorService.shutdown();
    }
    
    private void processQueue() {
    	// Submit a new worker if none is processing
        if (isProcessing.compareAndSet(false, true))
            executorService.submit(new Worker()); 
    }
    
    private class Worker implements Runnable {
        @Override
        public void run() {
            try {
            	// Process tasks until the queue is empty
                while (!taskQueue.isEmpty()) {
                    Runnable task = taskQueue.poll();
                    task.run(); 
                }
            } finally {
            	// Reset the processing flag
                isProcessing.set(false); 
                
                // Restart processing if there are more tasks
                if (!taskQueue.isEmpty())
                    processQueue();
            }
        }
    }
}
