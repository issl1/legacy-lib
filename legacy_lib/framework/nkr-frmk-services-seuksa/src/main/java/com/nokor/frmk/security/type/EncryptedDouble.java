package com.nokor.frmk.security.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.seuksa.frmk.tools.security.CryptoHelper;


/**
 * 
 * @author prasnar
 *
 */
public class EncryptedDouble extends BaseUserType implements UserType {


	/**
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], org.hibernate.engine.spi.SessionImplementor, java.lang.Object)
	 */
    @Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
    	 try {
             String val = rs.getString(names[0]);
             String res = CryptoHelper.decrypt(val);
             Double l = Double.parseDouble(res);
             return l;
         } catch (Exception e) {
             throw new RuntimeException(e.getMessage());
         }
	}
    
    /**
     * @see com.nokor.frmk.security.type.BaseUserType#noNullSet(java.sql.PreparedStatement, java.lang.Object, int)
     */
    @Override
    protected void noNullSet(PreparedStatement st, Object value, int index) {
        try {
            Double l = (Double) value;
            String res = CryptoHelper.encrypt(l.toString());
            st.setString(index, res);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

	
}
