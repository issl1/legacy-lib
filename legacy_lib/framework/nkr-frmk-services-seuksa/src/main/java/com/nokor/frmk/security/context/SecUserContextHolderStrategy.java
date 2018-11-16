package com.nokor.frmk.security.context;


/**
 * @see org.springframework.security.core.context.SecUserContextHolderStrategy
 * 
 * @author prasnar
 *
 */
public interface SecUserContextHolderStrategy {
    /**
     * Clears the current context.
     */
    void clearContext();

    /**
     * Obtains the current context.
     *
     * @return a context (never <code>null</code> - create a default implementation if necessary)
     */
    SecUserContextOld getContext();

    /**
     * Sets the current context.
     *
     * @param context to the new argument (should never be <code>null</code>, although implementations must check if
     *        <code>null</code> has been passed and throw an <code>IllegalArgumentException</code> in such cases)
     */
    void setContext(SecUserContextOld context);

    /**
     * Creates a new, empty context implementation, for use by <tt>SecUserContextRepository</tt> implementations,
     * when creating a new context for the first time.
     *
     * @return the empty context.
     */
    SecUserContextOld createEmptyContext();
}
