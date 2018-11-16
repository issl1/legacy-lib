package com.nokor.efinance.gui.ui.panel.report.uwperformance;

import com.nokor.frmk.vaadin.util.exporter.ExcelExporter;
import com.nokor.frmk.vaadin.util.filebuilder.FileBuilder;
import com.vaadin.data.Container;
import com.vaadin.ui.Table;

public class UWExcelExporter extends ExcelExporter{

	
	 public UWExcelExporter() {
		super();
	}

	public UWExcelExporter(Container container, Object[] visibleColumns) {
		super(container, visibleColumns);
	}

	public UWExcelExporter(Container container) {
		super(container);
	}

	public UWExcelExporter(Table table) {
		super(table);
	}

	@Override
	 protected FileBuilder createFileBuilder(Container container) {
	        return new UWExcelFileBuilder(container);
	 }
}
