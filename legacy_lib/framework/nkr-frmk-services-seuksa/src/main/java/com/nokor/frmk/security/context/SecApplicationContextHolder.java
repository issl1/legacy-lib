package com.nokor.frmk.security.context;

import org.springframework.util.Assert;

/**
 * 
 * @author prasnar
 *
 */
public final class SecApplicationContextHolder {
    private static SecApplicationContext context;


    /**
     * 
     */
    public static void clearContext() {
    	context = null;
    }

    /**
     * 
     * @return
     */
    public static SecApplicationContext getContext() {
        if (context == null) {
        	context = new SecApplicationContext();
        }

        return context;
    }

    /**
     * 
     * @param appContext
     */
    public static void setContext(SecApplicationContext appContext) {
        Assert.notNull(appContext, "Only non-null SecApplicationContext instances are permitted");
        context = appContext;
    }

    /**
     * 
     * @return
     */
    public static SecApplicationContext createEmptyContext() {
        return new SecApplicationContext();
    }
}

