package com.nokor.frmk.vaadin.util.exporter;

import com.nokor.frmk.vaadin.util.filebuilder.ExcelFileBuilder;
import com.nokor.frmk.vaadin.util.filebuilder.FileBuilder;
import com.vaadin.data.Container;
import com.vaadin.ui.Table;

/**
 * @author ky.nora
 */
public class ExcelExporter extends Exporter {	
	/**	 */
	private static final long serialVersionUID = 3123043612189221878L;
	
	public static String MIME_TYPE = "application/vnd.ms-excel";
	
    public ExcelExporter() {
        super();
    }

    /**
     * @param table
     */
    public ExcelExporter(Table table) {
        super(table);
    }

    /**
     * @param container
     * @param visibleColumns
     */
    public ExcelExporter(Container container, Object[] visibleColumns) {
        super(container, visibleColumns);
    }

    /**
     * @param container
     */
    public ExcelExporter(Container container) {
        super(container);
    }

    @Override
    protected FileBuilder createFileBuilder(Container container) {
        return new ExcelFileBuilder(container);
    }
    
}
