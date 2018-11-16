package com.nokor.common.app.content.model.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

@Entity
@Table(name="ts_cms_content_type")
public class ContentType extends EntityRefA {
	/** */
	private static final long serialVersionUID = 8729208434007123543L;

	private ContentType parent;
    private List<ContentType> children = new ArrayList<ContentType>();

	/**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cnt_typ_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "cnt_typ_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "cnt_typ_desc", nullable = false, length = 255)
	@Override
    public String getDesc() {
        return desc;
    }

	/**
	 * @return the parent
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_cnt_typ_id", nullable = true)
	public ContentType getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(ContentType parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	@OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
	public List<ContentType> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<ContentType> children) {
		this.children = children;
	}


}
