package com.nokor.frmk.vaadin.ui.widget.toolbar.event;

import java.io.Serializable;

import com.vaadin.ui.Button.ClickEvent;

/**
 * @author ly.youhort
 */
public interface AddClickListener extends Serializable {
	void addButtonClick(ClickEvent event);
}
