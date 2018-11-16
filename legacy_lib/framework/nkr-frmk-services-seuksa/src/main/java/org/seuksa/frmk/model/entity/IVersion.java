package org.seuksa.frmk.model.entity;

/**
 * Shall implements Hibernate version annotation
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface IVersion {
	Long getVersion();
	void setVersion(Long version);
}
