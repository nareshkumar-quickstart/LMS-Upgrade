package com.softech.vu360.lms.web.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.softech.vu360.lms.batchImport.BatchImportConnectionManager;

public class SpringContextListener implements ApplicationListener  {

    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ContextRefreshedEvent) {
            ContextRefreshedEvent cxt = (ContextRefreshedEvent)event;
            BatchImportConnectionManager bicm = (BatchImportConnectionManager)cxt.getApplicationContext().getBean("batchImportConnectionManager");
            bicm.startListening();
        }
    }
    
}
