package com.nokor.common.app.content.model.eref;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.eref.RefDataEntity;

import com.nokor.common.app.content.model.base.ContentCategory;
import com.nokor.frmk.helper.FrmkServicesHelper;

/**
 * List of ContentCategory
 * 
 * @author prasnar
 *
 */
public enum EContentCategoryDefault implements RefDataEntity<ContentCategory> {
	DEFAULT(1);
	
	private final long id;

	/**
     * 
     */
	private EContentCategoryDefault(final long id) {
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
	public ContentCategory getEntity() {
		return FrmkServicesHelper.ENTITY_SRV.getById(ContentCategory.class, id);
	}
	
	/**
	 * 
	 * @return
	 */
	public static ContentCategory getDefault() {
		return DEFAULT.getEntity();
	}

}