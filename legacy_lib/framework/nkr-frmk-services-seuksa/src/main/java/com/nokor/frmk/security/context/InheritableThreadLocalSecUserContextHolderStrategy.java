package com.nokor.frmk.security.context;

import org.springframework.util.Assert;

/**
 * @see org.springframework.security.core.context.InheritableThreadLocalSecUserContextHolderStrategy
 * 
 * @author prasnar
 *
 */
final class InheritableThreadLocalSecUserContextHolderStrategy implements SecUserContextHolderStrategy {
    private static final ThreadLocal<SecUserContextOld> contextHolder = new InheritableThreadLocal<SecUserContextOld>();

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
