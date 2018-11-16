package com.nokor.frmk.vaadin.ui.widget.toolbar;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.NativeButton;

/**
 * Edit click listener
 * @author ly.youhort
 */
public class EditClickButton extends NativeButton {
	private static final long serialVersionUID = 4418322679948858981L;
	
	/**
	 * @param listener
	 */
	public EditClickButton(final EditClickListener listener) {
		super(I18N.message("edit"));
		
		if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	setIcon(FontAwesome.PENCIL);
        }
        else {
        	setIcon(new ThemeResource("../nkr-default/icons/16/edit.png"));
        }
		addClickListener(new ClickListener() {
			private static final long serialVersionUID = -2251020151459848966L;
			
			@Override
            public void buttonClick(ClickEvent event) {
				listener.editButtonClick(event);
			}
         });
	}
}
