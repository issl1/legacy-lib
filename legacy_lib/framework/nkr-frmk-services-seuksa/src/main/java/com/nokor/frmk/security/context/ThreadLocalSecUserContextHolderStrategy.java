package com.nokor.frmk.security.context;

import org.springframework.util.Assert;

/**
 * @see org.springframework.security.core.context.ThreadLocalSecUserContextHolderStrategy
 * 
 * @author prasnar
 *
 */
final class ThreadLocalSecUserContextHolderStrategy implements SecUserContextHolderStrategy {
    private static final ThreadLocal<SecUserContextOld> contextHolder = new ThreadLocal<SecUserContextOld>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    @Override
    public SecUserContextOld getContext() {
    	SecUserContextOld ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
    }

    @Override
    public void setContext(SecUserContextOld context) {
        Assert.notNull(context, "Only non-null SecUserContext instances are permitted");
        contextHolder.set(context);
    }

    @Override
    public SecUserContextOld createEmptyContext() {
        return new SecUserContextOld();
    }
}


