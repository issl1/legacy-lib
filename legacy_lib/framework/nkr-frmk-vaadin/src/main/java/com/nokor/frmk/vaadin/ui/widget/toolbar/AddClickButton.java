package com.nokor.frmk.vaadin.ui.widget.toolbar;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.NativeButton;

/**
 * Add click listener
 * @author ly.youhort
 */
public class AddClickButton extends NativeButton {
	private static final long serialVersionUID = 1361495121996626709L;
	
	/**
	 * @param listener
	 */
	public AddClickButton(final AddClickListener listener) {
		super(I18N.message("add"));
		
		if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	setIcon(FontAwesome.PLUS);
        }
        else {
        	setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
        }
		addClickListener(new ClickListener() {
			private static final long serialVersionUID = -946176322406700098L;
			@Override
             public void buttonClick(ClickEvent event) {
				listener.addButtonClick(event);
             }
         });
	}
}
