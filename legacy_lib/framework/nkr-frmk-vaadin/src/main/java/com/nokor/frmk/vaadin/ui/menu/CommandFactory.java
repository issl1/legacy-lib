/**
 * 
 */
package com.nokor.frmk.vaadin.ui.menu;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.menu.model.MenuItemEntity;
import com.vaadin.server.Page;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * @author prasnar
 *
 */
public class CommandFactory implements Serializable {
	/** */
	private static final long serialVersionUID = 6121470677357403124L;

	protected static final Logger logger = LoggerFactory.getLogger(CommandFactory.class);
	
	/**
	 * 
	 */
	private CommandFactory() {
	}

	
	/**
	 * 
	 * @param menuItem
	 * @return
	 */
	public static Command create(final MenuItemEntity menuItem) {
		return new Command() {
			
			/**	 */
			private static final long serialVersionUID = -5917209086321466944L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				if (!menuItem.isPopupWindow()) {
					String viewName = menuItem.getAction();
					Page.getCurrent().setUriFragment("!" + viewName);
				} else {
					String windowClazzName = "<EMPTY>";
					try {
						windowClazzName = menuItem.getAction();
						Class<Window> windowClazz = (Class<Window>) Class.forName(windowClazzName);
						UI.getCurrent().addWindow(windowClazz.newInstance());
					} catch (Exception e) {
						logger.error("Error while trying to display Popup Window [" + menuItem.getCode() + "] [" + windowClazzName + "]");
						throw new IllegalStateException(e);
					}
				}
			}
		};
	}
	
	
}
