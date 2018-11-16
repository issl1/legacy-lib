package com.nokor.ersys.collab.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.eref.ELanguage;
import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.ersys.core.hr.model.organization.Employee;
import com.nokor.frmk.helper.SeuksaServicesHelper;

/**
 * 
 * @author prasnar
 *
 */
@MappedSuperclass
public abstract class BaseTask extends EntityWkf implements MTask {
	/** */
	private static final long serialVersionUID = -5045559914591113827L;

	private String code;
	private String desc;
	private String descEn;	
	private Project project;
	private Employee reporter;
	private Employee assignee;
	private TaskClassification classification; 	// Domain/SubDomain/../Component
	private ETaskType type;  					// Task/Bug/Enhancement/Todo..
	private ETaskPriority priority;				// P1, P2, P3, P4.. (for delivery)
	private ETaskSeverity severity;				// Blocker, Major, Normal, Minor (real aspect in the development/team)
	private Date startDate;
	private Date endDate;
	private String comment;
	private String keywords;
	private String keywordsEn;

	/**
     * 
     * @return
     */
    public static BaseTask createInstance() {
    	BaseTask tas = EntityFactory.createInstance(BaseTask.class);
    	tas.setPriority(ETaskPriority.P2);
    	tas.setSeverity(ETaskSeverity.NORMAL);
    	tas.setClassification(SeuksaServicesHelper.ENTITY_SRV.getById(TaskClassification.class, TaskClassification.DEFAULT_DOMAIN_ID));
        return tas;
    }


	@Column(name = "tas_code", nullable = true, length = 10)
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "tas_desc", nullable = true)
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "tas_desc_en", nullable = true)
	public String getDescEn() {
		return descEn;
	}

	/**
	 * @param descEn the descEn to set
	 */
	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

	/**
	 * @return the project
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="prj_id", nullable = true)
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the reporter
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reporter_emp_id", nullable = true)
	public Employee getReporter() {
		return reporter;
	}

	/**
	 * @param reporter the reporter to set
	 */
	public void setReporter(Employee reporter) {
		this.reporter = reporter;
	}

	/**
	 * @return the assignee
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="assignee_emp_id", nullable = true)
	public Employee getAssignee() {
		return assignee;
	}

	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(Employee assignee) {
		this.assignee = assignee;
	}


	/**
	 * @return the classification
	 */
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="tas_cla_id", nullable = true)
	public TaskClassification getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(TaskClassification classification) {
		this.classification = classification;
	}

	/**
	 * @return the keywords
	 */
	@Column(name = "tas_keywords", nullable = true)
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
	@Column(name = "tas_keywords_en", nullable = true)
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
	 * @return the type
	 */
    @Column(name = "tas_typ_id", nullable = false)
    @Convert(converter = ETaskType.class)
	public ETaskType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ETaskType type) {
		this.type = type;
	}


	/**
	 * @return the priority
	 */
    @Column(name = "tas_pri_id", nullable = true)
    @Convert(converter = ETaskPriority.class)
	public ETaskPriority getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(ETaskPriority priority) {
		this.priority = priority;
	}

	/**
	 * @return the severity
	 */
    @Column(name = "tas_sev_id", nullable = true)
    @Convert(converter = ETaskSeverity.class)
	public ETaskSeverity getSeverity() {
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(ETaskSeverity severity) {
		this.severity = severity;
	}

	/**
	 * @return the startDate
	 */
	@Column(name = "tas_start_date", nullable = true)
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
	@Column(name = "tas_end_date", nullable = true)
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
	 * @return the comment
	 */
	@Column(name = "tas_comment", nullable = true, length = 255)
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
