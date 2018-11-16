package com.nokor.common.app.action.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_action_definition")
public class ActionDefinition extends EntityRefA {
	/** */
	private static final long serialVersionUID = 2307071478271779658L;

	private String execValue;			// depending onEActionType: Java class name, Script name, constraint values, URL, ... 
	private EActionType type;
	private String inputDefinition;
	private String outputDefinition;
	private String comment;

	/**
	 * 
	 * @return
	 */
	public static ActionDefinition createInstance() {
		ActionDefinition comm = EntityFactory.createInstance(ActionDefinition.class);
        return comm;
    }

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "act_def_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
    
    /**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
	 */
	@Column(name = "act_def_code", nullable = true, length = 20)
	@Override
	public String getCode() {
		return code;
	}


	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
	 */
	@Column(name = "act_def_desc", nullable = false, length = 255)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "act_def_desc_en", nullable = true, length = 255)
	@Override
	public String getDescEn() {
		return descEn;
	}

	/**
	 * @return the execValue
	 */
	@Column(name = "act_def_exec_value", nullable = false)
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
	@Column(name = "act_def_input_definition", nullable = true)
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
	@Column(name = "act_def_output_definition", nullable = true)
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
	@Column(name = "act_def_comment", nullable = true)
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
