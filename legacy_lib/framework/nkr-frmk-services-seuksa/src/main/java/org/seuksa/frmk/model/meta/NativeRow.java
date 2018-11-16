package org.seuksa.frmk.model.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ly.youhort
 *
 */
public class NativeRow implements Serializable {

    private static final long serialVersionUID = 1951840781152266763L;

    private List<NativeColumn> columns;

    /**
     * 
     */
    public NativeRow() {
        super();
    }

    /**
     * @return the columns
     */
    public List<NativeColumn> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumn(final List<NativeColumn> columns) {
        this.columns = columns;
    }

    /**
     * 
     * @param column
     */
    public void addColumn(final NativeColumn column) {
        if (this.columns == null) {
            this.columns = new ArrayList<NativeColumn>();
        }
        this.columns.add(column);
    }

}
