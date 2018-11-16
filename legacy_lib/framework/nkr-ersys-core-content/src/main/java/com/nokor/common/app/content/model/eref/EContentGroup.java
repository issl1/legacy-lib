package com.nokor.common.app.content.model.eref;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.eref.RefDataEntity;

import com.nokor.common.app.content.model.base.ContentGroup;
import com.nokor.frmk.helper.FrmkServicesHelper;

/**
 * 
 * @author keomorakort.man
 *
 */
public enum EContentGroup implements RefDataEntity<ContentGroup> {
	NORMAL(1L),
	MENU(2L), 
	LINK(3L), 
	FOOTER(4L), 
	PARTNER(5L);

	private final long id;

	private EContentGroup(final long id) {
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
		return I18N.value(this.getClass().getSimpleName() + "." + getId() + "");
	}

	@Override
	public ContentGroup getEntity() {
		return FrmkServicesHelper.ENTITY_SRV.getById(ContentGroup.class, id);
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
