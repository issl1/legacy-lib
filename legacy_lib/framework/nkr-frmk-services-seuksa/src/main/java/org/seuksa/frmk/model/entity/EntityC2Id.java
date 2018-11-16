package org.seuksa.frmk.model.entity;

import javax.persistence.Id;


/**
 * Entity with a composite PK of 2 FKs, auto-generated Integer
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class EntityC2Id extends AbstractEntity {
    /** */
	private static final long serialVersionUID = -3282754568641421798L;

	protected PK2Id pkId = null;

    /**
     * 
     * @return
     */
    @Id
    public abstract PK2Id getPkId();
    
    /**
     * 
     * @param pkId
     */
    public void setPkId(PK2Id pkId) {
		this.pkId = pkId;
	}

   
}
