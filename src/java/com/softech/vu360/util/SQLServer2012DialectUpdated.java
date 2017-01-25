package com.softech.vu360.util;

import java.sql.Types;

import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.type.StandardBasicTypes;

public class SQLServer2012DialectUpdated extends SQLServer2012Dialect {

	public SQLServer2012DialectUpdated() {
		super();
	    registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
	}

}
