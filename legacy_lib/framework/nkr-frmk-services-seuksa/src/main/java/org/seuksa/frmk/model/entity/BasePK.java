package org.seuksa.frmk.model.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * 
 * @author prasnar
 * @version $Revision$
 */
@Embeddable
public abstract class BasePK implements Serializable {
	/** */
	private static final long serialVersionUID = -8975678855560770635L;

	public abstract boolean pkEquals(BasePK pk);
	public abstract int pkHashCode();
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof BasePK && pkEquals((BasePK) o);
	}
	
	/**
     * @see java.lang.Object#hashCode()
     */
	@Override
    public int hashCode() {
        return pkHashCode();
    }
}

	