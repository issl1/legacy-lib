package org.seuksa.frmk.model.entity;

import java.util.List;

/**
 * Abstract entity
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class AbstractEntity implements Entity {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Update only the defined parts of the Entity Is used for example for Actor
     * Its has Address, BankAccount, ActorDiploma, ...
     */
    public List<AbstractEntity> partsToUpdate;


    /**
     * 
     * @return
     */
    private boolean isTransient() {
        return getId() == null;
    }

    /**
     * 
     * @param compareTo
     * @return
     */
    private boolean hasSameBusinessSignatureAs(final AbstractEntity compareTo) {
        return hashCode() == compareTo.hashCode();
    }

    /**
     * 
     * @param compareTo
     * @return
     */
    private boolean hasSameNonDefaultIdAs(final AbstractEntity compareTo) {
        return getId() != null && getId().equals(compareTo.getId());
    }

    /**
     * @return the partsToUpdate
     */
    public List<AbstractEntity> getPartsToUpdate() {
        return partsToUpdate;
    }

    /**
     * @param partsToUpdate
     *            the partsToUpdate to set
     */
    public void setPartsToUpdate(final List<AbstractEntity> partsToUpdate) {
        this.partsToUpdate = partsToUpdate;
    }

    /**
     * 
     * @param clazz
     * @return
     */
    public boolean needToUpdate(final Class clazz) {
        return getPartsToUpdate() != null && getPartsToUpdate().contains(clazz);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        AbstractEntity compareTo = (AbstractEntity) obj;

        return compareTo != null && (hasSameNonDefaultIdAs(compareTo) ||
        // Since the IDs aren't the same, 
        // either of them must be transient to
        // compare business value signatures
                ((isTransient() || compareTo.isTransient()) && hasSameBusinessSignatureAs(compareTo)));
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (getClass().getName() + "|" + getHashCodeSuffix()).hashCode();
    }

    /**
     * 
     * @return
     */
    public int getHashCodeSuffix() {
        if (getId() != null)
            return getId().hashCode();

        return 0;
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
