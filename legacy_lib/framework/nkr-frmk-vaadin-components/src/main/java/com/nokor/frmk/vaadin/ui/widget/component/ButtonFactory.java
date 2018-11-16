package com.nokor.frmk.vaadin.ui.widget.component;

import org.apache.commons.lang.StringUtils;

import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.themes.Runo;

public final class ButtonFactory {

	/**
	 * @return
	 */
	public static Button getAddButton() {
		return getAddButton("add");
    }
	
	/**
	 * @param hasCaption
	 * @return
	 */
	public static Button getAddButton(String caption) {
		Button btnSave = new NativeButton(I18N.message(caption));
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
		return btnSave;
    }
	
	/**
	 * @return
	 */
	public static Button getSaveButton() {
		return getSaveButton("save");
    }
	
	/**
	 * @return
	 */
	public static Button getSaveButton(String caption) {
		Button btnSave = new NativeButton(I18N.message(caption));
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		return btnSave;
    }
	
	/**
	 * @return
	 */
	public static Button getCancelButton() {
		return getCancelButton("cancel");
	}
	
	/**
	 * @return
	 */
	public static Button getCancelButton(String caption) {
		Button btnCancel = new NativeButton(I18N.message(caption));
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		return btnCancel;
    }
	
	/**
	 * @return
	 */
	public static Button getDeleteButton() {
		return getDeleteButton("delete");
	}

	/**
	 * @return
	 */
	public static Button getDeleteButton(String caption) {
		Button btnCancel = new NativeButton(I18N.message(caption));
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		return btnCancel;
    }
	
	/**
	 * @return
	 */
	public static Button getSearchButton() {
		return getSearchButton("search");
	}
	
	/**
	 * @return
	 */
	public static Button getSearchButton(String caption) {
		Button btnSearch = new NativeButton(I18N.message(caption));
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/searh.png"));
		return btnSearch;
    }
	
	/**
	 * @return
	 */
	public static Button getPrintButton() {
		return getPrintButton("print");
	}
	
	/**
	 * @return
	 */
	public static Button getPrintButton(String caption) {
		Button btnPrint = new NativeButton(I18N.message(caption));
		btnPrint.setIcon(new ThemeResource("../nkr-default/icons/16/print.png"));
		return btnPrint;
    }
	
	/**
	 * @return
	 */
	public static Button getResetButton() {
		Button btnReset = new NativeButton(I18N.message("reset"));
		// btnReset.setIcon(new ThemeResource("../nkr-default/icons/16/searh.png"));
		return btnReset;
    }

	/**
	 * @param width
	 * @param height
	 * @param icon
	 * @param tooltip
	 * @return
	 */
	public static Button getToolbarButton(int width, int height, ThemeResource icon, String tooltip) {
		Button btn = getButton("", width, height, icon);
		if (StringUtils.isNotEmpty(tooltip)) {
			btn.setDescription(tooltip);
		}
		btn.setStyleName(Runo.BUTTON_LINK);
		return btn;
	}
	
	/**
	 * @param
	 * @return
	 */
	public static Button getButton(String caption, ThemeResource icon) {
		Button btn = new NativeButton(caption);
		if (icon != null) {
			btn.setIcon(icon);
		}
		return btn;
	}
	
	/**
	 * @param width
	 * @param height
	 * @param
	 * @return
	 */
	public static Button getButton(String caption, int width, int height, ThemeResource icon) {
		Button btn = new NativeButton(caption);
		btn.setWidth(width, Unit.PIXELS);
		btn.setHeight(height, Unit.PIXELS);
		if (icon != null) {
			btn.setIcon(icon);
		}
		return btn;
	}
}
