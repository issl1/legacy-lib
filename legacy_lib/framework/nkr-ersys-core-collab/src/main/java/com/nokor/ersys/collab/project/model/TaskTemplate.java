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

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.action.model.ActionDefinition;
import com.nokor.frmk.helper.SeuksaServicesHelper;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_task_template")
public class TaskTemplate extends BaseTask implements MTask {
	/** */
	private static final long serialVersionUID = 7621269973233363602L;
	
	private ETaskTemplateCategory category;

	private Date estimatedEndDate;
	private Float estimatedDuration;
	private Date deadline;

	private TaskTemplate parent;
	private ActionDefinition action;
	
	private List<TaskTemplate> children;
	private List<TaskTemplateRecipient> recipients;
	
	
	/**
     * 
     * @return
     */
    public static TaskTemplate createInstance() {
    	TaskTemplate tas = EntityFactory.createInstance(TaskTemplate.class);
    	tas.setPriority(ETaskPriority.P2);
    	tas.setSeverity(ETaskSeverity.NORMAL);
    	tas.setClassification(SeuksaServicesHelper.ENTITY_SRV.getById(TaskClassification.class, TaskClassification.DEFAULT_DOMAIN_ID));
        return tas;
    }

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tas_tmp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the category
	 */
    @Column(name = "tas_tmp_cat_id", nullable = false)
    @Convert(converter = ETaskTemplateCategory.class)
	public ETaskTemplateCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(ETaskTemplateCategory category) {
		this.category = category;
	}
	
	/**
	 * @return the recipients
	 */
	@OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
	public List<TaskTemplateRecipient> getRecipients() {
		return recipients;
	}

	/**
	 * @param recipients the recipients to set
	 */
	public void setRecipients(List<TaskTemplateRecipient> recipients) {
		this.recipients = recipients;
	}

	/**
	 * @return the estimatedEndDate
	 */
	@Column(name = "tas_estimated_end_date", nullable = true)
	public Date getEstimatedEndDate() {
		return estimatedEndDate;
	}

	/**
	 * @param estimatedEndDate the estimatedEndDate to set
	 */
	public void setEstimatedEndDate(Date estimatedEndDate) {
		this.estimatedEndDate = estimatedEndDate;
	}

	/**
	 * @return the estimatedDuration
	 */
	@Column(name = "tas_estimated_duration", nullable = true)
	public Float getEstimatedDuration() {
		return estimatedDuration;
	}

	/**
	 * @param estimatedDuration the estimatedDuration to set
	 */
	public void setEstimatedDuration(Float estimatedDuration) {
		this.estimatedDuration = estimatedDuration;
	}
	

	/**
	 * @return the deadline
	 */
	@Column(name = "tas_deadline", nullable = true)
	public Date getDeadline() {
		return deadline;
	}

	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}


	/**
	 * @return the parent
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_tas_tmp_id", nullable = true)
	public TaskTemplate getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(TaskTemplate parent) {
		this.parent = parent;
	}


	/**
	 * @return the action
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="act_def_id", nullable = true)
	public ActionDefinition getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(ActionDefinition action) {
		this.action = action;
	}

	/**
	 * @return the children
	 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	public List<TaskTemplate> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<TaskTemplate> children) {
		this.children = children;
	}

	
}
