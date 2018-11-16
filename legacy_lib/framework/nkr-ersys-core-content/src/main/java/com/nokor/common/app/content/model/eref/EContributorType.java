package com.nokor.common.app.content.model.eref;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.eref.RefDataEntity;

import com.nokor.common.app.content.model.base.ContributorType;
import com.nokor.frmk.helper.FrmkServicesHelper;

/**
 * 
 * @author prasnar
 *
 */
public enum EContributorType implements RefDataEntity<ContributorType> {
	AUTHOR(1),
	PRINTING(2),
	PUBLICATION(3),
	SPONSORING(4),
	DATA_ENTRY(5),
	VERIFICATION(6),
	VALIDATION(7),
	ADOPTION(8);

	
	private final long id;

	/**
     * 
     */
	private EContributorType(final long id) {
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
	public ContributorType getEntity() {
		return FrmkServicesHelper.ENTITY_SRV.getById(ContributorType.class, id);
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}		
}