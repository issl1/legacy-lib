package org.seuksa.frmk.model.entity;



/**
 * Entity with a composite PK of 3 FKs, auto-generated Integer
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class EntityC3Id extends AbstractEntity {
    /** */
	private static final long serialVersionUID = 4423570269273994572L;

	protected PK3Id pkId = null;

    /**
     * 
     * @return
     */
    public abstract PK3Id getPkId();
    
    /**
     * 
     * @param pkId
     */
    public void setPkId(PK3Id pkId) {
		this.pkId = pkId;
	}

}
