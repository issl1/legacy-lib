package org.seuksa.frmk.model.entity;

/**
 * The PK is a composite of 3 Integer
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class PK3Id extends BasePK {
	/** */
	private static final long serialVersionUID = -5740345916459586692L;

	public abstract Long getId1();
    public abstract Long getId2();
    public abstract Long getId3();
   
	@Override
	public boolean pkEquals(BasePK pk) {
		return pk != null 
		&& getId1().equals(((PK3Id) pk).getId1())
		&& getId2().equals(((PK3Id) pk).getId2())
		&& getId3().equals(((PK3Id) pk).getId3());
	}
	
	@Override
	public int pkHashCode() {
        return (getClass().getName() 
        		+ "|" + getId1() 
        		+ "|" + getId2() 
        		+ "|" + getId3()).hashCode();
	}
}
