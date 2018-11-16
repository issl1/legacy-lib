package com.nokor.frmk.security.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author prasnar
 *
 */
public abstract class BaseUserType implements UserType {
    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int, org.hibernate.engine.spi.SessionImplementor)
     */
    @Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
    	if (value == null) {
            st.setNull(index, Types.VARBINARY);
        } else {
            noNullSet(st, value, index);
        }
	}
    
    protected abstract void noNullSet(PreparedStatement st, Object value, int index);
    
	/**
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
     */
	@Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

	/**
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	@Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

	/**
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	@Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    /**
     * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
     */
	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null) {
            return false;
        }
        return x.equals(y);
    }

    /**
     * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
     */
	@Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    /**
     * @see org.hibernate.usertype.UserType#isMutable()
     */
	@Override
    public boolean isMutable() {
        return false;
    }

    /**
     * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
     */
	@Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return target;
    }

	/**
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	@Override
    public final int[] sqlTypes() {
        return new int[] { Types.LONGVARBINARY };
    }

	/**
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	@Override
    public Class returnedClass() {
        return String.class;
    }

	
}
