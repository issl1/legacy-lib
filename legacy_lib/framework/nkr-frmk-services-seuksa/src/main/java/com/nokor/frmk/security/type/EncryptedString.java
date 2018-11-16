package com.nokor.frmk.security.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;

import com.nokor.frmk.security.crypt.SecCipher;

/**
 * 
 * @author prasnar
 *
 */
public class EncryptedString extends BaseUserType implements UserType {
	@Autowired
	private SecCipher secCipher;
	/**
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], org.hibernate.engine.spi.SessionImplementor, java.lang.Object)
	 */
    @Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
    	 try {
             String val = rs.getString(names[0]);
             String res = secCipher.decrypt(val);
             return res;
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
            String res = secCipher.encrypt((String) value);
            st.setString(index, res);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

  
}
