package org.seuksa.frmk.dao.tools;

import java.sql.Types;

import org.seuksa.frmk.model.meta.FieldType;


/**
 * 
 * @author ly.youhort
 *
 */
public class NativeColumnUtils {

	/**
	 * 
	 */
    private NativeColumnUtils() {
    }

    /**
     * 
     * @param type
     * @return
     */
    public static FieldType getCusType(final int type) {
        switch (type) {
            case Types.BOOLEAN:
            case Types.BIT:
                return FieldType.BOOLEAN;
            case Types.DATE:
                return FieldType.DATE;
            case Types.TIME:
                return FieldType.TIME;
            case Types.TIMESTAMP:
                return FieldType.DATETIME;
            case Types.INTEGER:
                return FieldType.INTEGER;
            case Types.BIGINT:
                return FieldType.LONG;   
            case Types.DOUBLE:
                return FieldType.DOUBLE;
            case Types.FLOAT:
                return FieldType.FLOAT;
            case Types.VARCHAR:
            case Types.CHAR:
                return FieldType.STRING;
            default:
                return FieldType.STRING;
        }
    }

}
