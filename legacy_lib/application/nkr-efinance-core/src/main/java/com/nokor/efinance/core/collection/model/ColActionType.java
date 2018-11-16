package com.nokor.efinance.core.collection.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;
/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tu_col_action_type")
public class ColActionType extends EntityA {

	/** */
	private static final long serialVersionUID = 410094650931415734L;
	
	private EColType colType;
	private EColAction colAction;
	
	/**
     * 
     * @return
     */
    public static ColActionType createInstance() {
    	ColActionType colActionType = EntityFactory.createInstance(ColActionType.class);
        return colActionType;
    }

	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "col_act_typ_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the colType
	 */
	@Column(name = "col_typ_id", nullable = true)
	@Convert(converter = EColType.class)
	public EColType getColType() {
		return colType;
	}

	/**
	 * @param colType the colType to set
	 */
	public void setColType(EColType colType) {
		this.colType = colType;
	}

	/**
	 * @return the colAction
	 */
	@Column(name = "col_act_id", nullable = true)
	@Convert(converter = EColAction.class)
	public EColAction getColAction() {
		return colAction;
	}

	/**
	 * @param colAction the colAction to set
	 */
	public void setColAction(EColAction colAction) {
		this.colAction = colAction;
	}

}
