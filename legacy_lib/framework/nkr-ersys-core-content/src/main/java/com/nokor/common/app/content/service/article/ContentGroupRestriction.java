package com.nokor.common.app.content.service.article;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.content.model.base.ContentGroup;

/**
 * 
 * @author prasnar
 *
 */
public class ContentGroupRestriction extends BaseRestrictions<ContentGroup> {
	/** */
	private static final long serialVersionUID = -4129945552958435031L;

	private Long grpArtId;
	private String desc;
	private Long parentGrpArtId;

	/**
     *
     */
	public ContentGroupRestriction() {
		super(ContentGroup.class);
	}

	@Override
	public void preBuildAssociation() {
		addAssociation("parent", "par", JoinType.LEFT_OUTER_JOIN);
	}

	@Override
	public void preBuildSpecificCriteria() {
		if (StringUtils.isNotEmpty(desc)) {
			addCriterion(Restrictions.ilike("desc", desc+"%"));
		}

		if (grpArtId != null && grpArtId > 0) {
			addCriterion(Restrictions.eq("id", grpArtId));
		}

		if (parentGrpArtId != null && parentGrpArtId > 0) {
			addCriterion(Restrictions.eq("par.id", parentGrpArtId));
		}

	}

	/**
	 * @return the grpArtId
	 */
	public Long getGrpArtId() {
		return grpArtId;
	}

	/**
	 * @param grpArtId
	 *            the grpArtId to set
	 */
	public void setGrpArtId(Long grpArtId) {
		this.grpArtId = grpArtId;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the parentGrpArtId
	 */
	public Long getParentGrpArtId() {
		return parentGrpArtId;
	}

	/**
	 * @param parentGrpArtId
	 *            the parentGrpArtId to set
	 */
	public void setParentGrpArtId(Long parentGrpArtId) {
		this.parentGrpArtId = parentGrpArtId;
	}

}
