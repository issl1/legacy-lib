package org.seuksa.frmk.model.entity;


/**
 * The PK is a composite of 2 EntityA
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class PK2A extends BasePK {
	/** */
	private static final long serialVersionUID = -6394240913393326186L;

	public abstract EntityA getEntity1();
    public abstract EntityA getEntity2();
	public abstract void setEntity1(EntityA entity1);
	public abstract void setEntity2(EntityA entity2);
   
	@Override
	public boolean pkEquals(BasePK pk) {
		return pk != null 
		&& getEntity1().equals(((PK2A) pk).getEntity1())
		&& getEntity2().equals(((PK2A) pk).getEntity2());
	}
	
	@Override
	public int pkHashCode() {
        return (getClass().getName() 
        		+ "|" + getEntity1() 
        		+ "|" + getEntity2()).hashCode();
	}
}
