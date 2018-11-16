package com.nokor.common.app.content.model.eref;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.eref.RefDataEntity;

import com.nokor.common.app.content.model.base.ContentType;
import com.nokor.frmk.helper.FrmkServicesHelper;

/**
 * List of DocumentType 
 * 
 * @author prasnar
 *
 */
public enum EDocumentType implements RefDataEntity<ContentType> {
	BOOK (1L),
	REPORT (2L),
	LAW (3L),
	DRAFT_LAW (4L);
	
	private final long id;

	/**
     * 
     */
	private EDocumentType(final long id) {
		this.id = id;
	}

	/**
	 * return code
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * return desc
	 */
	@Override
	public String getDesc() {
		return this.getClass().getSimpleName() + "." + I18N.value(getId() + "");
	}
	
	@Override
	public ContentType getEntity() {
		return FrmkServicesHelper.ENTITY_SRV.getById(ContentType.class, id);
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}
}