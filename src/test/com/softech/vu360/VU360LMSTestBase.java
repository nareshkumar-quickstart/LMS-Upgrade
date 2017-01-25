package com.softech.vu360;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public abstract class VU360LMSTestBase extends AbstractJUnit4SpringContextTests {
    public VU360LMSTestBase() {
        super();
    }
}
