package org.seuksa.frmk.model.entity;

import java.io.Serializable;

/**
 * Data id
 * 
 * @author prasnar
 * 
 */
public interface MyDataId extends Serializable, Cloneable {
    Long getId();
    
    String getCode();
    
    String getDesc();
}
