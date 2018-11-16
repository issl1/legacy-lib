package org.seuksa.frmk.tools;

import java.util.Date;

/**
 * Date interval object representation.
 *
 * @author prasnar
 */
public class DateRange {
    private Date creationDate;
    private Date expirationDate;

    /**
     * 
     * @param creationDate
     * @param expirationDate
     */
    public DateRange(Date creationDate, Date expirationDate) {
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
