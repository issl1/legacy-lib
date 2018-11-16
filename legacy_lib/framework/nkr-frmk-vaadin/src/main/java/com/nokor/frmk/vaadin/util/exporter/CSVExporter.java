package com.nokor.frmk.vaadin.util.exporter;

import com.nokor.frmk.vaadin.util.filebuilder.CSVFileBuilder;
import com.nokor.frmk.vaadin.util.filebuilder.FileBuilder;
import com.vaadin.data.Container;
import com.vaadin.ui.Table;

/**
 * @author ky.nora
 */
public class CSVExporter extends Exporter {
	/**	 */
	private static final long serialVersionUID = -8542252946583607354L;
	
	public static String MIME_TYPE = "text/cvs";
	
    public CSVExporter() {
        super();
    }
    
    public CSVExporter(Table table) {
        super(table);
    }

    public CSVExporter(Container container, Object[] visibleColumns) {
        super(container, visibleColumns);
    }

    public CSVExporter(Container container) {
        super(container);
    }

    @Override
    protected FileBuilder createFileBuilder(Container container) {
        return new CSVFileBuilder(container);
    }

}
