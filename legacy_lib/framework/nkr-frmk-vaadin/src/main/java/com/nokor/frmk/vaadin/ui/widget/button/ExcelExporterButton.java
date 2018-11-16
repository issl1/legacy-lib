package com.nokor.frmk.vaadin.ui.widget.button;

import com.nokor.frmk.vaadin.util.exporter.ExcelExporter;
import com.vaadin.data.Container;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author ly.youhort
 */
public class ExcelExporterButton extends NativeButton {

	private static final long serialVersionUID = 2163621974674153345L;
		
	private Container container;
	
	/**
	 * @param container
	 */
	public ExcelExporterButton() {
		setIcon(new ThemeResource("../nkr-default/icons/16/excel.png"));
        setStyleName(Reindeer.BUTTON_LINK);
		this.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -345128294252780849L;
			public void buttonClick(ClickEvent event) {
	        	final ExcelExporter excelExp = new ExcelExporter();
	            excelExp.setContainerToBeExported(container);
	            excelExp.sendConvertedFileToUser(getUI());
	        }
	    });
	}

	/**
	 * @return the container
	 */
	public Container getContainer() {
		return container;
	}

	/**
	 * @param container the container to set
	 */
	public void setContainer(Container container) {
		this.container = container;
	}	
}
