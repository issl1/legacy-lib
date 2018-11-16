package com.nokor.common.app.content.model.eref;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.eref.RefDataEntity;

import com.nokor.common.app.content.model.base.DependencyType;
import com.nokor.frmk.helper.FrmkServicesHelper;

/**
 * List of DependencyType 
 * 
 * @author prasnar
 *
 */
public enum EArticleDependencyType implements RefDataEntity<DependencyType> {
	CHILD_CONTENT (EDependencyType.CHILD_CONTENT);

	private final long id;

	
	/**
	 * 
	 * @param def
	 */
	private EArticleDependencyType(final EDependencyType def) {
		this.id = def.getId();
	}
	
	/**
     * 
     */
	private EArticleDependencyType(final long id) {
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
	public DependencyType getEntity() {
		return FrmkServicesHelper.ENTITY_SRV.getById(DependencyType.class, id);
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}


}