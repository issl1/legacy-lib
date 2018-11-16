package com.nokor.frmk.vaadin.web;

import javax.servlet.ServletException;

import ru.xpoft.vaadin.SpringVaadinServlet;

import com.nokor.frmk.vaadin.web.listener.VaadinSessionInitListener;

/**
 * 
 * @author prasnar
 *
 */
public class ApplicationVaadinServlet extends SpringVaadinServlet {
	/** */
	private static final long serialVersionUID = -1351470317930079824L;

	@Override
    protected final void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(new VaadinSessionInitListener());
    }
}