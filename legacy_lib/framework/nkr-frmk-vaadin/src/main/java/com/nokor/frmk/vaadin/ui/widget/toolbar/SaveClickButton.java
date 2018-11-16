package com.nokor.frmk.vaadin.ui.widget.toolbar;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.NativeButton;

/**
 * Save click listener
 * @author ly.youhort
 */
public class SaveClickButton extends NativeButton {
	private static final long serialVersionUID = -7962536477684472233L;

	/**
	 * @param listener
	 */
	public SaveClickButton(final SaveClickListener listener) {
		super(I18N.message("save"));

		if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	setIcon(FontAwesome.SAVE);
        }
        else {
        	setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
        }
		addClickListener(new ClickListener() {
			private static final long serialVersionUID = -946176322406700098L;
			
			@Override
             public void buttonClick(ClickEvent event) {
				listener.saveButtonClick(event);
             }
         });
	}
}
