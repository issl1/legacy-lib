package com.nokor.ersys.collab.project.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.eref.ELanguage;
import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.partner.model.Partner;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_project")
public class Project extends EntityWkf implements MProject {
	/** */
	private static final long serialVersionUID = -5045559914591113827L;

    protected String code;
    protected String desc;
    protected String descEn;
	private EProjectType type;
	private EProjectCategory category;
	private Date startDate;
	private Date endDate;
	private String comment;
	private OrgStructure orgStructureOwner;
	private String keywords;
	private String keywordsEn;
	private ETaskTemplateCategory taskTemplateCategory;
	private Partner partner;

	private List<ProjectAssignee> assignees;
	

    /**
     * Create Instance
     * @return
     */
    public static Project createInstance() {
    	Project project = EntityFactory.createInstance(Project.class);
    	project.setType(EProjectType.INTERNAL);
		return project;
	}

	
	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prj_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
   
    /**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "prj_code", nullable = true, length = 10)
	public String getCode() {
		return code;
	}

    /**
     * @param code the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

 	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "prj_desc", nullable = false, length = 10)
    public String getDesc() {
        return desc;
    }
	
    /**
     * @param desc the desc to set
     */
    public void setDesc(final String desc) {
        this.desc = desc;
    }

	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "prj_desc_en", nullable = false, length = 100)
	public String getDescEn() {
		return descEn;
	}
	
    /**
     * @param desc the descEn to set
     */
    public void setDescEn(final String descEn) {
        this.descEn = descEn;
    }
    

	/**
	 * @return the type
	 */
    @Column(name = "prj_typ_id", nullable = false)
    @Convert(converter = EProjectType.class)
	public EProjectType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EProjectType type) {
		this.type = type;
	}

	/**
	 * @return the category
	 */
    @Column(name = "prj_cat_id", nullable = true)
    @Convert(converter = EProjectCategory.class)
	public EProjectCategory getCategory() {
		return category;
	}


	/**
	 * @param category the category to set
	 */
	public void setCategory(EProjectCategory category) {
		this.category = category;
	}


	/**
	 * @return the startDate
	 */
	@Column(name = "prj_start_date", nullable = true)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Column(name = "prj_end_date", nullable = true)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the keywords
	 */
	@Column(name = "cnt_keywords", nullable = true, columnDefinition = "TEXT")
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 *            the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}


	/**
	 * @return the keywordsEn
	 */
	@Column(name = "tas_keywords_en", nullable = true, columnDefinition = "TEXT")
	public String getKeywordsEn() {
		return keywordsEn;
	}

	/**
	 * @param keywordsEn
	 *            the keywordsEn to set
	 */
	public void setKeywordsEn(String keywordsEn) {
		this.keywordsEn = keywordsEn;
	}

	@Transient
	public String getKeywords(ELanguage lang) {
		if (lang == ELanguage.ENGLISH) {
			return keywordsEn;
		} else{
			return keywords;
		}
	}
	
	
	/**
	 * @return the taskTemplateCategory
	 */
    @Column(name = "tas_tmp_cat_id", nullable = true)
    @Convert(converter = EProjectCategory.class)
	public ETaskTemplateCategory getTaskTemplateCategory() {
		return taskTemplateCategory;
	}

	/**
	 * @param taskTemplateCategory the taskTemplateCategory to set
	 */
	public void setTaskTemplateCategory(ETaskTemplateCategory taskTemplateCategory) {
		this.taskTemplateCategory = taskTemplateCategory;
	}


	/**
	 * @return the partner
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="par_id", nullable = true)
	public Partner getPartner() {
		return partner;
	}


	/**
	 * @param partner the partner to set
	 */
	public void setPartner(Partner partner) {
		this.partner = partner;
	}


	/**
	 * @return the orgStructureOwner
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_org_str_id", nullable = true)
	public OrgStructure getOrgStructureOwner() {
		return orgStructureOwner;
	}


	/**
	 * @param orgStructureOwner the orgStructureOwner to set
	 */
	public void setOrgStructureOwner(OrgStructure orgStructureOwner) {
		this.orgStructureOwner = orgStructureOwner;
	}


	/**
	 * @return the comment
	 */
	@Column(name = "prj_comment", nullable = true, length = 255)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the assignees
	 */
	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
	public List<ProjectAssignee> getAssignees() {
		return assignees;
	}

	/**
	 * @param assignees the assignees to set
	 */
	public void setAssignees(List<ProjectAssignee> assignees) {
		this.assignees = assignees;
	}



    
}
