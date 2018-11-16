package com.nokor.frmk.vaadin.ui.widget.toolbar;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.CloseClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.NativeButton;

/**
 * Close click listener
 * @author ly.youhort
 */
public class CloseClickButton extends NativeButton {
	/** */
	private static final long serialVersionUID = -320881168150410449L;

	/**
	 * @param listener
	 */
	public CloseClickButton(final CloseClickListener listener) {
		super(I18N.message("close"));

		if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	setIcon(FontAwesome.TIMES);
        }
        else {
        	setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
        }
		addClickListener(new ClickListener() {
			private static final long serialVersionUID = -3290768423189730139L;
			
			@Override
             public void buttonClick(ClickEvent event) {
				listener.closeButtonClick(event);
             }
         });
	}
}
