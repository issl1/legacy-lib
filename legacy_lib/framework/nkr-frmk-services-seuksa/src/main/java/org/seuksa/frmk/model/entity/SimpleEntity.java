package org.seuksa.frmk.model.entity;

import javax.persistence.Transient;

/**
 * 
 * @author prasnar
 *
 */
public abstract class SimpleEntity extends AbstractEntity implements IHasAssignedIdI {
    /** */
	private static final long serialVersionUID = 2708276849265797816L;
	
	protected Long id = null;

    /**
     * @return the id
     */
    @Transient
    public abstract Long getId();
    
    @Override
    public void setId(final Long id) {
        this.id = id;
    }
    
	
}
