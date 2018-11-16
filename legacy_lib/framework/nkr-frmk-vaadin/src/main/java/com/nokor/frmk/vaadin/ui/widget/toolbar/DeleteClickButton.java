package com.nokor.frmk.vaadin.ui.widget.toolbar;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.NativeButton;

/**
 * Delete click listener
 * @author ly.youhort
 */
public class DeleteClickButton extends NativeButton {
	private static final long serialVersionUID = 3191492490843209218L;
	
	/**
	 * @param listener
	 */
	public DeleteClickButton(final DeleteClickListener listener) {
		super(I18N.message("delete"));

		if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	setIcon(FontAwesome.TRASH_O);
        }
        else {
        	setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
        }
		addClickListener(new ClickListener() {
			private static final long serialVersionUID = -3290768423189730139L;
			
			@Override
             public void buttonClick(ClickEvent event) {
				listener.deleteButtonClick(event);
             }
         });
	}
}
