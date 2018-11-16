package org.seuksa.frmk.model.entity;


/**
 * The PK is a composite of 2 Integer
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class PK2Id extends BasePK {
	/** */
	private static final long serialVersionUID = -5955779885119831056L;

	public abstract Long getId1();
    public abstract Long getId2();
   
	@Override
	public boolean pkEquals(BasePK pk) {
		return pk != null 
		&& getId1().equals(((PK2Id) pk).getId1())
		&& getId2().equals(((PK2Id) pk).getId2());
	}
	
	@Override
	public int pkHashCode() {
        return (getClass().getName() 
        		+ "|" + getId1() 
        		+ "|" + getId2()).hashCode();
	}
}
