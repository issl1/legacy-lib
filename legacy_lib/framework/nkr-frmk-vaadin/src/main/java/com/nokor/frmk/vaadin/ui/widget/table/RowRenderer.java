package com.nokor.frmk.vaadin.ui.widget.table;

import org.seuksa.frmk.model.entity.Entity;

import com.vaadin.data.Item;

/**
 * @author ly.youhort
 *
 */
public interface RowRenderer {
	void renderer(Item item, Entity entity);
}
