package com.softech.vu360.lms.autoAlertGenerator.queue.events;

import java.util.EventObject;

import com.softech.vu360.lms.model.AlertQueue;

/**
 * @author ramiz.uddin
 * @version 10/19/12 3:56 PM
 */

/*
* An event object for Queue
*
* */
public class QueueEvent extends EventObject {

    private AlertQueue _queue;

    public QueueEvent(Object source, AlertQueue queue) {

        super( source );
        _queue = queue;

    }

    public AlertQueue getQueue() {
        return _queue;
    }

}
