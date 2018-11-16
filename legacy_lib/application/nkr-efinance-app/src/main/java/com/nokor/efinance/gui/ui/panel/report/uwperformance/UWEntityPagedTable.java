package com.nokor.efinance.gui.ui.panel.report.uwperformance;

import org.seuksa.frmk.model.entity.Entity;

import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedTable;
import com.nokor.frmk.vaadin.util.exporter.CSVExporter;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.Reindeer;

public class UWEntityPagedTable<T extends Entity> extends EntityPagedTable<T>{

	public UWEntityPagedTable(PagedDataProvider dataProvider) {
		super(dataProvider);
	}

	public UWEntityPagedTable(String caption, PagedDataProvider dataProvider) {
		super(caption, dataProvider);
	}

	public HorizontalLayout createExportBar() {
        HorizontalLayout exportBar = new HorizontalLayout();
        final Button excelExporter = new Button("", new ClickListener() {
			private static final long serialVersionUID = -345128294252780849L;
			public void buttonClick(ClickEvent event) {
            	final UWExcelExporter excelExp = new UWExcelExporter();
            	IndexedContainer tmpContainer = getDataProvider().getIndexedContainer(0, (int) getDataProvider().getTotalRecords());
                excelExp.setContainerToBeExported(tmpContainer);
                excelExp.sendConvertedFileToUser(getUI());
            }
        });
        excelExporter.setIcon(new ThemeResource("../nkr-default/icons/16/excel.png"));
        excelExporter.setStyleName(Reindeer.BUTTON_LINK);
        
        final Button csvExporter = new Button("", new ClickListener() {
			private static final long serialVersionUID = 1544211032887048179L;
			public void buttonClick(ClickEvent event) {
            	final CSVExporter csvlExp = new CSVExporter();
            	IndexedContainer tmpContainer = getDataProvider().getIndexedContainer(0, (int) getDataProvider().getTotalRecords());
                csvlExp.setContainerToBeExported(tmpContainer);
                csvlExp.sendConvertedFileToUser(getUI());
            }
        });
        csvExporter.setIcon(new ThemeResource("../nkr-default/icons/16/csv.png"));
        csvExporter.setStyleName(Reindeer.BUTTON_LINK);
        
        exportBar.addComponent(excelExporter);
        exportBar.addComponent(csvExporter);
        exportBar.setSpacing(true);
        exportBar.setWidth(null);
       
        return exportBar;
    }
}
