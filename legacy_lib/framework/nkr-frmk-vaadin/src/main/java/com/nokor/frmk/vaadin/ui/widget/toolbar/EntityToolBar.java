package com.nokor.frmk.vaadin.ui.widget.toolbar;

import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * @author ly.youhort
 */
public class EntityToolBar extends HorizontalLayout {
	
	private static final long serialVersionUID = 765208338996283429L;
	
	private Button btnAdd = null;
	private Button btnDelete = null;
	private Button btnRefresh = null;
	private Button btnPrint = null;
		
	public EntityToolBar() {
		
		setMargin(true);
		setSpacing(true);
		
		btnAdd = new Button(I18N.message("add"));
		btnAdd.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
		
		btnDelete = new Button(I18N.message("delete"));
		btnDelete.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		btnRefresh = new Button(I18N.message("refresh"));
		btnRefresh.setIcon(new ThemeResource("../nkr-default/icons/16/refresh.png"));
		
		btnPrint = new Button(I18N.message("print"));
		btnPrint.setIcon(new ThemeResource("../nkr-default/icons/16/print.png"));
		
		addComponent(btnAdd);
		addComponent(btnDelete);
		addComponent(btnRefresh);
		addComponent(btnPrint);
		
		Label space = new Label();
		addComponent(space);
		setExpandRatio(space, 1f);
		
		setWidth("100%");
		setStyleName("toolbar");
	}
}

