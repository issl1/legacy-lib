package org.seuksa.frmk.tools.type;


/**
 * 
 */
public enum Month {
    January(0), February(1), March(2), April(3), May(4), June(5), July(6), August(7), September(8), October(9), November(10), December(11);

    private int id;

    /**
     * 
     * @param id
     */
    private Month(int id) {
        this.id = id;
    }

    /**
     * 
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id
     * @return
     */
    public static Month getMonth(int id) {
        return values()[id];
    }

}