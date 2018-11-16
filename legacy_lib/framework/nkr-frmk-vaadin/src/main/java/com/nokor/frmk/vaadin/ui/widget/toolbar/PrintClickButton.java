package com.nokor.frmk.vaadin.ui.widget.toolbar;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.PrintClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.NativeButton;

/**
 * Print click listener
 * @author ly.youhort
 */
public class PrintClickButton extends NativeButton {
	private static final long serialVersionUID = -6759157138690140360L;
	
	/**
	 * @param listener
	 */
	public PrintClickButton(final PrintClickListener listener) {
		super(I18N.message("print"));

		if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	setIcon(FontAwesome.PRINT);
        }
        else {
        	setIcon(new ThemeResource("../nkr-default/icons/16/print.png"));
        }
		addClickListener(new ClickListener() {
			private static final long serialVersionUID = 4631970544741092058L;
			
			@Override
             public void buttonClick(ClickEvent event) {
				listener.printButtonClick(event);
             }
         });
	}
}
