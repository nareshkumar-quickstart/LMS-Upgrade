package com.softech.vu360.lms.model;

import java.io.Serializable;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.enhanced.TableGenerator;

public class PrimaryKeyGenerator extends TableGenerator {

    @Override
    public synchronized Serializable generate(SessionImplementor session, Object obj){
    //public Serializable generate(final SharedSessionContractImplementor session, final Object obj){
        return (Long) super.generate(session, obj) + 1;
    }
}