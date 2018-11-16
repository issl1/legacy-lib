package com.nokor.common.app.content.service.doc;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.common.app.content.model.doc.DocDependency;

/**
 * 
 * @author keomorakort.man
 * 
 */
public class DocDependencyRestriction extends BaseRestrictions<DocDependency> {
	private static final String AS_DOC_CHILD = "child";
	private static final String AS_DOC_PARENT = "parent";

	private Long docChildId;
	private Long docParentId;

	public DocDependencyRestriction() {
		super(DocDependency.class);
	}

	@Override
	public void preBuildAssociation() {
		addAssociation("doc", AS_DOC_CHILD, JoinType.LEFT_OUTER_JOIN);
		addAssociation("other", AS_DOC_PARENT, JoinType.LEFT_OUTER_JOIN);
	}

	@Override
	public void preBuildSpecificCriteria() {
		if (docChildId != null) {
			addCriterion(Restrictions.eq(AS_DOC_CHILD + ".id", docChildId));
		}

		if (docParentId != null) {
			addCriterion(Restrictions.eq(AS_DOC_PARENT + ".id", docParentId));
		}
	}

	/**
	 * @return the docChildId
	 */
	public Long getDocChildId() {
		return docChildId;
	}

	/**
	 * @param docChildId
	 *            the docChildId to set
	 */
	public void setDocChildId(Long docChildId) {
		this.docChildId = docChildId;
	}

	/**
	 * @return the docParentId
	 */
	public Long getDocParentId() {
		return docParentId;
	}

	/**
	 * @param docParentId
	 *            the docParentId to set
	 */
	public void setDocParentId(Long docParentId) {
		this.docParentId = docParentId;
	}
}
