package com.nokor.efinance.core.web;

import ru.xpoft.vaadin.SpringVaadinServlet;

import com.vaadin.server.ClientConnector;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;

/**
 * Servlet to integrate with JMetter
 * @author youhort.ly
 */
public class JMeterServlet extends SpringVaadinServlet {

	private static final long serialVersionUID = -6620000313501758859L;

	public JMeterServlet() {
		super();
        System.setProperty(getPackageName() + "." + "disable-xsrf-protection", "true");
    }
	
	@Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        JMeterService service = new JMeterService(this, deploymentConfiguration);
        initializePlugin(service);
        service.init();
        return service;
    }
	
	/**
	 * @return
	 */
	private String getPackageName() {
        String pkgName;
        final Package pkg = this.getClass().getPackage();
        if (pkg != null) {
            pkgName = pkg.getName();
        } else {
            final String className = this.getClass().getName();
            pkgName = new String(className.toCharArray(), 0, className.lastIndexOf('.'));
        }
        return pkgName;
    }
	
	/**
	 * @author youhort.ly
	 *
	 */
	public static class JMeterService extends VaadinServletService {
        private static final long serialVersionUID = -5874716650679865909L;

        /**
         * @param servlet
         * @param deploymentConfiguration
         * @throws ServiceException
         */
        public JMeterService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration) throws ServiceException {
            super(servlet, deploymentConfiguration);
        }

        @Override
        protected VaadinSession createVaadinSession(VaadinRequest request) throws ServiceException {
            return new JMeterSession(this);
        }
    }

	/**
	 * @author youhort.ly
	 */
    public static class JMeterSession extends VaadinSession {
        private static final long serialVersionUID = 4596901275146146127L;

        /**
         * @param service
         */
        public JMeterSession(VaadinService service) {
            super(service);
        }

        @Override
        public String createConnectorId(ClientConnector connector) {
            if (connector instanceof Component) {
                Component component = (Component) connector;
                return component.getId() == null ? super.createConnectorId(connector) : component.getId();
            }
            return super.createConnectorId(connector);
        }
    }
}
