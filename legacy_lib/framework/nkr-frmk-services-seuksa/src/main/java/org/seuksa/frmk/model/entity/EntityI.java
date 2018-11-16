package org.seuksa.frmk.model.entity;

/**
 * The id is a manual-generated Integer Id (primary key)
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class EntityI extends EntityA implements IHasAssignedIdI {
    /** */
	private static final long serialVersionUID = 6239205231059026185L;

	/**
     * 
     * @param id
     */
    public EntityI(Long id) {
        this.id = id;
    }
}
