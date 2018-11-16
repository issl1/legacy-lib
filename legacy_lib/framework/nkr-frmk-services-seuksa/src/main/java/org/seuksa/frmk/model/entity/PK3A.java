package org.seuksa.frmk.model.entity;


/**
 * The PK is a composite of 3 EntityA
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class PK3A extends BasePK {
	/** */
	private static final long serialVersionUID = -5957100469537267450L;

	public abstract EntityA getEntity1();
    public abstract EntityA getEntity2();
    public abstract EntityA getEntity3();
   
	@Override
	public boolean pkEquals(BasePK pk) {
		return pk != null 
		&& getEntity1().equals(((PK3A) pk).getEntity1())
		&& getEntity2().equals(((PK3A) pk).getEntity2())
		&& getEntity3().equals(((PK3A) pk).getEntity3());
	}
	
	@Override
	public int pkHashCode() {
        return (getClass().getName() 
        		+ "|" + getEntity1() 
        		+ "|" + getEntity2()
        		+ "|" + getEntity3()).hashCode();
	}
}
