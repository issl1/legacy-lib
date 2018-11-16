package com.nokor.frmk.vaadin.util.exporter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

import com.nokor.frmk.vaadin.util.filebuilder.FileBuilder;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

/**
 * Simple Table Exporter
 * @author ky.nora
 */
public abstract class Exporter implements StreamSource {
    
	private static final long serialVersionUID = 1356154416466729352L;

	public static String MIME_TYPE = "text/plain";
	
    protected FileBuilder fileBuilder;
    private Locale locale;
    private String dateFormatString;
    protected String downloadFileName;
    
    public Exporter() {
    	
    }
    
    /**
     * @param table
     */
    public Exporter(Table table) {
    	this();
        setTableToBeExported(table);
    }

    /**
     * @param container
     * @param visibleColumns
     */
    public Exporter(Container container, Object[] visibleColumns) {
        this();
        setContainerToBeExported(container);
        setVisibleColumns(visibleColumns);
    }

    /**
     * @param container
     */
    public Exporter(Container container) {
        this(container, null);
    }

    /**
     * @param table
     */
    public void setTableToBeExported(Table table) {
        setContainerToBeExported(table.getContainerDataSource());
        setVisibleColumns(table.getVisibleColumns());
        setHeader(I18N.message(table.getCaption()));
        for (Object column : table.getVisibleColumns()) {
            String header = table.getColumnHeader(column);
            if (header != null) {
                setColumnHeader(column, header);
            }
        }
    }

    /**
     * @param container
     */
	public void setContainerToBeExported(Container container) {
		fileBuilder = createFileBuilder(container);
		if (locale != null) {
			fileBuilder.setLocale(locale);
		}
		if (dateFormatString != null) {
			fileBuilder.setDateFormat(dateFormatString);
		}
	}

	/**
	 * @param visibleColumns
	 */
    public void setVisibleColumns(Object[] visibleColumns) {
        fileBuilder.setVisibleColumns(visibleColumns);
    }

    /**
     * @param propertyId
     * @param header
     */
    public void setColumnHeader(Object propertyId, String header) {
        fileBuilder.setColumnHeader(propertyId, header);
    }

    /**
     * @param header
     */
    public void setHeader(String header) {
        fileBuilder.setHeader(header);
    }
    
    /**
     * @param locale
     */
    public void setLocale(Locale locale){
    	this.locale = locale;
    }
    
    /**
     * @param dateFormat
     */
    public void setDateFormat(String dateFormat) {
    	this.dateFormatString = dateFormat;
    }

    protected abstract FileBuilder createFileBuilder(Container container);
    
    @Override
    public InputStream getStream() {
        try {
            return new FileInputStream(fileBuilder.getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * @param app
     */
    public void sendConvertedFileToUser(final UI app) {
        // Create a resource
        final FileResource res = new FileResource(fileBuilder.getFile());
        app.getPage().open(res, null, false);
    }
}
