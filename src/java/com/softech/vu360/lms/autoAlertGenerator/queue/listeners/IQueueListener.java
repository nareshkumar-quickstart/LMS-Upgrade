package com.softech.vu360.lms.autoAlertGenerator.queue.listeners;

import com.softech.vu360.lms.autoAlertGenerator.queue.events.QueueEvent;

/**
 * An interface for Queue Listeners
 *
 * @author ramiz.uddin
 * @version 10/19/12 3:56 PM
 */

public interface IQueueListener {

    public void BeforeQueueing(QueueEvent queueEvent);
    public void Saving(QueueEvent queueEvent);

}

