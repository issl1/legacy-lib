package com.nokor.frmk.auditlog.envers;

import org.hibernate.HibernateException;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.config.spi.StandardConverters;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.envers.boot.internal.EnversIntegrator;
import org.hibernate.envers.boot.internal.EnversService;
import org.hibernate.event.service.spi.EventListenerGroup;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostCollectionRecreateEventListener;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.jboss.logging.Logger;

import com.nokor.frmk.auditlog.envers.listener.event.CollectionRecreateEventListener;
import com.nokor.frmk.auditlog.envers.listener.event.DeleteEventListener;
import com.nokor.frmk.auditlog.envers.listener.event.InsertEventListener;
import com.nokor.frmk.auditlog.envers.listener.event.PreCollectionRemoveEventListener;
import com.nokor.frmk.auditlog.envers.listener.event.PreCollectionUpdateEventListener;
import com.nokor.frmk.auditlog.envers.listener.event.UpdateEventListener;
import com.nokor.frmk.helper.FrmkAppConfigFileHelper;


/**
 * 
 * @author prasnar
 *
 */
public class AuditEnversIntegrator extends EnversIntegrator   {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			EnversIntegrator.class.getName()
	);

	/**
	 * @see org.hibernate.envers.boot.internal.EnversIntegrator#integrate(org.hibernate.boot.Metadata, org.hibernate.engine.spi.SessionFactoryImplementor, org.hibernate.service.spi.SessionFactoryServiceRegistry)
	 */
	@Override
	public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

		EnversService enversService = (EnversService) serviceRegistry.getService(EnversService.class);
		
		if (!(enversService.isEnabled())) {
			LOG.debug("Skipping Envers listener registrations : EnversService disabled");
			return;
		}

		boolean autoRegister = ((Boolean) ((ConfigurationService) serviceRegistry
				.getService(ConfigurationService.class)).getSetting(
				"hibernate.envers.autoRegisterListeners",
				StandardConverters.BOOLEAN, Boolean.valueOf(true)))
				.booleanValue();

    	if ( !autoRegister ) {
			LOG.debug( "Skipping Envers listener auto registration" );
			return;
		}
    	
    	if (!(enversService.isInitialized())) {
			throw new HibernateException("Expecting EnversService to have been initialized prior to call to EnversIntegrator#integrate");
		}

		if (!(enversService.getEntitiesConfigurations().hasAuditedEntities())) {
			LOG.debug("Skipping Envers listener registrations : No audited entities found");
			return;
		}

    	// no need to call the default EnversIntegrator.integrate, because it is already automatically add in the integrators list, then run before
//        super.integrate(metadata, sessionFactory, serviceRegistry);
    	
        EventListenerRegistry listenerRegistry = serviceRegistry.getService( EventListenerRegistry.class );

        LOG.info("Registering Hibernate Envers event listeners");
        if (enversService.getEntitiesConfigurations().hasAuditedEntities()) {
        	if (FrmkAppConfigFileHelper.isEnversEventInsert()) {
        		EventListenerGroup<PostInsertEventListener> eventListenerGroup = listenerRegistry.getEventListenerGroup(EventType.POST_INSERT);
        		eventListenerGroup.clear();
        		eventListenerGroup.addDuplicationStrategy(AuditEnversListenerDuplicationStrategy.INSTANCE);
        		eventListenerGroup.appendListener(new InsertEventListener(enversService));
        	}
        	if (FrmkAppConfigFileHelper.isEnversEventUpdate()) {
        		EventListenerGroup<PostUpdateEventListener> eventListenerGroup = listenerRegistry.getEventListenerGroup(EventType.POST_UPDATE);
        		eventListenerGroup.clear();
        		eventListenerGroup.addDuplicationStrategy(AuditEnversListenerDuplicationStrategy.INSTANCE);
        		eventListenerGroup.appendListener(new UpdateEventListener(enversService));
        	}
        	if (FrmkAppConfigFileHelper.isEnversEventDelete()) {
        		EventListenerGroup<PostDeleteEventListener> eventListenerGroup = listenerRegistry.getEventListenerGroup(EventType.POST_DELETE);
        		eventListenerGroup.clear();
        		eventListenerGroup.addDuplicationStrategy(AuditEnversListenerDuplicationStrategy.INSTANCE);
        		eventListenerGroup.appendListener(new DeleteEventListener(enversService));
        	}
        	if (FrmkAppConfigFileHelper.isEnversEventCollectionRecreate()) {
        		EventListenerGroup<PostCollectionRecreateEventListener> eventListenerGroup = listenerRegistry.getEventListenerGroup(EventType.POST_COLLECTION_RECREATE);
        		eventListenerGroup.clear();
        		eventListenerGroup.addDuplicationStrategy(AuditEnversListenerDuplicationStrategy.INSTANCE);
        		eventListenerGroup.appendListener(new CollectionRecreateEventListener(enversService));
        	}
        	if (FrmkAppConfigFileHelper.isEnversEventCollectionRemove()) {
        		EventListenerGroup<org.hibernate.event.spi.PreCollectionRemoveEventListener> eventListenerGroup = listenerRegistry.getEventListenerGroup(EventType.PRE_COLLECTION_REMOVE);
        		eventListenerGroup.clear();
        		eventListenerGroup.addDuplicationStrategy(AuditEnversListenerDuplicationStrategy.INSTANCE);
        		eventListenerGroup.appendListener(new PreCollectionRemoveEventListener(enversService));
         	}
        	if (FrmkAppConfigFileHelper.isEnversEventCollectionUpdate()) {
        		EventListenerGroup<org.hibernate.event.spi.PreCollectionUpdateEventListener> eventListenerGroup = listenerRegistry.getEventListenerGroup(EventType.PRE_COLLECTION_UPDATE);
        		eventListenerGroup.clear();
        		eventListenerGroup.addDuplicationStrategy(AuditEnversListenerDuplicationStrategy.INSTANCE);
        		eventListenerGroup.appendListener(new PreCollectionUpdateEventListener(enversService));
        	}
        }

    }
    
   
}