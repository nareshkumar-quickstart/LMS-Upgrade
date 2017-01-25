package com.softech.vu360.lms.autoAlertGenerator.queue.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.softech.vu360.lms.autoAlertGenerator.logging.Logger;

/**
 * User: ramiz.uddin
 * Date: 10/31/12
 * Time: 3:35 PM
 */
public abstract class Executor<T> {

    protected ExecutorService executor;
    private Set<Future<T>> set = new HashSet<Future<T>>();
    
    protected Executor() {

        int scaleFactor = 1;
        int cpu = Runtime.getRuntime().availableProcessors();
        int maxThreads = cpu * scaleFactor;
        maxThreads = (maxThreads > 0 ? maxThreads : 1);

        // Using a ThreadPoolExecutor to Parallelize Independent Single-Threaded Tasks
        // javacodegeeks.com/2011/12/using-threadpoolexecutor-to-parallelize.html
		//      int scaleFactor = 1;
		//      int cpu = Runtime.getRuntime().availableProcessors();
		//      int maxThreads = cpu * scaleFactor;
		//      maxThreads = (maxThreads > 0 ? maxThreads : 1);
		
		      // Using a ThreadPoolExecutor to Parallelize Independent Single-Threaded Tasks
		      // javacodegeeks.com/2011/12/using-threadpoolexecutor-to-parallelize.html
		      executor = Executors.newSingleThreadExecutor();
  }
    


    protected void submit(Callable<T> callable) {
        Future<T> future =  executor.submit(callable);
        set.add(future);

        for (Future<T> each : set) {

            T completedTrigger = null;
            try {
                completedTrigger = each.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Logger.write("Trigger completed.");

        }

    }

}
