package com.nokor.frmk.annotation;

import org.seuksa.frmk.tools.exception.DaoException;

/**
 * Exception occurs when system cannot find NamedHqlQuery for called interface method.
 *
 * @author prasnar
 */
public class QueryResourceAnnotationException extends DaoException {
    /** */
	private static final long serialVersionUID = 7006376573066897775L;

	/**
	 * 
	 * @param message
	 */
	public QueryResourceAnnotationException(String message) {
        super(message);
    }
	
	 /**
     * 
     * @param e
     */
    public QueryResourceAnnotationException(Exception e) {
        super(e);
    }

     /**
     * 
     * @param message
     * @param innerException
     */
    public QueryResourceAnnotationException(String message, Exception innerException) {
        super(message, innerException);
    }
}
