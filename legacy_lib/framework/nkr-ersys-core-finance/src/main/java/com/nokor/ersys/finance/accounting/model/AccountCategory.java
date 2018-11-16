package com.nokor.ersys.finance.accounting.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_account_category")
public class AccountCategory extends EntityRefA implements MAccountCategory {
	/** */
	private static final long serialVersionUID = -68202306425175922L;

	private String name;
	private String nameEn;
	private OrganizationCategory orgCategory;
	private AccountCategory parent;
    private ECategoryRoot root;
	private String comment;
	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acc_cat_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "acc_cat_code", nullable = false)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "acc_cat_desc", nullable = true, length = 100)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "acc_cat_desc_en", nullable = true, length = 100)
	@Override
    public String getDescEn() {
        return descEn;
    }


	/**
	 * @return the name
	 */
    @Column(name="acc_cat_name", nullable = true)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the nameEn
	 */
    @Column(name="acc_cat_name_en", nullable = true)
	public String getNameEn() {
		return nameEn;
	}

	/**
	 * @param nameEn the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	
	/**
	 * @return
	 */
	@Transient
	public String getNameLocale() {
		if (I18N.isEnglishLocale()) {
			return getNameEn();
		} else {
			return getName();
		}
	}
	
	/**
	 * @return the root
	 */
    @Column(name="root_acc_cat_id", nullable = false)
    @Convert(converter = ECategoryRoot.class)
	public ECategoryRoot getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(ECategoryRoot root) {
		this.root = root;
	}

	/**
	 * @return the orgCategory
	 */
//    @ManyToOne
//    @JoinColumn(name="org_acc_cat_id", nullable = false)
	@OneToOne(mappedBy="category", fetch = FetchType.LAZY)
	public OrganizationCategory getOrgCategory() {
		return orgCategory;
	}

	/**
	 * @param orgCategory the orgCategory to set
	 */
	public void setOrgCategory(OrganizationCategory orgCategory) {
		this.orgCategory = orgCategory;
	}

	/**
	 * @return the parent
	 */
    @ManyToOne
    @JoinColumn(name="parent_acc_cat_id", nullable = true)
	public AccountCategory getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(AccountCategory parent) {
		this.parent = parent;
	}
	
	
	/**
	 * @return the comment
	 */
	@Column(name = "acc_cat_comment", nullable = true)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}


}
