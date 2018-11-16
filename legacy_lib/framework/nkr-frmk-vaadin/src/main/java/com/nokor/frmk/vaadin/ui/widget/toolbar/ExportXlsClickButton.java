package com.nokor.frmk.vaadin.ui.widget.toolbar;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.ExportXlsClickListener;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.NativeButton;

/**
 * Export Xls click listener
 * @author phirun.kong
 *
 */
public class ExportXlsClickButton extends NativeButton {
	
	/**	 */
	private static final long serialVersionUID = 2273228243561806309L;

	/**
	 * @param listener
	 */
	public ExportXlsClickButton(final ExportXlsClickListener listener) {
		super(I18N.message("export.xls"));

		if (AppConfigFileHelper.isFontAwesomeIcon()) {
        	setIcon(FontAwesome.FILE_EXCEL_O);
        }
        else {
        	setIcon(new ThemeResource("../nkr-default/icons/16/excel.png"));
        }
		addClickListener(new ClickListener() {
			
			/**		 */
			private static final long serialVersionUID = 7429385577890340384L;

			@Override
             public void buttonClick(ClickEvent event) {
				listener.exportXlsButtonClick(event);
             }
         });
	}
}
