package com.nokor.frmk.vaadin.util.exporter;

import com.nokor.frmk.vaadin.util.filebuilder.FileBuilder;
import com.nokor.frmk.vaadin.util.filebuilder.PdfFileBuilder;
import com.vaadin.data.Container;
import com.vaadin.ui.Table;

/**
 * @author ky.nora
 */
public class PdfExporter extends Exporter {
	
	/**	 */
	private static final long serialVersionUID = -2078349880833051223L;
	
	public static String MIME_TYPE = "application/pdf";
	
    public PdfExporter() {
        super();
    }

    public PdfExporter(Table table) {
        super(table);
    }

    public PdfExporter(Container container, Object[] visibleColumns) {
        super(container, visibleColumns);
    }

    public PdfExporter(Container container) {
        super(container);
    }

    @Override
    protected FileBuilder createFileBuilder(Container container) {
        return new PdfFileBuilder(container);
    }
    
    public void setWithBorder(boolean withBorder) {
        ((PdfFileBuilder) fileBuilder).setWithBorder(withBorder);
    }
    
}
