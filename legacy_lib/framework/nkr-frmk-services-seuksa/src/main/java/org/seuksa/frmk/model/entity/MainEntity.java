package org.seuksa.frmk.model.entity;



/**
 * 
 * @author prasnar
 *
 */
public abstract class MainEntity extends EntityA {
	/** */
	private static final long serialVersionUID = 7075597313695322305L;

	/**
	 * Should be overridden if any SubEntities to persist at creation
	 * Ex: addCascadeAtCreation(address);

	 */
	public void setCascadeAtCreation() {
		// By default do nothing
	}

    
}
