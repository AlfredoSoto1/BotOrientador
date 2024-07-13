/*
 * Copyright 2024 Alfredo Soto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package assistant.discord.core;

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
