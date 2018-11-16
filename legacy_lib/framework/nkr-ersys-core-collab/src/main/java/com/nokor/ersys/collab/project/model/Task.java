package com.nokor.ersys.collab.project.model;

import java.util.Date;
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
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.action.model.ActionExecution;
import com.nokor.frmk.helper.SeuksaServicesHelper;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_task")
public class Task extends BaseTask implements MTask {
	/** */
	private static final long serialVersionUID = -5045559914591113827L;


	private Date estimatedEndDate;
	private Float estimatedDuration;
	private Date deadline;

	private Task parent;
	private ActionExecution action;
	
	private List<Task> children;
	private List<TaskRecipient> recipients;
	
	
	/**
     * 
     * @return
     */
    public static Task createInstance() {
    	Task tas = EntityFactory.createInstance(Task.class);
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
    @Column(name = "tas_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}


	/**
	 * @return the recipients
	 */
	@OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
	public List<TaskRecipient> getRecipients() {
		return recipients;
	}

	/**
	 * @param recipients the recipients to set
	 */
	public void setRecipients(List<TaskRecipient> recipients) {
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
    @JoinColumn(name="parent_tas_id", nullable = true)
	public Task getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Task parent) {
		this.parent = parent;
	}


	/**
	 * @return the action
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="act_exe_id", nullable = true)
	public ActionExecution getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(ActionExecution action) {
		this.action = action;
	}

	/**
	 * @return the children
	 */
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	public List<Task> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Task> children) {
		this.children = children;
	}

	/**
	 * Get Remaining Day
	 * @return
	 */
	@Transient
    public Long getRemainingDays() {
    	if (deadline == null) {
    		return Long.valueOf(0);
    	}
    	Long remainingDays = DateUtils.getDiffInDays(deadline, DateUtils.today());
    	if (remainingDays < 0) {
    		remainingDays = Long.valueOf(0);
    	}
    	return remainingDays;
    }
	
}
