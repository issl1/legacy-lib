package com.nokor.frmk.vaadin.ui.panel;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Abstract search panel
 * @author ly.youhort
 */
public abstract class AbstractSearchPanel<T extends Entity> extends Panel implements VaadinServicesHelper {

	private static final long serialVersionUID = 1758781365223334094L;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected Button btnSearch;
	protected Button btnReset;
	// This variable is used for Specific EntityRef
	protected Class<T> searchEntityClass;
	
	/**
	 * Initialization
	 */
	public AbstractSearchPanel (final String caption, final AbstractTablePanel<T> tablePanel) {
		initControl(caption, tablePanel);
	}
	
	public AbstractSearchPanel (final String caption, final AbstractTablePanel<T> tablePanel, Class<T> searchEntityClass) {
		this.searchEntityClass = searchEntityClass;
		initControl(caption, tablePanel);
	}
	
	/**
	 * 
	 * @param caption
	 * @param tablePanel
	 */
	private void initControl(String caption, final AbstractTablePanel<T> tablePanel) {
		setCaption(caption);
		VerticalLayout containLayout = new VerticalLayout();
		containLayout.setStyleName("panel-search");
		containLayout.setMargin(true);

		btnSearch = new Button(I18N.message("search"));
		if (AppConfigFileHelper.isFontAwesomeIcon()) {
			btnSearch.setIcon(FontAwesome.SEARCH);
        }
        else {
        	btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
        }
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setImmediate(true);
		btnSearch.addClickListener(new ClickListener() {
			/**	 */
			private static final long serialVersionUID = -5644792188986529962L;

			@Override
			public void buttonClick(ClickEvent event) {
				tablePanel.searchButtonClick(event);
			}
		});
		
		
		btnReset = new Button(I18N.message("reset"));
		if (AppConfigFileHelper.isFontAwesomeIcon()) {
			btnReset.setIcon(FontAwesome.ERASER);
        }
        else {
        	btnReset.setIcon(new ThemeResource("../nkr-default/icons/16/reset.png"));
        }
		btnReset.setClickShortcut(KeyCode.ESCAPE, null);
		btnReset.setImmediate(true);
		btnReset.addClickListener(new ClickListener() {
			/**	 */
			private static final long serialVersionUID = 3257468831521150461L;

			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setStyleName("panel-search-center");
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		containLayout.addComponent(createForm());
		containLayout.addComponent(ComponentFactory.getVerticalLayout(10));
		containLayout.addComponent(buttonsLayout);
		setContent(containLayout);
	}
		
	protected abstract void reset();
	protected abstract Component createForm();
	
	public abstract BaseRestrictions<T> getRestrictions();
}
