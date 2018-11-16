package com.nokor.common.app.action.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_action_execution")
public class ActionExecution extends EntityA {
	/** */
	private static final long serialVersionUID = 2190630627328888218L;

	// Definition info
	private ActionDefinition action;	// only for info, the initial definitions are copied
	private String execValue;			// depending onEActionType: Java class name, Script name, constraint values, URL, ... 
	private EActionType type;
	private String inputDefinition;
	private String outputDefinition;
	private String comment;

	// Execution info
	private Date execDate;
	private String entityClass; 
	private Long entityId; 
	private String inputValues;
	private Integer sortIndex; 

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "act_exe_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the action
	 */
	@ManyToOne(fetch=FetchType.LAZY)
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
	 * @return the execValue
	 */
	@Column(name = "act_exe_exec_value", nullable = false)
	public String getExecValue() {
		return execValue;
	}

	/**
	 * @param execValue the execValue to set
	 */
	public void setExecValue(String execValue) {
		this.execValue = execValue;
	}
	
	/**
	 * @return the type
	 */
    @Column(name = "act_typ_id", nullable = false)
    @Convert(converter = EActionType.class)
	public EActionType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EActionType type) {
		this.type = type;
	}

	/**
	 * @return the inputDefinition
	 */
	@Column(name = "act_exe_input_definition", nullable = true)
	public String getInputDefinition() {
		return inputDefinition;
	}

	/**
	 * @param inputDefinition the inputDefinition to set
	 */
	public void setInputDefinition(String inputDefinition) {
		this.inputDefinition = inputDefinition;
	}

	
	/**
	 * @return the outputDefinition
	 */
	@Column(name = "act_exe_output_definition", nullable = true)
	public String getOutputDefinition() {
		return outputDefinition;
	}

	/**
	 * @param outputDefinition the outputDefinition to set
	 */
	public void setOutputDefinition(String outputDefinition) {
		this.outputDefinition = outputDefinition;
	}

	/**
	 * @return the comment
	 */
	@Column(name = "act_exe_comment", nullable = true)
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
	 * @return the execDate
	 */
	@Column(name = "act_cfg_exec_date", nullable = true)
	public Date getExecDate() {
		return execDate;
	}

	/**
	 * @param execDate the execDate to set
	 */
	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}

	/**
	 * @return the entityClass
	 */
    @Column(name="act_cfg_entity_class", nullable = true)
	public String getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass the entityClass to set
	 */
	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * @return the entityId
	 */
    @Column(name="act_cfg_entity_id", nullable = true)
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the inputValues
	 */
	@Column(name = "act_exe_input_values", nullable = true)
	public String getInputValues() {
		return inputValues;
	}

	/**
	 * @param inputValues the inputValues to set
	 */
	public void setInputValues(String inputValues) {
		this.inputValues = inputValues;
	}
  
	/**
     * @return SortIndex
     */
    @Column(name = "sort_index")
    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * @param sortIndex
     */
    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }
    
	
}
