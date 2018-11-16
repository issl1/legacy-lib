package org.seuksa.frmk.dao.vo;

import java.io.Serializable;

/**
 * @author ly.youhort
 *
 */
public class SysColumn implements Serializable {

    private static final long serialVersionUID = 5752612615459525453L;

    private String name;

    public SysColumn() {
        super();
    }

    public SysColumn(final String name) {
        this.name = name;
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

}
