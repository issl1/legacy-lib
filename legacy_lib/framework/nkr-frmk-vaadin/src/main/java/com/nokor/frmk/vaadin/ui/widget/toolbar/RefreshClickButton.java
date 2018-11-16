package com.nokor.frmk.vaadin.ui.widget.toolbar;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.NativeButton;

/**
 * Refresh click listener
 * @author ly.youhort
 */
public class RefreshClickButton extends NativeButton  {
	private static final long serialVersionUID = 5244946870864313294L;
	
	/**
	 * @param listener
	 */
	public RefreshClickButton(final SearchClickListener listener) {
		super(I18N.message("refresh"));

		if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	setIcon(FontAwesome.REFRESH);
        }
        else {
        	setIcon(new ThemeResource("../nkr-default/icons/16/refresh.png"));
        }
		addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8235791037160428077L;
			
			@Override
             public void buttonClick(ClickEvent event) {
				listener.searchButtonClick(event);
             }
         });
	}
	
}
