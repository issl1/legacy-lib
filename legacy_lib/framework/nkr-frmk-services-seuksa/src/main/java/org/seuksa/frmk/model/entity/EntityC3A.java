package org.seuksa.frmk.model.entity;

import javax.persistence.EmbeddedId;


/**
 * Entity with a composite PK of 3 FKs, EntityA
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class EntityC3A extends AbstractEntity {
    /** */
	private static final long serialVersionUID = 7518557171007538225L;

	protected PK3A pkId = null;

    /**
     * 
     * @return
     */
	@EmbeddedId
    public abstract PK3A getPkId();
    
    /**
     * 
     * @param pkId
     */
    public void setPkId(PK3A pkId) {
		this.pkId = pkId;
	}


}
