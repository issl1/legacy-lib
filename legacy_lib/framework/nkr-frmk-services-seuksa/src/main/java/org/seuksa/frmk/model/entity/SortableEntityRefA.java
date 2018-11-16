package org.seuksa.frmk.model.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * This class is a sortable table reference
 * 
 * @author prasnar
 * @version $Revision$
 */
@MappedSuperclass
public abstract class SortableEntityRefA extends EntityRefA {
	/** */
	private static final long serialVersionUID = -6633727155644108297L;
	
	protected int sortKey;

    /**
     * @return the sortKey
     */
    @Column(name = "SORT_KEY", nullable = true)
    public int getSortKey() {
        return sortKey;
    }

    /**
     * @param sortKey the sortKey to set
     */
    public void setSortKey(int sortKey) {
        this.sortKey = sortKey;
    }

}
