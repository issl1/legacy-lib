package org.seuksa.frmk.model.entity.audit;

import java.util.Date;

/**
 * Block system info in table: createDate, createDate, updateDate, updateUser
 * @author prasnar
 *
 */
public interface SysInfoAuditable {

    String CREATE_DATE_PROPERTY = "createDate";
    String CREATE_USER_PROPERTY = "createUser";
    String UPDATE_DATE_PROPERTY = "updateDate";
    String UPDATE_USER_PROPERTY = "updateUser";

    /**
     * getCreateDate
     * @return Date
     */
    Date getCreateDate();

    /**
     * setCreateDate
     * @param date
     */
    void setCreateDate(final Date date);

    /**
     * getCreateUser
     * @return String
     */
    String getCreateUser();

    /**
     * setCreateUser
     * @param user
     */
    void setCreateUser(final String user);

    /**
     * getUpdateDate
     * @return Date
     */
    Date getUpdateDate();

    /**
     * setUpdateDate
     * @param date
     */
    void setUpdateDate(final Date date);

    /**
     * getUpdateUser
     * @return
     */
    String getUpdateUser();

    /**
     * setUpdateUser
     * @param user
     */
    void setUpdateUser(final String user);


}
