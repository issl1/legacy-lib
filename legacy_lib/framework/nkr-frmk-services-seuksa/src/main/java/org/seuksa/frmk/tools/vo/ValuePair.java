package org.seuksa.frmk.tools.vo;

import java.io.Serializable;

/**
 * General purpose value bean
 * 
 * @author ly.youhort
 */
public class ValuePair implements Serializable {

    private static final long serialVersionUID = 3200246069367058380L;

    private String code;
    private String value;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(final String value) {
        this.value = value;
    }
}