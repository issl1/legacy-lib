package org.seuksa.frmk.dao.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ly.youhort
 *
 */
public class SysTable implements Serializable {

    private static final long serialVersionUID = 8646860425493288036L;

    private String name;
    private List<SysColumn> columns;

    public SysTable() {
        super();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the columns
     */
    public List<SysColumn> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(final List<SysColumn> columns) {
        this.columns = columns;
    }

    /**
     * @param column
     */
    public void addColumn(final SysColumn column) {
        if (columns == null) {
            columns = new ArrayList<SysColumn>();
        }
        columns.add(column);
    }
}
