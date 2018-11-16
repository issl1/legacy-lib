package org.seuksa.frmk.model.meta;

import java.io.Serializable;


/**
 * 
 * @author ly.youhort
 *
 */
public class NativeColumn implements Serializable {

    private static final long serialVersionUID = 891635860454263206L;

    private String name;
    private FieldType type;
    private Serializable value;

    /**
     * 
     */
    public NativeColumn() {
        super();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public FieldType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(final FieldType type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    public Serializable getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(final Serializable value) {
        this.value = value;
    }

}
