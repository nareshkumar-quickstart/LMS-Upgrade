package com.softech.vu360.lms.util;

public class NullTool {
    public NullTool()
    {
    }

    public boolean isNull(Object object)
    {
        return object == null;
    }
    
    public boolean isNotNull(Object object)
    {
        return !this.isNull(object);
    }
}
