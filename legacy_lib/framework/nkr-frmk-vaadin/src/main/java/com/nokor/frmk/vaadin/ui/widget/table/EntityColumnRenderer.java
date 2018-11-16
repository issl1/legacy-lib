package com.nokor.frmk.vaadin.ui.widget.table;

import org.seuksa.frmk.model.entity.Entity;

/**
 * 
 * @author youhort.ly
 *
 */
public abstract class EntityColumnRenderer implements ColumnRenderer {
	/** */
	private static final long serialVersionUID = -6548038299056742168L;
	private Entity entity;
	
	/**
	 * 
	 */
	public EntityColumnRenderer() {
	}
	
	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}
