package com.nokor.efinance.gui.ui.panel.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.collection.callcenter.InboxWelcomeCallPanel;
import com.nokor.efinance.gui.ui.panel.collection.field.InboxSupervisorFieldPanel;
import com.nokor.efinance.gui.ui.panel.collection.field.leader.ColFieldLeaderPanel;
import com.nokor.efinance.gui.ui.panel.collection.field.staff.ColFieldStaffPanel;
import com.nokor.efinance.gui.ui.panel.collection.inside.leader.ColInsideRepoLeaderPanel;
import com.nokor.efinance.gui.ui.panel.collection.inside.staff.ColInsideRepoStaffPanel;
import com.nokor.efinance.gui.ui.panel.collection.inside.supervisor.ColInsideRepoSupervisorPanel;
import com.nokor.efinance.gui.ui.panel.collection.oa.InboxSupervisorOAPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.InboxLeaderPhonePanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.InboxSupervisorPhonePanel;
import com.nokor.efinance.gui.ui.panel.inbox.collection.ar.ARStaffPanel;
import com.nokor.efinance.gui.ui.panel.inbox.collection.oastaff.ColOAStaffPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;


/**
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DashboardPanel.NAME)
public class DashboardPanel extends VerticalLayout implements View, FinServicesHelper {
	/** */
	private static final long serialVersionUID = 1732049481930388234L;

	public static final String NAME = "dashboard";
		
	private InboxStaffCmPanel inboxStaffCmPanel;
	private InboxLeaderPhonePanel inboxLeaderPhonePanel;
	private InboxSupervisorPhonePanel inboxSupervisorPhonePanel;
	
	private InboxSupervisorFieldPanel inboxSupervisorFieldPanel;
	
	private InboxSupervisorOAPanel inboxSupervisorOAPanel;
	
	private InboxCollectionPhoneStaff inboxCollectionPhoneStaff;
	//private ColPhoneStaffPanel colPhoneStaffPanel; // new screen
	
//	private InboxCallCenterStaff inboxCallCenterStaff;
//	private InboxCallCenterLeaderPanel inboxCallCenterLeader;
	private InboxWelcomeCallPanel inboxWelcomeCallPanel;
	
	// Profile Field
	private ColFieldStaffPanel colFieldStaffPanel; 
	//private ColFieldStaffPanel colFieldStaffPanel; // new screen
	private ColFieldLeaderPanel colFieldLeaderPanel;
	
	//Profile Inside Repo
	private ColInsideRepoStaffPanel colInsideRepoStaffPanel;
	//private ColInsideStaffPanel colInsideStaffPanel; // new screen
	private ColInsideRepoLeaderPanel colInsideRepoLeaderPanel;
	private ColInsideRepoSupervisorPanel colInsideRepoSupervisorPanel;
	
	//Profile Marketing 
	private InboxMarketingLeader inboxMarketingLeader; 
	
	//Profile OA
	private ColOAStaffPanel colOAStaffPanel; // new screen (don't have old screen)
	
	//Prpfile AR
	private ARStaffPanel arStaffPanel;
	
	@PostConstruct
	public void PostConstruct() {
		if (ProfileUtil.isCMProfile()) {
			inboxStaffCmPanel = new InboxStaffCmPanel();
			addComponent(inboxStaffCmPanel);
		} else if (ProfileUtil.isColPhoneStaff()) {
			//inboxCollectionPhoneStaff = new InboxCollectionPhoneStaff();
			inboxCollectionPhoneStaff = new InboxCollectionPhoneStaff();
			addComponent(inboxCollectionPhoneStaff);
		} else if (ProfileUtil.isColPhoneLeader()) {			
			inboxLeaderPhonePanel = new InboxLeaderPhonePanel();
			addComponent(inboxLeaderPhonePanel);
		} else if (ProfileUtil.isColSupervisor()) {
			if (ProfileUtil.isColPhoneSupervisor()) {
				inboxSupervisorPhonePanel = new InboxSupervisorPhonePanel();
				addComponent(inboxSupervisorPhonePanel);
			} else if (ProfileUtil.isColFieldSupervisor()) {
				inboxSupervisorFieldPanel = new InboxSupervisorFieldPanel();
				addComponent(inboxSupervisorFieldPanel);
			} else if (ProfileUtil.isColInsideRepoSupervisor()) {
				colInsideRepoSupervisorPanel = new ColInsideRepoSupervisorPanel();
				addComponent(colInsideRepoSupervisorPanel);
			} else if (ProfileUtil.isColOASupervisor()) {
				inboxSupervisorOAPanel = new InboxSupervisorOAPanel();
				addComponent(inboxSupervisorOAPanel);
			}
		/*} else if (ProfileUtil.isCallCenterStaff()) {
			inboxCallCenterStaff = new InboxCallCenterStaff();
			addComponent(inboxCallCenterStaff);
		} else if (ProfileUtil.isCallCenterLeader()) {
			inboxCallCenterLeader = new InboxCallCenterLeaderPanel();
			addComponent(inboxCallCenterLeader);*/
		} else if (ProfileUtil.isCallCenter()) {
			inboxWelcomeCallPanel = new InboxWelcomeCallPanel();
			addComponent(inboxWelcomeCallPanel);
		} else if (ProfileUtil.isColFieldStaff()) {
			colFieldStaffPanel = new ColFieldStaffPanel();
			addComponent(colFieldStaffPanel);
		} else if (ProfileUtil.isColFieldLeader()) {
			colFieldLeaderPanel = new ColFieldLeaderPanel();
			addComponent(colFieldLeaderPanel);
		} else if (ProfileUtil.isColInsideRepoStaff()) {
			colInsideRepoStaffPanel = new ColInsideRepoStaffPanel();
			addComponent(colInsideRepoStaffPanel);
		} else if (ProfileUtil.isColInsideRepoLeader()) {
			colInsideRepoLeaderPanel = new ColInsideRepoLeaderPanel();
			addComponent(colInsideRepoLeaderPanel);
		} else if (ProfileUtil.isMarketingLeader()) {
			inboxMarketingLeader = new InboxMarketingLeader();
			addComponent(inboxMarketingLeader);
		} else if (ProfileUtil.isColOAStaff()) {
			colOAStaffPanel = new ColOAStaffPanel();
			addComponent(colOAStaffPanel);
		} else if (ProfileUtil.isARStaff()) {
			arStaffPanel = new ARStaffPanel();
			addComponent(arStaffPanel);
		}
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
