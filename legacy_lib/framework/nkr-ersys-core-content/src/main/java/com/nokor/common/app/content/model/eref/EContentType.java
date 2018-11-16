package com.nokor.common.app.content.model.eref;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.eref.RefDataEntity;

import com.nokor.common.app.content.model.base.ContentType;
import com.nokor.frmk.helper.FrmkServicesHelper;

/**
 * List of ContentType
 * - Document: Book, Law, Draft Law, Report...
 * - Text: Normal, Header, Footer, 
 * - Comment: Comment, Annotation, 
 * - Article: Default
 * 
 * @author prasnar
 *
 */
public enum EContentType implements RefDataEntity<ContentType> {
	DEFAULT(1);

	private final long id;

	/**
     * 
     */
	private EContentType(final long id) {
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
	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ContentType getEntity() {
		return FrmkServicesHelper.ENTITY_SRV.getById(ContentType.class, id);
	}
	
	/**
	 * 
	 * @return
	 */
	public static ContentType getDefault() {
		return DEFAULT.getEntity();
	}
}