package org.seuksa.frmk.model.entity;


/**
 * The id (primary key) is an user-manually assigned String Code
 * 
 * @author prasnar
 * @version $Revision$
 */
public abstract class EntityS extends AbstractEntity implements IHasAssignedIdS {
    /** */
	private static final long serialVersionUID = -8600500687229545326L;

	protected String code = null;

    /**
     * @return the code
     */
    public abstract String getCode();

    /**
     * @see org.seuksa.frmk.model.entity.IHasAssignedIdS#setCode(java.lang.String)
     */
    public void setCode(String code) {
        this.code = code;
    }


}
