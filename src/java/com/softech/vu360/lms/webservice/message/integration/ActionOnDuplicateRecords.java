
package com.softech.vu360.lms.webservice.message.integration;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActionOnDuplicateRecords.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActionOnDuplicateRecords">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Update"/>
 *     &lt;enumeration value="Ignore"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActionOnDuplicateRecords")
@XmlEnum
public enum ActionOnDuplicateRecords {

    @XmlEnumValue("Update")
    UPDATE("Update"),
    @XmlEnumValue("Ignore")
    IGNORE("Ignore");
    private final String value;

    ActionOnDuplicateRecords(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ActionOnDuplicateRecords fromValue(String v) {
        for (ActionOnDuplicateRecords c: ActionOnDuplicateRecords.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
