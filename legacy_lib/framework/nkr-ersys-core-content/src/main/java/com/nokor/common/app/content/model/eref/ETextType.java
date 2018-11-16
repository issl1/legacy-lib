package com.nokor.common.app.content.model.eref;

import java.util.Arrays;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.eref.RefDataEntity;

import com.nokor.common.app.content.model.base.ContentType;
import com.nokor.frmk.helper.FrmkServicesHelper;

/**
 * List of ContentType 
 * 
 * @author prasnar
 *
 */
public enum ETextType implements RefDataEntity<ContentType> {
	ARTICLE (1L),
	HEADER (2L),
	FOOTER (3L),
	CHAPTER (4L),
	SECTION (5L);

	public static final List<Long> HEADER_FOOTER_ID_LIST = Arrays.asList(HEADER.getId(), FOOTER.getId());

	private final long id;

	/**
     * 
     */
	private ETextType(final long id) {
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